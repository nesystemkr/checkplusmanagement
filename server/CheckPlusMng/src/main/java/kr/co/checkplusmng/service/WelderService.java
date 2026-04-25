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
import kr.co.checkplusmng.dao.WelderDao;
import kr.co.checkplusmng.dao.WifiDao;
import kr.co.checkplusmng.model.MW_Welder;
import kr.co.checkplusmng.model.MW_Wifi;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/welder")
public class WelderService extends BaseService<MW_Welder> {
	public WelderService() {
		super(MW_Welder.class, "");
		_dao = new WelderDao();
	}
	
	@Override
	protected void updateItem(MW_Welder existOne, MW_Welder newOne) {
		existOne.setModelName(newOne.getModelName());
		existOne.setWeldType(newOne.getWeldType());
		existOne.setCustomized(newOne.getCustomized());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
	}
}
