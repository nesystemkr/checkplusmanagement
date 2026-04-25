package kr.co.checkplusmng.service;

import jakarta.ws.rs.Path;
import kr.co.checkplusmng.dao.CompanyDao;
import kr.co.checkplusmng.model.MW_Company;

@Path("/{version}/company")
public class CompanyService extends BaseService<MW_Company> {
	public CompanyService() {
		super(MW_Company.class, "CUS");
		_dao = new CompanyDao();
	}
	
	@Override
	protected void updateItem(MW_Company existOne, MW_Company newOne) {
		existOne.setName(newOne.getName());
		existOne.setAddress(newOne.getAddress());
		existOne.setTelephone(newOne.getTelephone());
		existOne.setEmail(newOne.getEmail());
		existOne.setOfficer(newOne.getOfficer());
		existOne.setOfficerTel(newOne.getOfficerTel());
		existOne.setTaxid(newOne.getTaxid());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
	}
}
