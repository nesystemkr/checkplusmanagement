package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Menu;
import kr.peelknight.common.model.CM_MenuAuth;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class MenuDao {
	public SqlSessionFactory factory =  null;
	
	public MenuDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_Menu> selectMenus(long parentIdKey, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			if (parentIdKey > -1) {
				param.put("parentIdKey", parentIdKey);
			}
			return session.selectList("kr.peelknight.mybatis.menuMapper.selectMenu", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectMenuPaging(long parentIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (parentIdKey > -1) {
				param.put("parentIdKey", parentIdKey);
			}
			return session.selectOne("kr.peelknight.mybatis.menuMapper.selectMenuPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_Menu selectMenuByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.mybatis.menuMapper.selectMenu", param);
		} finally {
			session.close();
		}
	}

	public List<CM_Menu> selectMenusWithUserType(String userType, String status) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (userType != null && !"".equals(userType)) {
				param.put("userType", userType);
			}
			if (status != null) {
				param.put("status", status);
			}
			List<CM_Menu> list = session.selectList("kr.peelknight.mybatis.menuMapper.selectMenuWithUserType", param);
			return list;
		} finally {
			session.close();
		}
	}
	
	public void insertMenu(CM_Menu menu) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.menuMapper.insertMenu", menu);
			if (menu.getMenuAuths() != null) {
				for (int ii=0; ii<menu.getMenuAuths().size(); ii++) {
					menu.getMenuAuths().get(ii).setMenuIdKey(menu.getIdKey());
					session.insert("kr.peelknight.mybatis.menuMapper.insertMenuAuth", menu.getMenuAuths().get(ii));
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateMenu(CM_Menu menu) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			session.update("kr.peelknight.mybatis.menuMapper.updateMenu", menu);
			if (menu.getMenuAuths() != null) {

				for (int ii=0; ii<menu.getMenuAuths().size(); ii++) {
					menu.getMenuAuths().get(ii).setMenuIdKey(menu.getIdKey());
					param.put("menuIdKey", menu.getMenuAuths().get(ii).getMenuIdKey());
					param.put("userType", menu.getMenuAuths().get(ii).getUserType());
					CM_MenuAuth menuAuth = session.selectOne("kr.peelknight.mybatis.menuMapper.selectMenuAuth", param);
					if (menuAuth == null) {
						session.insert("kr.peelknight.mybatis.menuMapper.insertMenuAuth", menu.getMenuAuths().get(ii));
					} else {
						session.update("kr.peelknight.mybatis.menuMapper.updateMenuAuthWidthUserType", menu.getMenuAuths().get(ii));
					}
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void updateMenus(List<CM_Menu> list) {
		SqlSession session = factory.openSession();
		try {
			CM_Menu menu;
			for (int ii=0; ii<list.size(); ii++) {
				menu = list.get(ii);
				session.update("kr.peelknight.mybatis.menuMapper.updateMenu", menu);
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteMenu(CM_Menu menu) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.mybatis.menuMapper.deleteMenu", menu);
			session.delete("kr.peelknight.mybatis.menuMapper.deleteMenuAuth", menu);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public List<CM_MenuAuth> selectMenuAuthWithMenu(CM_Menu menu) {
		SqlSession session = factory.openSession();
		try {
			return session.selectList("kr.peelknight.mybatis.menuMapper.selectMenuAuthWithMenu", menu);
		} finally {
			session.close();
		}
	}
	
	public CM_Paging selectMaxOrderSeq(CM_Menu menu) {
		SqlSession session = factory.openSession();
		try {
			return session.selectOne("kr.peelknight.mybatis.menuMapper.selectMaxOrderSeqWithMenu", menu);
		} finally {
			session.close();
		}
	}
	
	public CM_MenuAuth selectMenuAuthByMenuIdKeyAndUserType(long menuIdKey, String userType) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("menuIdKey", menuIdKey);
			param.put("userType", userType);
			return session.selectOne("kr.peelknight.mybatis.menuMapper.selectMenuAuth", param);
		} finally {
			session.close();
		}
	}
	
	public void insertMenuAuth(CM_MenuAuth menuAuth) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.menuMapper.insertMenuAuth", menuAuth);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void insertMenuAuths(List<CM_MenuAuth> menuAuths) {
		SqlSession session = factory.openSession();
		try {
			CM_MenuAuth menuAuth;
			for (int ii=0; ii<menuAuths.size(); ii++) {
				menuAuth = menuAuths.get(ii);
				session.insert("kr.peelknight.mybatis.menuMapper.insertMenuAuth", menuAuth);
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateMenuAuth(CM_MenuAuth menuAuth) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.menuMapper.updateMenuAuth", menuAuth);
			session.commit();
		} finally {
			session.close();
		}
	}
}
