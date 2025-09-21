package kr.peelknight.fragment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.fragment.model.CM_GridColButton;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class GridColButtonDao {
	SqlSessionFactory factory =  null;

	public GridColButtonDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_GridColButton> selectGridColButtons(long gridIdKey, long colIdKey, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (gridIdKey > 0) {
				param.put("gridIdKey", gridIdKey);
			}
			if (colIdKey > 0) {
				param.put("colIdKey", colIdKey);
			}
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.selectGridColButton", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectGridColButtonPaging(long gridIdKey, long colIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (gridIdKey > 0) {
				param.put("gridIdKey", gridIdKey);
			}
			if (colIdKey > 0) {
				param.put("colIdKey", colIdKey);
			}
			return session.selectOne("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.selectGridColButtonPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_GridColButton selectGridColButtonByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.selectGridColButton", param);
		} finally {
			session.close();
		}
	}

	public void insertGridColButton(CM_GridColButton gridColButton) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.insertGridColButton", gridColButton);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateGridColButton(CM_GridColButton gridColButton) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.updateGridColButton", gridColButton);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteGridColButton(CM_GridColButton gridColButton) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.deleteGridColButton", gridColButton);
			session.commit();
		} finally {
			session.close();
		}
	}
}
