package kr.nesystem.appengine.common.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.CM_Menu;
import kr.nesystem.appengine.common.model.CM_MenuAuth;

public class MenuAuthDao extends BaseDao<CM_MenuAuth> {
	public MenuAuthDao() {
		super(CM_MenuAuth.class);
	}
	
	public List<CM_MenuAuth> selectMenuAuthWithMenu(HttpSession session, CM_Menu menu) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("menuIdKey", menu.getIdKey());
		return super.list(session, filter, -1, 0);
	}
	
	public List<CM_MenuAuth> selectMenuAuthWithUserType(HttpSession session, String userType) throws Exception {
		PropertyFilter filter = null;
		if (userType != null) {
			filter = PropertyFilter.eq("userType", userType);
		}
		return super.list(session, filter, -1, 0);
	}
}
