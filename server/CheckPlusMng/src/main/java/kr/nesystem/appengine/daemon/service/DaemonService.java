package kr.nesystem.appengine.daemon.service;

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
import kr.nesystem.appengine.common.dao.CodeDao;
import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.L10N;
import kr.nesystem.appengine.common.util.ResponseUtil;
import kr.nesystem.appengine.daemon.dao.DaemonDao;
import kr.nesystem.appengine.daemon.model.CM_Daemon;

@Path("/{version}/daemon")
public class DaemonService {
	DaemonDao dao = new DaemonDao();
	CodeDao codeDao = new CodeDao();

	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectDaemons(@Context HttpServletRequest request,
								  @PathParam("page") int page,
								  @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_PagingList<CM_Code> pagingCode = codeDao.selectCodeByType(request.getSession(), "DAEMONRUNNING");
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_Daemon> paging = dao.pagingList(request.getSession(), null, offset, Constant.DEFAULT_SIZE);
			if (paging.getList() != null) {
				CM_Daemon item;
				for (int ii=0; ii<paging.getList().size(); ii++) {
					item = paging.getList().get(ii);
					item.setRunning(DaemonContextListener.isRunning(item) ? "1": "0");
					if (pagingCode != null) {
						for (int jj=0; jj<pagingCode.getList().size(); jj++) {
							if (pagingCode.getList().get(jj).getCode().equals(item.getRunning())) {
								item.setRunningName(L10N.get(pagingCode.getList().get(jj).getCodeName(), request.getSession()));
							}
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
	public Response selectDaemon(@Context HttpServletRequest request,
								 @PathParam("idKey") long idKey,
								 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Daemon existOne = dao.select(request.getSession(), idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CodeDao codeDao = new CodeDao();
			CM_PagingList<CM_Code> paging = codeDao.selectCodeByType(request.getSession(), "DAEMONRUNNING");
			existOne.setRunning(DaemonContextListener.isRunning(existOne) ? "1": "0");
			if (paging.getList() != null) {
				for (int jj = 0; jj < paging.getList().size(); jj++) {
					if (paging.getList().get(jj).getCode().equals(existOne.getRunning())) {
//						existOne.setRunningName(L10N.get(listCode.get(jj).getCodeName(), request.getSession()));
					}
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_Daemon>(CM_Daemon.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertDaemon(CM_Daemon daemon) throws Exception {
		try {
			if (AuthToken.isValidToken(daemon.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			dao.insert(daemon);
			return ResponseUtil.getResponse((new ModelHandler<CM_Daemon>(CM_Daemon.class)).convertToJson(daemon));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response updateDaemon(CM_Daemon daemon,
								 @PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(daemon.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Daemon existOne = dao.select(null, daemon.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setDaemonName(daemon.getDaemonName());
			existOne.setClassName(daemon.getClassName());
			existOne.setOrderSeq(daemon.getOrderSeq());
			existOne.setStatus(daemon.getStatus());
			existOne.setAutoStartYN(daemon.getAutoStartYN());
			dao.update(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Daemon>(CM_Daemon.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response deleteDaemon(@PathParam("idKey") long idKey,
								 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Daemon existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Daemon>(CM_Daemon.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/start")
	public Response startDaemon(@PathParam("idKey") long idKey,
								@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Daemon existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (DaemonContextListener.isRunning(existOne)) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			DaemonContextListener.startDaemon(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Daemon>(CM_Daemon.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/stop")
	public Response stopDaemon(@PathParam("idKey") long idKey,
							   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Daemon existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (DaemonContextListener.isRunning(existOne) == false) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			DaemonContextListener.stopDaemon(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Daemon>(CM_Daemon.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
