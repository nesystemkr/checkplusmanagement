package kr.nesystem.appengine.board.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.board.model.CM_BoardContentReply;
import kr.nesystem.appengine.common.dao.BaseDao;

public class BoardContentReplyDao extends BaseDao<CM_BoardContentReply> {
	public BoardContentReplyDao() {
		super(CM_BoardContentReply.class);
	}

	public List<CM_BoardContentReply> selectBoardContentReplys(HttpSession session, long boardContentIdKey, int offset, int size) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("boardContentIdKey", boardContentIdKey);
		return super.list(session, filter, -1, 0);
	}
}
