package kr.peelknight.generate.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.generate.model.GN_GenColInfo;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class GenerateDao {
	SqlSessionFactory factory =  null;
	
	public GenerateDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}
	
	@SuppressWarnings("rawtypes")
	public List<GN_GenColInfo> getColInfos(String databaseName, String tableName) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("tableName", tableName);
			Connection conn = session.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + databaseName + "." + tableName + " LIMIT 0, 1");
			org.mariadb.jdbc.client.result.ResultSetMetaData rsmd = (org.mariadb.jdbc.client.result.ResultSetMetaData)rs.getMetaData();
			Class[] cArg = new Class[1];
			cArg[0] = int.class;
			Method method = rsmd.getClass().getDeclaredMethod("getColumnInformation", cArg);
			method.setAccessible(true);
			List<GN_GenColInfo> list = new ArrayList<>();
			boolean isPrimaryKey;
			for (int ii=1; ii<=rsmd.getColumnCount(); ii++) {
				Object ci = method.invoke(rsmd, ii);
				Method method2 = ci.getClass().getDeclaredMethod("isPrimaryKey");
				Object flag = method2.invoke(ci);
				isPrimaryKey = (boolean)flag;
				
				if (Types.VARCHAR == rsmd.getColumnType(ii) ||
					Types.CHAR == rsmd.getColumnType(ii) ||
					Types.LONGVARCHAR == rsmd.getColumnType(ii)) {
					list.add(new GN_GenColInfo(rsmd.getColumnName(ii), "String", isPrimaryKey));
				} else if (Types.DATE == rsmd.getColumnType(ii) ||
						Types.TIME == rsmd.getColumnType(ii) ||
						Types.TIMESTAMP == rsmd.getColumnType(ii)) {
					list.add(new GN_GenColInfo(rsmd.getColumnName(ii), "Date", isPrimaryKey));
				} else if (Types.BIGINT == rsmd.getColumnType(ii)) {
					list.add(new GN_GenColInfo(rsmd.getColumnName(ii), "long", isPrimaryKey));
				} else if (Types.BIT == rsmd.getColumnType(ii) ||
						Types.TINYINT == rsmd.getColumnType(ii) ||
						Types.NUMERIC == rsmd.getColumnType(ii) ||
						Types.DECIMAL == rsmd.getColumnType(ii) ||
						Types.INTEGER == rsmd.getColumnType(ii) ||
						Types.SMALLINT == rsmd.getColumnType(ii)) {
					list.add(new GN_GenColInfo(rsmd.getColumnName(ii), "int", isPrimaryKey));
				} else if (Types.FLOAT == rsmd.getColumnType(ii) ||
						Types.REAL == rsmd.getColumnType(ii) ||
						Types.DOUBLE == rsmd.getColumnType(ii)) {
					list.add(new GN_GenColInfo(rsmd.getColumnName(ii), "double", isPrimaryKey));
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}
}
