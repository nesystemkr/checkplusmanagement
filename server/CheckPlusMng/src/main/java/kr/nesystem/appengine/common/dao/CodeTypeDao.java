package kr.nesystem.appengine.common.dao;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.CM_CodeType;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class CodeTypeDao extends BaseDao<CM_CodeType> {
	public CodeTypeDao() {
		super(CM_CodeType.class);
	}
	
	public CM_PagingList<CM_CodeType> selectCodeTypes(HttpSession session, int offset, int size) throws Exception {
		return super.pagingList(session, null, offset, size);
	}
}
