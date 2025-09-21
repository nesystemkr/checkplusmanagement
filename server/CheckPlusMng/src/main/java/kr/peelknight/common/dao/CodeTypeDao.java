package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_CodeType;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class CodeTypeDao {
	public SqlSessionFactory factory =  null;

	public CodeTypeDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_CodeType> selectCodeTypes(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.mybatis.codetypeMapper.selectCodeType", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectCodeTypePaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.mybatis.codetypeMapper.selectCodeTypePaging", param);
		} finally {
			session.close();
		}
	}

	public CM_CodeType selectCodeTypeByType(String typeParam) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("type", typeParam);
			return session.selectOne("kr.peelknight.mybatis.codetypeMapper.selectCodeType", param);
		} finally {
			session.close();
		}
	}

	public void insertCodeType(CM_CodeType code) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.codetypeMapper.insertCodeType", code);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateCodeType(CM_CodeType code) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.codetypeMapper.updateCodeType", code);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteCodeType(CM_CodeType code) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.mybatis.codetypeMapper.deleteCodeType", code);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void insertOrUpdateCodeTypes(List<CM_CodeType> list) {
		SqlSession session = factory.openSession();
		try {
			CM_CodeType item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if ("I".equals(item.getTouch())) {
					session.insert("kr.peelknight.mybatis.codetypeMapper.insertCodeType", item);
				} else if ("U".equals(item.getTouch())) {
					session.update("kr.peelknight.mybatis.codetypeMapper.updateCodeType", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteCodeTypes(List<CM_CodeType> list) {
		SqlSession session = factory.openSession();
		try {
			CM_CodeType item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if (item.getType() != null && !item.getType().isEmpty()) {
					session.delete("kr.peelknight.mybatis.codeMapper.deleteCodeByType", item);
					session.delete("kr.peelknight.mybatis.codetypeMapper.deleteCodeType", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
}
