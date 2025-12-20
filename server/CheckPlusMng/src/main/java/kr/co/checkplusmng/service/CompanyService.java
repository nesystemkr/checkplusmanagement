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
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.co.checkplusmng.dao.CompanyDao;
import kr.co.checkplusmng.model.MW_Company;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/company")
public class CompanyService {
	CompanyDao dao = new CompanyDao();
	
	// SignUp
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insert(MW_Company company) throws Exception {
		try {
			if (AuthToken.isValidToken(company.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (company.getCompanyId() == null || company.getCompanyId().length() == 0 ||
				company.getName() == null || company.getName().length() == 0 ||
				company.getAddress1() == null || company.getAddress1().length() == 0 ||
				company.getTelephone1() == null || company.getTelephone1().length() == 0 ||
				company.getMainOfficer() == null || company.getMainOfficer().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Company existOne = dao.selectByCompanyId(null, company.getCompanyId());
			if (existOne != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			dao.insert(company);
			return ResponseUtil.getResponse((new ModelHandler<MW_Company>(MW_Company.class)).convertToJson(company));
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
						   MW_Company company) {
		try {
			if (AuthToken.isValidToken(company.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (idKey != company.getIdKey()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (company.getCompanyId() == null || company.getCompanyId().length() == 0 ||
				company.getName() == null || company.getName().length() == 0 ||
				company.getAddress1() == null || company.getAddress1().length() == 0 ||
				company.getTelephone1() == null || company.getTelephone1().length() == 0 ||
				company.getMainOfficer() == null || company.getMainOfficer().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Company existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setAddress1(company.getAddress1());
			existOne.setTelephone1(company.getTelephone1());
			existOne.setAddress2(company.getAddress2());
			existOne.setTelephone2(company.getTelephone2());
			existOne.setMainOfficer(company.getMainOfficer());
			existOne.setMainOfficerPosition(company.getMainOfficerPosition());
			existOne.setMainOfficerTelephone(company.getMainOfficerTelephone());
			existOne.setMainOfficerEmail(company.getMainOfficerEmail());
			existOne.setSubOfficer(company.getSubOfficer());
			existOne.setSubOfficerPosition(company.getSubOfficerPosition());
			existOne.setSubOfficerTelephone(company.getSubOfficerTelephone());
			existOne.setSubOfficerEmail(company.getSubOfficerEmail());
			existOne.setMemo(company.getMemo());
			existOne.setStatus(company.getStatus());
			existOne.setOrderSeq(company.getOrderSeq());
			dao.update(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_Company>(MW_Company.class)).convertToJson(existOne));
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
			MW_Company existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_Company>(MW_Company.class)).convertToJson(existOne));
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
			CM_PagingList<MW_Company> paging = dao.pagingList(request.getSession(), null, offset, Constant.DEFAULT_SIZE, "orderSeq");
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
			MW_Company existOne = dao.select(request.getSession(), idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<MW_Company>(MW_Company.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
