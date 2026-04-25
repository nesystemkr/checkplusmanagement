package kr.co.checkplusmng.service;

import java.util.Date;
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
import kr.co.checkplusmng.dao.DeviceDao;
import kr.co.checkplusmng.dao.InvoiceDao;
import kr.co.checkplusmng.model.MW_Activity_Element;
import kr.co.checkplusmng.model.MW_Device;
import kr.co.checkplusmng.model.MW_Invoice;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/invoice")
public class InvoiceService extends BaseService<MW_Invoice> {
	public InvoiceService() {
		super(MW_Invoice.class, "TAX");
		_dao = new InvoiceDao();
	}
	
	@Override
	protected void updateItem(MW_Invoice existOne, MW_Invoice newOne) {
		existOne.setInvoiceType(newOne.getInvoiceType());
		existOne.setIssueDate(newOne.getIssueDate());
		existOne.setIssueAmount(newOne.getIssueAmount());
		existOne.setApprovalNo(newOne.getApprovalNo());
		existOne.setEntryDate(newOne.getEntryDate());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
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
