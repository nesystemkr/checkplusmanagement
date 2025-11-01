package kr.peelknight.common.model;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_Code extends Model {
	private String type;
	private String code;
	private String codeName;
	private String codeNameLocale;
	private String comment;
	private int orderSeq;
	public String getType() {
		return type;
	}
	public void  setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void  setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void  setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeNameLocale() {
		return codeNameLocale;
	}
	public void setCodeNameLocale(String codeNameLocale) {
		this.codeNameLocale = codeNameLocale;
	}
	public String getComment() {
		return comment;
	}
	public void  setComment(String comment) {
		this.comment = comment;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
//	public void l10n(HttpSession session) {
//		codeNameLocale = L10N.get(codeName, session);
//	}
}
