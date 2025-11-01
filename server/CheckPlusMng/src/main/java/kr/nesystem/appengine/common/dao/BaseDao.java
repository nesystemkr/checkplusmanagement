package kr.nesystem.appengine.common.dao;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.EntityQuery.Builder;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.GAEModel;
import kr.peelknight.common.model.CM_Paging;

public class BaseDao<T extends GAEModel> {
	protected Datastore datastore;
	protected KeyFactory keyFactory;
	protected String tableName;
	protected Class<T> clazz;
	public BaseDao(Class<T> clazz) {
		try {
			this.datastore = DatastoreOptions.newBuilder()
					.setProjectId("checkplusmng")
					.setCredentials(GoogleCredentials.getApplicationDefault())
					.build()
					.getService();
//			DatastoreOptions options = DatastoreOptions.newBuilder().setProjectId("checkplusmng").build();
//			this.datastore = options.getService();
			this.tableName = clazz.getSimpleName();
			this.keyFactory = datastore.newKeyFactory().setKind(this.tableName);
			this.clazz = clazz;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CM_PagingList<T> pagingList(PropertyFilter filter,int offset, int size) throws Exception {
		List<T> list = list(filter, offset, size);
		CM_PagingList<T> ret = new CM_PagingList<>();
		ret.setList(list);
		CM_Paging paging = new CM_Paging();
		paging.setTotalCount(list.size());
		ret.setPaging(paging);
		return ret;
	}
	
	public List<T> list(PropertyFilter filter,int offset, int size) throws Exception {
		List<T> ret = new ArrayList<>();
		Builder builder = Query.newEntityQueryBuilder().setKind(tableName);
		if (filter != null) {
			builder.setFilter(filter);
		}
		if (size != 0 && offset >= 0) {
			builder.setLimit(size).setOffset(offset);
		}
		Query<Entity> query = builder.build();
		QueryResults<Entity> results = datastore.run(query);
		while (results.hasNext()) {
			Entity entity = results.next();
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			T model = constructor.newInstance();
			model.fromEntity(entity);
			ret.add(model);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public T select(String val) throws Exception {
		Key key = keyFactory.newKey(val);
		Entity entity = datastore.get(key);
		if (entity != null) {
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			GAEModel model = (GAEModel)constructor.newInstance();
			return (T)model.fromEntity(entity);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public T select(long val) throws Exception {
		Key key = keyFactory.newKey(val);
		Entity entity = datastore.get(key);
		if (entity != null) {
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			GAEModel model = (GAEModel)constructor.newInstance();
			return (T)model.fromEntity(entity);
		}
		return null;
	}
	
	public Entity insert(GAEModel model) throws Exception {
		Transaction txn = datastore.newTransaction();
		Entity ret = null;
		try {
			if (model.hasIdKey() == false) {
				Key key = model.toKey(keyFactory);
				if (txn.get(key) != null) {
					throw new Exception("Already exists: " + model.key());
				}
				ret = txn.add(model.toEntity(keyFactory));
			} else {
				ret = txn.add(model.toEntityAutoInc(keyFactory));
			}
			txn.commit();
			return ret; 
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}
	
	public void update(GAEModel model) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			Key key = model.toKey(keyFactory);
			Entity existOne = txn.get(key);
			if (existOne == null) {
				throw new Exception("Not found: " + model.key());
			}
			txn.update(model.toEntity(existOne));
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}
	
	public void delete(GAEModel model) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			Entity existOne = txn.get(model.toKey(keyFactory));
			if (existOne == null) {
				throw new Exception("Not found: " + model.key());
			}
			txn.delete(existOne.getKey());
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}

	
	public void delete(List<T> list) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			for (int ii = 0; ii < list.size(); ii++) {
				T model = list.get(ii);
				Entity existOne = txn.get(model.toKey(keyFactory));
				if (existOne == null) {
					throw new Exception("Not found: " + model.key());
				}
				txn.delete(existOne.getKey());
			}
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}
}
