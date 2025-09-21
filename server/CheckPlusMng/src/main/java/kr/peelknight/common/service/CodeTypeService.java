package kr.peelknight.common.service;

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

import kr.peelknight.common.Constant;
import kr.peelknight.common.dao.CodeTypeDao;
import kr.peelknight.common.model.CM_CodeType;
import kr.peelknight.common.model.CM_PagingList;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.ResponseUtil;

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
			CM_PagingList<CM_CodeType> paging = new CM_PagingList<CM_CodeType>();
			paging.setList(dao.selectCodeTypes(offset, Constant.DEFAULT_SIZE));
			paging.setPaging(dao.selectCodeTypePaging());
			paging.numbering(offset);
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
			CM_CodeType existType = dao.selectCodeTypeByType(typeParam);
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
			if (AuthToken.isValidToken(codeType.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			dao.insertCodeType(codeType);
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
			if (AuthToken.isValidToken(codeType.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_CodeType existOne = dao.selectCodeTypeByType(typeParam);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setTypeName(codeType.getTypeName());
			dao.updateCodeType(existOne);
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
			CM_CodeType existOne = dao.selectCodeTypeByType(typeParam);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.deleteCodeType(existOne);
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
			if (AuthToken.isValidToken(paging.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.insertOrUpdateCodeTypes(paging.getList());
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
			if (AuthToken.isValidToken(paging.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.deleteCodeTypes(paging.getList());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
