package kr.nesystem.model;

import java.io.Serializable;

public class Model implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer errCd;
	private String errMsg;
	private String reqToken;
	private Long no;
	private String touch;
	public Integer getErrCd() {
		return errCd;
	}
	public void setErrCd(Integer errCd) {
		this.errCd = errCd;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getReqToken() {
		return reqToken;
	}
	public void setReqToken(String reqToken) {
		this.reqToken = reqToken;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getTouch() {
		return touch;
	}
	public void setTouch(String touch) {
		this.touch = touch;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
