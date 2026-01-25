package kr.co.checkplusmng.service;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import kr.co.checkplusmng.dao.ActivityElementDao;
import kr.co.checkplusmng.dao.InvoiceDao;
import kr.co.checkplusmng.model.MW_Activity_Element;
import kr.co.checkplusmng.model.MW_Invoice;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/invoice")
public class InvoiceService {
	InvoiceDao dao = new InvoiceDao();
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/newId")
	public Response newId(@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<MW_Invoice> list = dao.list(null, null, -1, 0);
			int index = 1;
			String compId;
			boolean isFound;
			String format = "TAX_%05d";
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
			MW_Invoice ret = new MW_Invoice();
			ret.setIdString(compId);
			return ResponseUtil.getResponse((new ModelHandler<MW_Invoice>(MW_Invoice.class)).convertToJson(ret));
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
	public Response insertOrUpdateInvoices(CM_PagingList<MW_Invoice> paging) throws Exception {
		try {
			if (AuthToken.isValidToken(paging.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			for (int ii = 0; ii < paging.getList().size(); ii++) {
				if (paging.getList().get(ii).getActivityIdKey() == 0) {
					return ResponseUtil.getResponse(Status.BAD_REQUEST);
				}
			}
			InvoiceDao invoiceDao = new InvoiceDao();
			invoiceDao.insertOrUpdate(paging.getList());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response deleteInvoice(@PathParam("idKey") long idKey,
								  @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			MW_Invoice existOne = dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_Invoice>(MW_Invoice.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
