package kr.peelknight.fragment.model;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.CM_Code;
import kr.peelknight.common.model.Model;
//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_PopupRow extends Model {
	public static final int DATATYPE_NONE     = 0;
	public static final int DATATYPE_STRING   = 1;
	public static final int DATATYPE_CODETYPE = 2;
	public static final int DATATYPE_INTEGER  = 3;
	public static final int DATATYPE_LONG     = 4;
	public static final int DATATYPE_FLOAT    = 5;
	public static final int DATATYPE_DOUBLE   = 6;
	public static final int DATATYPE_DATE     = 7;
	public static final int DATATYPE_DATETIME = 8;
	public static final int DATATYPE_LOCATION = 9;
	public static final int DATATYPE_ATTACHMENT = 10;
	public static final int SHOWTYPE_NONE   = 0; //모두보이지 않는다.
	public static final int SHOWTYPE_DETAIL = 1; //상세창만 보인다.
	public static final int SHOWTYPE_INSERT = 2; //입력창만 보인다. 수정가능
	public static final int SHOWTYPE_UPDATE = 4; //수정창만 보인다. 수정가능
	public static final int SHOWTYPE_INSDIS = 8; //읽기전용 일경우
	public static final int SHOWTYPE_UPDDIS = 16; //읽기전용 일경우
	public static final int SHOWTYPE_ALL = SHOWTYPE_DETAIL | SHOWTYPE_INSERT | SHOWTYPE_UPDATE;
	
	private long idKey;
	private long popupIdKey;
	private String hidden;
	private String title;
	private String name;
	private int dataType;
	private String mandatory;
	private String mandatoryErrorMsg;
	private String defaultValue;
	private String codeType;
	private int showType;
	private int titleColSpan;
	private int valueColSpan;
	private String titleWidth;
	private String valueWidth;
	private int rowSeq;
	private int orderSeq;
	private List<CM_Code> codes;
	public CM_PopupRow() {
		hidden = "N";
		mandatory = "N";
		codes = null;
	}
	public CM_PopupRow(long idKey, long popupIdKey, String hidden, String title, String name, int dataType, String mandatory, String mandatoryErrorMsg, String defaultValue, String codeType, int showType, int titleColSpan, int valueColSpan, String titleWidth, String valueWidth, int rowSeq, int orderSeq) {
		this.idKey = idKey;
		this.popupIdKey = popupIdKey;
		this.hidden = hidden;
		this.title = title;
		this.name = name;
		this.dataType = dataType;
		this.mandatory = mandatory;
		this.mandatoryErrorMsg = mandatoryErrorMsg;
		this.defaultValue = defaultValue;
		this.codeType = codeType;
		this.showType = showType;
		this.titleColSpan = titleColSpan;
		this.valueColSpan = valueColSpan;
		this.titleWidth = titleWidth;
		this.valueWidth = valueWidth;
		this.rowSeq = rowSeq;
		this.orderSeq = orderSeq;
	}
	public CM_PopupRow(String title, String name, int dataType, String mandatory, String mandatoryErrorMsg,
					   String codeType, int showType) {
		this(0, 0, "N", title, name, dataType, mandatory, mandatoryErrorMsg, null, codeType, showType, 0, 0, null, null, 0, 0); 
		this.setTouch("I");
	}
	public CM_PopupRow(String name, String hidden) {
		this(0, 0, hidden, null, name, DATATYPE_NONE, "N", null, null, null, SHOWTYPE_NONE, 0, 0, null, null, 0, 0); 
		this.setTouch("I");
	}
	public CM_PopupRow(String title, String name, String codeType) {
		this(title, name, DATATYPE_CODETYPE, "N", null, codeType, SHOWTYPE_ALL);
	}
	public CM_PopupRow(String title, String name, String codeType, String defaultValue) {
		this(title, name, DATATYPE_CODETYPE, "N", null, codeType, SHOWTYPE_ALL);
		this.defaultValue = defaultValue;
	}
	public CM_PopupRow(String title, String name, String codeType, String mandatory, String mandatoryErrorMsg) {
		this(title, name, DATATYPE_CODETYPE, mandatory, mandatoryErrorMsg, codeType, SHOWTYPE_ALL);
	}
	public CM_PopupRow(String title, String name, String codeType, String mandatory, String mandatoryErrorMsg, String defaultValue) {
		this(title, name, DATATYPE_CODETYPE, mandatory, mandatoryErrorMsg, codeType, SHOWTYPE_ALL);
		this.defaultValue = defaultValue;
	}
	public CM_PopupRow(String title, String name, int dataType) {
		this(title, name, dataType, "N", null, null, SHOWTYPE_ALL);
	}
	public CM_PopupRow(String title, String name, int dataType, String defaultValue) {
		this(title, name, dataType, "N", null, null, SHOWTYPE_ALL);
		this.defaultValue = defaultValue;
	}
	public CM_PopupRow(String title, String name, int dataType, String mandatory, String mandatoryErrorMsg) {
		this(title, name, dataType, mandatory, mandatoryErrorMsg, null, SHOWTYPE_ALL);
	}
	public CM_PopupRow(String title, String name, int dataType, String mandatory, String mandatoryErrorMsg, String defaultValue) {
		this(title, name, dataType, mandatory, mandatoryErrorMsg, null, SHOWTYPE_ALL);
		this.defaultValue = defaultValue;
	}
	public CM_PopupRow(String title, String name, int dataType, int showType) {
		this(title, name, dataType, "N", null, null, showType);
	}
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public long getPopupIdKey() {
		return popupIdKey;
	}
	public void setPopupIdKey(long popupIdKey) {
		this.popupIdKey = popupIdKey;
	}
	public String getHidden() {
		return hidden;
	}
	public void setHidden(String hidden) {
		this.hidden = hidden;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getMandatoryErrorMsg() {
		return mandatoryErrorMsg;
	}
	public void setMandatoryErrorMsg(String mandatoryErrorMsg) {
		this.mandatoryErrorMsg = mandatoryErrorMsg;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public int getShowType() {
		return showType;
	}
	public void setShowType(int showType) {
		this.showType = showType;
	}
	public int getTitleColSpan() {
		return titleColSpan;
	}
	public void setTitleColSpan(int titleColSpan) {
		this.titleColSpan = titleColSpan;
	}
	public int getValueColSpan() {
		return valueColSpan;
	}
	public void setValueColSpan(int valueColSpan) {
		this.valueColSpan = valueColSpan;
	}
	public String getTitleWidth() {
		return titleWidth;
	}
	public void setTitleWidth(String titleWidth) {
		this.titleWidth = titleWidth;
	}
	public String getValueWidth() {
		return valueWidth;
	}
	public void setValueWidth(String valueWidth) {
		this.valueWidth = valueWidth;
	}
	public int getRowSeq() {
		return rowSeq;
	}
	public void setRowSeq(int rowSeq) {
		this.rowSeq = rowSeq;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
	public List<CM_Code> getCodes() {
		return codes;
	}
	public void setCodes(List<CM_Code> codes) {
		this.codes = codes;
	}
	public void l10n(HttpSession session) {
//		title = L10N.get(title, session);
		if (codes != null) {
			for (int ii=0; ii<codes.size(); ii++) {
				codes.get(ii).l10n(session);;
			}
		}
	}
}
