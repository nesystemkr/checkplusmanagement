package kr.nesystem.appengine.common.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

import jakarta.servlet.http.HttpSession;
import kr.peelknight.util.L10N;

public class CM_Code extends Model {
	private String type;
	private String code;
	private String codeName;
	private String codeNameLocale;
	private String comment;
	private int orderSeq;
	public String getType() {
		return type;
	}
	public void  setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void  setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void  setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeNameLocale() {
		return codeNameLocale;
	}
	public void setCodeNameLocale(String codeNameLocale) {
		this.codeNameLocale = codeNameLocale;
	}
	public String getComment() {
		return comment;
	}
	public void  setComment(String comment) {
		this.comment = comment;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
	public void l10n(HttpSession session) {
		codeNameLocale = L10N.get(codeName, session);
	}
	@Override
	public String key() {
		return type + "__" + code;
	}
	@Override
	public FullEntity<IncompleteKey> toEntityAutoInc(KeyFactory keyFactory) {
		return null;
	}
	@Override
	public Entity toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(toKey(keyFactory))
				.set("typeCode", key())
				.set("type", type)
				.set("code", code)
				.set("codeName", N2Z(codeName))
				.set("codeNameLocale", N2Z(codeNameLocale))
				.set("comment", N2Z(comment))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public Entity toEntity(Key key, Entity existOne) {
		return Entity.newBuilder(key, existOne)
				.set("codeName", N2Z(codeName))
				.set("codeNameLocale", N2Z(codeNameLocale))
				.set("comment", N2Z(comment))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public CM_Code fromEntity(Entity entity) {
		setType(entity.getString("type"));
		setCode(entity.getString("code"));
		setCodeName(entity.getString("codeName"));
		setCodeNameLocale(entity.getString("codeNameLocale"));
		setComment(entity.getString("comment"));
		setOrderSeq((int)entity.getLong("orderSeq"));
		return this;
	}
}
