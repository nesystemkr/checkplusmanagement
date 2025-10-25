package kr.nesystem.appengine.common.dao;

import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_Menu;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class MenuDao extends BaseDao<CM_Menu> {
	public MenuDao() {
		super(CM_Menu.class);
	}

	public CM_PagingList<CM_Menu> selectCodes(int offset, int size) throws Exception {
		return super.pagingList(null, offset, size);
	}

	public CM_PagingList<CM_Menu> selectCodeByType(String type) throws Exception {
		PropertyFilter filter = StructuredQuery.PropertyFilter.eq("type", type);
		return super.pagingList(filter, -1, 0);
	}
}
