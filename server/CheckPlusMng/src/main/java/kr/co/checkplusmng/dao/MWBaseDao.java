package kr.co.checkplusmng.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.Filter;
import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.dao.BaseDao;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.GAEModel;

public class MWBaseDao <T extends GAEModel> extends BaseDao<T> {
	public MWBaseDao(Class<T> clazz) {
		super(clazz);
	}
	
	public CM_PagingList<T> pagingList(HttpSession session, Filter filter, int offset, int size) throws Exception {
		return pagingList(session, filter, offset, size, "orderSeq");
	}
	public List<T> list(HttpSession session, Filter filter, int offset, int size) throws Exception {
		return list(session, filter, offset, size, "orderSeq");
	}
}
