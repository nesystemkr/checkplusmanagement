package kr.nesystem.appengine.common.model;

import java.util.List;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import jakarta.servlet.http.HttpSession;
import kr.peelknight.common.model.CM_Code;
import kr.peelknight.util.L10N;

public class CM_CodeType extends Model {
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
	public void l10n(HttpSession session) {
		typeName = L10N.get(typeName, session);
	}
	@Override
	public String key() {
		return type;
	}
	@Override
	public Entity toEntity(Datastore datastore) {
		return Entity.newBuilder(toKey(datastore))
				.set("type", type)
				.set("typeName", typeName)
				.set("typeNameLocale", typeNameLocale)
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
