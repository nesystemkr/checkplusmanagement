package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_BoardContentReply;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class BoardContentReplyDao {
	public SqlSessionFactory factory =  null;

	public BoardContentReplyDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public void insertBoardContentReply(CM_BoardContentReply boardContentReply) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.boardMapper.insertBoardContentReply", boardContentReply);
			session.update("kr.peelknight.mybatis.boardMapper.updateBoardContentReplyCount", boardContentReply);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateBoardContentReply(CM_BoardContentReply boardContentReply) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.boardMapper.updateBoardContentReply", boardContentReply);
			session.update("kr.peelknight.mybatis.boardMapper.updateBoardContentReplyCount", boardContentReply);
			session.commit();
		} finally {
			session.close();
		}
	}

	public CM_BoardContentReply selectBoardContentReplyByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoardContentReply", param);
		} finally {
			session.close();
		}
	}

	public List<CM_BoardContentReply> selectBoardContentReplys(long boardContentIdKey, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardContentIdKey", boardContentIdKey);
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.mybatis.boardMapper.selectBoardContentReply", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectBoardContentReplyPaging(long boardContentIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardContentIdKey", boardContentIdKey);
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoardContentReplyPaging", param);
		} finally {
			session.close();
		}
	}
}
