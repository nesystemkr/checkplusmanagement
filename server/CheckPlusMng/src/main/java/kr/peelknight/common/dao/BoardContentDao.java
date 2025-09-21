package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_BoardContent;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class BoardContentDao {
	public SqlSessionFactory factory =  null;

	public BoardContentDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_BoardContent> selectBoardContents(long boardIdKey, String status, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardIdKey", boardIdKey);
			if (status != null) {
				param.put("status", status);
			}
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.mybatis.boardMapper.selectBoardContent", param);
		} finally {
			session.close();
		}
	}

	public List<CM_BoardContent> selectBoardTopContents(long boardIdKey, String status) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardIdKey", boardIdKey);
			param.put("topYN", "Y");
			if (status != null) {
				param.put("status", status);
			}
			return session.selectList("kr.peelknight.mybatis.boardMapper.selectBoardContent", param);
		} finally {
			session.close();
		}
	}
	
	public List<CM_BoardContent> selectBoardChildContents(long boardIdKey, List<Long> parentIdKeys, String status) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardIdKey", boardIdKey);
			param.put("parentIdKeys", parentIdKeys);
			if (status != null) {
				param.put("status", status);
			}
			return session.selectList("kr.peelknight.mybatis.boardMapper.selectBoardContent", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectBoardContentPaging(long boardIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardIdKey", boardIdKey);
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoardContentPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_BoardContent selectBoardContentByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoardContentFull", param);
		} finally {
			session.close();
		}
	}

	public void insertBoardContent(CM_BoardContent boardContent) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.boardMapper.insertBoardContent", boardContent);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateBoardContent(CM_BoardContent boardContent) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.boardMapper.updateBoardContent", boardContent);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteBoardContent(CM_BoardContent boardContent) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.mybatis.boardMapper.deleteBoardContent", boardContent);
			session.commit();
		} finally {
			session.close();
		}
	}
}
