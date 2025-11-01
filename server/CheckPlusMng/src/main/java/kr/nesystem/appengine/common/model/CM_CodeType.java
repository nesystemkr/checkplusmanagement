package kr.nesystem.appengine.common.model;

import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.util.L10N;
import kr.peelknight.common.model.CM_Code;

public class CM_CodeType extends GAEModel {
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
	@Override
	public Object key() {
		return type;
	}
	@Override
	public FullEntity<IncompleteKey> toEntityAutoInc(KeyFactory keyFactory) {
		return null;
	}
	@Override
	public Entity toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(toKey(keyFactory))
				.set("type", type)
				.set("typeName", N2Z(typeName))
				.set("typeNameLocale", N2Z(typeNameLocale))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("typeName", N2Z(typeName))
				.set("typeNameLocale", N2Z(typeNameLocale))
				.build();
	}
	@Override
	public CM_CodeType fromEntity(Entity entity) {
		setType(entity.getString("type"));
		setTypeName(entity.getString("typeName"));
		setTypeNameLocale(entity.getString("typeNameLocale"));
		return this;
	}
}
