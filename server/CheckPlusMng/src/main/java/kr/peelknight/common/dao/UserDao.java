package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.common.model.CM_User;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class UserDao {
	public SqlSessionFactory factory =  null;
	
	public UserDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}
	
	public List<CM_User> selectUsers(int offset, int size) {
		return selectUsers(null, offset, size);
	}
	
	public List<CM_User> selectUsers(String userType, int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (userType != null) {
				param.put("userType", userType);
			}
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("kr.peelknight.mybatis.userMapper.selectUser", param);
		} finally {
			session.close();
		}
	}
	
	public CM_Paging selectUserPaging(String userType) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			if (userType != null) {
				param.put("userType", userType);
			}
			return session.selectOne("kr.peelknight.mybatis.userMapper.selectUserPaging", param);
		} finally {
			session.close();
		}
	}
	
	public CM_User selectUserByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("kr.peelknight.mybatis.userMapper.selectUser", param);
		} finally {
			session.close();
		}
	}
	
	public CM_User selectUserByUserId(String userId) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			return session.selectOne("kr.peelknight.mybatis.userMapper.selectUser", param);
		} finally {
			session.close();
		}
	}
	
	public void insertUser(CM_User user) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.peelknight.mybatis.userMapper.insertUser", user);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void updateUser(CM_User user) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.userMapper.updateUser", user);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void deleteUser(CM_User user) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.mybatis.userMapper.deleteUser", user);
			session.commit();
		} finally {
			session.close();
		}
	}
}
