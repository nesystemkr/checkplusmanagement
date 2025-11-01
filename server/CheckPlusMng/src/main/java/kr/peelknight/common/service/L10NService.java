package kr.peelknight.common.service;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import kr.peelknight.common.Constant;
import kr.peelknight.common.dao.L10NDao;
import kr.peelknight.common.func.InitBaseFunctions;
import kr.peelknight.common.model.CM_L10N;
import kr.peelknight.common.model.CM_L10NLocale;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.common.model.CM_PagingList;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
//import kr.peelknight.util.L10N;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/l10n")
public class L10NService {
	L10NDao dao = new L10NDao();

	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectL10Ns(@PathParam("page") int page,
								@QueryParam("q") String authToken,
								@QueryParam("search") String search) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_L10N> paging = new CM_PagingList<CM_L10N>();
			paging.setList(dao.selectL10Ns(offset, Constant.DEFAULT_SIZE, search));
			paging.setPaging(dao.selectL10NPaging(search));
			paging.numbering(offset);
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response selectL10N(@PathParam("idKey") long idKey,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_L10N existOne = dao.selectL10NByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_L10N>(CM_L10N.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertL10N(CM_L10N l10N) throws Exception {
		try {
			if (AuthToken.isValidToken(l10N.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			dao.insertL10N(l10N);
			return ResponseUtil.getResponse((new ModelHandler<CM_L10N>(CM_L10N.class)).convertToJson(l10N));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response updateL10N(CM_L10N l10N,
							   @PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(l10N.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_L10N existOne = dao.selectL10NByIdKey(l10N.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setDefaultString(l10N.getDefaultString());
			dao.updateL10N(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_L10N>(CM_L10N.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response deleteL10N(@PathParam("idKey") long idKey,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_L10N existOne = dao.selectL10NByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.deleteL10N(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_L10N>(CM_L10N.class)).convertToJson(existOne));
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
	public Response insertOrUpdateL10Ns(CM_PagingList<CM_L10N> paging) throws Exception {
		try {
			if (AuthToken.isValidToken(paging.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.insertOrUpdateL10Ns(paging.getList());
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
	public Response deleteL10Ns(CM_PagingList<CM_L10N> paging) {
		try {
			if (AuthToken.isValidToken(paging.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.deleteL10Ns(paging.getList());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/locale/list")
	public Response selectL10NLocale(@PathParam("idKey") long idKey,
									 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_L10N existOne = dao.selectL10NByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_PagingList<CM_L10NLocale> paging = new CM_PagingList<>();
			paging.setList(dao.selectL10NLocales(existOne.getIdString(), null, -1, 0));
			paging.setPaging(new CM_Paging());
			paging.numbering(0);
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/locale/item/{localeIdKey}")
	public Response selectL10NLocaleItem(@PathParam("idKey") long idKey,
										 @PathParam("localeIdKey") long localeIdKey,
										 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_L10N existOne = dao.selectL10NByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_L10NLocale existLocale = dao.selectL10NLocaleByIdKey(localeIdKey);
			if (existLocale == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_L10NLocale>(CM_L10NLocale.class)).convertToJson(existLocale));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/locale/list")
	public Response insertOrUpdateL10NLocales(@PathParam("idKey") long idKey,
											  CM_PagingList<CM_L10NLocale> paging) throws Exception {
		try {
			if (AuthToken.isValidToken(paging.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_L10N existOne = dao.selectL10NByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			CM_L10NLocale item;
			for (int ii=0; ii<paging.getList().size(); ii++) {
				item = paging.getList().get(ii);
				item.setIdString(existOne.getIdString());
				if (item.getIdKey() == 0) {
					CM_L10NLocale existLocale = dao.selectL10NLocaleByIdStringNLocale(item.getIdString(), item.getLocale());
					if (existLocale != null) {
						return ResponseUtil.getResponse(Status.CONFLICT);
					}
				}
			}
			dao.insertOrUpdateL10NLocales(paging.getList());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/locale/list/delete")
	public Response deleteL10NLocales(@PathParam("idKey") long idKey,
									  CM_PagingList<CM_L10NLocale> paging) {
		try {
			if (AuthToken.isValidToken(paging.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_L10N existOne = dao.selectL10NByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			dao.deleteL10NLocales(paging.getList());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/refresh")
	public Response refresh(@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
//			L10N.loadResource();
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/reloadfromfile")
	public Response relaodfromfile(@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			InitBaseFunctions initFunc = new InitBaseFunctions();
			initFunc.recreateL10NTable();
			initFunc.insertL10Ns_default("lang.properties");
			initFunc.insertL10Ns_locale("lang_en.properties", "en_US");
//			L10N.loadResource();
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
