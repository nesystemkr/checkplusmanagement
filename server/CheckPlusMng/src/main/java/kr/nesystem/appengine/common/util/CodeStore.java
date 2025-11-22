package kr.nesystem.appengine.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.nesystem.appengine.common.dao.CodeDao;
import kr.nesystem.appengine.common.dao.CodeTypeDao;
import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_CodeType;

public class CodeStore {
	static Map<String, Map<String, CM_Code>> _stores = null;
	static {
		reload();
	}
	static public void reload() {
		try {
			if (_stores != null) {
				return;
			}
			_stores = new HashMap<>();
			CodeTypeDao typeDao = new CodeTypeDao();
			CodeDao dao = new CodeDao();
			List<CM_CodeType> listType = typeDao.list(null, null, -1, 0);
			for (int ii = 0; ii < listType.size(); ii++) {
				CM_CodeType codeType = listType.get(ii);
				List<CM_Code> listCode = dao.selectCodeByType(null, listType.get(ii).getType()).getList();
				Map<String, CM_Code> mapCode = new HashMap<>();
				for (int jj = 0; jj < listCode.size(); jj++) {
					CM_Code code = listCode.get(jj);
					mapCode.put(code.getCode(), code);
				}
				_stores.put(codeType.getType(), mapCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static public void clear() {
		_stores = null;
	}
	static public String get(String type, String code) {
		Map<String, CM_Code> mapCode = _stores.get(type);
		if (mapCode != null) {
			CM_Code item = mapCode.get(code);
			if (item != null) {
				return item.getCodeName();
			}
		}
		return null;
	}
}
