package kr.co.checkplusmng.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.co.checkplusmng.model.MW_Company;
import kr.nesystem.appengine.common.dao.BaseDao;

public class CompanyDao extends BaseDao<MW_Company> {
	public CompanyDao() {
		super(MW_Company.class);
	}
	
	public MW_Company selectByCompanyId(HttpSession session, String companyId) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("companyId", companyId);
		List<MW_Company> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	

}
