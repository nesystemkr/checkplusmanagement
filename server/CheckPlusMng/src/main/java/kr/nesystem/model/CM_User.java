package kr.nesystem.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_User extends Model {
	private long idKey;
	private String userId;
	private String password;
	private String userType;
	private String userName;
	private String status;
	private int loginFailCount;
	private Date lastLoginDate;
	private long lastLoginSeq;
	private Date createDate;
	private String statusName;
	private String userTypeName;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public String getUserId() {
		if (userId != null) {
			return userId.toLowerCase();
		}
		return null;
	}
	public void setUserId(String userId) {
		if (userId != null) {
			this.userId = userId.toLowerCase();
		} else {
			this.userId = null;
		}
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getLoginFailCount() {
		return loginFailCount;
	}
	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public long getLastLoginSeq() {
		return lastLoginSeq;
	}
	public void setLastLoginSeq(long lastLoginSeq) {
		this.lastLoginSeq = lastLoginSeq;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
}
