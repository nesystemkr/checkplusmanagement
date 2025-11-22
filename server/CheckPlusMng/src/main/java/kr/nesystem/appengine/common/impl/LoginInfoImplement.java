package kr.nesystem.appengine.common.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.dao.MenuDao;
import kr.nesystem.appengine.common.interf.LoginInfoInterface;
import kr.nesystem.appengine.common.model.CM_Menu;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.util.L10N;

public class LoginInfoImplement implements LoginInfoInterface {
	@Override
	public List<CM_Menu> getMenus(HttpSession session, String userType, long userIdKey, String lang) throws Exception {
		MenuDao menuDao = new MenuDao();
		CM_PagingList<CM_Menu> listMenu = menuDao.selectMenus(session, -1, 0);
		return getHierachyMenus(listMenu.getList());
	}
	
	public static List<CM_Menu> getHierachyMenus(List<CM_Menu> listMenu) {
		listMenu.sort(new Comparator<CM_Menu>() {
			@Override
			public int compare(CM_Menu o1, CM_Menu o2) {
				if (o1.getParentIdKey() == 0 && o2.getParentIdKey() == 0) {
					if (o1.getOrderSeq() < o2.getOrderSeq()) {
						return -1;
					} else if (o1.getOrderSeq() < o2.getOrderSeq()) {
						return 1;
					}
					return 0;
				} else if (o1.getParentIdKey() == 0) {
					return -1;
				} else if (o2.getParentIdKey() == 0) {
					return 1;
				} else {
					if (o1.getOrderSeq() < o2.getOrderSeq()) {
						return -1;
					} else if (o1.getOrderSeq() < o2.getOrderSeq()) {
						return 1;
					}
					return 0;
				}
			}
			
		});
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
