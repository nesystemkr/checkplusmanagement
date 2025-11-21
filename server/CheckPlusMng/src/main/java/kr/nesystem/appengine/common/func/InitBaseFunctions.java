package kr.nesystem.appengine.common.func;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.io.Resources;

import kr.nesystem.appengine.common.dao.L10NDao;
import kr.nesystem.appengine.common.dao.L10NLocaleDao;
import kr.nesystem.appengine.common.model.CM_L10N;
import kr.nesystem.appengine.common.model.CM_L10NLocale;

//@Path("/{version}/init")
public class InitBaseFunctions {
//	public boolean checkIfAdminExist() {
//		UserDao userDao = new UserDao();
//		CM_User existUser = null;
//		try {
//			existUser = userDao.selectUserByUserId("admin");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return existUser != null;
//	}
//	
//	public void createAdminUser() {
//		UserDao userDao = new UserDao();
//		userDao.insertUser(getUserModel("admin", "1234", "admin"));
//	}
//	
//	public void createServerBaseTable() {
//		InitDao dao = new InitDao();
//		dao.createTables();
//		dao.createFragmentTables();
//	}
	
	public void recreateL10NTable() throws Exception {
		L10NDao l10nDao = new L10NDao();
		L10NLocaleDao l10nLocaleDao = new L10NLocaleDao();
		l10nDao.deleteAll();
		l10nLocaleDao.deleteAll();
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
	
	public void  insertL10Ns_locale(String profileName, String locale) throws Exception {
		L10NLocaleDao l10nLocaleDao = new L10NLocaleDao();
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(profileName);
			Properties prop = new Properties();
			prop.load(inputStream);
			Set<Object> keys = prop.keySet();
			for (Object key : keys) {
				String val = prop.getProperty((String)key);
				l10nLocaleDao.insert(getL10NLocaleModel((String)key, locale, new String(val.getBytes("ISO-8859-1"), "UTF-8")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void recreateCMCodeTables() {
//		InitDao dao = new InitDao();
//		dao.recreateCMCodes();
//	}
//	
//	public void insertServerBaseCodes() {
//		CodeTypeDao typeDao = new CodeTypeDao();
//		CodeDao codeDao = new CodeDao();
//		typeDao.insertCodeType(getCodeTypeModel("LOCALE", "CODETYPE_LOCALE"));
//		codeDao.insertCode(getCodeModel("LOCALE", "ko_KR", "LOCALE_ko_KR", 1));
//		codeDao.insertCode(getCodeModel("LOCALE", "en_US", "LOCALE_en_US", 2));
//		typeDao.insertCodeType(getCodeTypeModel("USERTYPE", "CODETYPE_USERTYPE"));
//		typeDao.insertCodeType(getCodeTypeModel("USERSTATUS", "CODETYPE_USERSTATUS"));
//		typeDao.insertCodeType(getCodeTypeModel("BOARDSTATUS", "CODETYPE_BOARDSTATUS"));
//		typeDao.insertCodeType(getCodeTypeModel("BOARDCONTENTSTATUS", "CODETYPE_BOARDCONTENTSTATUS"));
//		typeDao.insertCodeType(getCodeTypeModel("MENUSTATUS", "CODETYPE_MENUSTATUS"));
//		typeDao.insertCodeType(getCodeTypeModel("DAEMONSTATUS", "CODETYPE_DAEMONSTATUS"));
//		typeDao.insertCodeType(getCodeTypeModel("DAEMONRUNNING", "CODETYPE_DAEMONRUNNING"));
//		typeDao.insertCodeType(getCodeTypeModel("DAEMONAUTOSTART", "CODETYPE_DAEMONAUTOSTART"));
//		codeDao.insertCode(getCodeModel("USERTYPE", "0", "USERTYPE_0", 1));
//		codeDao.insertCode(getCodeModel("USERTYPE", "1", "USERTYPE_1", 2));
//		codeDao.insertCode(getCodeModel("USERTYPE", "2", "USERTYPE_2", 3));
//		codeDao.insertCode(getCodeModel("USERTYPE", "3", "USERTYPE_3", 4));
//		codeDao.insertCode(getCodeModel("USERSTATUS", "0", "USERSTATUS_0", 1));
//		codeDao.insertCode(getCodeModel("USERSTATUS", "1", "USERSTATUS_1", 2));
//		codeDao.insertCode(getCodeModel("USERSTATUS", "9", "USERSTATUS_9", 3));
//		codeDao.insertCode(getCodeModel("BOARDSTATUS", "0", "BOARDSTATUS_0", 1));
//		codeDao.insertCode(getCodeModel("BOARDSTATUS", "1", "BOARDSTATUS_1", 2));
//		codeDao.insertCode(getCodeModel("BOARDCONTENTSTATUS", "0", "BOARDCONTENTSTATUS_0", 1));
//		codeDao.insertCode(getCodeModel("BOARDCONTENTSTATUS", "1", "BOARDCONTENTSTATUS_1", 2));
//		codeDao.insertCode(getCodeModel("MENUSTATUS", "0", "MENUSTATUS_0", 1));
//		codeDao.insertCode(getCodeModel("MENUSTATUS", "1", "MENUSTATUS_1", 2));
//		codeDao.insertCode(getCodeModel("DAEMONSTATUS", "0", "DAEMONSTATUS_0", 1));
//		codeDao.insertCode(getCodeModel("DAEMONSTATUS", "1", "DAEMONSTATUS_1", 2));
//		codeDao.insertCode(getCodeModel("DAEMONRUNNING", "0", "DAEMONRUNNING_0", 1));
//		codeDao.insertCode(getCodeModel("DAEMONRUNNING", "1", "DAEMONRUNNING_1", 2));
//		codeDao.insertCode(getCodeModel("DAEMONAUTOSTART", "Y", "DAEMONAUTOSTART_Y", 1));
//		codeDao.insertCode(getCodeModel("DAEMONAUTOSTART", "N", "DAEMONAUTOSTART_N", 2));
//	}
//	
//	public void insertServerBaseDaemon() {
//		DaemonDao daemonDao = new DaemonDao();
//		daemonDao.insertDaemon(getDaemonModel("Push Msg Daemon", "kr.peelknight.push.context.PushMsgDaemon"      , 1, "Y"));
//		daemonDao.insertDaemon(getDaemonModel("GMail Daemon"   , "kr.peelknight.messagequeue.context.GMailDaemon", 2, "Y"));
//		daemonDao.insertDaemon(getDaemonModel("SMS Daemon"     , "kr.peelknight.messagequeue.context.SMSDaemon"  , 3, "Y"));
//	}
//	
//	public CM_User getUserModel(String userId, String password, String userName) {
//		return getUserModel(userId, password, userName, "1");
//	}
//	
//	public CM_User getUserModel(String userId, String password, String userName, String userType) {
//		CM_User user = new CM_User();
//		user.setUserId(userId);
//		user.setPassword(CommonFunc.getHashedPassword(password, userId));
//		user.setUserType(userType);
//		user.setUserName(userName);
//		user.setStatus("1");
//		user.setLoginFailCount(0);
//		user.setLastLoginDate(null);
//		user.setLastLoginSeq(0);
//		user.setCreateDate(new Date());
//		return user;
//	}
//	
//	public CM_Code getCodeModel(String type, String key, String value, int orderSeq) {
//		CM_Code code = new CM_Code();
//		code.setType(type);
//		code.setCode(key);
//		code.setCodeName(value);
//		code.setOrderSeq(orderSeq);
//		return code;
//	}
//	
//	public CM_CodeType getCodeTypeModel(String type, String name) {
//		CM_CodeType codeType = new CM_CodeType();
//		codeType.setType(type);
//		codeType.setTypeName(name);
//		return codeType;
//	}
//	
//	public CM_Menu getMenuModel(long parentIdKey, String menuName, String menuUrl, int orderSeq) {
//		CM_Menu menu = new CM_Menu();
//		menu.setParentIdKey(parentIdKey);
//		menu.setMenuName(menuName);
//		menu.setMenuUrl(menuUrl);
//		menu.setStatus("1");
//		menu.setOrderSeq(orderSeq);
//		return menu;
//	}
//	
//	public CM_MenuAuth getMenuAuthModel(long menuIdKey, String userType) {
//		CM_MenuAuth menuAuth = new CM_MenuAuth();
//		menuAuth.setUserType(userType);
//		menuAuth.setMenuIdKey(menuIdKey);
//		menuAuth.setAllowYN("Y");
//		return menuAuth;
//	}
//	
//	public List<CM_MenuAuth> getMenuAuthModels(long menuIdKey, String[] userTypes) {
//		List<CM_MenuAuth> list = new ArrayList<>();
//		for (int ii=0; ii<userTypes.length; ii++) {
//			CM_MenuAuth menuAuth = new CM_MenuAuth();
//			menuAuth.setUserType(userTypes[ii]);
//			menuAuth.setMenuIdKey(menuIdKey);
//			menuAuth.setAllowYN("Y");
//			list.add(menuAuth);
//		}
//		return list;
//	}
	
	public CM_L10N getL10NModel(String idString, String defaultString) {
		CM_L10N l10n = new CM_L10N();
		l10n.setIdString(idString);
		l10n.setDefaultString(defaultString);
		return l10n;
	}
	
	public CM_L10NLocale getL10NLocaleModel(String idString, String locale, String localeString) {
		CM_L10NLocale l10nLocale = new CM_L10NLocale();
		l10nLocale.setIdString(idString);
		l10nLocale.setLocale(locale);
		l10nLocale.setLocaleString(localeString);
		return l10nLocale;
	}
	
//	public CM_Daemon getDaemonModel(String daemonName, String className, int orderSeq, String autoStartYN) {
//		CM_Daemon daemon = new CM_Daemon();
//		daemon.setDaemonName(daemonName);
//		daemon.setClassName(className);
//		daemon.setOrderSeq(orderSeq);
//		daemon.setStatus("1");
//		daemon.setAutoStartYN(autoStartYN);
//		return daemon;
//	}
//	
//	public void insertFragments() {
//		FragmentDao dao = new FragmentDao();
//		GridFragmentDao gridDao = new GridFragmentDao();
//		PopupFragmentDao popupDao = new PopupFragmentDao();
//		
//		CM_Fragment GR999001 = new CM_Fragment();
//		GR999001.setFragmentId("GR999001");
//		GR999001.setFragmentType(CM_Fragment.FRAGMENTTYPE_GRID);
//		GR999001.setComment("화면관리 전체 그리드");
//		GR999001.setCreateDate(new Date());
//		dao.insertFragment(GR999001);
//		CM_GridFragment GR999001_1 = new CM_GridFragment();
//		GR999001_1.setFragmentIdKey(GR999001.getIdKey());
//		GR999001_1.setServiceUrl("/svc/v1/fragment/list/{{page}");
//		GR999001_1.setNeedAuth("Y");
//		GR999001_1.setJqGrid("Y");
//		GR999001_1.setShowCheckBox("N");
//		GR999001_1.setStretchColumn("action");
//		GR999001_1.setPaging("Y");
//		GR999001_1.setPopupFragmentId("PO999001");
//		GR999001_1.setColModel(new ArrayList<>());
//		GR999001_1.getColModel().add(new CM_GridColModel("idKey", "Y"));
//		GR999001_1.getColModel().add(new CM_GridColModel("fragmentType", "Y"));
//		GR999001_1.getColModel().add(new CM_GridColModel("no", "NO", 50, CM_GridColModel.COLALIGN_CENTER));
//		GR999001_1.getColModel().add(new CM_GridColModel("fragmentId", "GR9990_FRAGMENTID", 150, CM_GridColModel.COLALIGN_CENTER));
//		GR999001_1.getColModel().add(new CM_GridColModel("fragmentTypeName", "GR9990_FRAGMENTTYPE", 150, CM_GridColModel.COLALIGN_CENTER));
//		GR999001_1.getColModel().add(new CM_GridColModel("comment", "GR9990_COMMENT", 400, CM_GridColModel.COLALIGN_LEFT));
//		GR999001_1.getColModel().add(new CM_GridColModel("createDate", "GR9990_CREATEDATE", 180, CM_GridColModel.COLALIGN_CENTER, CM_GridColModel.COLFORMAT_DATETIME));
//		CM_GridColModel action = new CM_GridColModel("action", "Action", CM_GridColModel.COLALIGN_CENTER);
//		GR999001_1.getColModel().add(action);
//		action.setButtons(new ArrayList<>());
//		action.getButtons().add(new CM_GridColButton(CM_GridColButton.GRIDBUTTON_DETAIL));
//		action.getButtons().add(new CM_GridColButton(CM_GridColButton.GRIDBUTTON_UPDATE));
//		action.getButtons().add(new CM_GridColButton(CM_GridColButton.GRIDBUTTON_DELETE));
//		for(int ii=0; ii<action.getButtons().size(); ii++) {
//			action.getButtons().get(ii).setOrderSeq(ii + 1);
//		}
//		for(int ii=0; ii<GR999001_1.getColModel().size(); ii++) {
//			GR999001_1.getColModel().get(ii).setOrderSeq(ii + 1);
//		}
//		gridDao.updateGridFragment(GR999001_1);
//		
//		CM_Fragment PO999001 = new CM_Fragment();
//		PO999001.setFragmentId("PO999001");
//		PO999001.setFragmentType(CM_Fragment.FRAGMENTTYPE_POPUP);
//		PO999001.setComment("화면관리 기본 팝업");
//		PO999001.setCreateDate(new Date());
//		dao.insertFragment(PO999001);
//		CM_PopupFragment PO999001_1 = new CM_PopupFragment();
//		PO999001_1.setFragmentIdKey(PO999001.getIdKey());
//		PO999001_1.setSelectTitle("COMMON_SELECT_TITLE");
//		PO999001_1.setSelectUrl("/svc/v1/fragment/{{idKey}");
//		PO999001_1.setPrevUrl("/svc/v1/fragment/{{idKey}/prev");
//		PO999001_1.setNextUrl("/svc/v1/fragment/{{idKey}/next");
//		PO999001_1.setSelectErrorMsg("COMMON_SELECT_ERROR");
//		PO999001_1.setInsertTitle("COMMON_INSERT_TITLE");
//		PO999001_1.setInsertUrl("/svc/v1/fragment");
//		PO999001_1.setInsertMethod("POST");
//		PO999001_1.setInsertErrorMsg("COMMON_INSERT_ERROR");
//		PO999001_1.setUpdateTitle("COMMON_UPDATE_TITLE");
//		PO999001_1.setUpdateUrl("/svc/v1/fragment/{{idKey}");
//		PO999001_1.setUpdateMethod("PUT");
//		PO999001_1.setUpdateErrorMsg("COMMON_UPDATE_ERROR");
//		PO999001_1.setDeleteConfirm("COMMON_CONFIRM_DELETE");
//		PO999001_1.setDeleteUrl("/svc/v1/fragment/{{idKey}");
//		PO999001_1.setDeleteMethod("DELETE");
//		PO999001_1.setDeleteErrorMsg("COMMON_DELETE_ERROR");
//		PO999001_1.setConfirmButton("COMMON_CONFIRM");
//		PO999001_1.setCancelButton("COMMON_CANCEL");
//		PO999001_1.setWidth(600);
//		PO999001_1.setHeight(0);
//		PO999001_1.setRows(new ArrayList<>());
//		PO999001_1.getRows().add(new CM_PopupRow("idKey", "Y"));
//		PO999001_1.getRows().add(new CM_PopupRow("PO9990_FRAGMENTID", "fragmentId", CM_PopupRow.DATATYPE_STRING, "Y", "화면부속아이디를 입력하세요."));
//		PO999001_1.getRows().add(new CM_PopupRow("PO9990_FRAGMENTTYPE", "fragmentType", "FRAGMENTTYPE", "Y", "화면부속유형을 입력하세요."));
//		PO999001_1.getRows().add(new CM_PopupRow("PO9990_COMMENT", "comment", CM_PopupRow.DATATYPE_STRING));
//		for(int ii=0; ii<PO999001_1.getRows().size(); ii++) {
//			PO999001_1.getRows().get(ii).setRowSeq(ii + 1);
//			PO999001_1.getRows().get(ii).setOrderSeq(ii + 1);
//		}
//		popupDao.updatePopupFragment(PO999001_1);
//		
//		CM_Fragment PO999002 = new CM_Fragment();
//		PO999002.setFragmentId("PO999002");
//		PO999002.setFragmentType(CM_Fragment.FRAGMENTTYPE_POPUP);
//		PO999002.setComment("화면관리 그리드관리 팝업");
//		PO999002.setCreateDate(new Date());
//		dao.insertFragment(PO999002);
//		CM_PopupFragment PO999002_1 = new CM_PopupFragment();
//		PO999002_1.setFragmentIdKey(PO999002.getIdKey());
//		PO999002_1.setWidth(1200);
//		PO999002_1.setHeight(0);
//		PO999002_1.setSelectTitle("COMMON_SELECT_TITLE");
//		PO999002_1.setSelectUrl("/svc/v1/fragment/grid/{{idKey}");
//		PO999002_1.setSelectErrorMsg("COMMON_SELECT_ERROR");
//		PO999002_1.setUpdateTitle("COMMON_UPDATE_TITLE");
//		PO999002_1.setUpdateUrl("/svc/v1/fragment/grid/{{fragmentIdKey}");
//		PO999002_1.setUpdateMethod("PUT");
//		PO999002_1.setUpdateErrorMsg("COMMON_UPDATE_ERROR");
//		PO999002_1.setConfirmButton("COMMON_CONFIRM");
//		PO999002_1.setCancelButton("COMMON_CANCEL");
//		PO999002_1.setRows(new ArrayList<>());
//		PO999002_1.getRows().add(new CM_PopupRow("fragmentIdKey", "Y"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_SERVICEURL", "serviceUrl", CM_PopupRow.DATATYPE_STRING, "Y", "조회URL을 입력하세요."));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_NEEDAUTH", "needAuth", "BOOLEANYN", "Y"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_JGGRID", "jqGrid", "BOOLEANYN", "Y"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_SHOWCHECKBOX", "showCheckBox", "BOOLEANYN", "N"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_STRETCHCOLUMN", "stretchColumn", CM_PopupRow.DATATYPE_STRING));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_PAGING", "paging", "BOOLEANYN", "Y"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_POPUPFRAGMENTID", "popupFragmentId", CM_PopupRow.DATATYPE_STRING));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_GRIDADDBUTTON", "gridAddButton", "BOOLEANYN", "N"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_GRIDDELBUTTON", "gridDelButton", "BOOLEANYN", "N"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_GRIDSAVEBUTTON", "gridSaveButton", "BOOLEANYN", "N"));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_GRIDSAVEURL", "gridSaveUrl", CM_PopupRow.DATATYPE_STRING));
//		PO999002_1.getRows().add(new CM_PopupRow("PO999002_GRIDREFRESHBUTTON", "gridRefreshButton", "BOOLEANYN", "N"));
//		for(int ii=0; ii<PO999002_1.getRows().size(); ii++) {
//			PO999002_1.getRows().get(ii).setRowSeq(ii + 1);
//			PO999002_1.getRows().get(ii).setOrderSeq(ii + 1);
//		}
//		PO999002_1.setGridFragmentId("GR999002");
//		popupDao.updatePopupFragment(PO999002_1);
//		
//		CM_Fragment GR999002 = new CM_Fragment();
//		GR999002.setFragmentId("GR999002");
//		GR999002.setFragmentType(CM_Fragment.FRAGMENTTYPE_GRID);
//		GR999002.setComment("화면관리 그리드관리 팝업그리드");
//		GR999002.setCreateDate(new Date());
//		dao.insertFragment(GR999002);
//		CM_GridFragment GR999002_1 = new CM_GridFragment();
//		GR999002_1.setFragmentIdKey(GR999002.getIdKey());
//		GR999002_1.setServiceUrl("/svc/v1/fragment/grid/{{idKey}/colmodels/0");
//		GR999002_1.setNeedAuth("Y");
//		GR999002_1.setJqGrid("Y");
//		GR999002_1.setShowCheckBox("Y");
//		GR999002_1.setStretchColumn("label");
//		GR999002_1.setPaging("N");
//		GR999002_1.setGridAddButton("Y");
//		GR999002_1.setGridDelButton("Y");
//		GR999002_1.setGridSaveButton("N");
//		GR999002_1.setGridRefreshButton("Y");
//		GR999002_1.setColModel(new ArrayList<>());
//		GR999002_1.getColModel().add(new CM_GridColModel("idKey", "Y"));
//		GR999002_1.getColModel().add(new CM_GridColModel("gridIdKey", "Y"));
//		GR999002_1.getColModel().add(new CM_GridColModel("no", "NO", 50, CM_GridColModel.COLALIGN_CENTER));
//		GR999002_1.getColModel().add(new CM_GridColModel("name", "GR999002_COLMODEL_NAME", 200, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999002_1.getColModel().add(new CM_GridColModel("hidden", "GR999002_COLMODEL_HIDDEN", 60, "BOOLEANYN", CM_GridColModel.COLALIGN_CENTER));
//		GR999002_1.getColModel().add(new CM_GridColModel("label", "GR999002_COLMODEL_LABEL", 150, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999002_1.getColModel().add(new CM_GridColModel("width", "GR999002_COLMODEL_WIDTH", 60, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999002_1.getColModel().add(new CM_GridColModel("align", "GR999002_COLMODEL_ALIGN", 70, "COLALIGN", CM_GridColModel.COLALIGN_CENTER));
//		GR999002_1.getColModel().add(new CM_GridColModel("format", "GR999002_COLMODEL_FORMAT", 70, "COLFORMAT", CM_GridColModel.COLALIGN_CENTER));
//		GR999002_1.getColModel().add(new CM_GridColModel("codeType", "GR999002_COLMODEL_CODETYPE", 150, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999002_1.getColModel().add(new CM_GridColModel("editType", "GR999002_COLMODEL_EDITTYPE", 70, "EDITTYPE", CM_GridColModel.COLALIGN_CENTER));
//		GR999002_1.getColModel().add(new CM_GridColModel("orderSeq", "GR999002_COLMODEL_ORDERSEQ", 60, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		for(int ii=0; ii<GR999002_1.getColModel().size(); ii++) {
//			GR999002_1.getColModel().get(ii).setOrderSeq(ii + 1);
//		}
//		gridDao.updateGridFragment(GR999002_1);
//		
//		CM_Fragment PO999003 = new CM_Fragment();
//		PO999003.setFragmentId("PO999003");
//		PO999003.setFragmentType(CM_Fragment.FRAGMENTTYPE_POPUP);
//		PO999003.setComment("화면관리 팝업관리 팝업");
//		PO999003.setCreateDate(new Date());
//		dao.insertFragment(PO999003);
//		CM_PopupFragment PO999003_1 = new CM_PopupFragment();
//		PO999003_1.setFragmentIdKey(PO999003.getIdKey());
//		PO999003_1.setWidth(1200);
//		PO999003_1.setHeight(0);
//		PO999003_1.setSelectTitle("COMMON_SELECT_TITLE");
//		PO999003_1.setSelectUrl("/svc/v1/fragment/popup/{{idKey}");
//		PO999003_1.setSelectErrorMsg("COMMON_SELECT_ERROR");
//		PO999003_1.setUpdateTitle("COMMON_UPDATE_TITLE");
//		PO999003_1.setUpdateUrl("/svc/v1/fragment/popup/{{fragmentIdKey}");
//		PO999003_1.setUpdateMethod("PUT");
//		PO999003_1.setUpdateErrorMsg("COMMON_UPDATE_ERROR");
//		PO999003_1.setConfirmButton("COMMON_CONFIRM");
//		PO999003_1.setCancelButton("COMMON_CANCEL");
//		PO999003_1.setRows(new ArrayList<>());
//		PO999003_1.getRows().add(new CM_PopupRow("fragmentIdKey", "Y"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_WIDTH", "width", CM_PopupRow.DATATYPE_STRING));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_HEIGHT", "height", CM_PopupRow.DATATYPE_STRING));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_SELECT_TITLE", "selectTitle", CM_PopupRow.DATATYPE_STRING, "Y", "조회팝업 제목을 입력하세요.", "COMMON_SELECT_TITLE"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_SELECT_URL", "selectUrl", CM_PopupRow.DATATYPE_STRING, "Y", "조회URL을 입력하세요."));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_PREV_URL", "prevUrl", CM_PopupRow.DATATYPE_STRING));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_NEXT_URL", "nextUrl", CM_PopupRow.DATATYPE_STRING));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_SELECT_ERRORMSG", "selectErrorMsg", CM_PopupRow.DATATYPE_STRING, "COMMON_SELECT_ERROR"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_INSERT_TITLE", "insertTitle", CM_PopupRow.DATATYPE_STRING, "COMMON_INSERT_TITLE"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_INSERT_URL", "insertUrl", CM_PopupRow.DATATYPE_STRING));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_INSERT_METHOD", "insertMethod", CM_PopupRow.DATATYPE_STRING, "POST"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_INSERT_ERRORMSG", "insertErrorMsg", CM_PopupRow.DATATYPE_STRING, "COMMON_INSERT_ERROR"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_UPDATE_TITLE", "updateTitle", CM_PopupRow.DATATYPE_STRING, "COMMON_UPDATE_TITLE"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_UPDATE_URL", "updateUrl", CM_PopupRow.DATATYPE_STRING));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_UPDATE_METHOD", "updateMethod", CM_PopupRow.DATATYPE_STRING, "PUT"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_UPDATE_ERRORMSG", "updateErrorMsg", CM_PopupRow.DATATYPE_STRING, "COMMON_UPDATE_ERROR"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_DELETE_CONFIRM", "deleteConfirm", CM_PopupRow.DATATYPE_STRING, "COMMON_CONFIRM_DELETE"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_DELETE_URL", "deleteUrl", CM_PopupRow.DATATYPE_STRING));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_DELETE_METHOD", "deleteMethod", CM_PopupRow.DATATYPE_STRING, "DELETE"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_CONFIRMBUTTON", "confirmButton", CM_PopupRow.DATATYPE_STRING, "COMMON_CONFIRM"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_CANCELBUTTON", "cancelButton", CM_PopupRow.DATATYPE_STRING, "COMMON_CANCEL"));
//		PO999003_1.getRows().add(new CM_PopupRow("PO999003_GRIDFRAGMENTID", "gridFragmentId", CM_PopupRow.DATATYPE_STRING));
//		for(int ii=0; ii<PO999003_1.getRows().size(); ii++) {
//			PO999003_1.getRows().get(ii).setRowSeq(ii + 1);
//			PO999003_1.getRows().get(ii).setOrderSeq(ii + 1);
//		}
//		PO999003_1.setGridFragmentId("GR999003");
//		popupDao.updatePopupFragment(PO999003_1);
//
//		CM_Fragment GR999003 = new CM_Fragment();
//		GR999003.setFragmentId("GR999003");
//		GR999003.setFragmentType(CM_Fragment.FRAGMENTTYPE_GRID);
//		GR999003.setComment("화면관리 팝업관리 팝업그리드");
//		GR999003.setCreateDate(new Date());
//		dao.insertFragment(GR999003);
//		CM_GridFragment GR999003_1 = new CM_GridFragment();
//		GR999003_1.setFragmentIdKey(GR999003.getIdKey());
//		GR999003_1.setServiceUrl("/svc/v1/fragment/popup/{{idKey}/rows/0");
//		GR999003_1.setNeedAuth("Y");
//		GR999003_1.setJqGrid("Y");
//		GR999003_1.setShowCheckBox("Y");
//		GR999002_1.setStretchColumn("mandatoryErrorMsg");
//		GR999003_1.setPaging("N");
//		GR999003_1.setGridAddButton("Y");
//		GR999003_1.setGridDelButton("Y");
//		GR999003_1.setGridSaveButton("N");
//		GR999003_1.setGridRefreshButton("Y");
//		GR999003_1.setColModel(new ArrayList<>());
//		GR999003_1.getColModel().add(new CM_GridColModel("idKey", "Y"));
//		GR999003_1.getColModel().add(new CM_GridColModel("popupIdKey", "Y"));
//		GR999003_1.getColModel().add(new CM_GridColModel("no", "NO", 50, CM_GridColModel.COLALIGN_CENTER));
//		GR999003_1.getColModel().add(new CM_GridColModel("hidden", "GR999003_ROW_HIDDEN", 60, "BOOLEANYN", CM_GridColModel.COLALIGN_CENTER));
//		GR999003_1.getColModel().add(new CM_GridColModel("title", "GR999003_ROW_TITLE", 100, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("name", "GR999003_ROW_NAME", 100, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("dataType", "GR999003_ROW_DATATYPE", 70, "ROWDATATYPE", CM_GridColModel.COLALIGN_CENTER));
//		GR999003_1.getColModel().add(new CM_GridColModel("mandatory", "GR999003_ROW_MANDATORY", 60, "BOOLEANYN", CM_GridColModel.COLALIGN_CENTER));
//		GR999003_1.getColModel().add(new CM_GridColModel("mandatoryErrorMsg", "GR999003_ROW_MANDATORY_ERRORMSG", 150, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("defaultValue", "GR999003_ROW_DEFAULTVALUE", 80, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("codeType", "GR999003_ROW_CODETYPE", 120, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("showType", "GR999003_ROW_SHOWTYPE", 80, "ROWSHOWTYPE"));
//		GR999003_1.getColModel().add(new CM_GridColModel("titleColSpan", "GR990003_ROW_TITLECOLSPAN", 60, CM_GridColModel.COLALIGN_CENTER, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("valueColSpan", "GR990003_ROW_VALUECOLSPAN", 60, CM_GridColModel.COLALIGN_CENTER, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("titleWidth", "GR990003_ROW_TITLEWIDTH", 60, CM_GridColModel.COLALIGN_CENTER, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("valueWidth", "GR990003_ROW_VALUEWIDTH", 60, CM_GridColModel.COLALIGN_CENTER, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("rowSeq", "GR990003_ROW_ROWSEQ", 50, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		GR999003_1.getColModel().add(new CM_GridColModel("orderSeq", "GR999003_ROW_ORDERSEQ", 50, CM_GridColModel.COLALIGN_LEFT, CM_GridColModel.COLFORMAT_NONE, CM_GridColModel.EDITTYPE_EDITABLE));
//		for(int ii=0; ii<GR999003_1.getColModel().size(); ii++) {
//			GR999003_1.getColModel().get(ii).setOrderSeq(ii + 1);
//		}
//		gridDao.updateGridFragment(GR999003_1);
//	}
}
