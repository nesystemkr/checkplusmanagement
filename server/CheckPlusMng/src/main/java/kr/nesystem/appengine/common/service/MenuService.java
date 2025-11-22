package kr.nesystem.appengine.common.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import kr.nesystem.appengine.common.dao.MenuAuthDao;
import kr.nesystem.appengine.common.dao.MenuDao;
import kr.nesystem.appengine.common.model.CM_Menu;
import kr.nesystem.appengine.common.model.CM_MenuAuth;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/menu")
public class MenuService {
	MenuDao dao = new MenuDao();
	MenuAuthDao authDao = new MenuAuthDao();
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectMenus(@Context HttpServletRequest request,
								@PathParam("page") int page,
								@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_Menu> paging = dao.pagingList(request.getSession(), null, offset, Constant.DEFAULT_SIZE);
			if (paging.getList() != null) {
				for (int ii=0; ii<paging.getList().size(); ii++) {
					CM_Menu menu = paging.getList().get(ii);
					menu.setMenuAuths(authDao.selectMenuAuthWithMenu(request.getSession(), menu));
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response selectMenu(@Context HttpServletRequest request,
							   @PathParam("idKey") long idKey,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Menu existOne = dao.select(request.getSession(), idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setMenuAuths(authDao.selectMenuAuthWithMenu(request.getSession(), existOne));
			return ResponseUtil.getResponse((new ModelHandler<CM_Menu>(CM_Menu.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/listbyusertype")
	public Response selectMenusAll(@Context HttpServletRequest request,
								   @QueryParam("q") String authToken) {
		return selectMenusByUsertype(request.getSession(), null, authToken);
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/listbyusertype/{userType}")
	public Response selectMenusByUsertype(@Context HttpServletRequest request,
										  @PathParam("userType") String userType,
										  @QueryParam("q") String authToken) {
		return selectMenusByUsertype(request.getSession(), userType, authToken);
	}
	
	@SuppressWarnings("rawtypes")
	public Response selectMenusByUsertype(HttpSession session, String userType, String authToken) {
		try {
System.out.println("OJKIMOJKIMOJKIM 0001  " + userType);
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<CM_Menu> list = new ArrayList<>();
			List<CM_MenuAuth> menuAuthList = authDao.selectMenuAuthWithUserType(session, userType);
			for (int ii = 0; ii < menuAuthList.size(); ii++) {
				CM_MenuAuth menuAuth = menuAuthList.get(ii);
				CM_Menu menu = dao.select(session, menuAuth.getMenuIdKey());
				list.add(menu);
				if (userType == null) {
					menu.setMenuAuths(authDao.selectMenuAuthWithMenu(session, menu));
				}
			}
			CM_PagingList<CM_Menu> paging = new CM_PagingList<>();
			paging.setList(list);
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{parentIdKey}/list")
	public Response selectSubMenus(@Context HttpServletRequest request,
								   @PathParam("parentIdKey") long parentIdKey,
								   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<CM_Menu> list = dao.selectMenus(request.getSession(), parentIdKey, -1, Constant.DEFAULT_SIZE);
			return ResponseUtil.getResponse((new ModelHandler<List>(List.class)).convertToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertMenu(CM_Menu menu) throws Exception {
		try {
			if (AuthToken.isValidToken(menu.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int maxOrderSeq = dao.selectMaxOrderSeq(menu);
			menu.setOrderSeq(maxOrderSeq + 1);
			dao.insert(menu);
			return ResponseUtil.getResponse((new ModelHandler<CM_Menu>(CM_Menu.class)).convertToJson(menu));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response updateMenu(CM_Menu menu,
							   @PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(menu.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Menu existOne = dao.select(null, menu.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setMenuAuths(menu.getMenuAuths());
			existOne.setParentIdKey(menu.getParentIdKey());
			existOne.setMenuName(menu.getMenuName());
			existOne.setMenuUrl(menu.getMenuUrl());
			existOne.setStatus(menu.getStatus());
			dao.update(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Menu>(CM_Menu.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list")
	public Response updateMenus(CM_PagingList<CM_Menu> paging) throws Exception {
		try {
			if (AuthToken.isValidToken(paging.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			List<CM_Menu> existList = new ArrayList<>();
			CM_Menu existOne;
			for (int ii=0; ii<paging.getList().size(); ii++) {
				existOne = dao.select(null, paging.getList().get(ii).getIdKey());
				if (existOne == null) {
					continue;
				}
				existOne.setParentIdKey(paging.getList().get(ii).getParentIdKey());
				existOne.setOrderSeq(paging.getList().get(ii).getOrderSeq());
				existList.add(existOne);
			}
			if (existList.size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.update(existList);
			CM_PagingList<CM_Menu> retPaging = new CM_PagingList<>();
			retPaging.setList(existList);
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(retPaging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response deleteMenu(@PathParam("idKey") long idKey,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Menu existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Menu>(CM_Menu.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
