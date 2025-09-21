package kr.peelknight.common.service;

import java.lang.reflect.Constructor;
import java.util.Date;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import kr.peelknight.common.Constant;
import kr.peelknight.common.dao.DeviceDao;
import kr.peelknight.common.dao.MenuDao;
import kr.peelknight.common.dao.UserDao;
import kr.peelknight.common.interf.LoginInfoInterface;
import kr.peelknight.common.model.CM_Login;
import kr.peelknight.common.model.CM_User;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.CommonFunc;
import kr.peelknight.util.L10N;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/login")
public class LoginService {
	UserDao userDao = new UserDao();
	DeviceDao deviceDao = new DeviceDao();
	MenuDao menuDao = new MenuDao();
	
	// SignIn
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	public Response login(@Context HttpServletRequest request,
						  CM_Login login) {
		try {
			if (login.getUserId() == null || login.getUserId().length() == 0 ||
				login.getPassword() == null || login.getPassword().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (login.getDeviceType() == null ||
				(login.getDeviceType() != null && !login.getDeviceType().equals("WEB"))) {
				if (login.getVersion() == null) {
					return ResponseUtil.getResponse(Status.HTTP_VERSION_NOT_SUPPORTED);
				}
			}
			
			CM_User existUser = userDao.selectUserByUserId(login.getUserId());
			if (existUser == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existUser.getPassword() != null) {
				if (!existUser.getPassword().equals(CommonFunc.getHashedPassword(login.getPassword(), login.getUserId()))) {
					existUser.setLoginFailCount(existUser.getLoginFailCount() + 1);
					userDao.updateUser(existUser);
					return ResponseUtil.getResponse(Status.NOT_ACCEPTABLE);
				}
			} else {
				return ResponseUtil.getResponse(Status.FORBIDDEN);
			}
			if (existUser.getStatus() != null) {
				if (existUser.getStatus().equals("9")) {
					return ResponseUtil.getResponse(Status.CONFLICT);
				}
			}
			if (existUser.getLoginFailCount() >= 5) {
				return ResponseUtil.getResponse(Status.GONE);
			}
			
			if (login.getUniqueId() != null && login.getPushKey() != null) {
				deviceDao.manageDeviceInfo(existUser.getIdKey(), login.getUniqueId(), login.getPushKey(), login.getDeviceType());
			}
			
			CM_Login result = new CM_Login();
			long tenYearTime = (new Date()).getTime() + (1000 * 60 * 60 * 24 * 365); // 1년
			long lastSeq = existUser.getLastLoginSeq() + 1;
			result.setAuthToken(AuthToken.getToken(existUser.getIdKey(), tenYearTime, existUser.getUserType(), lastSeq));
			result.setUserId(existUser.getUserId());
			result.setUserIdKey(existUser.getIdKey());
			result.setUserName(existUser.getUserName());
			result.setUserType(existUser.getUserType());
			
			existUser.setLoginFailCount(0);
			existUser.setLastLoginDate(new Date());
			existUser.setLastLoginSeq(lastSeq);
			userDao.updateUser(existUser);
			
			//사용자 정보를 인터페이스를 통해서 가져온다.
			//메뉴정보를 인터페이스를 통해서 가져온다.
			Class<?> clazz = Class.forName(Constant.LOGININFO_IMPL_CLASS);
//System.out.println(Constant.LOGININFO_IMPL_CLASS);
			Constructor<?> ctor = clazz.getConstructor();
			Object object = ctor.newInstance();
			result.setMenus(((LoginInfoInterface)object).getMenus(existUser.getUserType(), existUser.getIdKey(), L10N.getLang(request.getSession())));
			
			return ResponseUtil.getResponse((new ModelHandler<CM_Login>(CM_Login.class)).convertToJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//SignIn with AuthToken
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	public Response loginWithAuthToken(@Context HttpServletRequest request,
									   CM_Login login) {
		try {
			if (AuthToken.isValidToken(login.getAuthToken(), null) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (login.getDeviceType() == null ||
				(login.getDeviceType() != null && !login.getDeviceType().equals("WEB"))) {
				if (login.getVersion() == null) {
					return ResponseUtil.getResponse(Status.HTTP_VERSION_NOT_SUPPORTED);
				}
			}
			
			CM_User existUser = userDao.selectUserByIdKey(AuthToken.getIdKey(login.getAuthToken(), null));
			if (existUser.getStatus() != null) {
				if (existUser.getStatus().equals("9")) {
					return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
				}
			}
			
			deviceDao.manageDeviceInfo(existUser.getIdKey(), login.getUniqueId(), login.getPushKey(), login.getDeviceType());
			
			CM_Login result = new CM_Login();
			long tenYearTime = (new Date()).getTime() + (1000 * 60 * 60 * 24 * 365); // 1년
			long lastSeq = existUser.getLastLoginSeq() + 1;
			result.setAuthToken(AuthToken.getToken(existUser.getIdKey(), tenYearTime, existUser.getUserType(), lastSeq));
			result.setUserId(existUser.getUserId());
			result.setUserIdKey(existUser.getIdKey());
			result.setUserName(existUser.getUserName());
			result.setUserType(existUser.getUserType());
			
			existUser.setLastLoginSeq(lastSeq);
			userDao.updateUser(existUser);
			
			return ResponseUtil.getResponse((new ModelHandler<CM_Login>(CM_Login.class)).convertToJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/logout")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response logout(CM_Login login) {
		try {
			if (AuthToken.isValidToken(login.getAuthToken(), null) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_User existUser = userDao.selectUserByIdKey(AuthToken.getIdKey(login.getAuthToken(), null));
			if (existUser.getStatus() != null) {
				if (existUser.getStatus().equals("9")) {
					return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
				}
			}
			deviceDao.deleteDeviceInfo(existUser.getIdKey(), login.getUniqueId(), login.getDeviceType());
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Path("/menus")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	public Response menus(@Context HttpServletRequest request,
						  @QueryParam("lang") String lang,
						  @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (lang == null) {
				ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			long userIdKey = AuthToken.getIdKey(authToken);
			CM_User existUser = userDao.selectUserByIdKey(userIdKey);
			if (existUser == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_Login result = new CM_Login();
			Class<?> clazz = Class.forName(Constant.LOGININFO_IMPL_CLASS);
			Constructor<?> ctor = clazz.getConstructor();
			Object object = ctor.newInstance();
			result.setMenus(((LoginInfoInterface)object).getMenus(existUser.getUserType(), existUser.getIdKey(), lang));
			return ResponseUtil.getResponse((new ModelHandler<CM_Login>(CM_Login.class)).convertToJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
