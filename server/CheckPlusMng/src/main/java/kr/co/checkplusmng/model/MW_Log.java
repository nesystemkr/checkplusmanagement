package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class MW_Log extends GAEAutoIncModel {
	private String tableName;
	private Date logDate;
	private long userIdKey;
	private String operation;
	private String logData;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public long getUserIdKey() {
		return userIdKey;
	}
	public void setUserIdKey(long userIdKey) {
		this.userIdKey = userIdKey;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getLogData() {
		return logData;
	}
	public void setLogData(String logData) {
		this.logData = logData;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("tableName", N2Z(tableName))
				.set("logDate", D2Z(logDate))
				.set("userIdKey", userIdKey)
				.set("operation", N2Z(operation))
				.set("logData", N2Z(logData))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("tableName", N2Z(tableName))
				.set("logDate", D2Z(logDate))
				.set("userIdKey", userIdKey)
				.set("operation", N2Z(operation))
				.set("logData", N2Z(logData))
				.build();
	}
	@Override
	public MW_Log fromEntity(Entity entity) {
		super.fromEntity(entity);
		setTableName(entity.getString("tableName"));
		setLogDate(L2D(entity.getLong("logDate")));
		setUserIdKey(entity.getLong("userIdKey"));
		setOperation(entity.getString("operation"));
		setLogData(entity.getString("logData"));
 		return this;
 	}

}
