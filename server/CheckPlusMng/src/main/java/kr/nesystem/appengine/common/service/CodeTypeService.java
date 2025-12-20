package kr.nesystem.appengine.common.service;

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
import kr.nesystem.appengine.common.dao.CodeTypeDao;
import kr.nesystem.appengine.common.model.CM_CodeType;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.CodeStore;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/codetype")
public class CodeTypeService {
	CodeTypeDao dao = new CodeTypeDao();
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectCodeTypes(@Context HttpServletRequest request,
									@PathParam("page") int page,
									@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_CodeType> paging = dao.selectCodeTypes(request.getSession(), offset, Constant.DEFAULT_SIZE);
			if (paging.getList() != null) {
				CM_CodeType item;
				for (int ii=0; ii<paging.getList().size(); ii++) {
					item = paging.getList().get(ii);
					item.l10n(request.getSession());
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
	@Path("/{typeParam}")
	public Response selectCodeType(@Context HttpServletRequest request,
								   @PathParam("typeParam") String typeParam,
								   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_CodeType existType = dao.select(request.getSession(), typeParam);
			if (existType == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existType.l10n(request.getSession());
			return ResponseUtil.getResponse((new ModelHandler<CM_CodeType>(CM_CodeType.class)).convertToJson(existType));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertCodeType(CM_CodeType codeType) throws Exception {
		try {
			if (AuthToken.isValidToken(codeType.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			dao.insert(codeType);
			CodeStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<CM_CodeType>(CM_CodeType.class)).convertToJson(codeType));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{typeParam}")
	public Response updateCodeType(CM_CodeType codeType,
								   @PathParam("typeParam") String typeParam) throws Exception {
		try {
			if (AuthToken.isValidToken(codeType.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_CodeType existOne = dao.select(null, typeParam);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setTypeName(codeType.getTypeName());
			dao.update(existOne);
			CodeStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<CM_CodeType>(CM_CodeType.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{typeParam}")
	public Response deleteCodeType(@PathParam("typeParam") String typeParam,
								   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_CodeType existOne = dao.select(null, typeParam);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			CodeStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<CM_CodeType>(CM_CodeType.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list")
	public Response insertOrUpdateCodeTypes(CM_PagingList<CM_CodeType> paging) throws Exception {
		try {
			if (AuthToken.isValidToken(paging.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.insertOrUpdate(paging.getList());
			CodeStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/delete")
	public Response deleteCodeTypes(CM_PagingList<CM_CodeType> paging) {
		try {
			if (AuthToken.isValidToken(paging.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.delete(paging.getList());
			CodeStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
