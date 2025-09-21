package kr.peelknight.common.impl;

import java.util.ArrayList;
import java.util.List;

import kr.peelknight.common.dao.MenuDao;
import kr.peelknight.common.interf.LoginInfoInterface;
import kr.peelknight.common.model.CM_Menu;

public class LoginInfoImplement implements LoginInfoInterface {
	@Override
	public List<CM_Menu> getMenus(String userType, long userIdKey, String lang) {
		MenuDao menuDao = new MenuDao();
		List<CM_Menu> listMenu = menuDao.selectMenusWithUserType(userType, "1");
		return getHierachyMenus(listMenu, lang);
	}
	
	public static List<CM_Menu> getHierachyMenus(List<CM_Menu> listMenu, String lang) {
		List<CM_Menu> retMenu = new ArrayList<>();
		CM_Menu menu;
		CM_Menu parentMenu;
		for (int ii=0; ii<listMenu.size(); ii++) {
			menu = listMenu.get(ii);
			if (menu.getParentIdKey() == 0) {
				retMenu.add(menu);
			} else {
				for (int jj=0; jj<listMenu.size(); jj++) {
					parentMenu = listMenu.get(jj);
					if (parentMenu.getIdKey() == menu.getIdKey()) {
						continue;
					}
					if (parentMenu.getIdKey() == menu.getParentIdKey()) {
						if (parentMenu.getSubMenus() == null) {
							parentMenu.setSubMenus(new ArrayList<>());
						}
						parentMenu.getSubMenus().add(menu);
						break;
					}
				}
			}
			menu.l10n(lang);
		}
		return retMenu;
	}
}
