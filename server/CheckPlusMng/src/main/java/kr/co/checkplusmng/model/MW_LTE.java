package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class MW_LTE extends GAEAutoIncModel {
	private String lteId;
	private long projectIdKey;
	private String modelName;
	private String deviceSerialNo;
	private String usimSerialNo;
	private String telephoneNo;
	private String lteGateId;
	private String lteGatePw;
	private String lteWifiId;
	private String lteWifiPw;
	private Date registDate;
	private Date startDate;
	private Date endDate;
	private String contract;
	private String memo;
	public String getLteId() {
		return lteId;
	}
	public void setLteId(String lteId) {
		this.lteId = lteId;
	}
	public long getProjectIdKey() {
		return projectIdKey;
	}
	public void setProjectIdKey(long projectIdKey) {
		this.projectIdKey = projectIdKey;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getDeviceSerialNo() {
		return deviceSerialNo;
	}
	public void setDeviceSerialNo(String deviceSerialNo) {
		this.deviceSerialNo = deviceSerialNo;
	}
	public String getUsimSerialNo() {
		return usimSerialNo;
	}
	public void setUsimSerialNo(String usimSerialNo) {
		this.usimSerialNo = usimSerialNo;
	}
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}
	public String getLteGateId() {
		return lteGateId;
	}
	public void setLteGateId(String lteGateId) {
		this.lteGateId = lteGateId;
	}
	public String getLteGatePw() {
		return lteGatePw;
	}
	public void setLteGatePw(String lteGatePw) {
		this.lteGatePw = lteGatePw;
	}
	public String getLteWifiId() {
		return lteWifiId;
	}
	public void setLteWifiId(String lteWifiId) {
		this.lteWifiId = lteWifiId;
	}
	public String getLteWifiPw() {
		return lteWifiPw;
	}
	public void setLteWifiPw(String lteWifiPw) {
		this.lteWifiPw = lteWifiPw;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("lteId", N2Z(lteId))
				.set("projectIdKey", projectIdKey)
				.set("modelName", N2Z(modelName))
				.set("deviceSerialNo", N2Z(deviceSerialNo))
				.set("usimSerialNo", N2Z(usimSerialNo))
				.set("telephoneNo", N2Z(telephoneNo))
				.set("lteGateId", N2Z(lteGateId))
				.set("lteGatePw", N2Z(lteGatePw))
				.set("lteWifiId", N2Z(lteWifiId))
				.set("lteWifiPw", N2Z(lteWifiPw))
				.set("registDate", D2Z(registDate))
				.set("startDate", D2Z(startDate))
				.set("endDate", D2Z(endDate))
				.set("contract", N2Z(contract))
				.set("memo", N2Z(memo))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("lteId", N2Z(lteId))
				.set("projectIdKey", projectIdKey)
				.set("modelName", N2Z(modelName))
				.set("deviceSerialNo", N2Z(deviceSerialNo))
				.set("usimSerialNo", N2Z(usimSerialNo))
				.set("telephoneNo", N2Z(telephoneNo))
				.set("lteGateId", N2Z(lteGateId))
				.set("lteGatePw", N2Z(lteGatePw))
				.set("lteWifiId", N2Z(lteWifiId))
				.set("lteWifiPw", N2Z(lteWifiPw))
				.set("registDate", D2Z(registDate))
				.set("startDate", D2Z(startDate))
				.set("endDate", D2Z(endDate))
				.set("contract", N2Z(contract))
				.set("memo", N2Z(memo))
				.build();
	}
	@Override
	public MW_LTE fromEntity(Entity entity) {
		super.fromEntity(entity);
		setLteId(entity.getString("lteId"));
		setProjectIdKey(entity.getLong("projectIdKey"));
		setModelName(entity.getString("modelName"));
		setDeviceSerialNo(entity.getString("deviceSerialNo"));
		setUsimSerialNo(entity.getString("usimSerialNo"));
		setTelephoneNo(entity.getString("telephoneNo"));
		setLteGateId(entity.getString("lteGateId"));
		setLteGatePw(entity.getString("lteGatePw"));
		setLteWifiId(entity.getString("lteWifiId"));
		setLteWifiPw(entity.getString("lteWifiPw"));
		setRegistDate(L2D(entity.getLong("registDate")));
		setStartDate(L2D(entity.getLong("startDate")));
		setEndDate(L2D(entity.getLong("endDate")));
		setContract(entity.getString("contract"));
		setMemo(entity.getString("memo"));
		return this;
	}
}
