package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;

public class CM_L10N extends Model {
	private long idKey;
	private String idString;
	private String defaultString;
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
	public String key() {
		return String.valueOf(idKey);
	}
	@Override
	public Entity toEntity(Datastore datastore) {
		return Entity.newBuilder(toKey(datastore))
				.set("idKey", idKey)
				.set("idString", idString)
				.set("defaultString", defaultString)
				.build();
	}
	@Override
	public CM_L10N fromEntity(Entity entity) {
		return null;
	}
}
