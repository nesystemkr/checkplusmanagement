package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Board;
import kr.peelknight.common.model.CM_BoardAuth;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class BoardDao {
	public SqlSessionFactory factory =  null;

	public BoardDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_Board> selectBoards(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.mybatis.boardMapper.selectBoard", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectBoardPaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoardPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_Board selectBoardByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoard", param);
		} finally {
			session.close();
		}
	}
	
	public CM_Board selectBoardByBoardId(String boardId) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardId", boardId);
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoard", param);
		} finally {
			session.close();
		}
	}

	public void insertBoard(CM_Board board) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.boardMapper.insertBoard", board);
			if (board.getBoardAuths() != null) {
				CM_BoardAuth boardAuth;
				for (int ii=0; ii<board.getBoardAuths().size(); ii++) {
					boardAuth = board.getBoardAuths().get(ii);
					boardAuth.setBoardIdKey(board.getIdKey());
					session.insert("kr.peelknight.mybatis.boardMapper.insertBoardAuth", boardAuth);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateBoard(CM_Board board) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.boardMapper.updateBoard", board);
			session.delete("kr.peelknight.mybatis.boardMapper.deleteBoardAuth", board);
			if (board.getBoardAuths() != null) {
				CM_BoardAuth boardAuth;
				for (int ii=0; ii<board.getBoardAuths().size(); ii++) {
					boardAuth = board.getBoardAuths().get(ii);
					boardAuth.setBoardIdKey(board.getIdKey());
					session.insert("kr.peelknight.mybatis.boardMapper.insertBoardAuth", boardAuth);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public List<CM_BoardAuth> selectBoardAuthWithBoard(CM_Board board) {
		SqlSession session = factory.openSession();
		try {
			return session.selectList("kr.peelknight.mybatis.boardMapper.selectBoardAuthWithBoard", board);
		} finally {
			session.close();
		}
	}
	
	public CM_BoardAuth selectBoardAuthByBoardIdKeyAndUserType(long boardIdKey, String userType) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("boardIdKey", boardIdKey);
			param.put("userType", userType);
			return session.selectOne("kr.peelknight.mybatis.boardMapper.selectBoardAuth", param);
		} finally {
			session.close();
		}
	}
	
	public void insertBoardAuth(CM_BoardAuth boardAuth) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.boardMapper.insertBoardAuth", boardAuth);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateBoardAuth(CM_BoardAuth boardAuth) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.boardMapper.updateBoardAuth", boardAuth);
			session.commit();
		} finally {
			session.close();
		}
	}
}
