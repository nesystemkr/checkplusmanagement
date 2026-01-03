package kr.co.checkplusmng.model;

import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Activity extends MW_IDBaseModel {
	private long projectIdKey;
	private double hwTotalAmount;
	private double hwActualAmount;
	private Date deliveryDate;
	private double swTotalAmount;
	private double swActualAmount;
	public long getProjectIdKey() {
		return projectIdKey;
	}
	public void setProjectIdKey(long projectIdKey) {
		this.projectIdKey = projectIdKey;
	}
	public double getHwTotalAmount() {
		return hwTotalAmount;
	}
	public void setHwTotalAmount(double hwTotalAmount) {
		this.hwTotalAmount = hwTotalAmount;
	}
	public double getHwActualAmount() {
		return hwActualAmount;
	}
	public void setHwActualAmount(double hwActualAmount) {
		this.hwActualAmount = hwActualAmount;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public double getSwTotalAmount() {
		return swTotalAmount;
	}
	public void setSwTotalAmount(double swTotalAmount) {
		this.swTotalAmount = swTotalAmount;
	}
	public double getSwActualAmount() {
		return swActualAmount;
	}
	public void setSwActualAmount(double swActualAmount) {
		this.swActualAmount = swActualAmount;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("projectIdKey", projectIdKey)
				.set("hwTotalAmount", hwTotalAmount)
				.set("hwActualAmount", hwActualAmount)
				.set("deliveryDate", D2Z(deliveryDate))
				.set("swTotalAmount", swTotalAmount)
				.set("swActualAmount", swActualAmount)
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("projectIdKey", projectIdKey)
				.set("hwTotalAmount", hwTotalAmount)
				.set("hwActualAmount", hwActualAmount)
				.set("deliveryDate", D2Z(deliveryDate))
				.set("swTotalAmount", swTotalAmount)
				.set("swActualAmount", swActualAmount)
				.build();
	}
	@Override
	public MW_Activity fromEntity(Entity entity) {
		super.fromEntity(entity);
		setProjectIdKey(entity.getLong("projectIdKey"));
		setHwTotalAmount(entity.getDouble("hwTotalAmount"));
		setHwActualAmount(entity.getDouble("hwActualAmount"));
		setDeliveryDate(L2D(entity.getLong("deliveryDate")));
		setSwTotalAmount(entity.getDouble("swTotalAmount"));
		setSwActualAmount(entity.getDouble("swActualAmount"));
		return this;
	}
}
