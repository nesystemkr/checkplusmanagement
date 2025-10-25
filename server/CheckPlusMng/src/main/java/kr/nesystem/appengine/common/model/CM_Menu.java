package kr.nesystem.appengine.common.model;

import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

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
	private List<CM_Menu> subMenus;
	public CM_Menu() {
		super();
		hasIdKey = true;
	}
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
	public List<CM_Menu> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<CM_Menu> subMenus) {
		this.subMenus = subMenus;
	}
	@Override
	public String key() {
		return String.valueOf(idKey);
	}
	@Override
	public FullEntity<IncompleteKey> toEntityAutoInc(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("parentIdKey", parentIdKey)
				.set("menuName", N2Z(menuName))
				.set("menuLocale", N2Z(menuLocale))
				.set("menuUrl", N2Z(menuUrl))
				.set("comment", N2Z(comment))
				.set("status", N2Z(status))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public Entity toEntity(KeyFactory keyfactory) {
		return null;
	}
	@Override
	public Entity toEntity(Key key, Entity existOne) {
		return Entity.newBuilder(key, existOne)
				.set("parentIdKey", parentIdKey)
				.set("menuName", N2Z(menuName))
				.set("menuLocale", N2Z(menuLocale))
				.set("menuUrl", N2Z(menuUrl))
				.set("comment", N2Z(comment))
				.set("status", N2Z(status))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public Model fromEntity(Entity entity) {
		setIdKey(entity.getKey().getId());
		setParentIdKey(entity.getLong("parentIdKey"));
		setMenuName(entity.getString("menuName"));
		setMenuLocale(entity.getString("menuLocale"));
		setMenuUrl(entity.getString("menuUrl"));
		setComment(entity.getString("comment"));
		setStatus(entity.getString("status"));
		setOrderSeq((int)entity.getLong("orderSeq"));
		return this;
	}
}
