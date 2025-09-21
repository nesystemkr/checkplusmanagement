package kr.peelknight.socket.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.mybatis.MySqlSessionFactory;
import kr.peelknight.socket.model.SH_SocketLog;

public class SocketLogDao {
	private static Map<String, String> __mapSocketLogCreated = new HashMap<>(); 
	public SqlSessionFactory factory =  null;

	public SocketLogDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public void createBaseDateTable(SqlSession session, String baseDate) {
		if (null != __mapSocketLogCreated.get(baseDate)) {
			return;
		}
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("baseDate", baseDate);
			session.update("kr.peelknight.mybatis.socketlogMapper.createSHSocketLog", param);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			__mapSocketLogCreated.put(baseDate, baseDate);
		}
	}
	
	public List<SH_SocketLog> selectSocketLogs(String baseDate, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("baseDate", baseDate);
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.mybatis.socketlogMapper.selectSocketLog", param);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<SH_SocketLog>();
		} finally {
			session.close();
		}
	}

	public CM_Paging selectSocketLogPaging(String baseDate) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("baseDate", baseDate);
			return session.selectOne("kr.peelknight.mybatis.socketlogMapper.selectSocketLogPaging", param);
		} catch (Exception e) {
			e.printStackTrace();
			return new CM_Paging();
		} finally {
			session.close();
		}
	}

	public SH_SocketLog selectSocketLogByIdKey(String baseDate, long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("baseDate", baseDate);
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.mybatis.socketlogMapper.selectSocketLog", param);
		} finally {
			session.close();
		}
	}

	public void insertSocketLog(SH_SocketLog socketLog) {
		SqlSession session = factory.openSession();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String baseDate = sdf.format(new Date());
			createBaseDateTable(session, baseDate);
			socketLog.setBaseDate(baseDate);
			socketLog.setIdKey(0);
			socketLog.setLogDate(new Date());
			session.insert("kr.peelknight.mybatis.socketlogMapper.insertSocketLog", socketLog);
			session.commit();
		} finally {
			session.close();
		}
	}
}
