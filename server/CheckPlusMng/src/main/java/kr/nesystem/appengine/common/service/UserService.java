package kr.nesystem.appengine.common.service;

import java.util.Date;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.dao.UserDao;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.CM_User;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.CommonFunc;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/user")
public class UserService {
	UserDao dao = new UserDao();
	
	// SignUp
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertUser(CM_User user) throws Exception {
		try {
			if (user.getAuthToken() == null || !user.getAuthToken().equals("signupbyuser")) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			
			if (user.getUserId() == null || user.getUserId().length() == 0 ||
				user.getUserName() == null || user.getUserName().length() == 0 ||
				user.getPassword() == null || user.getPassword().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (user.getUserId().equals("admin")) {
				user.setUserType("1");
			}
			CM_User existUser = dao.selectByUserId(null, user.getUserId());
			if (existUser != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			user.setPassword(CommonFunc.getHashedPassword(user.getPassword(), user.getUserId()));
			user.setStatus("1");
			user.setLoginFailCount(0);
			user.setLastLoginDate(new Date());
			user.setLastLoginSeq(0);
			user.setCreateDate(new Date());
			dao.insert(user);
			return ResponseUtil.getResponse((new ModelHandler<CM_User>(CM_User.class)).convertToJson(user));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//Update User
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{userIdKey}")
	public Response updateUser(@PathParam("userIdKey") long userIdKey,
							   CM_User user) {
		try {
			if (AuthToken.isValidToken(user.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			String authUserType = AuthToken.getUserType(user.getAuthToken());
			if (authUserType.equals("1") == false && authUserType.equals("2") == false) {
				long authUserIdKey = AuthToken.getIdKey(user.getAuthToken());
				if (authUserIdKey != userIdKey) {
					return ResponseUtil.getResponse(Status.FORBIDDEN);
				}
			}
			if (userIdKey != user.getIdKey()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			CM_User existUser = dao.select(null, user.getIdKey());
			if (existUser == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (user.getUserId() != null) {
				existUser.setUserId(user.getUserId());
			}
			existUser.setUserType(user.getUserType());
			existUser.setUserName(user.getUserName());
			existUser.setStatus(user.getStatus());
			dao.update(existUser);
			return ResponseUtil.getResponse((new ModelHandler<CM_User>(CM_User.class)).convertToJson(existUser));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//Update User
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{userIdKey}")
	public Response deleteUser(@PathParam("userIdKey") long userIdKey,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			String authUserType = AuthToken.getUserType(authToken);
			if (authUserType.equals("1") == false && authUserType.equals("2") == false) {
				long authUserIdKey = AuthToken.getIdKey(authToken);
				if (authUserIdKey != userIdKey) {
					return ResponseUtil.getResponse(Status.FORBIDDEN);
				}
			}
			CM_User existUser = dao.select(null, userIdKey);
			if (existUser == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existUser);
			return ResponseUtil.getResponse((new ModelHandler<CM_User>(CM_User.class)).convertToJson(existUser));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{userIdKey}/initLoginFaileCount")
	public Response initLoginFailCount(@PathParam("userIdKey") long userIdKey,
									   @QueryParam("q") String authToken,
									   CM_User user) {
		try {
			if (AuthToken.isValidToken(user.getAuthToken(), authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			String authUserType = AuthToken.getUserType(user.getAuthToken(), authToken);
			if (authUserType.equals("1") == false && authUserType.equals("2") == false) {
				long authUserIdKey = AuthToken.getIdKey(user.getAuthToken(), authToken);
				if (authUserIdKey != userIdKey) {
					return ResponseUtil.getResponse(Status.FORBIDDEN);
				}
			}
			if (userIdKey != user.getIdKey()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			CM_User existUser = dao.select(null, user.getIdKey());
			if (existUser == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existUser.setLoginFailCount(0);
			dao.update(existUser);
			return ResponseUtil.getResponse((new ModelHandler<CM_User>(CM_User.class)).convertToJson(existUser));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{userIdKey}/password")
	public Response changePassword(@PathParam("userIdKey") long userIdKey,
								   @QueryParam("q") String authToken,
								   CM_User user) throws Exception {
		try {
			if (AuthToken.isValidToken(user.getAuthToken(), authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (user.getIdKey() == 0 ||
				user.getUserId() == null || user.getUserId().length() == 0 || 
				user.getPassword() == null || user.getPassword().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
				
			String authUserType = AuthToken.getUserType(user.getAuthToken(), authToken);
			long authUserIdKey = AuthToken.getIdKey(user.getAuthToken(), authToken);
			if (authUserType.equals("1") == false && authUserType.equals("2") == false) {
				if (authUserIdKey == userIdKey) {
					if (user.getUserName() == null || user.getUserName().length() == 0) {
						return ResponseUtil.getResponse(Status.BAD_REQUEST);
					}
				} else {
					return ResponseUtil.getResponse(Status.FORBIDDEN);
				}
			}
			
			CM_User existUser = dao.select(null, user.getIdKey());
			if (existUser == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (user.getUserName() != null) {
				if (!existUser.getPassword().equals(CommonFunc.getHashedPassword(user.getUserName(), user.getUserId()))) {
					return ResponseUtil.getResponse(Status.CONFLICT);
				}
			}
			existUser.setPassword(CommonFunc.getHashedPassword(user.getPassword(), user.getUserId()));
			existUser.setLoginFailCount(0);
			dao.update(existUser);
			return ResponseUtil.getResponse((new ModelHandler<CM_User>(CM_User.class)).convertToJson(user));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectUsers(@Context HttpServletRequest request,
								@PathParam("page") int page,
								@QueryParam("q") String authToken,
								@QueryParam("userType") String userType) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_User> paging = dao.selectByType(request.getSession(), userType, offset, Constant.DEFAULT_SIZE);
			if (paging.getList() != null) {
				CM_User item;
				for (int ii=0; ii<paging.getList().size(); ii++) {
					item = paging.getList().get(ii);
					item.setPassword(null);
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//User detail
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{userIdKey}")
	public Response selectUserByIdKey(@Context HttpServletRequest request,
									  @PathParam("userIdKey") long userIdKey,
									  @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_User existUser = dao.select(request.getSession(), userIdKey);
			if (existUser == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existUser.setPassword(null);
			return ResponseUtil.getResponse((new ModelHandler<CM_User>(CM_User.class)).convertToJson(existUser));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
