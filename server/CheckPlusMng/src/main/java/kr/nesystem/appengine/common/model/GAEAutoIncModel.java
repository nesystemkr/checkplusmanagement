package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Entity;

public abstract class GAEAutoIncModel extends GAEModel {
	protected long idKey;
	public GAEAutoIncModel() {
		super();
	}
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	@Override
	public Object key() {
		return Long.valueOf(idKey);
	}
	@Override
	public GAEModel fromEntity(Entity entity) {
		setIdKey(entity.getKey().getId());
		return this;
	}
}
