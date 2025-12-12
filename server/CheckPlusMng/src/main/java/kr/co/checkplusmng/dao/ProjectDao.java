package kr.co.checkplusmng.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.co.checkplusmng.model.MW_Project;
import kr.nesystem.appengine.common.dao.BaseDao;

public class ProjectDao extends BaseDao<MW_Project> {
	public ProjectDao() {
		super(MW_Project.class);
	}
	
	public MW_Project selectByProjectId(HttpSession session, String projectId) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("projectId", projectId);
		List<MW_Project> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
