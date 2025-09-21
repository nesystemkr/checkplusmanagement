package kr.peelknight.daemon.service;

import java.util.List;

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
import kr.peelknight.common.dao.CodeDao;
import kr.peelknight.daemon.dao.DaemonDao;
import kr.peelknight.daemon.model.CM_Daemon;
import kr.peelknight.common.model.CM_Code;
import kr.peelknight.common.model.CM_PagingList;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.L10N;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/daemon")
public class DaemonService {
	DaemonDao dao = new DaemonDao();

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
			CodeDao codeDao = new CodeDao();
			List<CM_Code> listCode = codeDao.selectCodeByType("DAEMONRUNNING");
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_Daemon> paging = new CM_PagingList<CM_Daemon>();
			paging.setList(dao.selectDaemons(offset, Constant.DEFAULT_SIZE));
			paging.setPaging(dao.selectDaemonPaging());
			paging.numbering(offset);
			if (paging.getList() != null) {
				CM_Daemon item;
				for (int ii=0; ii<paging.getList().size(); ii++) {
					item = paging.getList().get(ii);
					item.l10n(request.getSession());
					item.setRunning(DaemonContextListener.isRunning(item) ? "1": "0");
					if (listCode != null) {
						for (int jj=0; jj<listCode.size(); jj++) {
							if (listCode.get(jj).getCode().equals(item.getRunning())) {
								item.setRunningName(L10N.get(listCode.get(jj).getCodeName(), request.getSession()));
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
			CM_Daemon existOne = dao.selectDaemonByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CodeDao codeDao = new CodeDao();
			List<CM_Code> listCode = codeDao.selectCodeByType("DAEMONRUNNING");
			existOne.setRunning(DaemonContextListener.isRunning(existOne) ? "1": "0");
			if (listCode != null) {
				for (int jj=0; jj<listCode.size(); jj++) {
					if (listCode.get(jj).getCode().equals(existOne.getRunning())) {
						existOne.setRunningName(L10N.get(listCode.get(jj).getCodeName(), request.getSession()));
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
			if (AuthToken.isValidToken(daemon.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			dao.insertDaemon(daemon);
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
			if (AuthToken.isValidToken(daemon.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Daemon existOne = dao.selectDaemonByIdKey(daemon.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setDaemonName(daemon.getDaemonName());
			existOne.setClassName(daemon.getClassName());
			existOne.setOrderSeq(daemon.getOrderSeq());
			existOne.setStatus(daemon.getStatus());
			existOne.setAutoStartYN(daemon.getAutoStartYN());
			dao.updateDaemon(existOne);
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
			CM_Daemon existOne = dao.selectDaemonByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.deleteDaemon(existOne);
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
			CM_Daemon existOne = dao.selectDaemonByIdKey(idKey);
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
			CM_Daemon existOne = dao.selectDaemonByIdKey(idKey);
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
