package kr.peelknight.fragment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_FragmentCachedJson extends Model {
	private long fragmentIdKey;
	private String locale;
	private String cachedJson;
	public long getFragmentIdKey() {
		return fragmentIdKey;
	}
	public void setFragmentIdKey(long fragmentIdKey) {
		this.fragmentIdKey = fragmentIdKey;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getCachedJson() {
		return cachedJson;
	}
	public void setCachedJson(String cachedJson) {
		this.cachedJson = cachedJson;
	}
}
