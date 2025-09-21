package kr.peelknight.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_AttachmentGroup extends Model {
	private long idKey;
	private int groupFileCount;
	private long userIdKey;
	private List<CM_Attachment> attachments;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
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
}
