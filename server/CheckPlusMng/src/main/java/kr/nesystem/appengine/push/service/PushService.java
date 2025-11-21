package kr.nesystem.appengine.push.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.nesystem.appengine.common.dao.UserDao;
import kr.nesystem.appengine.common.model.CM_User;
import kr.nesystem.appengine.common.util.Kafka;
import kr.nesystem.appengine.common.util.ResponseUtil;
import kr.nesystem.appengine.push.model.PS_MsgTest;
import kr.nesystem.appengine.push.model.PS_QueueItem;

@Path("/{version}/push")
public class PushService {
	public static void pushMessage(String userId, String type, String title, String message,
								   Date reserveDate, long msgGroupIdKey, int pmsMaxRetryCount) {
		PS_QueueItem item = new PS_QueueItem();
		item.setUserId(userId);
		item.setType(type);
		item.setTitle(title);
		item.setMessage(message);
		item.setReserveDate(reserveDate);
		item.setMsgGroupIdKey(msgGroupIdKey);
		item.setPmsMaxRetryCount(pmsMaxRetryCount);
		pushMessage(item);
	}
	
	public static void pushMessage(long userIdKey, String type, String title, String message,
								   Date reserveDate, long msgGroupIdKey, int pmsMaxRetryCount) {
		PS_QueueItem item = new PS_QueueItem();
		item.setUserIdKey(userIdKey);
		item.setType(type);
		item.setTitle(title);
		item.setMessage(message);
		item.setReserveDate(reserveDate);
		item.setMsgGroupIdKey(msgGroupIdKey);
		item.setPmsMaxRetryCount(pmsMaxRetryCount);
		pushMessage(item);
	}
	
	public static void pushMessage(PS_QueueItem item) {
		String topicName = "pushmessages";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		StringBuilder sbKey = new StringBuilder("");
		if (item.getUserId() != null) {
			sbKey.append(item.getUserId());
		} else {
			sbKey.append(item.getUserIdKey());
		}
		sbKey.append("_").append(sdf.format(new Date()));
		ObjectMapper mapper = new ObjectMapper();
		try {
			Kafka.push(topicName, sbKey.toString(), mapper.writeValueAsString(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/test/UserIds")
	public Response testPushWithUserIds(PS_MsgTest msgTest) {
		try {
			if (msgTest.getUserIds() == null || msgTest.getUserIds().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			UserDao userDao = new UserDao();
			String userId;
			int sentCount = 0;
			int repeatCount = 1;
			if (msgTest.getRepeatCount() != null && msgTest.getRepeatCount().trim().length() > 0) {
				if (Integer.parseInt(msgTest.getRepeatCount()) > 1) {
					repeatCount = Integer.parseInt(msgTest.getRepeatCount());
				}
			}
			String[] userIds = msgTest.getUserIds().split(",");
			for (int ii=0; ii<userIds.length; ii++) {
				userId = userIds[ii];
				CM_User existUser = userDao.selectByUserId(null, userId);
				if (existUser == null) {
					continue;
				}
				for (int jj=0; jj<repeatCount; jj++) {
					PushService.pushMessage(userId, msgTest.getType(), msgTest.getTitle(), msgTest.getMessage(), null, 0, 5);
					sentCount++;
				}
			}
			String responseStr = String.valueOf(userIds.length) + " is trying. " + String.valueOf(sentCount) + " is sent!";
			return ResponseUtil.getResponse(responseStr);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
