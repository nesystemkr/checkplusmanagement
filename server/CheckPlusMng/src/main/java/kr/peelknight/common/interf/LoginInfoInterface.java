package kr.peelknight.common.interf;

import java.util.List;

import kr.peelknight.common.model.CM_Menu;

public interface LoginInfoInterface {
	public List<CM_Menu> getMenus(String userType, long userIdKey, String lang);
}
