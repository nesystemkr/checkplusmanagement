package kr.peelknight.fragment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.fragment.model.CM_GridColButton;
import kr.peelknight.fragment.model.CM_GridColModel;
import kr.peelknight.fragment.model.CM_GridFragment;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class GridFragmentDao {
	SqlSessionFactory factory =  null;

	public GridFragmentDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_GridFragment> selectGridFragments(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.fragment.mybatis.gridfragmentMapper.selectGridFragment", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectGridFragmentPaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.fragment.mybatis.gridfragmentMapper.selectGridFragmentPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_GridFragment selectGridFragmentByIdKey(long fragmentIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("fragmentIdKey", fragmentIdKey);
			return session.selectOne("kr.peelknight.fragment.mybatis.gridfragmentMapper.selectGridFragment", param);
		} finally {
			session.close();
		}
	}

	public void updateGridFragment(CM_GridFragment gridFragment) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.fragment.mybatis.gridfragmentMapper.updateGridFragment", gridFragment);
			if (gridFragment.getColModel() != null && gridFragment.getColModel().size() > 0) {
				CM_GridColModel colModel;
				for (int ii=0; ii<gridFragment.getColModel().size(); ii++) {
					colModel = gridFragment.getColModel().get(ii);
					colModel.setGridIdKey(gridFragment.getFragmentIdKey());
					if ("I".equals(colModel.getTouch())) {
						session.insert("kr.peelknight.fragment.mybatis.gridcolmodelMapper.insertGridColModel", colModel);
					} else if ("U".equals(colModel.getTouch())) {
						session.update("kr.peelknight.fragment.mybatis.gridcolmodelMapper.updateGridColModel", colModel);
					} else if ("D".equals(colModel.getTouch())) {
						session.delete("kr.peelknight.fragment.mybatis.gridcolmodelMapper.deleteGridColModel", colModel);
						session.delete("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.deleteGridColButtons", colModel);
					}
					if (colModel.getButtons() != null && colModel.getButtons().size() > 0) {
						CM_GridColButton button;
						for (int jj=0; jj<colModel.getButtons().size(); jj++) {
							button = colModel.getButtons().get(jj);
							button.setGridIdKey(gridFragment.getFragmentIdKey());
							button.setColIdKey(colModel.getIdKey());
							if ("I".equals(button.getTouch())) {
								session.insert("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.insertGridColButton", button);
							} else if ("U".equals(button.getTouch())) {
								session.update("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.updateGridColButton", button);
							} else if ("D".equals(button.getTouch())) {
								session.delete("kr.peelknight.fragment.mybatis.gridcolbuttonMapper.deleteGridColButton", button);
							}
						}
					}
				}
			}
			session.delete("kr.peelknight.fragment.mybatis.fragmentcachedjsonMapper.deleteFragmentCachedJsonByGrid", gridFragment);
			session.commit();
		} finally {
			session.close();
		}
	}
}
