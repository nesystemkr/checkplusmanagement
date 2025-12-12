package kr.co.checkplusmng.service;

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
import kr.co.checkplusmng.dao.WifiDao;
import kr.co.checkplusmng.model.MW_Wifi;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/wifi")
public class WifiService {
	WifiDao dao = new WifiDao();
	
	// SignUp
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insert(MW_Wifi wifi) throws Exception {
		try {
			if (AuthToken.isValidToken(wifi.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (wifi.getWifiId() == null || wifi.getWifiId().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Wifi existOne = dao.selectByWifiId(null, wifi.getWifiId());
			if (existOne != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			dao.insert(wifi);
			return ResponseUtil.getResponse((new ModelHandler<MW_Wifi>(MW_Wifi.class)).convertToJson(wifi));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//Update User
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response update(@PathParam("idKey") long idKey,
						   MW_Wifi wifi) {
		try {
			if (AuthToken.isValidToken(wifi.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (idKey != wifi.getIdKey()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (wifi.getWifiId() == null || wifi.getWifiId().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Wifi existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setProjectIdKey(wifi.getProjectIdKey());
			existOne.setModelName(wifi.getModelName());
			existOne.setSerialNo(wifi.getSerialNo());
			existOne.setMacAddress(wifi.getMacAddress());
			existOne.setApGateId(wifi.getApGateId());
			existOne.setApGatePw(wifi.getApGatePw());
			existOne.setApWifiId(wifi.getApWifiPw());
			existOne.setMemo(wifi.getMemo());
			dao.update(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_Wifi>(MW_Wifi.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//Update User
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response delete(@PathParam("idKey") long idKey,
						   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			MW_Wifi existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_Wifi>(MW_Wifi.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response list(@Context HttpServletRequest request,
						 @PathParam("page") int page,
						 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<MW_Wifi> paging = dao.pagingList(request.getSession(), null, offset, Constant.DEFAULT_SIZE);
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//User detail
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response one(@Context HttpServletRequest request,
						@PathParam("idKey") long idKey,
						@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			MW_Wifi existOne = dao.select(request.getSession(), idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<MW_Wifi>(MW_Wifi.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
