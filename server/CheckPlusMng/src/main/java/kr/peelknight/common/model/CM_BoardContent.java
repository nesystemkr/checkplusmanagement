package kr.peelknight.common.model;

import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_BoardContent extends Model {
	private long idKey;
	private long boardIdKey;
	private String title;
	private String content;
	private long attachGroupIdKey;
	private long parentIdKey;
	private String privateYN;
	private String topYN;
	private int viewCount;
	private int replyCount;
	private String status;
	private long creator;
	private Date createDate;
	private long modifier;
	private Date modifiedDate;
	private String creatorName;
	private String modifierName;
	private String statusName;
	private CM_AttachmentGroup attachmentGroup;
	private List<CM_BoardContent> answers;
	private List<CM_BoardContentReply> replies;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public long getBoardIdKey() {
		return boardIdKey;
	}
	public void setBoardIdKey(long boardIdKey) {
		this.boardIdKey = boardIdKey;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getAttachGroupIdKey() {
		return attachGroupIdKey;
	}
	public void setAttachGroupIdKey(long attachGroupIdKey) {
		this.attachGroupIdKey = attachGroupIdKey;
	}
	public long getParentIdKey() {
		return parentIdKey;
	}
	public void setParentIdKey(long parentIdKey) {
		this.parentIdKey = parentIdKey;
	}
	public String getPrivateYN() {
		return privateYN;
	}
	public void setPrivateYN(String privateYN) {
		this.privateYN = privateYN;
	}
	public String getTopYN() {
		return topYN;
	}
	public void setTopYN(String topYN) {
		this.topYN = topYN;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
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
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public CM_AttachmentGroup getAttachmentGroup() {
		return attachmentGroup;
	}
	public void setAttachmentGroup(CM_AttachmentGroup attachmentGroup) {
		this.attachmentGroup = attachmentGroup;
	}
	public List<CM_BoardContent> getAnswers() {
		return answers;
	}
	public void setAnswers(List<CM_BoardContent> answers) {
		this.answers = answers;
	}
	public List<CM_BoardContentReply> getReplies() {
		return replies;
	}
	public void setReplies(List<CM_BoardContentReply> replies) {
		this.replies = replies;
	}
//	public void l10n(HttpSession session) {
//		statusName = L10N.get(statusName, session);
//	}
}
