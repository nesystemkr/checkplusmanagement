package kr.nesystem.appengine.common.service;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import kr.nesystem.appengine.common.dao.CodeDao;
import kr.nesystem.appengine.common.dao.CodeTypeDao;
import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_CodeType;
import kr.peelknight.util.ResponseUtil;

@Path("/gae/init")
public class InitService {
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/server")
	public Response initServerBase() {
		try {
//			if (checkIfAdminExist() == true) {
//				ResponseUtil.getResponse(Status.CONFLICT);
//			}
//			createAdminUser();
			insertServerBaseCodes();
//			insertServerBaseL10Ns();
//			insertServerBaseMenus();
//			insertServerBaseDaemon();
//			insertFragments();
			
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public void insertServerBaseCodes() {
		CodeTypeDao typeDao = new CodeTypeDao();
		CodeDao codeDao = new CodeDao();
		typeDao.insertCodeType(getCodeTypeModel("LOCALE", "CODETYPE_LOCALE"));
		codeDao.insertCode(getCodeModel("LOCALE", "ko_KR", "LOCALE_ko_KR", 1));
		codeDao.insertCode(getCodeModel("LOCALE", "en_US", "LOCALE_en_US", 2));
		typeDao.insertCodeType(getCodeTypeModel("USERTYPE", "CODETYPE_USERTYPE"));
		typeDao.insertCodeType(getCodeTypeModel("USERSTATUS", "CODETYPE_USERSTATUS"));
		typeDao.insertCodeType(getCodeTypeModel("BOARDSTATUS", "CODETYPE_BOARDSTATUS"));
		typeDao.insertCodeType(getCodeTypeModel("BOARDCONTENTSTATUS", "CODETYPE_BOARDCONTENTSTATUS"));
		typeDao.insertCodeType(getCodeTypeModel("MENUSTATUS", "CODETYPE_MENUSTATUS"));
		typeDao.insertCodeType(getCodeTypeModel("DAEMONSTATUS", "CODETYPE_DAEMONSTATUS"));
		typeDao.insertCodeType(getCodeTypeModel("DAEMONRUNNING", "CODETYPE_DAEMONRUNNING"));
		typeDao.insertCodeType(getCodeTypeModel("DAEMONAUTOSTART", "CODETYPE_DAEMONAUTOSTART"));
		codeDao.insertCode(getCodeModel("USERTYPE", "0", "USERTYPE_0", 1));
		codeDao.insertCode(getCodeModel("USERTYPE", "1", "USERTYPE_1", 2));
		codeDao.insertCode(getCodeModel("USERTYPE", "2", "USERTYPE_2", 3));
		codeDao.insertCode(getCodeModel("USERTYPE", "3", "USERTYPE_3", 4));
		codeDao.insertCode(getCodeModel("USERSTATUS", "0", "USERSTATUS_0", 1));
		codeDao.insertCode(getCodeModel("USERSTATUS", "1", "USERSTATUS_1", 2));
		codeDao.insertCode(getCodeModel("USERSTATUS", "9", "USERSTATUS_9", 3));
		codeDao.insertCode(getCodeModel("BOARDSTATUS", "0", "BOARDSTATUS_0", 1));
		codeDao.insertCode(getCodeModel("BOARDSTATUS", "1", "BOARDSTATUS_1", 2));
		codeDao.insertCode(getCodeModel("BOARDCONTENTSTATUS", "0", "BOARDCONTENTSTATUS_0", 1));
		codeDao.insertCode(getCodeModel("BOARDCONTENTSTATUS", "1", "BOARDCONTENTSTATUS_1", 2));
		codeDao.insertCode(getCodeModel("MENUSTATUS", "0", "MENUSTATUS_0", 1));
		codeDao.insertCode(getCodeModel("MENUSTATUS", "1", "MENUSTATUS_1", 2));
		codeDao.insertCode(getCodeModel("DAEMONSTATUS", "0", "DAEMONSTATUS_0", 1));
		codeDao.insertCode(getCodeModel("DAEMONSTATUS", "1", "DAEMONSTATUS_1", 2));
		codeDao.insertCode(getCodeModel("DAEMONRUNNING", "0", "DAEMONRUNNING_0", 1));
		codeDao.insertCode(getCodeModel("DAEMONRUNNING", "1", "DAEMONRUNNING_1", 2));
		codeDao.insertCode(getCodeModel("DAEMONAUTOSTART", "Y", "DAEMONAUTOSTART_Y", 1));
		codeDao.insertCode(getCodeModel("DAEMONAUTOSTART", "N", "DAEMONAUTOSTART_N", 2));
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
	
//	public void insertServerBaseL10Ns() {
//		insertL10Ns_default("lang.properties");
//		insertL10Ns_locale("lang_en.properties", "en_US");
//		L10N.loadResource();
//	}
//	
//	public void insertServerBaseMenus() {
//		MenuDao menuDao = new MenuDao();
//		CM_Menu menuSiteMng = getMenuModel(0, "MENU_SITEMNG", null, 1);
//		CM_Menu menuSetup = getMenuModel(0, "MENU_SITESETUP", null, 2);
//		menuDao.insertMenu(menuSiteMng);
//		menuDao.insertMenu(menuSetup);
//		CM_Menu menuUser = getMenuModel(menuSiteMng.getIdKey(), "MENU_USERMNG", "/sitemng/user.jsp", 1);
//		CM_Menu menuMenu   = getMenuModel(menuSetup.getIdKey(), "MENU_MENUMNG"  , "/sitesetup/menu.jsp"  , 1);
//		CM_Menu menuCmCode = getMenuModel(menuSetup.getIdKey(), "MENU_CMCODEMNG", "/sitesetup/cmcode.jsp", 2);
//		CM_Menu menuL10N   = getMenuModel(menuSetup.getIdKey(), "MENU_L10NMNG"  , "/sitesetup/l10n.jsp"  , 3);
//		CM_Menu menuBoard  = getMenuModel(menuSetup.getIdKey(), "MENU_BOARDMNG" , "/sitesetup/board.jsp" , 4);
//		CM_Menu menuDemon  = getMenuModel(menuSetup.getIdKey(), "MENU_DAEMONMNG", "/sitesetup/daemon.jsp", 4);
//		menuDao.insertMenu(menuUser);
//		menuDao.insertMenu(menuMenu);
//		menuDao.insertMenu(menuCmCode);
//		menuDao.insertMenu(menuL10N);
//		menuDao.insertMenu(menuBoard);
//		menuDao.insertMenu(menuDemon);
//		menuDao.insertMenuAuths(getMenuAuthModels(menuSiteMng.getIdKey(), new String[] {"1", "2"}));
//		menuDao.insertMenuAuths(getMenuAuthModels(menuUser.getIdKey()  , new String[] {"1", "2"}));
//		menuDao.insertMenuAuths(getMenuAuthModels(menuSetup.getIdKey() , new String[] {"1"}));
//		menuDao.insertMenuAuths(getMenuAuthModels(menuMenu.getIdKey()  , new String[] {"1"}));
//		menuDao.insertMenuAuths(getMenuAuthModels(menuCmCode.getIdKey(), new String[] {"1"}));
//		menuDao.insertMenuAuths(getMenuAuthModels(menuL10N.getIdKey()  , new String[] {"1"}));
//		menuDao.insertMenuAuths(getMenuAuthModels(menuBoard.getIdKey() , new String[] {"1"}));
//		menuDao.insertMenuAuths(getMenuAuthModels(menuDemon.getIdKey() , new String[] {"1"}));
//	}
}
