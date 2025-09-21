package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.common.model.CM_L10N;
import kr.peelknight.common.model.CM_L10NLocale;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class L10NDao {
	public SqlSessionFactory factory =  null;

	public L10NDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_L10N> selectL10Ns(int offset, int size, String search) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			if (search != null) {
				param.put("search", search);
			}
			return session.selectList("kr.peelknight.common.mybatis.l10nMapper.selectL10N", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectL10NPaging(String search) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (search != null) {
				param.put("search", search);
			}
			return session.selectOne("kr.peelknight.common.mybatis.l10nMapper.selectL10NPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_L10N selectL10NByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.common.mybatis.l10nMapper.selectL10N", param);
		} finally {
			session.close();
		}
	}

	public void insertL10N(CM_L10N l10N) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.common.mybatis.l10nMapper.insertL10N", l10N);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateL10N(CM_L10N l10N) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.common.mybatis.l10nMapper.updateL10N", l10N);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteL10N(CM_L10N l10N) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.common.mybatis.l10nMapper.deleteL10N", l10N);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void insertOrUpdateL10Ns(List<CM_L10N> list) {
		SqlSession session = factory.openSession();
		try {
			CM_L10N item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if ("I".equals(item.getTouch())) {
					session.insert("kr.peelknight.common.mybatis.l10nMapper.insertL10N", item);
				} else if ("U".equals(item.getTouch())) {
					session.update("kr.peelknight.common.mybatis.l10nMapper.updateL10N", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void deleteL10Ns(List<CM_L10N> list) {
		SqlSession session = factory.openSession();
		try {
			CM_L10N item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if (item.getIdString() != null && !item.getIdString().isEmpty()) {
					session.delete("kr.peelknight.common.mybatis.l10nMapper.deleteL10N", item);
					session.delete("kr.peelknight.common.mybatis.l10nMapper.deleteL10NLocalByIdString", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public List<CM_L10NLocale> selectL10NLocales(String idString, String locale, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (idString != null) {
				param.put("idString", idString);
			}
			if (locale != null) {
				param.put("locale", locale);
			}
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.common.mybatis.l10nMapper.selectL10NLocale", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectL10NLocalePaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.common.mybatis.l10nMapper.selectL10NLocalePaging", param);
		} finally {
			session.close();
		}
	}

	public CM_L10NLocale selectL10NLocaleByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.common.mybatis.l10nMapper.selectL10NLocale", param);
		} finally {
			session.close();
		}
	}

	public CM_L10NLocale selectL10NLocaleByIdStringNLocale(String idString, String locale) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idString", idString);
			param.put("locale", locale);
			return session.selectOne("kr.peelknight.common.mybatis.l10nMapper.selectL10NLocale", param);
		} finally {
			session.close();
		}
	}

	public void insertL10NLocale(CM_L10NLocale l10NLocale) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.common.mybatis.l10nMapper.insertL10NLocale", l10NLocale);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateL10NLocale(CM_L10NLocale l10NLocale) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.common.mybatis.l10nMapper.updateL10NLocale", l10NLocale);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteL10NLocale(CM_L10NLocale l10NLocale) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.common.mybatis.l10nMapper.deleteL10NLocale", l10NLocale);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void insertOrUpdateL10NLocales(List<CM_L10NLocale> list) {
		SqlSession session = factory.openSession();
		try {
			CM_L10NLocale item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if ("I".equals(item.getTouch())) {
					session.insert("kr.peelknight.common.mybatis.l10nMapper.insertL10NLocale", item);
				} else if ("U".equals(item.getTouch())) {
					session.update("kr.peelknight.common.mybatis.l10nMapper.updateL10NLocale", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void deleteL10NLocales(List<CM_L10NLocale> list) {
		SqlSession session = factory.openSession();
		try {
			CM_L10NLocale item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if (item.getIdString() != null && !item.getIdString().isEmpty()) {
					session.delete("kr.peelknight.common.mybatis.l10nMapper.deleteL10NLocale", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void dropL10NTable() {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.initMapper.dropCML10N");
			session.update("kr.peelknight.mybatis.initMapper.dropCML10NLocale");
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void createL10NTable() {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.initMapper.createCML10N");
			session.update("kr.peelknight.mybatis.initMapper.createCML10NLocale");
			session.commit();
		} finally {
			session.close();
		}
	}
}
