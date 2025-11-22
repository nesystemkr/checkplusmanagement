package kr.nesystem.appengine.common.util;

import kr.nesystem.appengine.common.dao.UserDao;
import kr.nesystem.appengine.common.model.CM_User;

public class UserStore {
	static public String getName(long userIdKey) {
		try {
			UserDao dao = new UserDao();
			CM_User user = dao.select(null, userIdKey);
			if (user != null) {
				return user.getUserName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
