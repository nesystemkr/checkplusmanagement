package kr.nesystem.checkplusmng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.nesystem.checkplusmng.model.CM_User;
import kr.nesystem.model.CM_Paging;
import kr.nesystem.util.MySqlSessionFactory;

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
			Map<String, Object> param = new HashMap<String, Object>();
			if (userType != null) {
				param.put("userType", userType);
			}
			if (offset >= 0) {
				param.put("offset", offset);
				param.put("size", size);
			}
			return session.selectList("checkplusmng.userMapper.selectUser", param);
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
			return session.selectOne("checkplusmng.userMapper.selectUserPaging", param);
		} finally {
			session.close();
		}
	}
	
	public CM_User selectUserByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", idKey);
			return session.selectOne("checkplusmng.userMapper.selectUser", param);
		} finally {
			session.close();
		}
	}
	
	public CM_User selectUserByUserId(String userId) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			return session.selectOne("checkplusmng.userMapper.selectUser", param);
		} finally {
			session.close();
		}
	}
	
	public void insertUser(CM_User user) {
		SqlSession session = factory.openSession();
		try {
			session.insert("checkplusmng.userMapper.insertUser", user);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void updateUser(CM_User user) {
		SqlSession session = factory.openSession();
		try {
			session.update("checkplusmng.userMapper.updateUser", user);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void deleteUser(CM_User user) {
		SqlSession session = factory.openSession();
		try {
			session.delete("checkplusmng.userMapper.deleteUser", user);
			session.commit();
		} finally {
			session.close();
		}
	}
}
