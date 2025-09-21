package kr.peelknight.push.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.mybatis.MySqlSessionFactory;
import kr.peelknight.push.model.PS_Msg;

public class PushDao {
	public SqlSessionFactory factory =  null;
	
	public PushDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}
	
	public void insertPushMessage(PS_Msg msg) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.pushmsgMapper.insertPushMsg", msg);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void updatePushMessage(PS_Msg msg) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.pushmsgMapper.updatePushMsg", msg);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public List<PS_Msg> selectPmsCandidatedMsg() {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param;
			param = new HashMap<String, Object>();
			param.put("pmsCandidatedMsg", "1");
			param.put("offset", 0);
			param.put("size", 100);
			return session.selectList("kr.peelknight.mybatis.pushmsgMapper.selectPushMsg", param);
		} finally {
			session.close();
		}
	}
	
	public void updateSendDate(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param;
			param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			param.put("pmsStatus", "1");
			//시도횟수 1증가
			session.update("kr.peelknight.mybatis.pushmsgMapper.updateSendDateInPushMsg", param);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void updatePushResult(long idKey, boolean isSuccess, String errorCode, String errorMessage) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param;
			param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			if (isSuccess == true) {
				param.put("completeDate", new Date());
				param.put("pmsStatus", "S");
			} else {
				param.put("pmsStatus", "E");
			}
			param.put("errorCode", errorCode);
			param.put("errorMessage", errorMessage);
			session.update("kr.peelknight.mybatis.pushmsgMapper.updateErrorDataInPushMsg", param);
			session.commit();
		} finally {
			session.close();
		}
	}
}
