package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.common.model.CM_Version;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class VersionDao {
	public SqlSessionFactory factory =  null;
	
	public VersionDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}
	
	public void insertVersion(CM_Version version) {
		SqlSession session = factory.openSession();
		try {
			session.insert("kr.co.lucidcore.mybatis.versionMapper.insertVersion", version);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void updateVersion(CM_Version version) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.co.lucidcore.mybatis.versionMapper.updateVersion", version);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void deleteVersion(CM_Version version) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.co.lucidcore.mybatis.versionMapper.deleteVersion", version);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public CM_Version selectVersionByIdKey(long idKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("versionIdKey", idKey);
			CM_Version item = session.selectOne("kr.co.lucidcore.mybatis.versionMapper.selectVersion", param);
			return item;
		} finally {
			session.close();
		}
	}
	
	public CM_Version selectVersionByVersion(String version) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("version", version);
			CM_Version item = session.selectOne("kr.co.lucidcore.mybatis.versionMapper.selectVersion", param);
			return item;
		} finally {
			session.close();
		}
	}
	
	public List<CM_Version> selectVersions(int offset, int size) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("offset", offset);
			param.put("size", size);
			List<CM_Version> list = session.selectList("kr.co.lucidcore.mybatis.versionMapper.selectVersion", param);
			return list;
		} finally {
			session.close();
		}
	}
	
	public CM_Paging selectVersionPaging() {
		SqlSession session = factory.openSession();
		try {
			return session.selectOne("kr.co.lucidcore.mybatis.versionMapper.selectVersionPaging");
		} finally {
			session.close();
		}
	}
}
