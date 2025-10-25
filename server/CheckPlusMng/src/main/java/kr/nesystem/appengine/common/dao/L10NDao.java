package kr.nesystem.appengine.common.dao;

import kr.nesystem.appengine.common.model.CM_L10N;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class L10NDao extends BaseDao<CM_L10N> {
	public L10NDao() {
		super(CM_L10N.class);
	}

	public CM_PagingList<CM_L10N> selectL10Ns(int offset, int size, String search) throws Exception {
		return super.pagingList(null, -1, 0);
	}
}
