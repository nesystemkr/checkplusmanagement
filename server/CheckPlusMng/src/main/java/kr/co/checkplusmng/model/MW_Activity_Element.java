package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Activity_Element extends MW_IDBaseModel {
	private long activityIdKey;
	private String elementType;
	private long elementIdKey;
	private String elementTitle;
	private double unitPrice;
	private Date startDate;
	private Date endDate;
	public long getActivityIdKey() {
		return activityIdKey;
	}
	public void setActivityIdKey(long activityIdKey) {
		this.activityIdKey = activityIdKey;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public long getElementIdKey() {
		return elementIdKey;
	}
	public void setElementIdKey(long elementIdKey) {
		this.elementIdKey = elementIdKey;
	}
	public String getElementTitle() {
		return elementTitle;
	}
	public void setElementTitle(String elementTitle) {
		this.elementTitle = elementTitle;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
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
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("activityIdKey", activityIdKey)
				.set("elementType", elementType)
				.set("elementTitle", elementTitle)
				.set("unitPrice", unitPrice)
				.set("startDate", D2Z(startDate))
				.set("endDate", D2Z(endDate))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("activityIdKey", activityIdKey)
				.set("elementType", elementType)
				.set("elementTitle", elementTitle)
				.set("unitPrice", unitPrice)
				.set("startDate", D2Z(startDate))
				.set("endDate", D2Z(endDate))
				.build();
	}
	@Override
	public MW_Activity_Element fromEntity(Entity entity) {
		super.fromEntity(entity);
		setActivityIdKey(entity.getLong("activityIdKey"));
		setElementType(entity.getString("elementType"));
		setElementTitle(entity.getString("elementTitle"));
		setUnitPrice(entity.getDouble("unitPrice"));
		setStartDate(L2D(entity.getLong("startDate")));
		setEndDate(L2D(entity.getLong("endDate")));
		return this;
	}
}
