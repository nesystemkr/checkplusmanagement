package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class MW_Welder extends GAEAutoIncModel {
	private String welderId;
	private long projectIdKey;
	private String modelName;
	private String weldType;
	private String subDevice;
	private String customized;
	private Date installDate;
	private String installLocation;
	private String memo;
	public String getWelderId() {
		return welderId;
	}
	public void setWelderId(String welderId) {
		this.welderId = welderId;
	}
	public long getProjectIdKey() {
		return projectIdKey;
	}
	public void setProjectIdKey(long projectIdKey) {
		this.projectIdKey = projectIdKey;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getWeldType() {
		return weldType;
	}
	public void setWeldType(String weldType) {
		this.weldType = weldType;
	}
	public String getSubDevice() {
		return subDevice;
	}
	public void setSubDevice(String subDevice) {
		this.subDevice = subDevice;
	}
	public String getCustomized() {
		return customized;
	}
	public void setCustomized(String customized) {
		this.customized = customized;
	}
	public Date getInstallDate() {
		return installDate;
	}
	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}
	public String getInstallLocation() {
		return installLocation;
	}
	public void setInstallLocation(String installLocation) {
		this.installLocation = installLocation;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("welderId", N2Z(welderId))
				.set("projectIdKey", projectIdKey)
				.set("modelName", N2Z(modelName))
				.set("weldType", N2Z(weldType))
				.set("subDevice", N2Z(subDevice))
				.set("customized", N2Z(customized))
				.set("installDate", D2Z(installDate))
				.set("installLocation", N2Z(installLocation))
				.set("memo", N2Z(memo))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("welderId", N2Z(welderId))
				.set("projectIdKey", projectIdKey)
				.set("modelName", N2Z(modelName))
				.set("weldType", N2Z(weldType))
				.set("subDevice", N2Z(subDevice))
				.set("customized", N2Z(customized))
				.set("installDate", D2Z(installDate))
				.set("installLocation", N2Z(installLocation))
				.set("memo", N2Z(memo))
				.build();
	}
	@Override
	public MW_Welder fromEntity(Entity entity) {
		super.fromEntity(entity);
		setWelderId(entity.getString("welderId"));
		setProjectIdKey(entity.getLong("projectIdKey"));
		setModelName(entity.getString("modelName"));
		setWeldType(entity.getString("weldType"));
		setSubDevice(entity.getString("subDevice"));
		setCustomized(entity.getString("customized"));
		setInstallDate(L2D(entity.getLong("installDate")));
		setInstallLocation(entity.getString("installLocation"));
		setMemo(entity.getString("memo"));
		return this;
	}

}
