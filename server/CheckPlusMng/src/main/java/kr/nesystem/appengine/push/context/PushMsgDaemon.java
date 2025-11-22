package kr.nesystem.appengine.push.context;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import org.apache.kafka.common.errors.WakeupException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.turo.pushy.apns.util.concurrent.PushNotificationResponseListener;

import io.netty.util.concurrent.Future;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.dao.DeviceDao;
import kr.nesystem.appengine.common.dao.UserDao;
import kr.nesystem.appengine.common.model.CM_Device;
import kr.nesystem.appengine.common.model.CM_User;
import kr.nesystem.appengine.common.model.Model;
import kr.nesystem.appengine.daemon.common.StopThread;
import kr.nesystem.appengine.messagequeue.context.ConsumerBaseDaemon;
import kr.nesystem.appengine.push.dao.PushDao;
import kr.nesystem.appengine.push.model.PS_Msg;
import kr.nesystem.appengine.push.model.PS_QueueItem;

public class PushMsgDaemon extends ConsumerBaseDaemon {
	private static int fcmInited = 0;
	private UserDao userDao = new UserDao();
	private DeviceDao deviceDao = new DeviceDao();
	private PushDao pushDao = new PushDao();
	private MessageSendThread messageSendThread;
	public boolean sendRunning = false;
	
	public PushMsgDaemon() {
		super(PS_QueueItem.class.getName());
	}
	
	public void start() {
		super.start();
		messageSendThread = new MessageSendThread();
		messageSendThread.start();
	}
	
	public void stop() {
		super.stop();
		if (messageSendThread != null) {
			messageSendThread.setStop(true);
			messageSendThread.interrupt();
		}
	}
	
	public boolean isRunning() {
		return running && sendRunning;
	}
	
	private class MessageSendThread extends StopThread {
		public void run() {
			sendRunning = true;
			PushDao pushDao = new PushDao();
			try {
				List<PS_Msg> msgs; 
				PS_Msg msg;
				while (true) {
					sleep(10000);
					if (isStop == true) {
						break;
					}
					msgs = pushDao.selectPmsCandidatedMsg();
					if (msgs != null) {
						for (int ii = 0; ii < msgs.size(); ii++) {
							msg = msgs.get(ii);
							(new PushThread(msg)).start();
						}
					}
				}
			} catch (WakeupException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			sendRunning = false;
		}
	}
	
	@Override
	public String getTopicName() {
		return "pushmessages";
	}

	@Override
	public String getGroupId() {
		return "pushmessage";
	}

