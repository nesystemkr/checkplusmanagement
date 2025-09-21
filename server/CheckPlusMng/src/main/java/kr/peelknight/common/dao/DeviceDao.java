package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Device;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class DeviceDao {
	public SqlSessionFactory factory =  null;
	
	public DeviceDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}
	
	public void manageDeviceInfo(long userIdKey, String uniqueId, String pushKey, String deviceType) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param;
			
			param = new HashMap<String, Object>();
			param.put("uniqueId", uniqueId);
			param.put("userIdKey", userIdKey);
			param.put("pushKey", pushKey);
			session.delete("kr.peelknight.mybatis.deviceMapper.deleteOtherDevicesByUniqueId", param);
			session.delete("kr.peelknight.mybatis.deviceMapper.deleteOtherDevicesByPushKey", param);
			List<CM_Device> devices = session.selectList("kr.peelknight.mybatis.deviceMapper.selectDeviceByUniqueIdAndUserId", param);
			CM_Device device = null;
			if (devices != null && devices.size() > 0) {
				device = devices.get(0);
			}
			if (device != null) {
				boolean isUpdatePushKey = false;
				if (device.getPushKey() != null) {
					if (!device.getPushKey().equals(pushKey)) {
						isUpdatePushKey = true;
					}
				} else {
					if (pushKey != null) {
						isUpdatePushKey = true;
					}
				}
				if (isUpdatePushKey == true) {
					device.setPushKey(pushKey);
					session.update("kr.peelknight.mybatis.deviceMapper.updateDevicePushKeyWithUniqueId", device);
				}
			} else {
				devices = session.selectList("kr.peelknight.mybatis.deviceMapper.selectDeviceByPushKeyAndUserId", param);
				if (devices != null && devices.size() > 0) {
					device = devices.get(0);
				}
				if (device != null) {
					if (!device.getUniqueId().equals(uniqueId)) {
						device.setUniqueId(uniqueId);
						session.update("kr.peelknight.mybatis.deviceMapper.updateDeviceUniqueIdWithPushKey", device);
					}
				} else {
					device = new CM_Device();
					device.setUserIdKey(userIdKey);
					device.setUniqueId(uniqueId);
					device.setPushKey(pushKey);
					device.setDeviceType(deviceType);
					session.update("kr.peelknight.mybatis.deviceMapper.insertDevice", device);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public List<CM_Device> selectDevicesByUserIdKey(long userIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param;
			param = new HashMap<String, Object>();
			param.put("userIdKey", userIdKey);
			return session.selectList("kr.peelknight.mybatis.deviceMapper.selectDeviceByUserIdKey", param);
		} finally {
			session.close();
		}
	}
	
	public void deleteDeviceInfo(long userIdKey, String uniqueId, String deviceType) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param;
			
			param = new HashMap<String, Object>();
			param.put("uniqueId", uniqueId);
			param.put("userIdKey", userIdKey);
			session.delete("kr.peelknight.mybatis.deviceMapper.deleteDevicesByUniqueId", param);
			session.commit();
		} finally {
			session.close();
		}
	}
}
