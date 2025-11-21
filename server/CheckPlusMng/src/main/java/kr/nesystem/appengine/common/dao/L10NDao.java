package kr.nesystem.appengine.common.dao;

import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.CM_L10N;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class L10NDao extends BaseDeleteAllDao<CM_L10N> {
	public L10NDao() {
		super(CM_L10N.class);
	}

	public CM_PagingList<CM_L10N> selectL10Ns(HttpSession session, int offset, int size, String search) throws Exception {
		if (search != null) {
			char lastChar = search.charAt(search.length() - 1);
			String searchBase = search.substring(0, search.length() - 1);
			String nextSearch = searchBase + (char) (lastChar + 1);
			CompositeFilter leftFilter = CompositeFilter.and(PropertyFilter.ge("idString", search),
															 PropertyFilter.lt("idString", nextSearch));
			CompositeFilter rightFilter = CompositeFilter.and(PropertyFilter.ge("defaultString", search),
															  PropertyFilter.lt("defaultString", nextSearch));
			CompositeFilter filter = CompositeFilter.or(leftFilter, rightFilter);
			return super.pagingList(session, filter, -1, 0);
		}
		return super.pagingList(session, null, -1, 0);
	}
}
