package kr.nesystem.appengine.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import kr.nesystem.appengine.common.dao.CodeDao;
import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.peelknight.common.Constant;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/code")
public class CodeService {
	CodeDao dao = new CodeDao();
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectCodes(@Context HttpServletRequest request,
								@PathParam("page") int page,
								@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_Code> paging = dao.selectCodes(offset, Constant.DEFAULT_SIZE);
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{typeParam}/{codeParam}")
	public Response selectCode(@Context HttpServletRequest request,
							   @PathParam("typeParam") String typeParam,
							   @PathParam("codeParam") String codeParam,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Code existOne = dao.selectCodeByTypeNCode(typeParam, codeParam); dao.selectCodeByTypeNCode(typeParam, codeParam);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_Code>(CM_Code.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{typeParam}")
	public Response selectCodeWithType(@Context HttpServletRequest request,
									   @PathParam("typeParam") String typeParam,
									   @QueryParam("q") String authToken) {
		try {
//			if (AuthToken.isValidToken(authToken) == false) {
//				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
//			}
			CM_PagingList<CM_Code> paging = dao.selectCodeByType(typeParam);
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertCode(CM_Code code) throws Exception {
		try {
			if (AuthToken.isValidToken(code.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			dao.insert(code);
			return ResponseUtil.getResponse((new ModelHandler<CM_Code>(CM_Code.class)).convertToJson(code));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{typeParam}/{codeParam}")
	public Response updateCode(CM_Code code,
							   @PathParam("typeParam") String typeParam,
							   @PathParam("codeParam") String codeParam) throws Exception {
		try {
			if (AuthToken.isValidToken(code.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Code existOne = dao.selectCodeByTypeNCode(typeParam, codeParam);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setCodeName(code.getCodeName());
			existOne.setComment(code.getComment());
			existOne.setOrderSeq(code.getOrderSeq());
			dao.update(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Code>(CM_Code.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{typeParam}/{codeParam}")
	public Response deleteCode(@PathParam("typeParam") String typeParam,
							   @PathParam("codeParam") String codeParam,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Code existOne = dao.selectCodeByTypeNCode(typeParam, codeParam);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Code>(CM_Code.class)).convertToJson(existOne));
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
	public Response insertOrUpdateCodes(CM_PagingList<CM_Code> paging) throws Exception {
		try {
			if (AuthToken.isValidToken(paging.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.insertOrUpdate(paging.getList());
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
	public Response deleteCodes(CM_PagingList<CM_Code> paging) {
		try {
			if (AuthToken.isValidToken(paging.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.delete(paging.getList());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/types")
	public Response selectCodesWithTypes(@Context HttpServletRequest request,
										 CM_PagingList<CM_Code> paging) throws Exception {
		try {
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			Map<String, List<CM_Code>> retMap = new HashMap<>();
			if (paging.getList() != null) {
				CM_Code item;
				for (int ii = 0; ii < paging.getList().size(); ii++) {
					item = paging.getList().get(ii);
					retMap.put(item.getType(), dao.selectCodeByType(item.getType()).getList());
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<Map>(Map.class)).convertToJson(retMap));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/types/forselect")
	public Response selectCodesWithTypesForSelect(@Context HttpServletRequest request,
												  CM_PagingList<CM_Code> paging) throws Exception {
		try {
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			Map<String, Map<String, String>> retMap = new HashMap<>();
			CM_Code item;
			for (int ii=0; ii<paging.getList().size(); ii++) {
				item = paging.getList().get(ii);
				List<CM_Code> codes = dao.selectCodeByType(item.getType()).getList();
				Map<String, String> tmpMap = new HashMap<>();
				if (codes != null) {
					CM_Code code;
					for (int jj = 0; jj < codes.size(); jj++) {
						code = codes.get(jj);
						tmpMap.put(code.getCode(), code.getCodeName());
					}
				}
				retMap.put(item.getType(), tmpMap);
			}
			return ResponseUtil.getResponse((new ModelHandler<Map>(Map.class)).convertToJson(retMap));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}