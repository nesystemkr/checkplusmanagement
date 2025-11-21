package kr.nesystem.appengine.board.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.board.model.CM_Board;
import kr.nesystem.appengine.board.model.CM_BoardAuth;
import kr.nesystem.appengine.common.dao.BaseDao;

public class BoardAuthDao extends BaseDao<CM_BoardAuth> {
	public BoardAuthDao() {
		super(CM_BoardAuth.class);
	}

	public List<CM_BoardAuth> selectBoardAuthWithBoard(HttpSession session, CM_Board board) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("menuIdKey", board.getIdKey());
		return super.list(session, filter, -1, 0);
	}
	
	public CM_BoardAuth selectBoardAuthByBoardIdKeyAndUserType(HttpSession session, long boardIdKey, String userType) throws Exception {
		CompositeFilter filter = CompositeFilter.and(PropertyFilter.eq("boardIdKey", boardIdKey),
													 PropertyFilter.eq("userType", userType));
		List<CM_BoardAuth> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
