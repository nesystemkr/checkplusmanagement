package kr.nesystem.appengine.board.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.board.model.CM_Board;
import kr.nesystem.appengine.common.dao.BaseDao;

public class BoardDao extends BaseDao<CM_Board> {
	public BoardDao() {
		super(CM_Board.class);
	}

	public CM_Board selectBoardByBoardId(HttpSession session, String boardId) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("boardId", boardId);
		List<CM_Board> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
