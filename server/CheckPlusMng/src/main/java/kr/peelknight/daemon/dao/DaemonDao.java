package kr.peelknight.daemon.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.daemon.model.CM_Daemon;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class DaemonDao {
	SqlSessionFactory factory =  null;

	public DaemonDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public List<CM_Daemon> selectDaemons(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.daemon.mybatis.daemonMapper.selectDaemon", param);
		} finally {
			session.close();
		}
	}

	public CM_Paging selectDaemonPaging() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			return session.selectOne("kr.peelknight.daemon.mybatis.daemonMapper.selectDaemonPaging", param);
		} finally {
			session.close();
		}
	}

	public CM_Daemon selectDaemonByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.daemon.mybatis.daemonMapper.selectDaemon", param);
		} finally {
			session.close();
		}
	}

	public void insertDaemon(CM_Daemon daemon) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.daemon.mybatis.daemonMapper.insertDaemon", daemon);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateDaemon(CM_Daemon daemon) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.daemon.mybatis.daemonMapper.updateDaemon", daemon);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteDaemon(CM_Daemon daemon) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.daemon.mybatis.daemonMapper.deleteDaemon", daemon);
			session.commit();
		} finally {
			session.close();
		}
	}
}
