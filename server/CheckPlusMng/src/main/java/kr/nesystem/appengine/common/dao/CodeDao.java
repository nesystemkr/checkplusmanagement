package kr.nesystem.appengine.common.dao;

import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class CodeDao extends BaseDao<CM_Code> {
	public CodeDao() {
		super(CM_Code.class);
	}

	public CM_PagingList<CM_Code> selectCodes(int offset, int size) throws Exception {
		return super.pagingList(null, offset, size);
	}

	public CM_PagingList<CM_Code> selectCodeByType(String type) throws Exception {
		PropertyFilter filter = StructuredQuery.PropertyFilter.eq("type", type);
		return super.pagingList(filter, -1, 0);
	}

	public CM_Code selectCodeByTypeNCode(String type, String code) throws Exception {
		return super.select(type + "__" + code);
	}
}
