package kr.nesystem.appengine.common.model;

import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_Login extends GAEModel {
	private String userId;
	private String password;
	private String authToken;
	private String uniqueId;
	private String pushKey;
	private String deviceType;
	private Long userIdKey;
	private String userType;
	private String userName;
	private String snsType;
	private String snsId;
	private String version;
	private String upgradeUrl;
	private List<CM_Menu> menus;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId.toLowerCase();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getPushKey() {
		return pushKey;
	}
	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Long getUserIdKey() {
		return userIdKey;
	}
	public void setUserIdKey(Long userIdKey) {
		this.userIdKey = userIdKey;
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
	public String getSnsType() {
		return snsType;
	}
	public void setSnsType(String snsType) {
		this.snsType = snsType;
	}
	public String getSnsId() {
		return snsId;
	}
	public void setSnsId(String snsId) {
		this.snsId = snsId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUpgradeUrl() {
		return upgradeUrl;
	}
	public void setUpgradeUrl(String upgradeUrl) {
		this.upgradeUrl = upgradeUrl;
	}
	public List<CM_Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<CM_Menu> menus) {
		this.menus = menus;
	}
	@Override
	public Object key() {
		return userId;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(toKey(keyFactory))
				.set("userId", userId)
				.set("password", password)
				.set("authToken", N2Z(authToken))
				.set("uniqueId", N2Z(uniqueId))
				.set("pushKey", N2Z(pushKey))
				.set("deviceType", N2Z(deviceType))
				.set("userIdKey", N2Z(userIdKey))
				.set("userType", N2Z(userType))
				.set("userName", N2Z(userName))
				.set("snsType", N2Z(snsType))
				.set("snsId", N2Z(snsId))
				.set("version", N2Z(version))
				.set("upgradeUrl", N2Z(upgradeUrl))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("password", password)
				.set("authToken", authToken)
				.set("uniqueId", N2Z(uniqueId))
				.set("pushKey", N2Z(pushKey))
				.set("deviceType", N2Z(deviceType))
				.set("userIdKey", N2Z(userIdKey))
				.set("userType", N2Z(userType))
				.set("userName", N2Z(userName))
				.set("snsType", N2Z(snsType))
				.set("snsId", N2Z(snsId))
				.set("version", N2Z(version))
				.set("upgradeUrl", N2Z(upgradeUrl))
				.build();
	}
	@Override
	public CM_Login fromEntity(Entity entity) {
		setUserId(entity.getString("userId"));
		setPassword(entity.getString("password"));
		setAuthToken(entity.getString("authToken"));
		setUniqueId(entity.getString("uniqueId"));
		setPushKey(entity.getString("pushKey"));
		setDeviceType(entity.getString("deviceType"));
		setUserIdKey(entity.getLong("userIdKey"));
		setUserType(entity.getString("userType"));
		setUserName(entity.getString("userName"));
		setSnsType(entity.getString("snsType"));
		setSnsId(entity.getString("snsId"));
		setVersion(entity.getString("version"));
		setUpgradeUrl(entity.getString("upgradeUrl"));
		return this;
	}
}
