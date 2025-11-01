package kr.nesystem.appengine.common.dao;

import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_PagingList;

public class CodeDao extends BaseDao<CM_Code> {
	public CodeDao() {
		super(CM_Code.class);
	}

	public CM_PagingList<CM_Code> selectCodes(int offset, int size) throws Exception {
		return super.pagingList(null, offset, size);
	}

	public CM_PagingList<CM_Code> selectCodeByType(String type) throws Exception {
		PropertyFilter filter = StructuredQuery.PropertyFilter.eq("type", type);
		return super.pagingList(filter, -1, 0);
	}

	public CM_Code selectCodeByTypeNCode(String type, String code) throws Exception {
		return super.select(type + "__" + code);
	}
	
	public void insertOrUpdate(List<CM_Code> codes) throws Exception {
		Transaction txn = datastore.newTransaction();
		try {
			for (int ii = 0; ii < codes.size(); ii++) {
				CM_Code code = codes.get(ii);
				Key key = code.toKey(keyFactory);
				Entity existOne = txn.get(key);
				if (existOne != null) {
					txn.update(code.toEntity(existOne));
				} else {
					txn.add(code.toEntity(keyFactory));
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
