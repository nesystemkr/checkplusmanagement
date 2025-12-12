package kr.co.checkplusmng.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.co.checkplusmng.model.MW_Wifi;
import kr.nesystem.appengine.common.dao.BaseDao;

public class WifiDao extends BaseDao<MW_Wifi> {
	public WifiDao() {
		super(MW_Wifi.class);
	}
	
	public MW_Wifi selectByWifiId(HttpSession session, String wifiId) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("wifiId", wifiId);
		List<MW_Wifi> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
