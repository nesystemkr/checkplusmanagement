package kr.nesystem.checkplusmng.dao;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.nesystem.checkplusmng.model.CM_Code;
import kr.nesystem.model.CM_Paging;
import kr.nesystem.util.MySqlSessionFactory;

public class CodeDao {
	public SqlSessionFactory factory =  null;

	public CodeDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_Code> selectCodes(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("checkplusmng.codeMapper.selectCode", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectCodePaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("checkplusmng.codeMapper.selectCodePaging", param);
		} finally {
			session.close();
		}
	}

	public List<CM_Code> selectCodeByType(String typeParam) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("type", typeParam);
			return session.selectList("checkplusmng.codeMapper.selectCode", param);
		} finally {
			session.close();
		}
	}

	public CM_Code selectCodeByTypeNCode(String typeParam, String codeParam) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("type", typeParam);
			param.put("code", codeParam);
			return session.selectOne("checkplusmng.codeMapper.selectCode", param);
		} finally {
			session.close();
		}
	}

	public void insertCode(CM_Code code) {
		SqlSession session = factory.openSession();
		try {
			session.insert("checkplusmng.codeMapper.insertCode", code);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void insertCodes(List<CM_Code> list) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("list", list);
			session.insert("checkplusmng.codeMapper.insertCodes", param);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateCode(CM_Code code) {
		SqlSession session = factory.openSession();
		try {
			session.update("checkplusmng.codeMapper.updateCode", code);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteCode(CM_Code code) {
		SqlSession session = factory.openSession();
		try {
			session.delete("checkplusmng.codeMapper.deleteCode", code);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void insertOrUpdateCodes(List<CM_Code> list) {
		SqlSession session = factory.openSession();
		try {
			CM_Code item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if ("I".equals(item.getTouch())) {
					session.insert("checkplusmng.codeMapper.insertCode", item);
				} else if ("U".equals(item.getTouch())) {
					session.update("checkplusmng.codeMapper.updateCode", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteCodes(List<CM_Code> list) {
		SqlSession session = factory.openSession();
		try {
			CM_Code item;
			for (int ii=0; ii<list.size(); ii++) {
				item = list.get(ii);
				if (item.getType() != null && !item.getType().isEmpty()) {
					session.delete("checkplusmng.codeMapper.deleteCode", item);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
}
