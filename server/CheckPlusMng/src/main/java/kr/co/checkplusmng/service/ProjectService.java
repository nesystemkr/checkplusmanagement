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
import kr.co.checkplusmng.dao.ProjectDao;
import kr.co.checkplusmng.model.MW_Project;
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
			if (project.getCompanyId() == null || project.getCompanyId().length() == 0 ||
				project.getName() == null || project.getName().length() == 0 ||
				project.getAddress1() == null || project.getAddress1().length() == 0 ||
				project.getTelephone1() == null || project.getTelephone1().length() == 0 ||
				project.getMainOfficer() == null || project.getMainOfficer().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Project existOne = dao.selectByCompanyId(null, project.getCompanyId());
			if (existOne != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			dao.insert(project);
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
			if (project.getCompanyId() == null || project.getCompanyId().length() == 0 ||
				project.getName() == null || project.getName().length() == 0 ||
				project.getAddress1() == null || project.getAddress1().length() == 0 ||
				project.getTelephone1() == null || project.getTelephone1().length() == 0 ||
				project.getMainOfficer() == null || project.getMainOfficer().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Project existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existCompany.setAddress1(company.getAddress1());
			existCompany.setTelephone1(company.getTelephone1());
			existCompany.setAddress2(company.getAddress2());
			existCompany.setTelephone2(company.getTelephone2());
			existCompany.setMainOfficer(company.getMainOfficer());
			existCompany.setMainOfficerPosition(company.getMainOfficerPosition());
			existCompany.setMainOfficerTelephone(company.getMainOfficerTelephone());
			existCompany.setMainOfficerEmail(company.getMainOfficerEmail());
			existCompany.setSubOfficer(company.getSubOfficer());
			existCompany.setSubOfficerPosition(company.getSubOfficerPosition());
			existCompany.setSubOfficerTelephone(company.getSubOfficerTelephone());
			existCompany.setSubOfficerEmail(company.getSubOfficerEmail());
			existCompany.setMemo(company.getMemo());
			existCompany.setStatus(company.getStatus());
			dao.update(existCompany);
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
			return ResponseUtil.getResponse((new ModelHandler<MW_Project>(MW_Project.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
