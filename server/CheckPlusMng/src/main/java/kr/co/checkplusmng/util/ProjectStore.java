package kr.co.checkplusmng.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.checkplusmng.dao.ProjectDao;
import kr.co.checkplusmng.model.MW_Project;

public class ProjectStore {
	static Map<Long, MW_Project> _stores = null;
	static {
		load();
	}
	static public void load() {
		try {
			if (_stores != null) {
				return;
			}
			_stores = new HashMap<>();
			ProjectDao dao = new ProjectDao();
			List<MW_Project> list = dao.list(null, null, -1, 0);
			for (int ii = 0; ii < list.size(); ii++) {
				MW_Project item = list.get(ii);
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
		MW_Project item = _stores.get(idKey);
		if (item != null) {
			return item.getName();
		}
		return null;
	}
	static public MW_Project get(long idKey) {
		return _stores.get(idKey);
	}
}
