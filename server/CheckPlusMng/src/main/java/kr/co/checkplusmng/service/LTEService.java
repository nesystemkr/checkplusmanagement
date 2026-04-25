package kr.co.checkplusmng.service;

import jakarta.ws.rs.Path;
import kr.co.checkplusmng.dao.LTEDao;
import kr.co.checkplusmng.model.MW_LTE;

@Path("/{version}/lte")
public class LTEService extends BaseService<MW_LTE> {
	public LTEService() {
		super(MW_LTE.class, "LTE");
		_dao = new LTEDao();
	}
	
	@Override
	protected void updateItem(MW_LTE existOne, MW_LTE newOne) {
		existOne.setIdString(newOne.getIdString());
		existOne.setModelName(newOne.getModelName());
		existOne.setSerialNo(newOne.getSerialNo());
		existOne.setUsimNo(newOne.getUsimNo());
		existOne.setTelephone(newOne.getTelephone());
		existOne.setGateId(newOne.getGateId());
		existOne.setGatePw(newOne.getGatePw());
		existOne.setWifiId(newOne.getWifiId());
		existOne.setWifiPw(newOne.getWifiPw());
		existOne.setRegistDate(newOne.getRegistDate());
		existOne.setStartDate(newOne.getStartDate());
		existOne.setEndDate(newOne.getEndDate());
		existOne.setContract(newOne.getContract());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
	}
}
