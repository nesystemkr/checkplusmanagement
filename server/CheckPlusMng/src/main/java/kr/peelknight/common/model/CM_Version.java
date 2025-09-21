package kr.peelknight.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_Version extends Model {
	private long idKey;
	private String version;
	private String status;
	private String iosUpgradeUrl;
	private String androidUpgradeUrl;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIosUpgradeUrl() {
		return iosUpgradeUrl;
	}
	public void setIosUpgradeUrl(String iosUpgradeUrl) {
		this.iosUpgradeUrl = iosUpgradeUrl;
	}
	public String getAndroidUpgradeUrl() {
		return androidUpgradeUrl;
	}
	public void setAndroidUpgradeUrl(String androidUpgradeUrl) {
		this.androidUpgradeUrl = androidUpgradeUrl;
	}
}
