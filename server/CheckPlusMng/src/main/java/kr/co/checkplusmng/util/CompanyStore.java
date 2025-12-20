package kr.co.checkplusmng.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.checkplusmng.dao.CompanyDao;
import kr.co.checkplusmng.model.MW_Company;

public class CompanyStore {
	static Map<Long, MW_Company> _stores = null;
	static {
		load();
	}
	static public void load() {
		try {
			if (_stores != null) {
				return;
			}
			_stores = new HashMap<>();
			CompanyDao dao = new CompanyDao();
			List<MW_Company> list = dao.list(null, null, -1, 0);
			for (int ii = 0; ii < list.size(); ii++) {
				MW_Company item = list.get(ii);
				_stores.put(item.getIdKey(), item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static public void reload() {
		_stores = null;
		load();
	}
	static public String getName(long idKey) {
		MW_Company item = _stores.get(idKey);
		if (item != null) {
			return item.getName();
		}
		return null;
	}
}
