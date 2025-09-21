package kr.peelknight.common.service;

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

import kr.peelknight.common.Constant;
import kr.peelknight.common.dao.MenuDao;
import kr.peelknight.common.model.CM_Menu;
import kr.peelknight.common.model.CM_MenuAuth;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.common.model.CM_PagingList;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/menu")
public class MenuService {
	MenuDao dao = new MenuDao();
	
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
			CM_PagingList<CM_Menu> paging = new CM_PagingList<CM_Menu>();
			paging.setList(dao.selectMenus(-1, offset, Constant.DEFAULT_SIZE));
			paging.setPaging(dao.selectMenuPaging(-1));
			paging.numbering(offset);
			if (paging.getList() != null) {
				for (int ii=0; ii<paging.getList().size(); ii++) {
					CM_Menu menu = paging.getList().get(ii);
					menu.l10n(request.getSession());
					menu.setMenuAuths(dao.selectMenuAuthWithMenu(menu));
					if (menu.getMenuAuths() != null) {
						CM_MenuAuth menuAuth;
						for (int jj=0; jj<menu.getMenuAuths().size(); jj++) {
							menuAuth = menu.getMenuAuths().get(jj);
							menuAuth.l10n(request.getSession());
						}
					}
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
			CM_Menu existOne = dao.selectMenuByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.l10n(request.getSession());
			existOne.setMenuAuths(dao.selectMenuAuthWithMenu(existOne));
			if (existOne.getMenuAuths() != null) {
				CM_MenuAuth menuAuth;
				for (int jj=0; jj<existOne.getMenuAuths().size(); jj++) {
					menuAuth = existOne.getMenuAuths().get(jj);
					menuAuth.l10n(request.getSession());
				}
			}
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
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_PagingList<CM_Menu> paging = new CM_PagingList<CM_Menu>();
			paging.setList(dao.selectMenusWithUserType(userType, null));
			paging.setPaging(new CM_Paging());
			paging.numbering(0);
			if (paging.getList() != null) {
				for (int ii=0; ii<paging.getList().size(); ii++) {
					CM_Menu menu = paging.getList().get(ii);
					menu.l10n(session);
					menu.setMenuAuths(dao.selectMenuAuthWithMenu(menu));
					if (menu.getMenuAuths() != null) {
						CM_MenuAuth menuAuth;
						for (int jj=0; jj<menu.getMenuAuths().size(); jj++) {
							menuAuth = menu.getMenuAuths().get(jj);
							menuAuth.l10n(session);
						}
					}
				}
			}
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
	public Response selectSubMenus(@PathParam("parentIdKey") long parentIdKey,
								   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<CM_Menu> list = dao.selectMenus(parentIdKey, -1, Constant.DEFAULT_SIZE);
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
			if (AuthToken.isValidToken(menu.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int maxOrderSeq = 0;
			CM_Paging paging = dao.selectMaxOrderSeq(menu);
			if (paging != null) {
				maxOrderSeq = (int)paging.getTotalCount();
			}
			menu.setOrderSeq(maxOrderSeq + 1);
			dao.insertMenu(menu);
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
			if (AuthToken.isValidToken(menu.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Menu existOne = dao.selectMenuByIdKey(menu.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setMenuAuths(menu.getMenuAuths());
			existOne.setParentIdKey(menu.getParentIdKey());
			existOne.setMenuName(menu.getMenuName());
			existOne.setMenuUrl(menu.getMenuUrl());
			existOne.setStatus(menu.getStatus());
			dao.updateMenu(existOne);
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
			if (AuthToken.isValidToken(paging.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			List<CM_Menu> existList = new ArrayList<>();
			CM_Menu existOne;
			for (int ii=0; ii<paging.getList().size(); ii++) {
				existOne = dao.selectMenuByIdKey(paging.getList().get(ii).getIdKey());
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
			dao.updateMenus(existList);
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
			CM_Menu existOne = dao.selectMenuByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.deleteMenu(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Menu>(CM_Menu.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
