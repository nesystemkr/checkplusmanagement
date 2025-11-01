package kr.nesystem.appengine.common.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.CM_User;

public class UserDao extends BaseDao<CM_User> {
	public UserDao() {
		super(CM_User.class);
	}
	
	public CM_User selectByUserId(String userId) throws Exception {
		PropertyFilter filter = StructuredQuery.PropertyFilter.eq("userId", userId);
		List<CM_User> list = super.list(filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
	public CM_User selectByIdKey(long idKey) throws Exception {
		return super.select(idKey);
	}
	
	public CM_PagingList<CM_User> selectByType(String userType, int offset, int size) throws Exception {
		PropertyFilter filter = null;
		if (userType != null) {
			filter = StructuredQuery.PropertyFilter.eq("userType", userType);
		}
		return super.pagingList(filter, offset, size);
	}
}
