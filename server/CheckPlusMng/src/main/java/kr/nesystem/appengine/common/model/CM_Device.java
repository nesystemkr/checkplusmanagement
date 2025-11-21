package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_Device extends GAEAutoIncModel {
	private long userIdKey;
	private String uniqueId;
	private String pushKey;
	private String deviceType;
	public long getUserIdKey() {
		return userIdKey;
	}
	public void setUserIdKey(long userIdKey) {
		this.userIdKey = userIdKey;
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
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("userIdKey", userIdKey)
				.set("uniqueId", N2Z(uniqueId))
				.set("pushKey", N2Z(pushKey))
				.set("deviceType", N2Z(deviceType))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("uniqueId", N2Z(uniqueId))
				.set("pushKey", N2Z(pushKey))
				.set("deviceType", N2Z(deviceType))
				.build();
	}
	@Override
	public CM_Device fromEntity(Entity entity) {
		super.fromEntity(entity);
		setUserIdKey(entity.getLong("userIdKey"));
		setUniqueId(entity.getString("uniqueId"));
		setPushKey(entity.getString("pushKey"));
		setDeviceType(entity.getString("deviceType"));
		return this;
	}

}
