package kr.co.checkplusmng.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.co.checkplusmng.model.MW_Welder;
import kr.nesystem.appengine.common.dao.BaseDao;

public class WelderDao extends BaseDao<MW_Welder> {
	public WelderDao() {
		super(MW_Welder.class);
	}
	
	public MW_Welder selectByWelderId(HttpSession session, String welderId) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("welderId", welderId);
		List<MW_Welder> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
