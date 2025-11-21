package kr.nesystem.appengine.common.dao;

import jakarta.servlet.http.HttpSession;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class CodeDao extends BaseDao<CM_Code> {
	public CodeDao() {
		super(CM_Code.class);
	}

	public CM_PagingList<CM_Code> selectCodes(HttpSession session, int offset, int size) throws Exception {
		return super.pagingList(session, null, offset, size);
	}

	public CM_PagingList<CM_Code> selectCodeByType(HttpSession session, String type) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("type", type);
		return super.pagingList(session, filter, -1, 0);
	}

	public CM_Code selectCodeByTypeNCode(HttpSession session, String type, String code) throws Exception {
		return super.select(session, type + "__" + code);
	}
}
