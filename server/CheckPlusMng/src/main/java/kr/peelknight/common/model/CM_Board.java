package kr.peelknight.common.model;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_Board extends Model {
	private long idKey;
	private String boardType;
	private String boardId;
	private String boardName;
	private String attachmentYN;
	private String answerYN;
	private String replyYN;
	private String topYN;
	private String loginViewYN;
	private String secretYN;
	private String status;
	private String statusName;
	private List<CM_BoardAuth> boardAuths;
	private boolean allowPrivateViewYN;
	private boolean allowUpdateYN;
	private boolean allowUpdateOthersYN;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public String getBoardType() {
		return boardType;
	}
	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}
	public String getBoardId() {
		return boardId;
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	public String getAttachmentYN() {
		return attachmentYN;
	}
	public void setAttachmentYN(String attachmentYN) {
		this.attachmentYN = attachmentYN;
	}
	public String getAnswerYN() {
		return answerYN;
	}
	public void setAnswerYN(String answerYN) {
		this.answerYN = answerYN;
	}
	public String getReplyYN() {
		return replyYN;
	}
	public void setReplyYN(String replyYN) {
		this.replyYN = replyYN;
	}
	public String getTopYN() {
		return topYN;
	}
	public void setTopYN(String topYN) {
		this.topYN = topYN;
	}
	public String getLoginViewYN() {
		return loginViewYN;
	}
	public void setLoginViewYN(String loginViewYN) {
		this.loginViewYN = loginViewYN;
	}
	public String getSecretYN() {
		return secretYN;
	}
	public void setSecretYN(String secretYN) {
		this.secretYN = secretYN;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public List<CM_BoardAuth> getBoardAuths() {
		return boardAuths;
	}
	public void setBoardAuths(List<CM_BoardAuth> boardAuths) {
		this.boardAuths = boardAuths;
	}
	public boolean isAllowPrivateViewYN() {
		return allowPrivateViewYN;
	}
	public void setAllowPrivateViewYN(boolean allowPrivateViewYN) {
		this.allowPrivateViewYN = allowPrivateViewYN;
	}
	public boolean isAllowUpdateYN() {
		return allowUpdateYN;
	}
	public void setAllowUpdateYN(boolean allowUpdateYN) {
		this.allowUpdateYN = allowUpdateYN;
	}
	public boolean isAllowUpdateOthersYN() {
		return allowUpdateOthersYN;
	}
	public void setAllowUpdateOthersYN(boolean allowUpdateOthersYN) {
		this.allowUpdateOthersYN = allowUpdateOthersYN;
	}
//	public void l10n(HttpSession session) {
//		statusName = L10N.get(statusName, session);
//	}
}