	@Override
	public void handleItem(Model item) {
		try {
			PS_QueueItem pItem = (PS_QueueItem)item;
			CM_User existUser = null;
			if (pItem.getUserId() != null) {
				existUser = userDao.selectByUserId(null, pItem.getUserId());
			} else {
				existUser = userDao.select(null, pItem.getUserIdKey());
			}
			if (existUser == null) {
				return;
			}
			List<CM_Device> devices = deviceDao.selectDevicesByUserIdKey(existUser.getIdKey());
			if (devices == null) {
				return;
			}
			CM_Device device;
			PS_Msg newMsg;
			for (int ii=0; ii<devices.size(); ii++) {
				device = devices.get(ii);
				newMsg = new PS_Msg();
				newMsg.setUserIdKey(existUser.getIdKey());
				newMsg.setDeviceType(device.getDeviceType());
				newMsg.setPushKey(device.getPushKey());
				newMsg.setMsgGroupIdKey(pItem.getMsgGroupIdKey());
				newMsg.setType(pItem.getType());
				newMsg.setTitle(pItem.getTitle());
				newMsg.setMessage(pItem.getMessage());
				newMsg.setCreateDate(new Date());
				if (pItem.getReserveDate() == null) {
					newMsg.setReserveDate(new Date());
				} else {
					newMsg.setReserveDate(pItem.getReserveDate());
				}
				newMsg.setPmsStatus("0");
				newMsg.setPmsMaxRetryCount(pItem.getPmsMaxRetryCount());
				newMsg.setPmsTryCount(0);
				pushDao.insert(newMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleList(List<Model> item) {
	}
	
	private class PushThread extends Thread {
		private PS_Msg __msg;
		public PushThread(PS_Msg msg) {
			super();
			setDaemon(true);
			__msg = msg;
		}
		
		@Override
		public void run() {
			if (__msg.getDeviceType() != null && __msg.getDeviceType().equals("iOS")) {
				PushMsgDaemon.pushToAPNS(__msg);
			} else if (__msg.getDeviceType() != null && __msg.getDeviceType().equals("android")) {
				PushMsgDaemon.pushToFCM(__msg);
			} else {
				PushDao pushDao = new PushDao();
				__msg.setErrorCode("000");
				__msg.setErrorMessage("DeviceType not matched!!!");
				__msg.setErrorDate(new Date());
				try {
					pushDao.update(__msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void pushToFCM(PS_Msg msg) {
		PushDao pushDao = new PushDao();
		try {
			pushDao.updateSendDate(msg);
			if (fcmInited == 0) {
				FileInputStream serviceAccount = new FileInputStream(Constant.FCM_PUSH_CREDENTIALS_PATH);
				FirebaseOptions options = FirebaseOptions.builder()
						.setProjectId(Constant.FCM_PUSH_PROJECTID)
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://rentalwelderandroid.firebaseio.com")
						.build();
				FirebaseApp.initializeApp(options);
				fcmInited = 1;
			}
			
			Message.Builder builder = Message.builder();
			if (msg.getTitle() != null && msg.getTitle().length() > 0) {
				builder.putData("title", URLEncoder.encode(msg.getTitle(), "UTF-8"));
			} else {
				builder.putData("type", URLEncoder.encode(msg.getType(), "UTF-8"));
			}
			builder.putData("msg", URLEncoder.encode(msg.getMessage(), "UTF-8"));
			builder.putData("msgId", String.valueOf(msg.getIdKey()));
			builder.setToken(msg.getPushKey());
			com.google.firebase.messaging.Message pushMessage = builder.build();
			String error = FirebaseMessaging.getInstance().send(pushMessage);
			pushDao.updatePushResult(msg, true, null, error);
		} catch (FirebaseMessagingException e) {
			pushDao.updatePushResult(msg, false, String.valueOf(e.getErrorCode()), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			pushDao.updatePushResult(msg, false, "999", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void pushToAPNS(PS_Msg msg) {
		try {
			PushDao pushDao = new PushDao();
			pushDao.updateSendDate(msg);
			ApnsClientBuilder builder = new ApnsClientBuilder();
			if (Constant.APNS_PUSH_DEV) {
				builder.setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST);
			} else {
				builder.setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST);
			}
			builder.setSigningKey(ApnsSigningKey.loadFromPkcs8File(
					new File(Constant.APNS_PUSH_P8_PATH),
					Constant.APNS_PUSH_TEAMID,
					Constant.APNS_PUSH_KEYID));
			ApnsClient apnsClient = builder.build();
			
			ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
			payloadBuilder.addCustomProperty("msgId", String.valueOf(msg.getIdKey()));
			if (msg.getTitle() != null && msg.getTitle().length() > 0) {
				payloadBuilder.setAlertTitle(msg.getTitle());
				payloadBuilder.setAlertBody(msg.getMessage());
			} else {
				payloadBuilder.setLocalizedAlertMessage(msg.getType(), msg.getMessage().split(","));
			}
			payloadBuilder.setBadgeNumber(msg.getBadgeNumber());
			String token = TokenUtil.sanitizeTokenString(msg.getPushKey());
			String payload = payloadBuilder.buildWithDefaultMaximumLength();
			SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, Constant.APNS_PUSH_APP_BUNDLEID, payload);
			PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture;
			sendNotificationFuture = apnsClient.sendNotification(pushNotification);
			PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
			if (pushNotificationResponse.isAccepted()) {
				if (pushNotificationResponse.getPushNotification() != null &&
					pushNotificationResponse.getPushNotification().getApnsId() != null) {
					pushDao.updatePushResult(msg, true, 
							null, pushNotificationResponse.getPushNotification().getApnsId().toString());
				} else {
					pushDao.updatePushResult(msg, true, null, "");
				}
			} else {
				pushDao.updatePushResult(msg, true,
						pushNotificationResponse.getTokenInvalidationTimestamp() != null ? "APP IS INVALID." : null,
						pushNotificationResponse.getRejectionReason());
			}
			sendNotificationFuture.addListener(new PushNotificationResponseListener<SimpleApnsPushNotification>() {
				@Override
				public void operationComplete(final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future) throws Exception {
					if (future.isSuccess()) {
						if (future.getPushNotification() != null &&
							future.getPushNotification().getApnsId() != null) {
							pushDao.updatePushResult(msg, true,
									null, future.getPushNotification().getApnsId().toString());
						} else {
							pushDao.updatePushResult(msg, true, null, "");
						}
					} else {
						pushDao.updatePushResult(msg, false, null, "UNKNOWN ERROR!!!");
					}
				}
			});
			final Future<Void> closeFuture = apnsClient.close();
			closeFuture.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pushMessageDirect(String userId, String title, String message) {
		try {
			UserDao userDao = new UserDao();
			DeviceDao deviceDao = new DeviceDao();
			CM_User existUser = userDao.select(null, userId);
			if (existUser == null) {
				return;
			}
			List<CM_Device> devices = deviceDao.selectDevicesByUserIdKey(existUser.getIdKey());
			if (devices == null) {
				return;
			}
			CM_Device device;
			PS_Msg newMsg;
			for (int ii=0; ii<devices.size(); ii++) {
				device = devices.get(ii);
				newMsg = new PS_Msg();
				newMsg.setUserIdKey(existUser.getIdKey());
				newMsg.setDeviceType(device.getDeviceType());
				newMsg.setPushKey(device.getPushKey());
				newMsg.setMsgGroupIdKey(0);
				newMsg.setType(null);
				newMsg.setTitle(title);
				newMsg.setMessage(message);
				newMsg.setCreateDate(new Date());
				newMsg.setReserveDate(new Date());
				newMsg.setPmsStatus("0");
				newMsg.setPmsMaxRetryCount(3);
				newMsg.setPmsTryCount(0);
				if (newMsg.getDeviceType() != null && newMsg.getDeviceType().equals("iOS")) {
					PushMsgDaemon.pushToAPNS(newMsg);
				} else if (newMsg.getDeviceType() != null && newMsg.getDeviceType().equals("android")) {
					PushMsgDaemon.pushToFCM(newMsg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
