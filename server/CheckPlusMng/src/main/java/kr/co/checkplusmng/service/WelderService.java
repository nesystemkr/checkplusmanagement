package kr.co.checkplusmng.service;

import jakarta.ws.rs.Path;
import kr.co.checkplusmng.dao.WelderDao;
import kr.co.checkplusmng.model.MW_Welder;

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
