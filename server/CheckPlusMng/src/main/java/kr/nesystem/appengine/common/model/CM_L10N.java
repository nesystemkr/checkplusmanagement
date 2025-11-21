package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_L10N extends GAEAutoIncModel {
	private String idString;
	private String defaultString;
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	public String getDefaultString() {
		return defaultString;
	}
	public void setDefaultString(String defaultString) {
		this.defaultString = defaultString;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("idString", N2Z(idString))
				.set("defaultString", N2Z(defaultString))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("idString", N2Z(idString))
				.set("defaultString", N2Z(defaultString))
				.build();
	}
	@Override
	public CM_L10N fromEntity(Entity entity) {
		super.fromEntity(entity);
		setIdString(entity.getString("idString"));
		setDefaultString(entity.getString("defaultString"));
		return this;
	}
}
