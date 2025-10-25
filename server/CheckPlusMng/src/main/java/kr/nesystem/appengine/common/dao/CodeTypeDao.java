package kr.nesystem.appengine.common.dao;

import kr.nesystem.appengine.common.model.CM_CodeType;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class CodeTypeDao extends BaseDao<CM_CodeType> {
	public CodeTypeDao() {
		super(CM_CodeType.class);
	}
	
	public CM_PagingList<CM_CodeType> selectCodeTypes(int offset, int size) throws Exception {
		return super.pagingList(null, offset, size);
	}
}
