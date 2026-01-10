package kr.co.checkplusmng.model;

import java.util.Date;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_LTE extends MW_IDBaseModel {
	private String modelName;
	private String serialNo;
	private String usimNo;
	private String telephone;
	private String gateId;
	private String gatePw;
	private String wifiId;
	private String wifiPw;
	private Date registDate;
	private Date startDate;
	private Date endDate;
	private String contract;
	private long currentActivityIdKey;
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
	public String getUsimNo() {
		return usimNo;
	}
	public void setUsimNo(String usimNo) {
		this.usimNo = usimNo;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
	public long getCurrentActivityIdKey() {
		return currentActivityIdKey;
	}
	public void setCurrentActivityIdKey(long currentActivityIdKey) {
		this.currentActivityIdKey = currentActivityIdKey;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("modelName", N2Z(modelName))
				.set("serialNo", N2Z(serialNo))
				.set("usimNo", N2Z(usimNo))
				.set("telephone", N2Z(telephone))
				.set("gateId", N2Z(gateId))
				.set("gatePw", N2Z(gatePw))
				.set("wifiId", N2Z(wifiId))
				.set("wifiPw", N2Z(wifiPw))
				.set("registDate", D2Z(registDate))
				.set("startDate", D2Z(startDate))
				.set("endDate", D2Z(endDate))
				.set("contract", N2Z(contract))
				.set("currentActivityIdKey", currentActivityIdKey)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("modelName", N2Z(modelName))
				.set("serialNo", N2Z(serialNo))
				.set("usimNo", N2Z(usimNo))
				.set("telephone", N2Z(telephone))
				.set("gateId", N2Z(gateId))
				.set("gatePw", N2Z(gatePw))
				.set("wifiId", N2Z(wifiId))
				.set("wifiPw", N2Z(wifiPw))
				.set("registDate", D2Z(registDate))
				.set("startDate", D2Z(startDate))
				.set("endDate", D2Z(endDate))
				.set("contract", N2Z(contract))
				.set("currentActivityIdKey", currentActivityIdKey)
				.build();
	}
	@Override
	public MW_LTE fromEntity(Entity entity) {
		super.fromEntity(entity);
		setModelName(entity.getString("modelName"));
		setSerialNo(entity.getString("serialNo"));
		setUsimNo(entity.getString("usimNo"));
		setTelephone(entity.getString("telephone"));
		setGateId(entity.getString("gateId"));
		setGatePw(entity.getString("gatePw"));
		setWifiId(entity.getString("wifiId"));
		setWifiPw(entity.getString("wifiPw"));
		setRegistDate(L2D(entity.getLong("registDate")));
		setStartDate(L2D(entity.getLong("startDate")));
		setEndDate(L2D(entity.getLong("endDate")));
		setContract(entity.getString("contract"));
		setCurrentActivityIdKey(entity.getLong("currentActivityIdKey"));
		return this;
	}
}
