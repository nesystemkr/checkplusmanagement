package kr.nesystem.appengine.common.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.EntityQuery.Builder;

import kr.nesystem.appengine.common.model.GAEModel;

public class BaseDeleteAllDao<T extends GAEModel> extends BaseDao<T> {
	public BaseDeleteAllDao(Class<T> clazz) {
		super(clazz);
	}
	
	public void deleteAll() throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			Builder builder = Query.newEntityQueryBuilder().setKind(tableName).setLimit(500);
			Query<Entity> query = builder.build();
			
			while (true) {
				QueryResults<Entity> results = datastore.run(query);
				List<Key> keysToDelete = new ArrayList<>();
				while (results.hasNext()) {
					keysToDelete.add(results.next().getKey());
				}
				if (keysToDelete.isEmpty()) {
					break;
				}
				for (Key key : keysToDelete) {
					txn.delete(key);
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
