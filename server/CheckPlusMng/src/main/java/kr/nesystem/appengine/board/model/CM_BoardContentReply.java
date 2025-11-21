package kr.nesystem.appengine.board.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class CM_BoardContentReply extends GAEAutoIncModel {
	private long boardContentIdKey;
	private long parentIdKey;
	private String reply;
	private long attachGroupIdKey;
	private String status;
	private long creator;
	private Date createDate;
	private long modifier;
	private Date modifiedDate;
	private String creatorName;
	private String modifierName;
	private CM_AttachmentGroup attachmentGroup;
	public long getBoardContentIdKey() {
		return boardContentIdKey;
	}
	public void setBoardContentIdKey(long boardContentIdKey) {
		this.boardContentIdKey = boardContentIdKey;
	}
	public long getParentIdKey() {
		return parentIdKey;
	}
	public void setParentIdKey(long parentIdKey) {
		this.parentIdKey = parentIdKey;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public long getAttachGroupIdKey() {
		return attachGroupIdKey;
	}
	public void setAttachGroupIdKey(long attachGroupIdKey) {
		this.attachGroupIdKey = attachGroupIdKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getCreator() {
		return creator;
	}
	public void setCreator(long creator) {
		this.creator = creator;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getModifier() {
		return modifier;
	}
	public void setModifier(long modifier) {
		this.modifier = modifier;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public CM_AttachmentGroup getAttachmentGroup() {
		return attachmentGroup;
	}
	public void setAttachmentGroup(CM_AttachmentGroup attachmentGroup) {
		this.attachmentGroup = attachmentGroup;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("boardContentIdKey", boardContentIdKey)
				.set("parentIdKey", parentIdKey)
				.set("reply", N2Z(reply))
				.set("attachGroupIdKey", attachGroupIdKey)
				.set("status", N2Z(status))
				.set("creator", creator)
				.set("createDate", D2Z(createDate))
				.set("modifier", modifier)
				.set("modifiedDate", D2Z(modifiedDate))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("boardContentIdKey", boardContentIdKey)
				.set("parentIdKey", parentIdKey)
				.set("reply", N2Z(reply))
				.set("attachGroupIdKey", attachGroupIdKey)
				.set("status", N2Z(status))
				.set("creator", creator)
				.set("createDate", D2Z(createDate))
				.set("modifier", modifier)
				.set("modifiedDate", D2Z(modifiedDate))
				.build();
	}
	@Override
	public CM_BoardContentReply fromEntity(Entity entity) {
		super.fromEntity(entity);
		setBoardContentIdKey((int)entity.getLong("boardContentIdKey"));
		setParentIdKey(entity.getLong("parentIdKey"));
		setReply(entity.getString("reply"));
		setAttachGroupIdKey(entity.getLong("attachGroupIdKey"));
		setStatus(entity.getString("status"));
		setCreator(entity.getLong("creator"));
		setCreateDate(L2D(entity.getLong("createDate")));
		setModifier(entity.getLong("modifier"));
		setModifiedDate(L2D(entity.getLong("modifiedDate")));
		return this;
	}
}
