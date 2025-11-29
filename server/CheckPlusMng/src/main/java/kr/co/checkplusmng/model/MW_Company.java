package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import kr.nesystem.appengine.common.model.GAEAutoIncModel;

public class MW_Company extends GAEAutoIncModel {
	private String companyId;
	private String name;
	private String address1;
	private String address2;
	private String telephone1;
	private String telephone2;
	private String mainOfficer;
	private String mainOfficerPosition;
	private String mainOfficerTelephone;
	private String mainOfficerEmail;
	private String subOfficer;
	private String subOfficerPosition;
	private String subOfficerTelephone;
	private String subOfficerEmail;
	private String memo;
	private String status;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getTelephone1() {
		return telephone1;
	}
	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}
	public String getTelephone2() {
		return telephone2;
	}
	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}
	public String getMainOfficer() {
		return mainOfficer;
	}
	public void setMainOfficer(String mainOfficer) {
		this.mainOfficer = mainOfficer;
	}
	public String getMainOfficerPosition() {
		return mainOfficerPosition;
	}
	public void setMainOfficerPosition(String mainOfficerPosition) {
		this.mainOfficerPosition = mainOfficerPosition;
	}
	public String getMainOfficerTelephone() {
		return mainOfficerTelephone;
	}
	public void setMainOfficerTelephone(String mainOfficerTelephone) {
		this.mainOfficerTelephone = mainOfficerTelephone;
	}
	public String getMainOfficerEmail() {
		return mainOfficerEmail;
	}
	public void setMainOfficerEmail(String mainOfficerEmail) {
		this.mainOfficerEmail = mainOfficerEmail;
	}
	public String getSubOfficer() {
		return subOfficer;
	}
	public void setSubOfficer(String subOfficer) {
		this.subOfficer = subOfficer;
	}
	public String getSubOfficerPosition() {
		return subOfficerPosition;
	}
	public void setSubOfficerPosition(String subOfficerPosition) {
		this.subOfficerPosition = subOfficerPosition;
	}
	public String getSubOfficerTelephone() {
		return subOfficerTelephone;
	}
	public void setSubOfficerTelephone(String subOfficerTelephone) {
		this.subOfficerTelephone = subOfficerTelephone;
	}
	public String getSubOfficerEmail() {
		return subOfficerEmail;
	}
	public void setSubOfficerEmail(String subOfficerEmail) {
		this.subOfficerEmail = subOfficerEmail;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("companyId", N2Z(companyId))
				.set("name", N2Z(name))
				.set("address1", N2Z(address1))
				.set("address2", N2Z(address2))
				.set("telephone1", N2Z(telephone1))
				.set("telephone2", N2Z(telephone2))
				.set("mainOfficer", N2Z(mainOfficer))
				.set("mainOfficerPosition", N2Z(mainOfficerPosition))
				.set("mainOfficerTelephone", N2Z(mainOfficerTelephone))
				.set("mainOfficerEmail", N2Z(mainOfficerEmail))
				.set("subOfficer", N2Z(subOfficer))
				.set("subOfficerPosition", N2Z(subOfficerPosition))
				.set("subOfficerTelephone", N2Z(subOfficerTelephone))
				.set("subOfficerEmail", N2Z(subOfficerEmail))
				.set("memo", N2Z(memo))
				.set("status", N2Z(status))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("companyId", N2Z(companyId))
				.set("name", N2Z(name))
				.set("address1", N2Z(address1))
				.set("address2", N2Z(address2))
				.set("telephone1", N2Z(telephone1))
				.set("telephone2", N2Z(telephone2))
				.set("mainOfficer", N2Z(mainOfficer))
				.set("mainOfficerPosition", N2Z(mainOfficerPosition))
				.set("mainOfficerTelephone", N2Z(mainOfficerTelephone))
				.set("mainOfficerEmail", N2Z(mainOfficerEmail))
				.set("subOfficer", N2Z(subOfficer))
				.set("subOfficerPosition", N2Z(subOfficerPosition))
				.set("subOfficerTelephone", N2Z(subOfficerTelephone))
				.set("subOfficerEmail", N2Z(subOfficerEmail))
				.set("memo", N2Z(memo))
				.set("status", N2Z(status))
				.build();
	}
	@Override
	public MW_Company fromEntity(Entity entity) {
		super.fromEntity(entity);
		setCompanyId(entity.getString("companyId"));
		setName(entity.getString("name"));
		setAddress1(entity.getString("address1"));
		setAddress2(entity.getString("address2"));
		setTelephone1(entity.getString("telephone1"));
		setTelephone2(entity.getString("telephone2"));
		setMainOfficer(entity.getString("mainOfficer"));
		setMainOfficerPosition(entity.getString("mainOfficerPosition"));
		setMainOfficerTelephone(entity.getString("mainOfficerTelephone"));
		setMainOfficerEmail(entity.getString("mainOfficerEmail"));
		setSubOfficer(entity.getString("subOfficer"));
		setSubOfficerPosition(entity.getString("subOfficerPosition"));
		setSubOfficerTelephone(entity.getString("subOfficerTelephone"));
		setSubOfficerEmail(entity.getString("subOfficerEmail"));
		setMemo(entity.getString("memo"));
		setStatus(entity.getString("status"));
		return this;
	}
}
