package kr.peelknight.fragment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.fragment.model.CM_GridColModel;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class GridColModelDao {
	SqlSessionFactory factory =  null;

	public GridColModelDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_GridColModel> selectGridColModels(long gridIdKey, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (gridIdKey > 0) {
				param.put("gridIdKey", gridIdKey);
			}
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.fragment.mybatis.gridcolmodelMapper.selectGridColModel", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectGridColModelPaging(long gridIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (gridIdKey > 0) {
				param.put("gridIdKey", gridIdKey);
			}
			return session.selectOne("kr.peelknight.fragment.mybatis.gridcolmodelMapper.selectGridColModelPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_GridColModel selectGridColModelByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.fragment.mybatis.gridcolmodelMapper.selectGridColModel", param);
		} finally {
			session.close();
		}
	}

	public void insertGridColModel(CM_GridColModel gridColModel) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.fragment.mybatis.gridcolmodelMapper.insertGridColModel", gridColModel);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateGridColModel(CM_GridColModel gridColModel) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.fragment.mybatis.gridcolmodelMapper.updateGridColModel", gridColModel);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteGridColModel(CM_GridColModel gridColModel) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.fragment.mybatis.gridcolmodelMapper.deleteGridColModel", gridColModel);
			session.commit();
		} finally {
			session.close();
		}
	}
}
