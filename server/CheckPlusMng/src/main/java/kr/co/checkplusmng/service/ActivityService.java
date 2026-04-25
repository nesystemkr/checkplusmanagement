package kr.co.checkplusmng.service;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import kr.co.checkplusmng.dao.ActivityDao;
import kr.co.checkplusmng.dao.ActivityElementDao;
import kr.co.checkplusmng.dao.InvoiceDao;
import kr.co.checkplusmng.dao.LTEDao;
import kr.co.checkplusmng.dao.WelderDao;
import kr.co.checkplusmng.dao.WifiDao;
import kr.co.checkplusmng.model.MW_Activity;
import kr.co.checkplusmng.model.MW_Activity_Element;
import kr.co.checkplusmng.model.MW_Invoice;
import kr.co.checkplusmng.model.MW_LTE;
import kr.co.checkplusmng.model.MW_Project;
import kr.co.checkplusmng.model.MW_Welder;
import kr.co.checkplusmng.model.MW_Wifi;
import kr.co.checkplusmng.util.CompanyStore;
import kr.co.checkplusmng.util.ProjectStore;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/activity")
public class ActivityService extends BaseService<MW_Activity> {
	public ActivityService() {
		super(MW_Activity.class, "ACT");
		_dao = new ActivityDao();
	}
	
	@Override
	protected void updateItem(MW_Activity existOne, MW_Activity newOne) {
		existOne.setHwTotalAmount(newOne.getHwTotalAmount());
		existOne.setHwActualAmount(newOne.getHwActualAmount());
		existOne.setDeliveryDate(newOne.getDeliveryDate());
		existOne.setSwTotalAmount(newOne.getSwTotalAmount());
		existOne.setSwActualAmount(newOne.getSwActualAmount());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
	}
	
	@Override
	protected void fillupSubData(MW_Activity item) {
		MW_Project project = ProjectStore.get(item.getProjectIdKey());
		item.setProjectIdString(project.getIdString());
		item.setCustomerName(CompanyStore.getName(project.getCustomerIdKey()));
		item.setBrokerName(CompanyStore.getName(project.getBrokerIdKey()));
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{projectIdKey}/list/{page}")
	public Response listInProject(@Context HttpServletRequest request,
								  @PathParam("page") int page,
								  @QueryParam("q") String authToken,
								  @PathParam("projectIdKey") long projectIdKey) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			PropertyFilter filter = PropertyFilter.eq("projectIdKey", projectIdKey);
			CM_PagingList<MW_Activity> paging = _dao.pagingList(request.getSession(), filter, offset, Constant.DEFAULT_SIZE, null);
			if (paging != null && paging.getList() != null) {
				for (int ii = 0; ii < paging.getList().size(); ii++) {
					MW_Activity item = paging.getList().get(ii);
					fillupSubData(item);
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public String getNewElementId(ActivityElementDao elementDao, List<MW_Activity_Element> elements) throws Exception {
		List<MW_Activity_Element> list =  elementDao.list(null, null, -1, 0, null);
		int index = 1;
		String compId;
		boolean isFound;
		String format = "ELE_%05d";
		if (list != null || elements != null) {
			while (true) {
				compId = String.format(format, index);
				isFound = false;
				if (list != null) {
					for (int ii = 0; ii < list.size(); ii++) {
						if (compId.equals(list.get(ii).getIdString())) {
							isFound = true;
							break;
						}
					}
				}
				if (elements != null) {
					for (int ii = 0; ii < elements.size(); ii++) {
						if (compId.equals(elements.get(ii).getIdString())) {
							isFound = true;
							break;
						}
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
		return compId;
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{activityIdKey}/elements")
	public Response getElements(@Context HttpServletRequest request,
								@PathParam("activityIdKey") long activityIdKey,
								@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			ActivityElementDao elementDao = new ActivityElementDao();
			PropertyFilter filter = PropertyFilter.eq("activityIdKey", activityIdKey);
			CM_PagingList<MW_Activity_Element> paging = elementDao.pagingList(request.getSession(), filter, -1, 0, null);
			if (paging != null && paging.getList() != null) {
				for (int ii = 0; ii < paging.getList().size(); ii++) {
					MW_Activity_Element item = paging.getList().get(ii);
//					fillupSubData(item);
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/elements")
	public Response insertOrUpdateElements(CM_PagingList<MW_Activity_Element> paging) throws Exception {
		try {
			if (AuthToken.isValidToken(paging.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (paging.getList() == null || paging.getList().size() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			ActivityElementDao elementDao = new ActivityElementDao();
			for (int ii = 0; ii < paging.getList().size(); ii++) {
				if (paging.getList().get(ii).getActivityIdKey() == 0) {
					return ResponseUtil.getResponse(Status.BAD_REQUEST);
				}
				if (paging.getList().get(ii).getIdString() == null || paging.getList().get(ii).getIdString().isEmpty()) {
					paging.getList().get(ii).setIdString(getNewElementId(elementDao, paging.getList()));
				}
			}
			
			elementDao.insertOrUpdate(paging.getList());
			WifiDao wifiDao = new WifiDao();
			LTEDao lteDao = new LTEDao();
			WelderDao welderDao = new WelderDao();
			for (int ii = 0; ii < paging.getList().size(); ii++) {
				MW_Activity_Element item = paging.getList().get(ii);
				if ("2".equals(item.getElementType())) { //WIFI
					MW_Wifi tempItem = wifiDao.select(null, item.getElementIdKey());
					if (tempItem != null) {
						tempItem.setCurrentActivityIdKey(item.getActivityIdKey());
					}
					wifiDao.update(tempItem);
				} else if ("3".equals(item.getElementType())) { //LTE
					MW_LTE tempItem = lteDao.select(null, item.getElementIdKey());
					if (tempItem != null) {
						tempItem.setCurrentActivityIdKey(item.getActivityIdKey());
					}
					lteDao.update(tempItem);
				} else if ("4".equals(item.getElementType())) { //WELDER
					MW_Welder tempItem = welderDao.select(null, item.getElementIdKey());
					if (tempItem != null) {
						tempItem.setCurrentActivityIdKey(item.getActivityIdKey());
					}
					welderDao.update(tempItem);
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/element/{idKey}")
	public Response deleteElement(@PathParam("idKey") long idKey,
								  @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			ActivityElementDao elementDao = new ActivityElementDao();
			MW_Activity_Element existOne = elementDao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			elementDao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<MW_Activity_Element>(MW_Activity_Element.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{activityIdKey}/invoices")
	public Response getInvoices(@Context HttpServletRequest request,
								@PathParam("activityIdKey") long activityIdKey,
								@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			InvoiceDao invoiceDao = new InvoiceDao();
			PropertyFilter filter = PropertyFilter.eq("activityIdKey", activityIdKey);
			CM_PagingList<MW_Invoice> paging = invoiceDao.pagingList(request.getSession(), filter, -1, 0, null);
			if (paging != null && paging.getList() != null) {
				for (int ii = 0; ii < paging.getList().size(); ii++) {
					MW_Invoice item = paging.getList().get(ii);
//					fillupSubData(item);
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
 