package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_Version extends GAEAutoIncModel {
	private String version;
	private String status;
	private String iosUpgradeUrl;
	private String androidUpgradeUrl;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIosUpgradeUrl() {
		return iosUpgradeUrl;
	}
	public void setIosUpgradeUrl(String iosUpgradeUrl) {
		this.iosUpgradeUrl = iosUpgradeUrl;
	}
	public String getAndroidUpgradeUrl() {
		return androidUpgradeUrl;
	}
	public void setAndroidUpgradeUrl(String androidUpgradeUrl) {
		this.androidUpgradeUrl = androidUpgradeUrl;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("version", N2Z(version))
				.set("status", N2Z(status))
				.set("iosUpgradeUrl", N2Z(iosUpgradeUrl))
				.set("androidUpgradeUrl", N2Z(androidUpgradeUrl))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("version", N2Z(version))
				.set("status", N2Z(status))
				.set("iosUpgradeUrl", N2Z(iosUpgradeUrl))
				.set("androidUpgradeUrl", N2Z(androidUpgradeUrl))
				.build();
	}
	@Override
	public GAEModel fromEntity(Entity entity) {
		super.fromEntity(entity);
		setVersion(entity.getString("version"));
		setStatus(entity.getString("status"));
		setIosUpgradeUrl(entity.getString("iosUpgradeUrl"));
		setAndroidUpgradeUrl(entity.getString("androidUpgradeUrl"));
		return null;
	}
}
