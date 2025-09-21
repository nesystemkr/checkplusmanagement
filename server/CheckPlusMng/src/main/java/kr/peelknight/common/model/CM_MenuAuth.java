package kr.peelknight.common.model;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_MenuAuth extends Model {
	private long idKey;
	private String userType;
	private long menuIdKey;
	private String allowYN;
	private String userTypeName;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public long getMenuIdKey() {
		return menuIdKey;
	}
	public void setMenuIdKey(long menuIdKey) {
		this.menuIdKey = menuIdKey;
	}
	public String getAllowYN() {
		return allowYN;
	}
	public void setAllowYN(String allowYN) {
		this.allowYN = allowYN;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	public void l10n(HttpSession session) {
		userTypeName = L10N.get(userTypeName, session);
	}
}
