package kr.nesystem.appengine.common.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.Filter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_L10NLocale;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class L10NLocaleDao extends BaseDeleteAllDao<CM_L10NLocale> {
	public L10NLocaleDao() {
		super(CM_L10NLocale.class);
	}
	
	public CM_PagingList<CM_L10NLocale> selectL10NLocales(String idString, String locale, int offset, int size) throws Exception {
		Filter filter = null;
		if (idString != null && locale != null) {
			filter = CompositeFilter.and(PropertyFilter.eq("idString", idString),
										 PropertyFilter.eq("locale", locale));
		} else if (idString != null) {
			filter = PropertyFilter.eq("idString", idString);
		} else if (locale != null) {
			filter = PropertyFilter.eq("locale", locale);
		}
		return super.pagingList(null, filter, offset, size);
	}
	
	public CM_L10NLocale selectL10NLocaleByIdStringNLocale(String idString, String locale) throws Exception {
		CompositeFilter filter = CompositeFilter.and(PropertyFilter.eq("idString", idString),
													 PropertyFilter.eq("locale", locale));
		List<CM_L10NLocale> list = super.list(null, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
