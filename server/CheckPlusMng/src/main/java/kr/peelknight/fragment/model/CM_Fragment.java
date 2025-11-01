package kr.peelknight.fragment.model;

import java.util.Date;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;
//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_Fragment extends Model {
	public static final int FRAGMENTTYPE_NONE = 0;
	public static final int FRAGMENTTYPE_GRID = 1;
	public static final int FRAGMENTTYPE_POPUP = 2;
	
	private long idKey;
	private String fragmentId;
	private int fragmentType;
	private String comment;
	private Date createDate;
	private String cachedJson;
	private String fragmentTypeName;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public String getFragmentId() {
		return fragmentId;
	}
	public void setFragmentId(String fragmentId) {
		this.fragmentId = fragmentId;
	}
	public int getFragmentType() {
		return fragmentType;
	}
	public void setFragmentType(int fragmentType) {
		this.fragmentType = fragmentType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCachedJson() {
		return cachedJson;
	}
	public void setCachedJson(String cachedJson) {
		this.cachedJson = cachedJson;
	}
	public String getFragmentTypeName() {
		return fragmentTypeName;
	}
	public void setFragmentTypeName(String fragmentTypeName) {
		this.fragmentTypeName = fragmentTypeName;
	}
//	public void l10n(HttpSession session) {
//		fragmentTypeName = L10N.get(fragmentTypeName, session);
//	}
}
