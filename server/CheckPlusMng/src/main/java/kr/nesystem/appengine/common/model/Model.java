package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public abstract class Model {
	public abstract String key();
	public abstract Entity toEntity(Datastore datastore);
	public abstract Model fromEntity(Entity entity);
	public Key toKey(Datastore datastore) {
		return datastore.newKeyFactory().setKind(this.getClass().getSimpleName()).newKey(key());
	}
	public static <T> Key toKey(Datastore datastore, Class<T> clazz, String key) {
		return datastore.newKeyFactory().setKind(clazz.getSimpleName()).newKey(key);
	}
}
