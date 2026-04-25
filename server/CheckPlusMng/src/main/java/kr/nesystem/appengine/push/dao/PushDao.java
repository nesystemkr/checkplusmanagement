package kr.nesystem.appengine.push.dao;

import java.util.Date;
import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.dao.BaseDao;
import kr.nesystem.appengine.push.model.PS_Msg;

public class PushDao extends BaseDao<PS_Msg> {
	public PushDao() {
		super(PS_Msg.class);
	}
	
	public void updateSendDate(PS_Msg msg) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			Entity existOne = txn.get(msg.toKey(keyFactory));
			if (existOne == null) {
				throw new Exception("Not found: " + msg.key());
			}
			Entity entity = msg.toEntity(existOne, "1", msg.getPmsTryCount() + 1, new Date());
			txn.update(entity);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}
	
	public void updatePushResult(PS_Msg msg, boolean isSuccess, String errorCode, String errorMessage) {
		Transaction txn = datastore.newTransaction();
		try {
			Entity existOne = txn.get(msg.toKey(keyFactory));
			if (existOne == null) {
				return;
			}
			Entity entity;
			if (isSuccess == true) {
				entity = msg.toEntity(existOne, "S", new Date(), errorCode, errorMessage);
			} else {
				entity = msg.toEntity(existOne, "E", errorCode, errorMessage);
			}
			txn.update(entity);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		} 
	}
	
	public List<PS_Msg> selectPmsCandidatedMsg() {
		try {
			CompositeFilter pmsStatusFilter = CompositeFilter.or(PropertyFilter.eq("pmsStatus", "0"),
																 PropertyFilter.eq("pmsStatus", "E"));
			CompositeFilter sendDateFilter = CompositeFilter.or(PropertyFilter.eq("sendDate", 0),
																PropertyFilter.le("sendDate", (new Date()).getTime() - 60000));
			CompositeFilter filter = CompositeFilter.and(PropertyFilter.le("reserveDate", (new Date()).getTime()),
														 pmsStatusFilter,
														 sendDateFilter);
			List<PS_Msg> list = super.list(null, filter, 0, 100);
			for (int ii = 0; ii < list.size(); ii++) {
				PS_Msg msg = list.get(ii);
				if (msg.getPmsMaxRetryCount() > msg.getPmsTryCount()) {
					msg.setPmsStatus("F");
					Entity existOne = datastore.get(msg.toKey(keyFactory));
					if (existOne == null) {
						continue;
					}
					datastore.update(msg.toEntity(existOne));
					list.remove(ii);
					ii--;
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
