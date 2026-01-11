package kr.co.checkplusmng.model;

import java.util.Date;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Project extends MW_IDBaseModel {
	private String name;
	private long customerIdKey;
	private long brokerIdKey;
	private Date contractDate;
	private String checkPlusId;
	private String checkPlusPw;
	private String customerName;
	private String brokerName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCustomerIdKey() {
		return customerIdKey;
	}
	public void setCustomerIdKey(long customerIdKey) {
		this.customerIdKey = customerIdKey;
	}
	public long getBrokerIdKey() {
		return brokerIdKey;
	}
	public void setBrokerIdKey(long brokerIdKey) {
		this.brokerIdKey = brokerIdKey;
	}
	public Date getContractDate() {
		return contractDate;
	}
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	public String getCheckPlusId() {
		return checkPlusId;
	}
	public void setCheckPlusId(String checkPlusId) {
		this.checkPlusId = checkPlusId;
	}
	public String getCheckPlusPw() {
		return checkPlusPw;
	}
	public void setCheckPlusPw(String checkPlusPw) {
		this.checkPlusPw = checkPlusPw;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("name", N2Z(name))
				.set("customerIdKey", customerIdKey)
				.set("brokerIdKey", brokerIdKey)
				.set("contractDate", D2Z(contractDate))
				.set("checkPlusId", N2Z(checkPlusId))
				.set("checkPlusPw", N2Z(checkPlusPw))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("name", N2Z(name))
				.set("customerIdKey", customerIdKey)
				.set("brokerIdKey", brokerIdKey)
				.set("contractDate", D2Z(contractDate))
				.set("checkPlusId", N2Z(checkPlusId))
				.set("checkPlusPw", N2Z(checkPlusPw))
				.build();
	}
	@Override
	public MW_Project fromEntity(Entity entity) {
		super.fromEntity(entity);
		setName(entity.getString("name"));
		setCustomerIdKey(entity.getLong("customerIdKey"));
		setBrokerIdKey(entity.getLong("brokerIdKey"));
		setContractDate(L2D(entity.getLong("contractDate")));
		setCheckPlusId(entity.getString("checkPlusId"));
		setCheckPlusPw(entity.getString("checkPlusPw"));
		return this;
	}
}
