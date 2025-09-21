package kr.peelknight.fragment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.fragment.model.CM_Fragment;
import kr.peelknight.fragment.model.CM_GridFragment;
import kr.peelknight.fragment.model.CM_PopupFragment;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class FragmentDao {
	SqlSessionFactory factory =  null;

	public FragmentDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_Fragment> selectFragments(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.fragment.mybatis.fragmentMapper.selectFragment", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectFragmentPaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.fragment.mybatis.fragmentMapper.selectFragmentPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_Fragment selectFragmentByFragmentId(String fragmentId) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("fragmentId", fragmentId);
			return session.selectOne("kr.peelknight.fragment.mybatis.fragmentMapper.selectFragment", param);
		} finally {
			session.close();
		}
	}
	
	public CM_Fragment selectFragmentByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.fragment.mybatis.fragmentMapper.selectFragment", param);
		} finally {
			session.close();
		}
	}

	public void insertFragment(CM_Fragment fragment) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.fragment.mybatis.fragmentMapper.insertFragment", fragment);
			CM_GridFragment gridFragment = new CM_GridFragment();
			CM_PopupFragment popupFragment = new CM_PopupFragment();
			gridFragment.init();
			popupFragment.init();
			gridFragment.setFragmentIdKey(fragment.getIdKey());
			popupFragment.setFragmentIdKey(fragment.getIdKey());
			session.insert("kr.peelknight.fragment.mybatis.gridfragmentMapper.insertGridFragment", gridFragment);
			session.insert("kr.peelknight.fragment.mybatis.popupfragmentMapper.insertPopupFragment", popupFragment);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateFragment(CM_Fragment fragment) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.fragment.mybatis.fragmentMapper.updateFragment", fragment);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteFragment(CM_Fragment fragment) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.fragment.mybatis.fragmentMapper.deleteFragment", fragment);
			session.delete("kr.peelknight.fragment.mybatis.gridfragmentMapper.deleteGridFragment", fragment);
			session.delete("kr.peelknight.fragment.mybatis.popupfragmentMapper.deletePopupFragment", fragment);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void updateFragmentCachedJson(String fragmentId, String cachedJson) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("fragmentId", fragmentId);
			param.put("cachedJson", cachedJson);
			session.update("kr.peelknight.fragment.mybatis.fragmentMapper.updateFragmentCachedJson", param);
			session.commit();
		} finally {
			session.close();
		}
	}
}
