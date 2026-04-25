package kr.co.checkplusmng.service;

import jakarta.ws.rs.Path;
import kr.co.checkplusmng.dao.WifiDao;
import kr.co.checkplusmng.model.MW_Wifi;

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
