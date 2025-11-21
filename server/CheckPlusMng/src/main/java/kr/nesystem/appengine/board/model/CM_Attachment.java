package kr.nesystem.appengine.board.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class CM_Attachment extends GAEAutoIncModel {
	private long groupIdKey;
	private String fileName;
	private int fileSize;
	private int downCount;
	private String serverFilePath;
	public long getGroupIdKey() {
		return groupIdKey;
	}
	public void setGroupIdKey(long groupIdKey) {
		this.groupIdKey = groupIdKey;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public int getDownCount() {
		return downCount;
	}
	public void setDownCount(int downCount) {
		this.downCount = downCount;
	}
	public String getServerFilePath() {
		return serverFilePath;
	}
	public void setServerFilePath(String serverFilePath) {
		this.serverFilePath = serverFilePath;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("groupIdKey", groupIdKey)
				.set("fileName", N2Z(fileName))
				.set("fileSize", fileSize)
				.set("downCount", downCount)
				.set("serverFilePath", N2Z(serverFilePath))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("groupIdKey", groupIdKey)
				.set("fileName", N2Z(fileName))
				.set("fileSize", fileSize)
				.set("downCount", downCount)
				.set("serverFilePath", N2Z(serverFilePath))
				.build();
	}
	@Override
	public CM_Attachment fromEntity(Entity entity) {
		super.fromEntity(entity);
		setGroupIdKey(entity.getLong("groupIdKey"));
		setFileName(entity.getString("fileName"));
		setFileSize((int)entity.getLong("fileSize"));
		setDownCount((int)entity.getLong("downCount"));
		setServerFilePath(entity.getString("serverFilePath"));
		return this;
	}
}
