package kr.nesystem.appengine.board.model;

import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class CM_AttachmentGroup extends GAEAutoIncModel {
	private int groupFileCount;
	private long userIdKey;
	private List<CM_Attachment> attachments;
	public int getGroupFileCount() {
		return groupFileCount;
	}
	public void setGroupFileCount(int groupFileCount) {
		this.groupFileCount = groupFileCount;
	}
	public long getUserIdKey() {
		return userIdKey;
	}
	public void setUserIdKey(long userIdKey) {
		this.userIdKey = userIdKey;
	}
	public List<CM_Attachment> getAttachments(){
		return attachments;
	}
	public void setAttachments(List<CM_Attachment> attachments){
		this.attachments = attachments;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("groupFileCount", groupFileCount)
				.set("userIdKey", userIdKey)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("groupFileCount", groupFileCount)
				.set("userIdKey", userIdKey)
				.build();
	}
	@Override
	public CM_AttachmentGroup fromEntity(Entity entity) {
		super.fromEntity(entity);
		setGroupFileCount((int)entity.getLong("groupFileCount"));
		setUserIdKey(entity.getLong("userIdKey"));
		return this;
	}
}
