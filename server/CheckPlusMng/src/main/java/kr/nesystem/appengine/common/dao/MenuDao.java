package kr.nesystem.appengine.common.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.CM_Menu;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class MenuDao extends BaseDao<CM_Menu> {
	public MenuDao() {
		super(CM_Menu.class);
	}

	public CM_PagingList<CM_Menu> selectMenus(HttpSession session, int offset, int size) throws Exception {
		return super.pagingList(session, null, offset, size);
	}

	public List<CM_Menu> selectMenus(HttpSession session, long parentIdKey, int offset, int size) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("parentIdKey", parentIdKey);
		return super.list(session, filter, offset, size);
	}

	public int selectMaxOrderSeq(CM_Menu menu) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("parentIdKey", menu.getParentIdKey());
		List<CM_Menu> list = super.list(null, filter, -1, 0);
		int ret = 0;
		for (int ii = 0; ii < list.size(); ii++) {
			if (list.get(ii).getOrderSeq() > ret) {
				ret = list.get(ii).getOrderSeq();
			}
		}
		return ret;
	}
}
