package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class MW_Project extends GAEAutoIncModel {
	private String projectId;
	private String projectName;
	private long saleCompanyIdKey;
	private String saleCompanyName;
	private long contractCompanyIdKey;
	private String contractCompanyName;
	private Date contractDate;
	private String memo;
	private int orderSeq;
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public long getSaleCompanyIdKey() {
		return saleCompanyIdKey;
	}
	public void setSaleCompanyIdKey(long saleCompanyIdKey) {
		this.saleCompanyIdKey = saleCompanyIdKey;
	}
	public String getSaleCompanyName() {
		return saleCompanyName;
	}
	public void setSaleCompanyName(String saleCompanyName) {
		this.saleCompanyName = saleCompanyName;
	}
	public long getContractCompanyIdKey() {
		return contractCompanyIdKey;
	}
	public void setContractCompanyIdKey(long contractCompanyIdKey) {
		this.contractCompanyIdKey = contractCompanyIdKey;
	}
	public String getContractCompanyName() {
		return contractCompanyName;
	}
	public void setContractCompanyName(String contractCompanyName) {
		this.contractCompanyName = contractCompanyName;
	}
	public Date getContractDate() {
		return contractDate;
	}
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
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
				.set("projectId", N2Z(projectId))
				.set("projectName", N2Z(projectName))
				.set("saleCompanyIdKey", saleCompanyIdKey)
				.set("contractCompanyIdKey", contractCompanyIdKey)
				.set("contractDate", D2Z(contractDate))
				.set("memo", N2Z(memo))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("projectId", N2Z(projectId))
				.set("projectName", N2Z(projectName))
				.set("saleCompanyIdKey", saleCompanyIdKey)
				.set("contractCompanyIdKey", contractCompanyIdKey)
				.set("contractDate", D2Z(contractDate))
				.set("memo", N2Z(memo))
				.set("orderSeq", orderSeq)
				.build();
	}
	@Override
	public MW_Project fromEntity(Entity entity) {
		super.fromEntity(entity);
		setProjectId(entity.getString("projectId"));
		setProjectName(entity.getString("projectName"));
		setSaleCompanyIdKey(entity.getLong("saleCompanyIdKey"));
		setContractCompanyIdKey(entity.getLong("contractCompanyIdKey"));
		setContractDate(L2D(entity.getLong("contractDate")));
		setMemo(entity.getString("memo"));
		setOrderSeq((int)entity.getLong("orderSeq"));
		return this;
	}
}
