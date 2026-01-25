package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Invoice extends MW_IDBaseModel {
	private long activityIdKey;
	private String invoiceType;
	private Date issueDate;
	private double issueAmount;
	private String approvalNo;
	private Date entryDate;
	public long getActivityIdKey() {
		return activityIdKey;
	}
	public void setActivityIdKey(long activityIdKey) {
		this.activityIdKey = activityIdKey;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
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
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("activityIdKey", activityIdKey)
				.set("invoiceType", N2Z(invoiceType))
				.set("issueDate", D2Z(issueDate))
				.set("issueAmount", issueAmount)
				.set("approvalNo", N2Z(approvalNo))
				.set("entryDate", D2Z(entryDate))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("activityIdKey", activityIdKey)
				.set("invoiceType", N2Z(invoiceType))
				.set("issueDate", D2Z(issueDate))
				.set("issueAmount", issueAmount)
				.set("approvalNo", N2Z(approvalNo))
				.set("entryDate", D2Z(entryDate))
				.build();
	}
	@Override
	public MW_Invoice fromEntity(Entity entity) {
		super.fromEntity(entity);
		setActivityIdKey(entity.getLong("activityIdKey"));
		setInvoiceType(entity.getString("invoiceType"));
		setIssueDate(L2D(entity.getLong("issueDate")));
		setIssueAmount(entity.getDouble("issueAmount"));
		setApprovalNo(entity.getString("approvalNo"));
		setEntryDate(L2D(entity.getLong("entryDate")));
		return this;
	}
}
