package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Welder extends MW_IDBaseModel {
	private String modelName;
	private String weldType;
	private String customized;
	private long currentActivityIdKey;
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
	public String getCustomized() {
		return customized;
	}
	public void setCustomized(String customized) {
		this.customized = customized;
	}
	public long getCurrentActivityIdKey() {
		return currentActivityIdKey;
	}
	public void setCurrentActivityIdKey(long currentActivityIdKey) {
		this.currentActivityIdKey = currentActivityIdKey;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("modelName", N2Z(modelName))
				.set("weldType", N2Z(weldType))
				.set("customized", N2Z(customized))
				.set("currentActivityIdKey", currentActivityIdKey)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("modelName", N2Z(modelName))
				.set("weldType", N2Z(weldType))
				.set("customized", N2Z(customized))
				.set("currentActivityIdKey", currentActivityIdKey)
				.build();
	}
	@Override
	public MW_Welder fromEntity(Entity entity) {
		super.fromEntity(entity);
		setModelName(entity.getString("modelName"));
		setWeldType(entity.getString("weldType"));
		setCustomized(entity.getString("customized"));
		setCurrentActivityIdKey(entity.getLong("currentActivityIdKey"));
		return this;
	}
}
