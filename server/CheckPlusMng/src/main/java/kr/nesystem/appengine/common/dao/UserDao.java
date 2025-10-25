package kr.nesystem.appengine.common.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_User;

public class UserDao extends BaseDao<CM_User> {
	public UserDao() {
		super(CM_User.class);
	}
	
	public CM_User selectUserByUserId(String userId) throws Exception {
		PropertyFilter filter = StructuredQuery.PropertyFilter.eq("userId", userId);
		List<CM_User> list = super.list(filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

}
