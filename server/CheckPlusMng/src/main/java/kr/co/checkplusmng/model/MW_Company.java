package kr.co.checkplusmng.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class MW_Company extends MW_IDBaseModel {
	private String name;
	private String address;
	private String telephone;
	private String email;
	private String officer;
	private String officerTel;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOfficer() {
		return officer;
	}
	public void setOfficer(String officer) {
		this.officer = officer;
	}
	public String getOfficerTel() {
		return officerTel;
	}
	public void setOfficerTel(String officerTel) {
		this.officerTel = officerTel;
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return super.builder(keyFactory)
				.set("name", N2Z(name))
				.set("address", N2Z(address))
				.set("telephone", N2Z(telephone))
				.set("email", N2Z(email))
				.set("officer", N2Z(officer))
				.set("officerTel", N2Z(officerTel))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return super.builder(existOne)
				.set("name", N2Z(name))
				.set("address", N2Z(address))
				.set("telephone", N2Z(telephone))
				.set("email", N2Z(email))
				.set("officer", N2Z(officer))
				.set("officerTel", N2Z(officerTel))
				.build();
	}
	@Override
	public MW_Company fromEntity(Entity entity) {
		super.fromEntity(entity);
		setName(entity.getString("name"));
		setAddress(entity.getString("address"));
		setTelephone(entity.getString("telephone"));
		setEmail(entity.getString("email"));
		setOfficer(entity.getString("officer"));
		setOfficerTel(entity.getString("officerTel"));
		return this;
	}
}
