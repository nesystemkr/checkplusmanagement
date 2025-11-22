package kr.nesystem.appengine.push.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class PS_Msg extends GAEAutoIncModel {
	private long userIdKey;
	private String deviceType;
	private String pushKey;
	private long msgGroupIdKey;
	private String type;
	private String title;
	private String message;
	private int badgeNumber;
	private Date createDate;
	private Date reserveDate;
	private String pmsStatus;
	private int pmsMaxRetryCount;
	private int pmsTryCount;
	private Date sendDate;
	private Date completeDate;
	private String errorCode;
	private String errorMessage;
	private Date errorDate;
	private String receiveStatus;
	private Date receiveDate;
	private String readStatus;
	private Date readDate;
	public long getUserIdKey() {
		return userIdKey;
	}
	public void setUserIdKey(long userIdKey) {
		this.userIdKey = userIdKey;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getPushKey() {
		return pushKey;
	}
	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}
	public long getMsgGroupIdKey() {
		return msgGroupIdKey;
	}
	public void setMsgGroupIdKey(long msgGroupIdKey) {
		this.msgGroupIdKey = msgGroupIdKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getBadgeNumber() {
		return badgeNumber;
	}
	public void setBadgeNumber(int badgeNumber) {
		this.badgeNumber = badgeNumber;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}
	public String getPmsStatus() {
		return pmsStatus;
	}
	public void setPmsStatus(String pmsStatus) {
		this.pmsStatus = pmsStatus;
	}
	public int getPmsMaxRetryCount() {
		return pmsMaxRetryCount;
	}
	public void setPmsMaxRetryCount(int pmsMaxRetryCount) {
		this.pmsMaxRetryCount = pmsMaxRetryCount;
	}
	public int getPmsTryCount() {
		return pmsTryCount;
	}
	public void setPmsTryCount(int pmsTryCount) {
		this.pmsTryCount = pmsTryCount;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Date getErrorDate() {
		return errorDate;
	}
	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}
	public String getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	public Date getReadDate() {
		return readDate;
	}
	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("userIdKey", userIdKey)
				.set("deviceType", N2Z(deviceType))
				.set("pushKey", N2Z(pushKey))
				.set("msgGroupIdKey", msgGroupIdKey)
				.set("type", N2Z(type))
				.set("title", N2Z(title))
				.set("message", N2Z(message))
				.set("badgeNumber", badgeNumber)
				.set("createDate", D2Z(createDate))
				.set("reserveDate", D2Z(reserveDate))
				.set("pmsStatus", N2Z(pmsStatus))
				.set("pmsMaxRetryCount", pmsMaxRetryCount)
				.set("pmsTryCount", pmsTryCount)
				.set("sendDate", D2Z(sendDate))
				.set("completeDate", D2Z(completeDate))
				.set("errorCode", N2Z(errorCode))
				.set("errorMessage", N2Z(errorMessage))
				.set("errorDate", D2Z(errorDate))
				.set("receiveStatus", N2Z(receiveStatus))
				.set("receiveDate", D2Z(receiveDate))
				.set("readStatus", N2Z(readStatus))
				.set("readDate", D2Z(readDate))
				.build();
	}
	public Entity.Builder builder(Entity.Builder builder) {
		return builder.set("userIdKey", userIdKey)
				.set("deviceType", N2Z(deviceType))
				.set("pushKey", N2Z(pushKey))
				.set("msgGroupIdKey", msgGroupIdKey)
				.set("type", N2Z(type))
				.set("title", N2Z(title))
				.set("message", N2Z(message))
				.set("badgeNumber", badgeNumber)
				.set("createDate", D2Z(createDate))
				.set("reserveDate", D2Z(reserveDate))
				.set("pmsStatus", N2Z(pmsStatus))
				.set("pmsMaxRetryCount", pmsMaxRetryCount)
				.set("pmsTryCount", pmsTryCount)
				.set("sendDate", D2Z(sendDate))
				.set("completeDate", D2Z(completeDate))
				.set("errorCode", N2Z(errorCode))
				.set("errorMessage", N2Z(errorMessage))
				.set("errorDate", D2Z(errorDate))
				.set("receiveStatus", N2Z(receiveStatus))
				.set("receiveDate", D2Z(receiveDate))
				.set("readStatus", N2Z(readStatus))
				.set("readDate", D2Z(readDate));
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return builder(Entity.newBuilder(existOne.getKey(), existOne)).build();
	}
	@Override
	public PS_Msg fromEntity(Entity entity) {
		super.fromEntity(entity);
		setUserIdKey(entity.getLong("userIdKey"));
		setDeviceType(entity.getString("deviceType"));
		setPushKey(entity.getString("pushKey"));
		setMsgGroupIdKey(entity.getLong("msgGroupIdKey"));
		setType(entity.getString("type"));
		setTitle(entity.getString("title"));
		setMessage(entity.getString("message"));
		setBadgeNumber((int)entity.getLong("badgeNumber"));
		setCreateDate(L2D(entity.getLong("createDate")));
		setReserveDate(L2D(entity.getLong("reserveDate")));
		setPmsStatus(entity.getString("pmsStatus"));
		setPmsMaxRetryCount((int)entity.getLong("pmsMaxRetryCount"));
		setPmsTryCount((int)entity.getLong("pmsTryCount"));
		setSendDate(L2D(entity.getLong("sendDate")));
		setCompleteDate(L2D(entity.getLong("completeDate")));
		setErrorCode(entity.getString("errorCode"));
		setErrorMessage(entity.getString("errorMessage"));
		setErrorDate(L2D(entity.getLong("errorDate")));
		setReceiveStatus(entity.getString("receiveStatus"));
		setReceiveDate(L2D(entity.getLong("receiveDate")));
		setReadStatus(entity.getString("readStatus"));
		setReadDate(L2D(entity.getLong("readDate")));
		return this;
	}
	public Entity toEntity(Entity existOne, String pmsStatus, int pmsTryCount, Date sendDate) {
		return builder(Entity.newBuilder(existOne.getKey(), existOne))
				.set("pmsStatus", N2Z(pmsStatus))
				.set("pmsTryCount", pmsTryCount)
				.set("sendDate", D2Z(sendDate))
				.build();
	}
	public Entity toEntity(Entity existOne, String pmsStatus, Date completeDate, String errorCode, String errorMessage) {
		return builder(Entity.newBuilder(existOne.getKey(), existOne))
				.set("pmsStatus", N2Z(pmsStatus))
				.set("completeDate", D2Z(completeDate))
				.set("errorCode", N2Z(errorCode))
				.set("errorMessage", N2Z(errorMessage))
				.build();
	}
	public Entity toEntity(Entity existOne, String pmsStatus, String errorCode, String errorMessage) {
		return builder(Entity.newBuilder(existOne.getKey(), existOne))
				.set("pmsStatus", N2Z(pmsStatus))
				.set("errorCode", N2Z(errorCode))
				.set("errorMessage", N2Z(errorMessage))
				.build();
	}
}
