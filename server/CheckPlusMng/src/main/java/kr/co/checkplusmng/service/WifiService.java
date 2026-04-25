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
import kr.co.checkplusmng.dao.WifiDao;
import kr.co.checkplusmng.model.MW_LTE;
import kr.co.checkplusmng.model.MW_Wifi;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/wifi")
public class WifiService extends BaseService<MW_Wifi> {
	public WifiService() {
		super(MW_Wifi.class, "APW");
		_dao = new WifiDao();
	}
	
	@Override
	protected void updateItem(MW_Wifi existOne, MW_Wifi newOne) {
		existOne.setModelName(newOne.getModelName());
		existOne.setSerialNo(newOne.getSerialNo());
		existOne.setMacAddress(newOne.getMacAddress());
		existOne.setGateId(newOne.getGateId());
		existOne.setGatePw(newOne.getGatePw());
		existOne.setWifiId(newOne.getWifiId());
		existOne.setWifiPw(newOne.getWifiPw());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
	}
}
