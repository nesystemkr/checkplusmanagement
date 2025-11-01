package kr.peelknight.fragment.model;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;
//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_GridColModel extends Model {
	public static final int COLALIGN_LEFT = 0;
	public static final int COLALIGN_CENTER = 1;
	public static final int COLALIGN_RIGHT = 2;
	public static final int COLFORMAT_NONE = 0;
	public static final int COLFORMAT_DATE = 1;
	public static final int COLFORMAT_DATETIME = 2;
	public static final int COLFORMAT_COMMANUMBER = 3;
	public static final int EDITTYPE_NONE = 0; //수정불가
	public static final int EDITTYPE_EDITABLE = 1; //입력,수정가능
	public static final int EDITTYPE_NEWONLY = 2; //입력가능,수정불가
	
	private long idKey;
	private long gridIdKey;
	private String name;
	private String hidden;
	private String label;
	private int width;
	private int align;
	private int format;
	private String codeType;
	private int editType;
	private String mandatory;
	private int buttonPreSet;
	private int orderSeq;
	private List<CM_GridColButton> buttons;
	private Map<String, String> selectValues;
	private List<CM_GridColButton> list;
	public CM_GridColModel() {
		hidden = "N";
		mandatory = "N";
		buttons = null;
		selectValues = null;
		list = null;
	}
	public CM_GridColModel(long idKey, long gridIdKey, String name, String hidden, String label, int width, int align, int format, String codeType, int editType, String mandatory, int orderSeq) {
		this.idKey = idKey;
		this.gridIdKey = gridIdKey;
		this.name = name;
		this.hidden = hidden;
		this.label = label;
		this.width = width;
		this.align = align;
		this.format = format;
		this.codeType = codeType;
		this.editType = editType;
		this.mandatory = mandatory;
		this.orderSeq = orderSeq;
	}
	public CM_GridColModel(String name, String hidden) {
		this();
		this.name = name;
		this.hidden = hidden;
		this.setTouch("I");
	}
	public CM_GridColModel(String name, String label, int width, int align, int format) {
		this(0, 0, name, "N", label, width, align, format, null, EDITTYPE_NONE, "N", 0);
		this.setTouch("I");
	}
	public CM_GridColModel(String name, String label, int width, int align) {
		this(name, label, width, align, COLFORMAT_NONE);
	}
	public CM_GridColModel(String name, String label, int align) {
		this(name, label, 0, align, COLFORMAT_NONE);
	}
	public CM_GridColModel(String name, String label, int width, String codeType) {
		this(name, label, width, codeType, COLALIGN_LEFT);
	}
	public CM_GridColModel(String name, String label, int width, String codeType, int align) {
		this(0, 0, name, "N", label, width, align, COLFORMAT_NONE, codeType, EDITTYPE_EDITABLE, "N", 0);
		this.setTouch("I");
	}
	public CM_GridColModel(String name, String label, int width, int align, int format, int editType) {
		this(0, 0, name, "N", label, width, align, format, null, editType, "N", 0);
		this.setTouch("I");
	}
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public long getGridIdKey() {
		return gridIdKey;
	}
	public void setGridIdKey(long gridIdKey) {
		this.gridIdKey = gridIdKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHidden() {
		return hidden;
	}
	public void setHidden(String hidden) {
		this.hidden = hidden;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getAlign() {
		return align;
	}
	public void setAlign(int align) {
		this.align = align;
	}
	public int getFormat() {
		return format;
	}
	public void setFormat(int format) {
		this.format = format;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public int getEditType() {
		return editType;
	}
	public void setEditType(int editType) {
		this.editType = editType;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public int getButtonPreSet() {
		return buttonPreSet;
	}
	public void setButtonPreSet(int buttonPreSet) {
		this.buttonPreSet = buttonPreSet;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
	public List<CM_GridColButton> getButtons() {
		return buttons;
	}
	public void setButtons(List<CM_GridColButton> buttons) {
		this.buttons = buttons;
	}
	public Map<String, String> getSelectValues() {
		return selectValues;
	}
	public void setSelectValues(Map<String, String> selectValues) {
		this.selectValues = selectValues;
	}
	public List<CM_GridColButton> getList() {
		return list;
	}
	public void setList(List<CM_GridColButton> list) {
		this.list = list;
	}
	public void l10n(HttpSession session) {
//		this.label = L10N.get(label, session);
		if (buttons != null) {
			for (int ii=0; ii<buttons.size(); ii++) {
				buttons.get(ii).l10n(session);
			}
		}
	}
}
