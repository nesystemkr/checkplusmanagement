package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;

public class CM_L10N extends GAEModel {
	private long idKey;
	private String idString;
	private String defaultString;
	public CM_L10N() {
		super();
		hasIdKey = true;
	}
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
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
	public Object key() {
		return Long.valueOf(idKey);
	}
	@Override
	public FullEntity<IncompleteKey> toEntityAutoInc(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("idString", idString)
				.set("defaultString", defaultString)
				.build();
	}
	@Override
	public Entity toEntity(KeyFactory keyfactory) {
		return null;
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("idString", idString)
				.set("defaultString", defaultString)
				.build();
	}
	@Override
	public CM_L10N fromEntity(Entity entity) {
		setIdKey(entity.getKey().getId());
		setIdString(entity.getString("idString"));
		setDefaultString(entity.getString("defaultString"));
		return this;
	}
}
