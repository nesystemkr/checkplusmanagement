package kr.nesystem.appengine.push.dao;

import kr.nesystem.appengine.common.dao.BaseDao;
import kr.nesystem.appengine.push.model.PS_Msg;

public class PushDao extends BaseDao<PS_Msg> {
	public PushDao() {
		super(PS_Msg.class);
	}
//	
//	public void insertPushMessage(PS_Msg msg) {
//		SqlSession session = factory.openSession();
//		try {
//			session.insert("kr.peelknight.mybatis.pushmsgMapper.insertPushMsg", msg);
//			session.commit();
//		} finally {
//			session.close();
//		}
//	}
//	
//	public void updatePushMessage(PS_Msg msg) {
//		SqlSession session = factory.openSession();
//		try {
//			session.update("kr.peelknight.mybatis.pushmsgMapper.updatePushMsg", msg);
//			session.commit();
//		} finally {
//			session.close();
//		}
//	}
//	
//	public List<PS_Msg> selectPmsCandidatedMsg() {
//		SqlSession session = factory.openSession();
//		try {
//			HashMap<String, Object> param;
//			param = new HashMap<String, Object>();
//			param.put("pmsCandidatedMsg", "1");
//			param.put("offset", 0);
//			param.put("size", 100);
//			return session.selectList("kr.peelknight.mybatis.pushmsgMapper.selectPushMsg", param);
//		} finally {
//			session.close();
//		}
//	}
//	
//	public void updateSendDate(long idKey) {
//		SqlSession session = factory.openSession();
//		try {
//			HashMap<String, Object> param;
//			param = new HashMap<String, Object>();
//			param.put("idKey", idKey);
//			param.put("pmsStatus", "1");
//			//시도횟수 1증가
//			session.update("kr.peelknight.mybatis.pushmsgMapper.updateSendDateInPushMsg", param);
//			session.commit();
//		} finally {
//			session.close();
//		}
//	}
//	
//	public void updatePushResult(long idKey, boolean isSuccess, String errorCode, String errorMessage) {
//		SqlSession session = factory.openSession();
//		try {
//			HashMap<String, Object> param;
//			param = new HashMap<String, Object>();
//			param.put("idKey", idKey);
//			if (isSuccess == true) {
//				param.put("completeDate", new Date());
//				param.put("pmsStatus", "S");
//			} else {
//				param.put("pmsStatus", "E");
//			}
//			param.put("errorCode", errorCode);
//			param.put("errorMessage", errorMessage);
//			session.update("kr.peelknight.mybatis.pushmsgMapper.updateErrorDataInPushMsg", param);
//			session.commit();
//		} finally {
//			session.close();
//		}
//	}
}
