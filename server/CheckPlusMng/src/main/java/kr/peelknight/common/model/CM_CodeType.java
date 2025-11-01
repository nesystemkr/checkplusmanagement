package kr.peelknight.common.model;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_CodeType extends Model {
	private String type;
	private String typeName;
	private String typeNameLocale;
	private List<CM_Code> codes;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeNameLocale() {
		return typeNameLocale;
	}
	public void setTypeNameLocale(String typeNameLocale) {
		this.typeNameLocale = typeNameLocale;
	}
	public List<CM_Code> getCodes() {
		return codes;
	}
	public void setCodes(List<CM_Code> codes) {
		this.codes = codes;
	}
//	public void l10n(HttpSession session) {
//		typeName = L10N.get(typeName, session);
//	}
}
