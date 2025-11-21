package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_L10NLocale extends GAEAutoIncModel {
	private String idString;
	private String locale;
	private String localeString;
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getLocaleString() {
		return localeString;
	}
	public void setLocaleString(String localeString) {
		this.localeString = localeString;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("idString", N2Z(idString))
				.set("locale", N2Z(locale))
				.set("localeString", N2Z(localeString))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("idString", N2Z(idString))
				.set("locale", N2Z(locale))
				.set("localeString", N2Z(localeString))
				.build();
	}
	@Override
	public CM_L10NLocale fromEntity(Entity entity) {
		super.fromEntity(entity);
		setIdString(entity.getString("idString"));
		setLocale(entity.getString("locale"));
		setLocaleString(entity.getString("localeString"));
		return this;
	}
}
