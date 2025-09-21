package kr.peelknight.socket.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import kr.peelknight.common.Constant;
import kr.peelknight.socket.dao.SocketLogDao;
import kr.peelknight.socket.model.SH_SocketLog;
import kr.peelknight.common.model.CM_PagingList;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/socketlog")
public class SocketLogService {
	SocketLogDao dao = new SocketLogDao();

	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectSocketLogs(@PathParam("page") int page,
									 @QueryParam("q") String authToken,
									 @QueryParam("startDate") long startDate,
									 @QueryParam("endDate") long endDate) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<SH_SocketLog> paging = new CM_PagingList<SH_SocketLog>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String baseDate = sdf.format(new Date(startDate));
			paging.setList(dao.selectSocketLogs(baseDate, offset, Constant.DEFAULT_SIZE));
			paging.setPaging(dao.selectSocketLogPaging(baseDate));
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
	public Response selectSocketLog(@PathParam("idKey") long idKey,
									@QueryParam("q") String authToken,
									@QueryParam("startDate") long startDate,
									@QueryParam("endDate") long endDate) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String baseDate = sdf.format(new Date(startDate));
			SH_SocketLog existOne = dao.selectSocketLogByIdKey(baseDate, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<SH_SocketLog>(SH_SocketLog.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
