package kr.peelknight.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_Menu extends Model {
	private long idKey;
	private long parentIdKey;
	private String menuName;
	private String menuLocale;
	private String menuUrl;
	private String comment;
	private String status;
	private String statusName;
	private int orderSeq;
	private List<CM_MenuAuth> menuAuths;
	private List<CM_Menu> subMenus;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public long getParentIdKey() {
		return parentIdKey;
	}
	public void setParentIdKey(long parentIdKey) {
		this.parentIdKey = parentIdKey;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuLocale() {
		return menuLocale;
	}
	public void setMenuLocale(String menuLocale) {
		this.menuLocale = menuLocale;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
	public List<CM_MenuAuth> getMenuAuths() {
		return menuAuths;
	}
	public void setMenuAuths(List<CM_MenuAuth> menuAuths) {
		this.menuAuths = menuAuths;
	}
	public List<CM_Menu> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<CM_Menu> subMenus) {
		this.subMenus = subMenus;
	}
//	public void l10n(String lang) {
//		menuLocale = L10N.get(menuName, lang);
//		statusName = L10N.get(statusName, lang);
//	}
}
