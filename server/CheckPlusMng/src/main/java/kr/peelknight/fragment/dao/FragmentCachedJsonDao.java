package kr.peelknight.fragment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.fragment.model.CM_FragmentCachedJson;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class FragmentCachedJsonDao {
	SqlSessionFactory factory =  null;

	public FragmentCachedJsonDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_FragmentCachedJson> selectFragmentCachedJsons(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.selectFragmentCachedJson", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectFragmentCachedJsonPaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.selectFragmentCachedJsonPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_FragmentCachedJson selectFragmentCachedJsonByIdKey(long fragmentIdKey, String locale) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("fragmentIdKey", fragmentIdKey);
			param.put("locale", locale != null ? locale : "deflc");
			return session.selectOne("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.selectFragmentCachedJson", param);
		} finally {
			session.close();
		}
	}
	
	public void insertOrUpdateFragmentCachedJson(long fragmentIdKey, String locale, String cachedJson) {
		CM_FragmentCachedJson existOne = selectFragmentCachedJsonByIdKey(fragmentIdKey, locale);
		if (existOne != null) {
			existOne.setCachedJson(cachedJson);
			updateFragmentCachedJson(existOne);
		} else {
			CM_FragmentCachedJson newOne = new CM_FragmentCachedJson();
			newOne.setFragmentIdKey(fragmentIdKey);
			newOne.setLocale(locale != null ? locale : "deflc");
			newOne.setCachedJson(cachedJson);
			insertFragmentCachedJson(newOne);
		}
	}

	public void insertFragmentCachedJson(CM_FragmentCachedJson fragmentCachedJson) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.insertFragmentCachedJson", fragmentCachedJson);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateFragmentCachedJson(CM_FragmentCachedJson fragmentCachedJson) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.updateFragmentCachedJson", fragmentCachedJson);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteFragmentCachedJson(CM_FragmentCachedJson fragmentCachedJson) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.deleteFragmentCachedJson", fragmentCachedJson);
			session.commit();
		} finally {
			session.close();
		}
	}
}
