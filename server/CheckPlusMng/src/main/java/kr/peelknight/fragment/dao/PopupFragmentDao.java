package kr.peelknight.fragment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.fragment.model.CM_PopupFragment;
import kr.peelknight.fragment.model.CM_PopupRow;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class PopupFragmentDao {
	SqlSessionFactory factory =  null;

	public PopupFragmentDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_PopupFragment> selectPopupFragments(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.fragment.mybatis.popupfragmentMapper.selectPopupFragment", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectPopupFragmentPaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.fragment.mybatis.popupfragmentMapper.selectPopupFragmentPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_PopupFragment selectPopupFragmentByIdKey(long fragmentIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("fragmentIdKey", fragmentIdKey);
			return session.selectOne("kr.peelknight.fragment.mybatis.popupfragmentMapper.selectPopupFragment", param);
		} finally {
			session.close();
		}
	}

	public void updatePopupFragment(CM_PopupFragment popupFragment) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.fragment.mybatis.popupfragmentMapper.updatePopupFragment", popupFragment);
			if (popupFragment.getRows() != null) {
				CM_PopupRow row;
				for (int ii=0; ii<popupFragment.getRows().size(); ii++) {
					row = popupFragment.getRows().get(ii);
					row.setPopupIdKey(popupFragment.getFragmentIdKey());
					if ("I".equals(row.getTouch())) {
						session.insert("kr.peelknight.fragment.mybatis.popuprowMapper.insertPopupRow", row);
					} else if ("U".equals(row.getTouch())) {
						session.update("kr.peelknight.fragment.mybatis.popuprowMapper.updatePopupRow", row);
					} else if ("D".equals(row.getTouch())) {
						session.update("kr.peelknight.fragment.mybatis.popuprowMapper.deletePopupRow", row);
					}
				}
			}
			session.delete("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.deleteFragmentCachedJsonByPopup", popupFragment);
			session.commit();
		} finally {
			session.close();
		}
	}
}
