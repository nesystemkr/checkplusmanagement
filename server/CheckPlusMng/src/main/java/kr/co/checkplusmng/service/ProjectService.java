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
import kr.co.checkplusmng.dao.ProjectDao;
import kr.co.checkplusmng.model.MW_Project;
import kr.co.checkplusmng.util.CompanyStore;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/project")
public class ProjectService {
	ProjectDao dao = new ProjectDao();
	
	// SignUp
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insert(MW_Project project) throws Exception {
		try {
			if (AuthToken.isValidToken(project.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (project.getIdString() == null || project.getIdString().length() == 0 ||
				project.getName() == null || project.getName().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Project existOne = dao.selectByIdString(null, project.getIdString());
			if (existOne != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			dao.insert(project);
			CompanyStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<MW_Project>(MW_Project.class)).convertToJson(project));
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
						   MW_Project project) {
		try {
			if (AuthToken.isValidToken(project.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (idKey != project.getIdKey()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (project.getIdString() == null || project.getIdString().length() == 0 ||
				project.getName() == null || project.getName().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Project existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setName(project.getName());
			existOne.setCustomerIdKey(project.getCustomerIdKey());
			existOne.setBrokerIdKey(project.getBrokerIdKey());
			existOne.setContractDate(project.getContractDate());
			existOne.setMemo(project.getMemo());
			existOne.setOrderSeq(project.getOrderSeq());
			dao.update(existOne);
			CompanyStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<MW_Project>(MW_Project.class)).convertToJson(existOne));
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
			MW_Project existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			CompanyStore.reload();
			return ResponseUtil.getResponse((new ModelHandler<MW_Project>(MW_Project.class)).convertToJson(existOne));
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
			CM_PagingList<MW_Project> paging = dao.pagingList(request.getSession(), null, offset, Constant.DEFAULT_SIZE);
			if (paging != null && paging.getList() != null) {
				for (int ii = 0; ii < paging.getList().size(); ii++) {
					MW_Project item = paging.getList().get(ii);
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
			MW_Project existOne = dao.select(request.getSession(), idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			fillupSubData(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_Project>(MW_Project.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	private void fillupSubData(MW_Project item) {
		item.setCustomerName(CompanyStore.getName(item.getCustomerIdKey()));
		item.setBrokerName(CompanyStore.getName(item.getBrokerIdKey()));
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/newId")
	public Response newId(@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<MW_Project> list = dao.list(null, null, -1, 0);
			int index = 1;
			String compId;
			boolean isFound;
			String format = "PRO_%05d";
			if (list != null) {
				while (true) {
					compId = String.format(format, index);
					isFound = false;
					for (int ii = 0; ii < list.size(); ii++) {
						if (compId.equals(list.get(ii).getIdString())) {
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
			MW_Project ret = new MW_Project();
			ret.setIdString(compId);
			return ResponseUtil.getResponse((new ModelHandler<MW_Project>(MW_Project.class)).convertToJson(ret));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
