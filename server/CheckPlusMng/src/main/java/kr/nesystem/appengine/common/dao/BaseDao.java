package kr.nesystem.appengine.common.dao;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.Filter;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.Transaction;

import jakarta.servlet.http.HttpSession;

import com.google.cloud.datastore.EntityQuery.Builder;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.GAEAutoIncModel;
import kr.nesystem.appengine.common.model.GAEModel;

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
	
	public CM_PagingList<T> pagingList(HttpSession session, Filter filter, int offset, int size) throws Exception {
		return pagingList(session, filter, offset, size, null);
	}
	public CM_PagingList<T> pagingList(HttpSession session, Filter filter, int offset, int size, String sortField) throws Exception {
		List<T> list = list(session, filter, offset, size, sortField);
		CM_PagingList<T> ret = new CM_PagingList<>();
		ret.setList(list);
		ret.getPaging().setTotalCount(totalCount(filter));
		ret.numbering(offset);
		return ret;
	}
	
	public List<T> list(HttpSession session, Filter filter, int offset, int size) throws Exception {
		return list(session, filter, offset, size, null);
	}
	
	public List<T> list(HttpSession session, Filter filter, int offset, int size, String sortField) throws Exception {
		List<T> ret = new ArrayList<>();
		Builder builder = Query.newEntityQueryBuilder().setKind(tableName);
		if (filter != null) {
			builder.setFilter(filter);
		}
		if (size != 0 && offset >= 0) {
			builder.setLimit(size).setOffset(offset);
		}
		if (sortField != null) {
			builder.addOrderBy(OrderBy.asc(sortField));
		}
		Query<Entity> query = builder.build();
		QueryResults<Entity> results = datastore.run(query);
		while (results.hasNext()) {
			Entity entity = results.next();
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			T model = constructor.newInstance();
			model.fromEntity(entity);
			model.l10n(session);
			ret.add(model);
		}
		return ret;
	}
	
	public int totalCount(Filter filter) throws Exception {
		int ret = 0;
		Builder builder = Query.newEntityQueryBuilder().setKind(tableName);
		if (filter != null) {
			builder.setFilter(filter);
		}
		Query<Entity> query = builder.build();
		QueryResults<Entity> results = datastore.run(query);
		while (results.hasNext()) {
			results.next();
			ret++;
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public T select(HttpSession session, String val) throws Exception {
		Entity entity = datastore.get(keyFactory.newKey(val));
		if (entity != null) {
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			GAEModel model = (GAEModel)constructor.newInstance();
			T ret = (T)model.fromEntity(entity);
			ret.l10n(session);
			return ret;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public T select(HttpSession session, long val) throws Exception {
		Entity entity = datastore.get(keyFactory.newKey(val));
		if (entity != null) {
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			GAEModel model = (GAEModel)constructor.newInstance();
			T ret = (T)model.fromEntity(entity);
			ret.l10n(session);
			return ret;
		}
		return null;
	}
	
	public Entity insert(GAEModel model) throws Exception {
		Transaction txn = datastore.newTransaction();
		Entity ret = null;
		try {
			if (!(model instanceof GAEAutoIncModel)) {
				if (txn.get(model.toKey(keyFactory)) != null) {
					throw new Exception("Already exists: " + model.key());
				}
			}
			ret = txn.add(model.toEntity(keyFactory));
			txn.commit();
			return ret; 
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}
	
	public List<Entity> insert(List<T> list) throws Exception {
		Transaction txn = datastore.newTransaction();
		List<Entity> ret = new ArrayList<>();
		try {
			for (int ii = 0; ii < list.size(); ii++) {
				T model = list.get(ii);
				if (!(model instanceof GAEAutoIncModel)) {
					Entity existOne = txn.get(model.toKey(keyFactory));
					if (existOne == null) {
						throw new Exception("Not found: " + model.key());
					}
				}
				ret.add(txn.add(model.toEntity(keyFactory)));
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
			Entity existOne = txn.get(model.toKey(keyFactory));
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
	
	public void update(List<T> list) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			for (int ii = 0; ii < list.size(); ii++) {
				T model = list.get(ii);
				Entity existOne = txn.get(model.toKey(keyFactory));
				if (existOne == null) {
					throw new Exception("Not found: " + model.key());
				}
				txn.update(model.toEntity(existOne));
			}
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}
	
	public void insertOrUpdate(List<T> list) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			for (int ii = 0; ii < list.size(); ii++) {
				T model = list.get(ii);
				Entity existOne = null;
				if (model instanceof GAEAutoIncModel) {
					if (((GAEAutoIncModel) model).getIdKey() != 0) {
						Key key = model.toKey(keyFactory);
						existOne = txn.get(key);
					}
				} else {
					Key key = model.toKey(keyFactory);
					existOne = txn.get(key);
				}
				if (existOne != null) {
					txn.update(model.toEntity(existOne));
				} else {
					txn.add(model.toEntity(keyFactory));
				}
			}
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
