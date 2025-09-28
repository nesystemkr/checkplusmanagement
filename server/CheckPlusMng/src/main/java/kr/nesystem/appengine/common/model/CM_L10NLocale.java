package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

public class CM_L10NLocale extends Model {
	private long idKey;
	private String idString;
	private String locale;
	private String localeString;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
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
	public String key() {
		return String.valueOf(getIdKey());
	}
	@Override
	public Entity toEntity(Datastore datastore) {
		KeyFactory keyFactory = datastore.newKeyFactory().setKind("L10NLocale");
		Key key = keyFactory.newKey(idKey); // ID가 있는 키 생성
		return Entity.newBuilder(key)
				.set("idKey", idKey)
				.set("idString", idString)
				.set("locale", locale)
				.set("localeString", localeString)
				.build();
	}
	@Override
	public CM_L10NLocale fromEntity(Entity entity) {
		return null;
	}
}
