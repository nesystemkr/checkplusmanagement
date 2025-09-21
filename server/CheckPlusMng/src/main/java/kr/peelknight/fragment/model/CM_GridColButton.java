package kr.peelknight.fragment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_GridColButton extends Model {
	public static final int GRIDBUTTON_CUSTOM = 0;
	public static final int GRIDBUTTON_DETAIL = 1;
	public static final int GRIDBUTTON_UPDATE = 2;
	public static final int GRIDBUTTON_DELETE = 3;
	private long idKey;
	private long gridIdKey;
	private long colIdKey;
	private int preSet;
	private String iconCls;
	private String callback;
	private int orderSeq;
	public CM_GridColButton(long idKey, long gridIdKey, long colIdKey, int preSet, String iconCls, String callback, int orderSeq) {
		this.idKey = idKey;
		this.gridIdKey = gridIdKey;
		this.colIdKey = colIdKey;
		this.preSet = preSet;
		this.iconCls = iconCls;
		this.callback = callback;
		this.orderSeq = orderSeq;
	}
	public CM_GridColButton(int preSet) {
		this.preSet = preSet;
		this.setTouch("I");
	}
	public CM_GridColButton(String iconCls, String callback) {
		this.preSet = GRIDBUTTON_CUSTOM;
		this.iconCls = iconCls;
		this.callback = callback;
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
	public long getColIdKey() {
		return colIdKey;
	}
	public void setColIdKey(long colIdKey) {
		this.colIdKey = colIdKey;
	}
	public int getPreSet() {
		return preSet;
	}
	public void setPreSet(int preSet) {
		this.preSet = preSet;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
}
