package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.FullEntity.Builder;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public abstract class MW_BaseModel extends GAEAutoIncModel {
	private String memo;
	private long orderSeq;
	public MW_BaseModel() {
		super();
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public long getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(long orderSeq) {
		this.orderSeq = orderSeq;
	}
	public <K extends IncompleteKey> Builder<IncompleteKey> builder(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("memo", N2Z(memo))
				.set("orderSeq", orderSeq);
	}
	public Entity.Builder builder(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("memo", N2Z(memo))
				.set("orderSeq", orderSeq);
	}
	@Override
	public MW_BaseModel fromEntity(Entity entity) {
		super.fromEntity(entity);
		setMemo(entity.getString("memo"));
		setOrderSeq((int)entity.getLong("orderSeq"));
		return this;
	}
}
