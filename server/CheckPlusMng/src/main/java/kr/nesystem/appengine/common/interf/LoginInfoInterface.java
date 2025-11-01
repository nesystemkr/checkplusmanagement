package kr.nesystem.appengine.common.interf;

import java.util.List;

import kr.nesystem.appengine.common.model.CM_Menu;

public interface LoginInfoInterface {
	public List<CM_Menu> getMenus(String userType, long userIdKey, String lang) throws Exception;
}
