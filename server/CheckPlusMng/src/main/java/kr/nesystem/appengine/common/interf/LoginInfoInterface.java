package kr.nesystem.appengine.common.interf;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.CM_Menu;

public interface LoginInfoInterface {
	public List<CM_Menu> getMenus(HttpSession session, String userType, long userIdKey, String lang) throws Exception;
}
