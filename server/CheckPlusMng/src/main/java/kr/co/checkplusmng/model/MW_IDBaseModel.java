package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.FullEntity.Builder;

public abstract class MW_IDBaseModel extends MW_BaseModel {
	private String idString;
	public MW_IDBaseModel() {
		super();
	}
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	public <K extends IncompleteKey> Builder<IncompleteKey> builder(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("idString", N2Z(idString));
	}
	public Entity.Builder builder(Entity existOne) {
		return super.builder(existOne)
				.set("idString", N2Z(idString));
	}
	@Override
	public MW_IDBaseModel fromEntity(Entity entity) {
		super.fromEntity(entity);
		setIdString(entity.getString("idString"));
		return this;
	}
}
