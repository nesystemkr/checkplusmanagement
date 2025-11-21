package kr.nesystem.appengine.common.model;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.util.L10N;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_MenuAuth extends GAEAutoIncModel {
	private String userType;
	private long menuIdKey;
	private String allowYN;
	private String userTypeName;
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public long getMenuIdKey() {
		return menuIdKey;
	}
	public void setMenuIdKey(long menuIdKey) {
		this.menuIdKey = menuIdKey;
	}
	public String getAllowYN() {
		return allowYN;
	}
	public void setAllowYN(String allowYN) {
		this.allowYN = allowYN;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	@Override
	public void l10n(HttpSession session) {
		userTypeName = L10N.get(userTypeName, session);
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("userType", N2Z(userType))
				.set("menuIdKey", menuIdKey)
				.set("allowYN", N2Z(allowYN))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("userType", N2Z(userType))
				.set("menuIdKey", menuIdKey)
				.set("allowYN", N2Z(allowYN))
				.build();
	}
	@Override
	public CM_MenuAuth fromEntity(Entity entity) {
		super.fromEntity(entity);
		setUserType(entity.getString("userType"));
		setMenuIdKey(entity.getLong("menuIdKey"));
		setAllowYN(entity.getString("allowYN"));
		return this;
	}
}
