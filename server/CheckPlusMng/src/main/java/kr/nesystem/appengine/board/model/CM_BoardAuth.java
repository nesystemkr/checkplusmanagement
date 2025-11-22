package kr.nesystem.appengine.board.model;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.GAEAutoIncModel;
import kr.nesystem.appengine.common.util.CodeStore;
import kr.nesystem.appengine.common.util.L10N;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_BoardAuth extends GAEAutoIncModel {
	private long boardIdKey;
	private String userType;
	private String allowPrivateViewYN;
	private String allowUpdateYN;
	private String allowUpdateOthersYN;
	private String userTypeName;
	public long getBoardIdKey() {
		return boardIdKey;
	}
	public void setBoardIdKey(long boardIdKey) {
		this.boardIdKey = boardIdKey;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	public String getAllowPrivateViewYN() {
		return allowPrivateViewYN;
	}
	public void setAllowPrivateViewYN(String allowPrivateViewYN) {
		this.allowPrivateViewYN = allowPrivateViewYN;
	}
	public String getAllowUpdateYN() {
		return allowUpdateYN;
	}
	public void setAllowUpdateYN(String allowUpdateYN) {
		this.allowUpdateYN = allowUpdateYN;
	}
	public String getAllowUpdateOthersYN() {
		return allowUpdateOthersYN;
	}
	public void setAllowUpdateOthersYN(String allowUpdateOthersYN) {
		this.allowUpdateOthersYN = allowUpdateOthersYN;
	}
	public void l10n(HttpSession session) {
		userTypeName = CodeStore.get("USERTYPE", userType);
		userTypeName = L10N.get(userTypeName, session);
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("boardIdKey", boardIdKey)
				.set("userType", N2Z(userType))
				.set("allowPrivateViewYN", N2Z(allowPrivateViewYN))
				.set("allowUpdateYN", N2Z(allowUpdateYN))
				.set("allowUpdateOthersYN", N2Z(allowUpdateOthersYN))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("boardIdKey", boardIdKey)
				.set("userType", N2Z(userType))
				.set("allowPrivateViewYN", N2Z(allowPrivateViewYN))
				.set("allowUpdateYN", N2Z(allowUpdateYN))
				.set("allowUpdateOthersYN", N2Z(allowUpdateOthersYN))
				.build();
	}
	@Override
	public CM_BoardAuth fromEntity(Entity entity) {
		super.fromEntity(entity);
		setBoardIdKey((int)entity.getLong("boardIdKey"));
		setUserType(entity.getString("userType"));
		setAllowPrivateViewYN(entity.getString("allowPrivateViewYN"));
		setAllowUpdateYN(entity.getString("allowUpdateYN"));
		setAllowUpdateOthersYN(entity.getString("allowUpdateOthersYN"));
		return this;
	}
}
