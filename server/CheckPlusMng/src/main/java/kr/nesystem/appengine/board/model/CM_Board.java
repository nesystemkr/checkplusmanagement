package kr.nesystem.appengine.board.model;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.GAEAutoIncModel;
import kr.nesystem.appengine.common.util.L10N;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_Board extends GAEAutoIncModel {
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
	private boolean allowPrivateViewYN;
	private boolean allowUpdateYN;
	private boolean allowUpdateOthersYN;
	private String statusName;
	private List<CM_BoardAuth> boardAuths;
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
	public void l10n(HttpSession session) {
		statusName = L10N.get(statusName, session);
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("boardType", N2Z(boardType))
				.set("boardId", N2Z(boardId))
				.set("boardName", N2Z(boardName))
				.set("attachmentYN", N2Z(attachmentYN))
				.set("answerYN", N2Z(answerYN))
				.set("replyYN", N2Z(replyYN))
				.set("topYN", N2Z(topYN))
				.set("loginViewYN", N2Z(loginViewYN))
				.set("secretYN", N2Z(secretYN))
				.set("status", N2Z(status))
				.set("allowPrivateViewYN", allowPrivateViewYN)
				.set("allowUpdateYN", allowUpdateYN)
				.set("allowUpdateOthersYN", allowUpdateOthersYN)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("boardType", N2Z(boardType))
				.set("boardId", N2Z(boardId))
				.set("boardName", N2Z(boardName))
				.set("attachmentYN", N2Z(attachmentYN))
				.set("answerYN", N2Z(answerYN))
				.set("replyYN", N2Z(replyYN))
				.set("topYN", N2Z(topYN))
				.set("loginViewYN", N2Z(loginViewYN))
				.set("secretYN", N2Z(secretYN))
				.set("status", N2Z(status))
				.set("allowPrivateViewYN", allowPrivateViewYN)
				.set("allowUpdateYN", allowUpdateYN)
				.set("allowUpdateOthersYN", allowUpdateOthersYN)
				.build();
	}
	@Override
	public CM_Board fromEntity(Entity entity) {
		super.fromEntity(entity);
		setBoardType(entity.getString("boardType"));
		setBoardId(entity.getString("boardId"));
		setBoardName(entity.getString("boardName"));
		setAttachmentYN(entity.getString("attachmentYN"));
		setAnswerYN(entity.getString("answerYN"));
		setReplyYN(entity.getString("replyYN"));
		setTopYN(entity.getString("topYN"));
		setLoginViewYN(entity.getString("loginViewYN"));
		setSecretYN(entity.getString("secretYN"));
		setStatus(entity.getString("status"));
		setAllowPrivateViewYN(entity.getBoolean("allowPrivateViewYN"));
		setAllowUpdateYN(entity.getBoolean("allowUpdateYN"));
		setAllowUpdateOthersYN(entity.getBoolean("allowUpdateOthersYN"));
		return this;
	}
}
