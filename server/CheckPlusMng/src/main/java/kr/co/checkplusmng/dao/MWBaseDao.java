package kr.co.checkplusmng.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.StructuredQuery.Filter;
import jakarta.servlet.http.HttpSession;
import kr.co.checkplusmng.model.MW_BaseModel;
import kr.nesystem.appengine.common.dao.BaseDao;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.GAEModel;

public class MWBaseDao <T extends MW_BaseModel> extends BaseDao<T> {
	public MWBaseDao(Class<T> clazz) {
		super(clazz);
	}
	
	@Override
	public CM_PagingList<T> pagingList(HttpSession session, Filter filter) throws Exception {
		return pagingList(session, filter, -1, 0, null, null, null);
	}
	
	@Override
	public CM_PagingList<T> pagingList(HttpSession session, Filter filter, int offset, int size) throws Exception {
		return pagingList(session, filter, offset, size, null, null, null);
	}
	
	@Override
	public CM_PagingList<T> pagingList(HttpSession session, Filter filter, int offset, int size, String search) throws Exception {
		return pagingList(session, filter, offset, size, search, null, null);
	}
	
	@Override
	public CM_PagingList<T> pagingList(HttpSession session, Filter filter, int offset, int size, String search, String sortField, String sortOrder) throws Exception {
		if (sortField == null) {
			sortField = "orderSeq";
		}
		if (sortOrder == null) {
			sortOrder = "asc";
		}
		return super.pagingList(session, filter, offset, size, search, sortField, sortOrder);
	}
	
	@Override
	public List<T> list(HttpSession session, Filter filter) throws Exception {
		return list(session, filter, -1, 0, null, null, null);
	}
	
	@Override
	public List<T> list(HttpSession session, Filter filter, int offset, int size) throws Exception {
		return list(session, filter, offset, size, null, null, null);
	}
	
	@Override
	public List<T> list(HttpSession session, Filter filter, int offset, int size, String search) throws Exception {
		return list(session, filter, offset, size, search, null, null);
	}
	
	public List<T> list(HttpSession session, Filter filter, int offset, int size, String search, String sortField, String sortOrder) throws Exception {
		if (sortField == null) {
			sortField = "orderSeq";
		}
		if (sortOrder == null) {
			sortOrder = "asc";
		}
		return super.list(session, filter, offset, size, search, sortField, sortOrder);
	}
	
	@Override
	public Entity insert(GAEModel model) throws Exception {
		Transaction txn = datastore.newTransaction();
		Entity ret = null;
		try {
			if (!(model instanceof MW_BaseModel)) {
				if (txn.get(model.toKey(keyFactory)) != null) {
					throw new Exception("Already exists: " + model.key());
				}
			}
			if (((MW_BaseModel)model).getOrderSeq() == 0) {
				((MW_BaseModel)model).setOrderSeq(getNextOrderSeq());
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
	
	@Override
	public List<Entity> insert(List<T> list) throws Exception {
		Transaction txn = datastore.newTransaction();
		List<Entity> ret = new ArrayList<>();
		try {
			long nextOrderSeq = getNextOrderSeq();
			for (int ii = 0; ii < list.size(); ii++) {
				T model = list.get(ii);
				if (!(model instanceof MW_BaseModel)) {
					Entity existOne = txn.get(model.toKey(keyFactory));
					if (existOne == null) {
						throw new Exception("Not found: " + model.key());
					}
				}
				if (((MW_BaseModel)model).getOrderSeq() == 0) {
					((MW_BaseModel)model).setOrderSeq(nextOrderSeq);
					nextOrderSeq++;
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
	
	public long getNextOrderSeq() throws Exception {
		List<T> list = list(null, null, -1, 0, "orderSeq");
		long ret = 0;
		for (int ii = 0; ii < list.size(); ii++) {
			T item = list.get(ii);
			if (ret < item.getOrderSeq()) {
				ret = item.getOrderSeq();
			}
		}
		return ret + 1;
	}
}
