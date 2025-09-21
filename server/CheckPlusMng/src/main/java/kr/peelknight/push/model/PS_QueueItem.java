package kr.peelknight.push.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PS_QueueItem extends Model {
	private long userIdKey;
	private String userId;
	private String type;
	private String title;
	private String message;
	private Date reserveDate;
	private long msgGroupIdKey;
	private int pmsMaxRetryCount;
	public long getUserIdKey() {
		return userIdKey;
	}
	public void setUserIdKey(long userIdKey) {
		this.userIdKey = userIdKey;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public Date getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}
	public long getMsgGroupIdKey() {
		return msgGroupIdKey;
	}
	public void setMsgGroupIdKey(long msgGroupIdKey) {
		this.msgGroupIdKey = msgGroupIdKey;
	}
	public int getPmsMaxRetryCount() {
		return pmsMaxRetryCount;
	}
	public void setPmsMaxRetryCount(int pmsMaxRetryCount) {
		this.pmsMaxRetryCount = pmsMaxRetryCount;
	}
}
