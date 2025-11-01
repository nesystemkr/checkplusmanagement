package kr.peelknight.common.service;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import kr.peelknight.common.dao.MenuDao;
import kr.peelknight.common.func.InitBaseFunctions;
import kr.peelknight.common.model.CM_Menu;
//import kr.peelknight.util.L10N;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/init")
public class InitService extends InitBaseFunctions {
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/serverbase")
	public Response initServerBase() {
		try {
			if (checkIfAdminExist() == true) {
				ResponseUtil.getResponse(Status.CONFLICT);
			}
			createServerBaseTable();
			createAdminUser();
			insertServerBaseCodes();
			insertServerBaseL10Ns();
			insertServerBaseMenus();
			insertServerBaseDaemon();
			insertFragments();
			
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/serverbase/recreate/cmcode")
	public Response reCreateCheckPlusCodes() {
		try {
			recreateCMCodeTables();
			insertServerBaseCodes();
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public void insertServerBaseL10Ns() {
		insertL10Ns_default("lang.properties");
		insertL10Ns_locale("lang_en.properties", "en_US");
//		L10N.loadResource();
	}
	
	public void insertServerBaseMenus() {
		MenuDao menuDao = new MenuDao();
		CM_Menu menuSiteMng = getMenuModel(0, "MENU_SITEMNG", null, 1);
		CM_Menu menuSetup = getMenuModel(0, "MENU_SITESETUP", null, 2);
		menuDao.insertMenu(menuSiteMng);
		menuDao.insertMenu(menuSetup);
		CM_Menu menuUser = getMenuModel(menuSiteMng.getIdKey(), "MENU_USERMNG", "/sitemng/user.jsp", 1);
		CM_Menu menuMenu   = getMenuModel(menuSetup.getIdKey(), "MENU_MENUMNG"  , "/sitesetup/menu.jsp"  , 1);
		CM_Menu menuCmCode = getMenuModel(menuSetup.getIdKey(), "MENU_CMCODEMNG", "/sitesetup/cmcode.jsp", 2);
		CM_Menu menuL10N   = getMenuModel(menuSetup.getIdKey(), "MENU_L10NMNG"  , "/sitesetup/l10n.jsp"  , 3);
		CM_Menu menuBoard  = getMenuModel(menuSetup.getIdKey(), "MENU_BOARDMNG" , "/sitesetup/board.jsp" , 4);
		CM_Menu menuDemon  = getMenuModel(menuSetup.getIdKey(), "MENU_DAEMONMNG", "/sitesetup/daemon.jsp", 4);
		menuDao.insertMenu(menuUser);
		menuDao.insertMenu(menuMenu);
		menuDao.insertMenu(menuCmCode);
		menuDao.insertMenu(menuL10N);
		menuDao.insertMenu(menuBoard);
		menuDao.insertMenu(menuDemon);
		menuDao.insertMenuAuths(getMenuAuthModels(menuSiteMng.getIdKey(), new String[] {"1", "2"}));
		menuDao.insertMenuAuths(getMenuAuthModels(menuUser.getIdKey()  , new String[] {"1", "2"}));
		menuDao.insertMenuAuths(getMenuAuthModels(menuSetup.getIdKey() , new String[] {"1"}));
		menuDao.insertMenuAuths(getMenuAuthModels(menuMenu.getIdKey()  , new String[] {"1"}));
		menuDao.insertMenuAuths(getMenuAuthModels(menuCmCode.getIdKey(), new String[] {"1"}));
		menuDao.insertMenuAuths(getMenuAuthModels(menuL10N.getIdKey()  , new String[] {"1"}));
		menuDao.insertMenuAuths(getMenuAuthModels(menuBoard.getIdKey() , new String[] {"1"}));
		menuDao.insertMenuAuths(getMenuAuthModels(menuDemon.getIdKey() , new String[] {"1"}));
	}
}
