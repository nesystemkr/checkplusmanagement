package kr.co.checkplusmng.service;

import jakarta.ws.rs.Path;
import kr.co.checkplusmng.dao.ProjectDao;
import kr.co.checkplusmng.model.MW_Project;
import kr.co.checkplusmng.util.ProjectStore;

@Path("/{version}/project")
public class ProjectService extends BaseService<MW_Project> {
	public ProjectService() {
		super(MW_Project.class, "PRO");
		_dao = new ProjectDao();
	}
	
	@Override
	protected void updateItem(MW_Project existOne, MW_Project newOne) {
		existOne.setName(newOne.getName());
		existOne.setCustomerIdKey(newOne.getCustomerIdKey());
		existOne.setBrokerIdKey(newOne.getBrokerIdKey());
		existOne.setContractDate(newOne.getContractDate());
		existOne.setMemo(newOne.getMemo());
		existOne.setOrderSeq(newOne.getOrderSeq());
	}
	
	@Override
	protected void fillupSubData(MW_Project item) {
		item.setCustomerName(ProjectStore.getName(item.getCustomerIdKey()));
		item.setBrokerName(ProjectStore.getName(item.getBrokerIdKey()));
	}
}
