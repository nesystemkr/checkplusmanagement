package kr.nesystem.appengine.board.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.board.model.CM_BoardContent;
import kr.nesystem.appengine.common.dao.BaseDao;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class BoardContentDao extends BaseDao<CM_BoardContent> {
	public BoardContentDao() {
		super(CM_BoardContent.class);
	}

	public CM_PagingList<CM_BoardContent> selectBoardContents(HttpSession session, long boardIdKey, String status, int offset, int size) throws Exception {
		CompositeFilter filter = CompositeFilter.and(PropertyFilter.eq("boardIdKey", boardIdKey),
													 PropertyFilter.eq("status", status));
		return super.pagingList(session, filter, offset, size);
	}
	
	public List<CM_BoardContent> selectBoardChildContents(HttpSession session, long boardIdKey, long parentIdKey, String status) throws Exception {
		CompositeFilter filter = CompositeFilter.and(PropertyFilter.eq("boardIdKey", boardIdKey),
													 PropertyFilter.eq("parentIdKey", parentIdKey));
		return super.list(session, filter, -1, 0);
	}

	public CM_PagingList<CM_BoardContent> selectBoardTopContents(HttpSession session, long boardIdKey, String status) throws Exception {
		CompositeFilter filter = CompositeFilter.and(PropertyFilter.eq("boardIdKey", boardIdKey),
													 PropertyFilter.eq("topYN", "Y"),
													 PropertyFilter.eq("status", status));
		return super.pagingList(session, filter, -1, 0);
	}
}
