package kr.peelknight.fragment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.fragment.model.CM_PopupRow;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class PopupRowDao {
	SqlSessionFactory factory =  null;

	public PopupRowDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_PopupRow> selectPopupRows(long popupIdKey, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (popupIdKey > 0) {
				param.put("popupIdKey", popupIdKey);
			}
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.fragment.mybatis.popuprowMapper.selectPopupRow", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectPopupRowPaging(long popupIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (popupIdKey > 0) {
				param.put("popupIdKey", popupIdKey);
			}
			return session.selectOne("kr.peelknight.fragment.mybatis.popuprowMapper.selectPopupRowPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_PopupRow selectPopupRowByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.fragment.mybatis.popuprowMapper.selectPopupRow", param);
		} finally {
			session.close();
		}
	}

	public void insertPopupRow(CM_PopupRow popupRow) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.fragment.mybatis.popuprowMapper.insertPopupRow", popupRow);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updatePopupRow(CM_PopupRow popupRow) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.fragment.mybatis.popuprowMapper.updatePopupRow", popupRow);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deletePopupRow(CM_PopupRow popupRow) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.fragment.mybatis.popuprowMapper.deletePopupRow", popupRow);
			session.commit();
		} finally {
			session.close();
		}
	}
}
