package kr.nesystem.appengine.common.model;

import jakarta.servlet.http.HttpSession;

public abstract class Model {
	private Long no;
	private String authToken;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public void l10n(HttpSession session) {
	}
}
