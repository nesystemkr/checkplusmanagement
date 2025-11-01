package kr.nesystem.appengine.common.impl;

import java.util.ArrayList;
import java.util.List;

import kr.nesystem.appengine.common.dao.MenuDao;
import kr.nesystem.appengine.common.interf.LoginInfoInterface;
import kr.nesystem.appengine.common.model.CM_Menu;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.util.L10N;

public class LoginInfoImplement implements LoginInfoInterface {
	@Override
	public List<CM_Menu> getMenus(String userType, long userIdKey, String lang) throws Exception {
		MenuDao menuDao = new MenuDao();
		CM_PagingList<CM_Menu> listMenu = menuDao.selectCodes(-1, 0);
		return getHierachyMenus(listMenu.getList());
	}
	
	public static List<CM_Menu> getHierachyMenus(List<CM_Menu> listMenu) {
		List<CM_Menu> retMenu = new ArrayList<>();
		CM_Menu menu;
		CM_Menu parentMenu;
		for (int ii=0; ii<listMenu.size(); ii++) {
			menu = listMenu.get(ii);
			menu.setMenuLocale(L10N.get(menu.getMenuName(), (String)null));
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
		}
		return retMenu;
	}
}
