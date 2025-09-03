package kr.nesystem.checkplusmng.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.nesystem.model.CM_CodeType;
import kr.nesystem.model.CM_Paging;
import kr.nesystem.util.MySqlSessionFactory;

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
			return session.selectList("checkplusmng.codetypeMapper.selectCodeType", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectCodeTypePaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("checkplusmng.codetypeMapper.selectCodeTypePaging", param);
		} finally {
			session.close();
		}
	}

	public CM_CodeType selectCodeTypeByType(String typeParam) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("type", typeParam);
			return session.selectOne("checkplusmng.codetypeMapper.selectCodeType", param);
		} finally {
			session.close();
		}
	}

	public void insertCodeType(CM_CodeType code) {
		SqlSession session = factory.openSession();
		try {
			session.insert("checkplusmng.codetypeMapper.insertCodeType", code);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateCodeType(CM_CodeType code) {
		SqlSession session = factory.openSession();
		try {
			session.update("checkplusmng.codetypeMapper.updateCodeType", code);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteCodeType(CM_CodeType code) {
		SqlSession session = factory.openSession();
		try {
			session.delete("checkplusmng.codetypeMapper.deleteCodeType", code);
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
					session.insert("checkplusmng.codetypeMapper.insertCodeType", item);
				} else if ("U".equals(item.getTouch())) {
					session.update("checkplusmng.codetypeMapper.updateCodeType", item);
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
					session.delete("checkplusmng.codeMapper.deleteCodeByType", item);
					session.delete("checkplusmng.codetypeMapper.deleteCodeType", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
}
