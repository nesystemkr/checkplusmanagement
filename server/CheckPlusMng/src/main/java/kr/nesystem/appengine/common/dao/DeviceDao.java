package kr.nesystem.appengine.common.dao;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.EntityQuery.Builder;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_Device;

public class DeviceDao  extends BaseDao<CM_Device> {
	public DeviceDao() {
		super(CM_Device.class);
	}
	
	public void manageDeviceInfo(long userIdKey, String uniqueId, String pushKey, String deviceType) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			Builder builder;
			Query<Entity> query;
			QueryResults<Entity> results;
			
			builder = Query.newEntityQueryBuilder();
			builder.setKind(tableName);
			builder.setFilter(PropertyFilter.eq("uniqueId", uniqueId));
			query = builder.build();
			results = datastore.run(query);
			while (results.hasNext()) {
				Entity entity = results.next();
				if (entity.getLong("userIdKey") != userIdKey) {
					txn.delete(entity.getKey());
				}
			}

			builder = Query.newEntityQueryBuilder();
			builder.setKind(tableName);
			builder.setFilter(PropertyFilter.eq("pushKey", pushKey));
			query = builder.build();
			results = datastore.run(query);
			while (results.hasNext()) {
				Entity entity = results.next();
				if (entity.getLong("userIdKey") != userIdKey) {
					txn.delete(entity.getKey());
				}
			}

			builder = Query.newEntityQueryBuilder();
			builder.setKind(tableName);
			builder.setFilter(PropertyFilter.eq("userIdKey", userIdKey));
			query = builder.build();
			results = datastore.run(query);
			while (results.hasNext()) {
				Entity entity = results.next();
				if (uniqueId != null && uniqueId.equals(entity.getString("uniqueId")) &&
					pushKey != null && !pushKey.equals(entity.getString("pushKey"))) {
					txn.update(Entity.newBuilder(entity)
							.set("pushKey", pushKey)
							.build());
				} else if (uniqueId != null && !uniqueId.equals(entity.getString("uniqueId")) &&
						   pushKey != null && pushKey.equals(entity.getString("pushKey"))) {
					txn.update(Entity.newBuilder(entity)
							.set("uniqueId", uniqueId)
							.build());
				} else {
					CM_Device device = new CM_Device();
					device.setUserIdKey(userIdKey);
					device.setUniqueId(uniqueId);
					device.setPushKey(pushKey);
					txn.add( device.toEntityAutoInc(keyFactory));
				}
			}
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}
	
	public void deleteDeviceInfo(long userIdKey, String uniqueId, String deviceType) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			Builder builder;
			Query<Entity> query;
			QueryResults<Entity> results;
			
			builder = Query.newEntityQueryBuilder();
			builder.setKind(tableName);
			builder.setFilter(PropertyFilter.eq("userIdKey", userIdKey));
			query = builder.build();
			results = datastore.run(query);
			while (results.hasNext()) {
				Entity entity = results.next();
				if (uniqueId != null && uniqueId.equals(entity.getString("uniqueId"))) {
					txn.delete(entity.getKey());
				}
			}
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}
}