package kr.co.checkplusmng.dao;

import kr.co.checkplusmng.model.MW_Project;
import kr.nesystem.appengine.common.dao.BaseDao;

public class ProjectDao extends BaseDao<MW_Project> {
	public ProjectDao() {
		super(MW_Project.class);
	}
}
