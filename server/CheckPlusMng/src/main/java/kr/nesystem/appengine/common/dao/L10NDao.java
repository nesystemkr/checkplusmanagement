package kr.nesystem.appengine.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.nesystem.appengine.common.model.CM_L10N;
import kr.nesystem.appengine.common.model.CM_L10NLocale;

public class L10NDao extends BaseDao {
	public List<CM_L10N> selectL10Ns(int offset, int size, String search) {
		return new ArrayList<CM_L10N>();
	}
	
	public List<CM_L10NLocale> selectL10NLocales(String idString, String locale, int offset, int size) {
		return new ArrayList<CM_L10NLocale>();
	}
}
