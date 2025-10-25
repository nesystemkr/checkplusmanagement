package kr.nesystem.appengine.common.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.Timestamp;

public abstract class Model {
	private Long no;
	protected boolean hasIdKey = false;
	public abstract String key();
	public abstract FullEntity<IncompleteKey> toEntityAutoInc(KeyFactory keyFactory);
	public abstract Entity toEntity(KeyFactory keyFactory);
	public abstract Entity toEntity(Key key, Entity existOne);
	public abstract Model fromEntity(Entity entity);
	public Key toKey(KeyFactory keyFactory) {
		return keyFactory.setKind(tableName()).newKey(key());
	}
	static public Key toKey(KeyFactory keyFactory, String tableName, String key) {
		return keyFactory.setKind(tableName).newKey(key);
	}
	public String tableName() {
		return this.getClass().getSimpleName();
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String N2Z(String value) {
		return value != null ? value : "";
	}
	public long D2Z(Date value) {
		return value != null ? value.getTime() : 0;
	}
	public Date TS2D(Timestamp value) {
		if (value.toDate().getTime() == 0) {
			return null;
		}
		return value.toDate();
	}
	public boolean hasIdKey() {
		return this.hasIdKey;
	}
}
