package kr.nesystem.appengine.common.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.util.CodeStore;
import kr.nesystem.appengine.common.util.L10N;

public class CM_User extends GAEAutoIncModel {
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
	@Override
	public void l10n(HttpSession session) {
		userTypeName = CodeStore.get("USERTYPE", userType);
		statusName = CodeStore.get("USERSTATUS", status);
		userTypeName = L10N.get(userTypeName, session);
		statusName = L10N.get(statusName, session);
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("userId", N2Z(userId))
				.set("password", N2Z(password))
				.set("userType", N2Z(userType))
				.set("userName", N2Z(userName))
				.set("status", N2Z(status))
				.set("loginFailCount", loginFailCount)
				.set("lastLoginDate", D2Z(lastLoginDate))
				.set("lastLoginSeq", lastLoginSeq)
				.set("createDate", D2Z(createDate))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("userId", N2Z(userId))
				.set("password", N2Z(password))
				.set("userType", N2Z(userType))
				.set("userName", N2Z(userName))
				.set("status", N2Z(status))
				.set("loginFailCount", loginFailCount)
				.set("lastLoginDate", D2Z(lastLoginDate))
				.set("lastLoginSeq", lastLoginSeq)
				.build();
	}
	@Override
	public CM_User fromEntity(Entity entity) {
		super.fromEntity(entity);
		setUserId(entity.getString("userId"));
		setPassword(entity.getString("password"));
		setUserType(entity.getString("userType"));
		setUserName(entity.getString("userName"));
		setStatus(entity.getString("status"));
		setLoginFailCount((int)entity.getLong("loginFailCount"));
		setLastLoginDate(L2D(entity.getLong("lastLoginDate")));
		setLastLoginSeq((int)entity.getLong("lastLoginSeq"));
		setCreateDate(L2D(entity.getLong("createDate")));
		return this;
	}
}
