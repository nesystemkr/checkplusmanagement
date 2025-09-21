package kr.peelknight.push.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PS_Msg extends Model {
	private long idKey;
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
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
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
}
