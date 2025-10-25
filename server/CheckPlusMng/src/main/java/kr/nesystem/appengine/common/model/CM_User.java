package kr.nesystem.appengine.common.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

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
	public CM_User() {
		super();
		hasIdKey = true;
	}
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
	@Override
	public String key() {
		return String.valueOf(idKey);
	}
	@Override
	public FullEntity<IncompleteKey> toEntityAutoInc(KeyFactory keyFactory) {
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
	public Entity toEntity(KeyFactory keyFactory) {
		return null;
	}
	@Override
	public Entity toEntity(Key key, Entity existOne) {
		return Entity.newBuilder(key, existOne)
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
		setIdKey(entity.getKey().getId());
		setUserId(entity.getString("userId"));
		setPassword(entity.getString("password"));
		setUserType(entity.getString("userType"));
		setUserName(entity.getString("userName"));
		setStatus(entity.getString("status"));
		setLoginFailCount((int)entity.getLong("loginFailCount"));
		setLastLoginDate(TS2D(entity.getTimestamp("lastLoginDate")));
		setLastLoginSeq((int)entity.getLong("lastLoginSeq"));
		setCreateDate(TS2D(entity.getTimestamp("createDate")));
		return this;
	}
}
