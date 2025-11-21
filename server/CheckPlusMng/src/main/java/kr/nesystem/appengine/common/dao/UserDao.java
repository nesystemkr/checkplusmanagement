package kr.nesystem.appengine.common.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.CM_User;

public class UserDao extends BaseDao<CM_User> {
	public UserDao() {
		super(CM_User.class);
	}
	
	public CM_User selectByUserId(HttpSession session, String userId) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("userId", userId);
		List<CM_User> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
	public CM_PagingList<CM_User> selectByType(HttpSession session, String userType, int offset, int size) throws Exception {
		PropertyFilter filter = null;
		if (userType != null) {
			filter = PropertyFilter.eq("userType", userType);
		}
		return super.pagingList(session, filter, offset, size);
	}
}
