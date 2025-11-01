package kr.peelknight.common.model;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_BoardAuth extends Model {
	private long idKey;
	private long boardIdKey;
	private String userType;
	private String userTypeName;
	private String allowPrivateViewYN;
	private String allowUpdateYN;
	private String allowUpdateOthersYN;
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
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	public String getAllowPrivateViewYN() {
		return allowPrivateViewYN;
	}
	public void setAllowPrivateViewYN(String allowPrivateViewYN) {
		this.allowPrivateViewYN = allowPrivateViewYN;
	}
	public String getAllowUpdateYN() {
		return allowUpdateYN;
	}
	public void setAllowUpdateYN(String allowUpdateYN) {
		this.allowUpdateYN = allowUpdateYN;
	}
	public String getAllowUpdateOthersYN() {
		return allowUpdateOthersYN;
	}
	public void setAllowUpdateOthersYN(String allowUpdateOthersYN) {
		this.allowUpdateOthersYN = allowUpdateOthersYN;
	}
//	public void l10n(HttpSession session) {
//		userTypeName = L10N.get(userTypeName, session);
//	}
}
