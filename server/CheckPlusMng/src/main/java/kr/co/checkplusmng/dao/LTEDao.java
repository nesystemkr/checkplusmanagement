package kr.co.checkplusmng.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.co.checkplusmng.model.MW_LTE;
import kr.nesystem.appengine.common.dao.BaseDao;

public class LTEDao extends BaseDao<MW_LTE> {
	public LTEDao() {
		super(MW_LTE.class);
	}
	
	public MW_LTE selectByLTEId(HttpSession session, String lteId) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("lteId", lteId);
		List<MW_LTE> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
