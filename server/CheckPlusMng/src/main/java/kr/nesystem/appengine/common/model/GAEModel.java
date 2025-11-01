package kr.nesystem.appengine.common.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

public abstract class GAEModel extends Model {
	protected boolean hasIdKey = false;
	public abstract Object key();
	public abstract FullEntity<IncompleteKey> toEntityAutoInc(KeyFactory keyFactory);
	public abstract Entity toEntity(KeyFactory keyFactory);
	public abstract Entity toEntity(Entity existOne);
	public abstract GAEModel fromEntity(Entity entity);
	public Key toKey(KeyFactory keyFactory) {
		Object bareKey = key();
		if (bareKey instanceof Long) {
			return keyFactory.setKind(tableName()).newKey(((Long)bareKey).longValue());
		} else if (bareKey instanceof Integer) {
			return keyFactory.setKind(tableName()).newKey(((Integer)bareKey).intValue());
		} else if (bareKey instanceof Short) {
			return keyFactory.setKind(tableName()).newKey(((Short)bareKey).shortValue());
		} else if (bareKey instanceof Byte) {
			return keyFactory.setKind(tableName()).newKey(((Byte)bareKey).byteValue());
		} else {
			return keyFactory.setKind(tableName()).newKey(bareKey.toString());
		}
	}
	static public Key toKey(KeyFactory keyFactory, String tableName, String key) {
		return keyFactory.setKind(tableName).newKey(key);
	}
	public String tableName() {
		return this.getClass().getSimpleName();
	}
	public String N2Z(String value) {
		return value != null ? value : "";
	}
	public long N2Z(Long value) {
		return value != null ? value : 0;
	}
	public long D2Z(Date value) {
		return value != null ? value.getTime() : 0;
	}
	public boolean hasIdKey() {
		return this.hasIdKey;
	}
	public Date L2D(long value) {
		if (value == 0) {
			return null;
		}
		return new Date(value);
	}
}
