package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class MW_Wifi extends GAEAutoIncModel {
	private String wifiId;
	private long projectIdKey;
	private String projectName;
	private String contractCompanyName;
	private String modelName;
	private String serialNo;
	private String macAddress;
	private String apGateId;
	private String apGatePw;
	private String apWifiId;
	private String apWifiPw;
	private String memo;
	private int orderSeq;
	public String getWifiId() {
		return wifiId;
	}
	public void setWifiId(String wifiId) {
		this.wifiId = wifiId;
	}
	public long getProjectIdKey() {
		return projectIdKey;
	}
	public void setProjectIdKey(long projectIdKey) {
		this.projectIdKey = projectIdKey;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getContractCompanyName() {
		return contractCompanyName;
	}
	public void setContractCompanyName(String contractCompanyName) {
		this.contractCompanyName = contractCompanyName;
	}
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
	public String getApGateId() {
		return apGateId;
	}
	public void setApGateId(String apGateId) {
		this.apGateId = apGateId;
	}
	public String getApGatePw() {
		return apGatePw;
	}
	public void setApGatePw(String apGatePw) {
		this.apGatePw = apGatePw;
	}
	public String getApWifiId() {
		return apWifiId;
	}
	public void setApWifiId(String apWifiId) {
		this.apWifiId = apWifiId;
	}
	public String getApWifiPw() {
		return apWifiPw;
	}
	public void setApWifiPw(String apWifiPw) {
		this.apWifiPw = apWifiPw;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("wifiId", N2Z(wifiId))
				.set("projectIdKey", projectIdKey)
				.set("modelName", N2Z(modelName))
				.set("serialNo", N2Z(serialNo))
				.set("macAddress", N2Z(macAddress))
				.set("apGateId", N2Z(apGateId))
				.set("apGatePw", N2Z(apGatePw))
				.set("apWifiId", N2Z(apWifiId))
				.set("apWifiPw", N2Z(apWifiPw))
				.set("memo", N2Z(memo))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("wifiId", N2Z(wifiId))
				.set("projectIdKey", projectIdKey)
				.set("modelName", N2Z(modelName))
				.set("serialNo", N2Z(serialNo))
				.set("macAddress", N2Z(macAddress))
				.set("apGateId", N2Z(apGateId))
				.set("apGatePw", N2Z(apGatePw))
				.set("apWifiId", N2Z(apWifiId))
				.set("apWifiPw", N2Z(apWifiPw))
				.set("memo", N2Z(memo))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public MW_Wifi fromEntity(Entity entity) {
		super.fromEntity(entity);
		setWifiId(entity.getString("wifiId"));
		setProjectIdKey(entity.getLong("projectIdKey"));
		setModelName(entity.getString("modelName"));
		setSerialNo(entity.getString("serialNo"));
		setMacAddress(entity.getString("macAddress"));
		setApGateId(entity.getString("apGateId"));
		setApGatePw(entity.getString("apGatePw"));
		setApWifiId(entity.getString("apWifiId"));
		setApWifiPw(entity.getString("apWifiPw"));
		setMemo(entity.getString("memo"));
		setOrderSeq((int)entity.getLong("orderSeq"));
		return this;
	}
}
