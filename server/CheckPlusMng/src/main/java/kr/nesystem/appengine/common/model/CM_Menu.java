package kr.nesystem.appengine.common.model;

import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.util.L10N;

public class CM_Menu extends GAEAutoIncModel {
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
	@Override
	public void l10n(HttpSession session) {
		menuLocale = L10N.get(menuName, session);
		statusName = L10N.get(statusName, session);
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("parentIdKey", parentIdKey)
				.set("menuName", N2Z(menuName))
				.set("menuUrl", N2Z(menuUrl))
				.set("comment", N2Z(comment))
				.set("status", N2Z(status))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("parentIdKey", parentIdKey)
				.set("menuName", N2Z(menuName))
				.set("menuUrl", N2Z(menuUrl))
				.set("comment", N2Z(comment))
				.set("status", N2Z(status))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public CM_Menu fromEntity(Entity entity) {
		super.fromEntity(entity);
		setParentIdKey(entity.getLong("parentIdKey"));
		setMenuName(entity.getString("menuName"));
		setMenuUrl(entity.getString("menuUrl"));
		setComment(entity.getString("comment"));
		setStatus(entity.getString("status"));
		setOrderSeq((int)entity.getLong("orderSeq"));
		return this;
	}
}
