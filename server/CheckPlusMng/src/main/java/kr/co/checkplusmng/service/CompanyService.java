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
			if (company.getIdString() == null || company.getIdString().length() == 0 ||
				company.getName() == null || company.getName().length() == 0 ||
				company.getAddress() == null || company.getAddress().length() == 0 ||
				company.getTelephone() == null || company.getTelephone().length() == 0 ||
				company.getOfficer() == null || company.getOfficer().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Company existOne = dao.selectByIdString(null, company.getIdString());
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
			if (company.getIdString() == null || company.getIdString().length() == 0 ||
				company.getName() == null || company.getName().length() == 0 ||
				company.getAddress() == null || company.getAddress().length() == 0 ||
				company.getTelephone() == null || company.getTelephone().length() == 0 ||
				company.getOfficer() == null || company.getOfficer().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			MW_Company existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setAddress(company.getAddress());
			existOne.setTelephone(company.getTelephone());
			existOne.setEmail(company.getEmail());
			existOne.setOfficer(company.getOfficer());
			existOne.setOfficerTel(company.getOfficerTel());
			existOne.setMemo(company.getMemo());
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
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/newId")
	public Response newId(@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<MW_Company> list = dao.list(null, null, -1, 0);
			int index = 1;
			String compId;
			boolean isFound;
			String format = "CUS_%05d";
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
			MW_Company ret = new MW_Company();
			ret.setIdString(compId);
			return ResponseUtil.getResponse((new ModelHandler<MW_Company>(MW_Company.class)).convertToJson(ret));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
