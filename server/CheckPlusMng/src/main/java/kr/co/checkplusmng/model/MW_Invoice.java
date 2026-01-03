package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Invoice extends MW_IDBaseModel {
	private long activityIdKey;
	private String invoideType;
	private Date issueDate;
	private double issueAmount;
	private String approvalNo;
	private Date entiryDate;
	public long getActivityIdKey() {
		return activityIdKey;
	}
	public void setActivityIdKey(long activityIdKey) {
		this.activityIdKey = activityIdKey;
	}
	public String getInvoideType() {
		return invoideType;
	}
	public void setInvoideType(String invoideType) {
		this.invoideType = invoideType;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public double getIssueAmount() {
		return issueAmount;
	}
	public void setIssueAmount(double issueAmount) {
		this.issueAmount = issueAmount;
	}
	public String getApprovalNo() {
		return approvalNo;
	}
	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}
	public Date getEntiryDate() {
		return entiryDate;
	}
	public void setEntiryDate(Date entiryDate) {
		this.entiryDate = entiryDate;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("activityIdKey", activityIdKey)
				.set("invoideType", invoideType)
				.set("issueDate", D2Z(issueDate))
				.set("issueAmount", issueAmount)
				.set("approvalNo", approvalNo)
				.set("entiryDate", D2Z(entiryDate))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("activityIdKey", activityIdKey)
				.set("invoideType", invoideType)
				.set("issueDate", D2Z(issueDate))
				.set("issueAmount", issueAmount)
				.set("approvalNo", approvalNo)
				.set("entiryDate", D2Z(entiryDate))
				.build();
	}
	@Override
	public MW_Invoice fromEntity(Entity entity) {
		super.fromEntity(entity);
		setActivityIdKey(entity.getLong("activityIdKey"));
		setInvoideType(entity.getString("invoideType"));
		setIssueDate(L2D(entity.getLong("issueDate")));
		setIssueAmount(entity.getDouble("issueAmount"));
		setApprovalNo(entity.getString("approvalNo"));
		setEntiryDate(L2D(entity.getLong("entiryDate")));
		return this;
	}
}
