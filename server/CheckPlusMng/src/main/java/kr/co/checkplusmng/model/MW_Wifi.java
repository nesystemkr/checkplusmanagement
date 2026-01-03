package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Wifi extends MW_IDBaseModel {
	private String modelName;
	private String serialNo;
	private String macAddress;
	private String gateId;
	private String gatePw;
	private String wifiId;
	private String wifiPw;
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getGateId() {
		return gateId;
	}
	public void setGateId(String gateId) {
		this.gateId = gateId;
	}
	public String getGatePw() {
		return gatePw;
	}
	public void setGatePw(String gatePw) {
		this.gatePw = gatePw;
	}
	public String getWifiId() {
		return wifiId;
	}
	public void setWifiId(String wifiId) {
		this.wifiId = wifiId;
	}
	public String getWifiPw() {
		return wifiPw;
	}
	public void setWifiPw(String wifiPw) {
		this.wifiPw = wifiPw;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("modelName", N2Z(modelName))
				.set("serialNo", N2Z(serialNo))
				.set("macAddress", N2Z(macAddress))
				.set("gateId", N2Z(gateId))
				.set("gatePw", N2Z(gatePw))
				.set("wifiId", N2Z(wifiId))
				.set("wifiPw", N2Z(wifiPw))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("modelName", N2Z(modelName))
				.set("serialNo", N2Z(serialNo))
				.set("macAddress", N2Z(macAddress))
				.set("gateId", N2Z(gateId))
				.set("gatePw", N2Z(gatePw))
				.set("wifiId", N2Z(wifiId))
				.set("wifiPw", N2Z(wifiPw))
				.build();
	}
	@Override
	public MW_Wifi fromEntity(Entity entity) {
		super.fromEntity(entity);
		setModelName(entity.getString("modelName"));
		setSerialNo(entity.getString("serialNo"));
		setMacAddress(entity.getString("macAddress"));
		setGateId(entity.getString("gateId"));
		setGatePw(entity.getString("gatePw"));
		setWifiId(entity.getString("wifiId"));
		setWifiPw(entity.getString("wifiPw"));
		return this;
	}
}
