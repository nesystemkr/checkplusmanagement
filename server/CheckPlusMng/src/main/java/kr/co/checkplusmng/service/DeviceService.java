package kr.co.checkplusmng.service;

import jakarta.ws.rs.Path;
import kr.co.checkplusmng.dao.DeviceDao;
import kr.co.checkplusmng.model.MW_Device;

@Path("/{version}/device")
public class DeviceService extends BaseService<MW_Device> {
	public DeviceService() {
		super(MW_Device.class, "DEV");
		_dao = new DeviceDao();
	}
	
	@Override
	protected void updateItem(MW_Device existOne, MW_Device newOne) {
		existOne.setModelName(newOne.getModelName());
		existOne.setModelType(newOne.getModelType());
		existOne.setWeldType(newOne.getWeldType());
		existOne.setCustomized(newOne.getCustomized());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
	}
}
