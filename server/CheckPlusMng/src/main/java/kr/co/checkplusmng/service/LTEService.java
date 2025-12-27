package kr.co.checkplusmng.service;

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
import kr.co.checkplusmng.dao.LTEDao;
import kr.co.checkplusmng.model.MW_LTE;
import kr.co.checkplusmng.model.MW_Project;
import kr.co.checkplusmng.util.CompanyStore;
import kr.co.checkplusmng.util.ProjectStore;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/lte")
public class LTEService {
	LTEDao dao = new LTEDao();
	
	// SignUp
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insert(MW_LTE lte) throws Exception {
		try {
			if (AuthToken.isValidToken(lte.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (lte.getLteId() == null || lte.getLteId().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_LTE existOne = dao.selectByLTEId(null, lte.getLteId());
			if (existOne != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			dao.insert(lte);
			return ResponseUtil.getResponse((new ModelHandler<MW_LTE>(MW_LTE.class)).convertToJson(lte));
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
						   MW_LTE lte) {
		try {
			if (AuthToken.isValidToken(lte.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (idKey != lte.getIdKey()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (lte.getLteId() == null || lte.getLteId().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_LTE existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			
			existOne.setProjectIdKey(lte.getProjectIdKey());
			existOne.setLteId(lte.getLteId());
			existOne.setModelName(lte.getModelName());
			existOne.setDeviceSerialNo(lte.getDeviceSerialNo());
			existOne.setUsimSerialNo(lte.getUsimSerialNo());
			existOne.setTelephoneNo(lte.getTelephoneNo());
			existOne.setLteGateId(lte.getLteGateId());
			existOne.setLteGatePw(lte.getLteGatePw());
			existOne.setLteWifiId(lte.getLteWifiId());
			existOne.setLteWifiPw(lte.getLteWifiPw());
			existOne.setRegistDate(lte.getRegistDate());
			existOne.setStartDate(lte.getStartDate());
			existOne.setEndDate(lte.getEndDate());
			existOne.setContract(lte.getContract());
			existOne.setMemo(lte.getMemo());
			existOne.setOrderSeq(lte.getOrderSeq());
			dao.update(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_LTE>(MW_LTE.class)).convertToJson(existOne));
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
			MW_LTE existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_LTE>(MW_LTE.class)).convertToJson(existOne));
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
			CM_PagingList<MW_LTE> paging = dao.pagingList(request.getSession(), null, offset, Constant.DEFAULT_SIZE, "orderSeq");
			if (paging != null && paging.getList() != null) {
				for (int ii = 0; ii < paging.getList().size(); ii++) {
					MW_LTE item = paging.getList().get(ii);
					fillupSubData(item);
				}
			}
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
			MW_LTE existOne = dao.select(request.getSession(), idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			fillupSubData(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_LTE>(MW_LTE.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	private void fillupSubData(MW_LTE item) {
		MW_Project project = ProjectStore.get(item.getProjectIdKey());
		if (project != null) {
			item.setProjectName(project.getProjectName());
			item.setContractCompanyName(CompanyStore.getName(project.getContractCompanyIdKey()));
		}
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/newId")
	public Response newId(@QueryParam("format") String format,
						  @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<MW_LTE> list = dao.list(null, null, -1, 0);
			int index = 1;
			String compId;
			boolean isFound;
			if (list != null) {
				while (true) {
					compId = String.format(format, index);
					isFound = false;
					for (int ii = 0; ii < list.size(); ii++) {
						if (compId.equals(list.get(ii).getLteId())) {
							isFound = true;
							break;
						}
					}
					if (isFound == false) {
						break;
					}
					index++;
				}
			} else {
				compId = String.format(format, index);
			}
			MW_LTE ret = new MW_LTE();
			ret.setLteId(compId);
			return ResponseUtil.getResponse((new ModelHandler<MW_LTE>(MW_LTE.class)).convertToJson(ret));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
