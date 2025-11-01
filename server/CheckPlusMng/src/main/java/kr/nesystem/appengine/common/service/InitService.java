package kr.nesystem.appengine.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.io.Resources;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import kr.nesystem.appengine.common.dao.CodeDao;
import kr.nesystem.appengine.common.dao.CodeTypeDao;
import kr.nesystem.appengine.common.dao.L10NDao;
import kr.nesystem.appengine.common.dao.MenuDao;
import kr.nesystem.appengine.common.dao.UserDao;
import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_CodeType;
import kr.nesystem.appengine.common.model.CM_L10N;
import kr.nesystem.appengine.common.model.CM_Menu;
import kr.nesystem.appengine.common.model.CM_User;
import kr.nesystem.appengine.common.util.L10N;
import kr.peelknight.util.CommonFunc;
import kr.peelknight.util.ResponseUtil;

@Path("/gae/init")
public class InitService {
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/server")
	public Response initServerBase() {
		try {
			if (checkIfAdminExist() == true) {
				ResponseUtil.getResponse(Status.CONFLICT);
			}
			createAdminUser();
			insertServerBaseCodes();
			insertServerBaseL10Ns();
			insertServerBaseMenus();
//			insertFragments();
			
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public void insertServerBaseCodes() throws Exception {
		CodeTypeDao typeDao = new CodeTypeDao();
		CodeDao codeDao = new CodeDao();
		typeDao.insert(getCodeTypeModel("LOCALE", "CODETYPE_LOCALE"));
		codeDao.insert(getCodeModel("LOCALE", "ko_KR", "LOCALE_ko_KR", 1));
		codeDao.insert(getCodeModel("LOCALE", "en_US", "LOCALE_en_US", 2));
		typeDao.insert(getCodeTypeModel("USERTYPE", "CODETYPE_USERTYPE"));
		typeDao.insert(getCodeTypeModel("USERSTATUS", "CODETYPE_USERSTATUS"));
		typeDao.insert(getCodeTypeModel("BOARDSTATUS", "CODETYPE_BOARDSTATUS"));
		typeDao.insert(getCodeTypeModel("BOARDCONTENTSTATUS", "CODETYPE_BOARDCONTENTSTATUS"));
		typeDao.insert(getCodeTypeModel("MENUSTATUS", "CODETYPE_MENUSTATUS"));
		typeDao.insert(getCodeTypeModel("DAEMONSTATUS", "CODETYPE_DAEMONSTATUS"));
		typeDao.insert(getCodeTypeModel("DAEMONRUNNING", "CODETYPE_DAEMONRUNNING"));
		typeDao.insert(getCodeTypeModel("DAEMONAUTOSTART", "CODETYPE_DAEMONAUTOSTART"));
		codeDao.insert(getCodeModel("USERTYPE", "0", "USERTYPE_0", 1));
		codeDao.insert(getCodeModel("USERTYPE", "1", "USERTYPE_1", 2));
		codeDao.insert(getCodeModel("USERTYPE", "2", "USERTYPE_2", 3));
		codeDao.insert(getCodeModel("USERTYPE", "3", "USERTYPE_3", 4));
		codeDao.insert(getCodeModel("USERSTATUS", "0", "USERSTATUS_0", 1));
		codeDao.insert(getCodeModel("USERSTATUS", "1", "USERSTATUS_1", 2));
		codeDao.insert(getCodeModel("USERSTATUS", "9", "USERSTATUS_9", 3));
		codeDao.insert(getCodeModel("BOARDSTATUS", "0", "BOARDSTATUS_0", 1));
		codeDao.insert(getCodeModel("BOARDSTATUS", "1", "BOARDSTATUS_1", 2));
		codeDao.insert(getCodeModel("BOARDCONTENTSTATUS", "0", "BOARDCONTENTSTATUS_0", 1));
		codeDao.insert(getCodeModel("BOARDCONTENTSTATUS", "1", "BOARDCONTENTSTATUS_1", 2));
		codeDao.insert(getCodeModel("MENUSTATUS", "0", "MENUSTATUS_0", 1));
		codeDao.insert(getCodeModel("MENUSTATUS", "1", "MENUSTATUS_1", 2));
		codeDao.insert(getCodeModel("DAEMONSTATUS", "0", "DAEMONSTATUS_0", 1));
		codeDao.insert(getCodeModel("DAEMONSTATUS", "1", "DAEMONSTATUS_1", 2));
		codeDao.insert(getCodeModel("DAEMONRUNNING", "0", "DAEMONRUNNING_0", 1));
		codeDao.insert(getCodeModel("DAEMONRUNNING", "1", "DAEMONRUNNING_1", 2));
		codeDao.insert(getCodeModel("DAEMONAUTOSTART", "Y", "DAEMONAUTOSTART_Y", 1));
		codeDao.insert(getCodeModel("DAEMONAUTOSTART", "N", "DAEMONAUTOSTART_N", 2));
	}

	public boolean checkIfAdminExist() {
		UserDao userDao = new UserDao();
		CM_User existUser = null;
		try {
			existUser = userDao.selectByUserId("admin");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return existUser != null;
	}
	
	public void createAdminUser() throws Exception {
		UserDao userDao = new UserDao();
		userDao.insert(getUserModel("admin", "1234", "admin"));
	}
	
	public CM_User getUserModel(String userId, String password, String userName) {
		return getUserModel(userId, password, userName, "1");
	}
	
	public CM_User getUserModel(String userId, String password, String userName, String userType) {
		CM_User user = new CM_User();
		user.setUserId(userId);
		user.setPassword(CommonFunc.getHashedPassword(password, userId));
		user.setUserType(userType);
		user.setUserName(userName);
		user.setStatus("1");
		user.setLoginFailCount(0);
		user.setLastLoginDate(null);
		user.setLastLoginSeq(0);
		user.setCreateDate(new Date());
		return user;
	}
	
	public CM_Code getCodeModel(String type, String key, String value, int orderSeq) {
		CM_Code code = new CM_Code();
		code.setType(type);
		code.setCode(key);
		code.setCodeName(value);
		code.setOrderSeq(orderSeq);
		return code;
	}
	
	public CM_CodeType getCodeTypeModel(String type, String name) {
		CM_CodeType codeType = new CM_CodeType();
		codeType.setType(type);
		codeType.setTypeName(name);
		return codeType;
	}
	
	public void insertServerBaseL10Ns() throws Exception {
		insertL10Ns_default("lang.properties");
		L10N.loadResource();
	}
	
	public void insertL10Ns_default(String profileFileName) throws Exception {
		L10NDao l10nDao = new L10NDao();
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(profileFileName);
			Properties prop = new Properties();
			prop.load(inputStream);
			Set<Object> keys = prop.keySet();
			for (Object key : keys) {
				String val = prop.getProperty((String)key);
				l10nDao.insert(getL10NModel((String)key, new String(val.getBytes("ISO-8859-1"), "UTF-8")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CM_L10N getL10NModel(String idString, String defaultString) {
		CM_L10N l10n = new CM_L10N();
		l10n.setIdString(idString);
		l10n.setDefaultString(defaultString);
		return l10n;
	}
	

	public void insertServerBaseMenus() throws Exception {
		MenuDao menuDao = new MenuDao();
		CM_Menu menuSiteMng = getMenuModel(0, "MENU_SITEMNG", null, 1);
		CM_Menu menuSetup = getMenuModel(0, "MENU_SITESETUP", null, 2);
		Entity entity;
		entity = menuDao.insert(menuSiteMng);
		menuSiteMng.setIdKey(entity.getKey().getId());
		entity = menuDao.insert(menuSetup);
		menuSetup.setIdKey(entity.getKey().getId());
		CM_Menu menuUser = getMenuModel(menuSiteMng.getIdKey(), "MENU_USERMNG", "/sitemng/user.jsp", 1);
		CM_Menu menuMenu   = getMenuModel(menuSetup.getIdKey(), "MENU_MENUMNG"  , "/sitesetup/menu.jsp"  , 1);
		CM_Menu menuCmCode = getMenuModel(menuSetup.getIdKey(), "MENU_CMCODEMNG", "/sitesetup/cmcode.jsp", 2);
		CM_Menu menuL10N   = getMenuModel(menuSetup.getIdKey(), "MENU_L10NMNG"  , "/sitesetup/l10n.jsp"  , 3);
		CM_Menu menuBoard  = getMenuModel(menuSetup.getIdKey(), "MENU_BOARDMNG" , "/sitesetup/board.jsp" , 4);
		CM_Menu menuDemon  = getMenuModel(menuSetup.getIdKey(), "MENU_DAEMONMNG", "/sitesetup/daemon.jsp", 4);
		menuDao.insert(menuUser);
		menuDao.insert(menuMenu);
		menuDao.insert(menuCmCode);
		menuDao.insert(menuL10N);
		menuDao.insert(menuBoard);
		menuDao.insert(menuDemon);
	}
	
	public CM_Menu getMenuModel(long parentIdKey, String menuName, String menuUrl, int orderSeq) {
		CM_Menu menu = new CM_Menu();
		menu.setParentIdKey(parentIdKey);
		menu.setMenuName(menuName);
		menu.setMenuUrl(menuUrl);
		menu.setStatus("1");
		menu.setOrderSeq(orderSeq);
		return menu;
	}
}
