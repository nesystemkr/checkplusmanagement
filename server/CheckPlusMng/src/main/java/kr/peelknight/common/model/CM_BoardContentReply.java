package kr.peelknight.common.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_BoardContentReply extends Model {
	private long idKey;
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
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
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
}
