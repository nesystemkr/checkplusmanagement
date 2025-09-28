package kr.nesystem.appengine.common.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.ObjectifyService;

import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_CodeType;
import kr.nesystem.appengine.common.model.CM_Paging;

public class CodeTypeDao extends BaseDao {
	public List<CM_CodeType> selectCodeTypes(int offset, int size) {
		List<CM_CodeType> ret = new ArrayList<>();
		Query<Entity> query;
		if (size == 0 || offset == -1) {
			query = Query.newEntityQueryBuilder().setKind(CM_CodeType.class.getSimpleName()).build();
		} else {
			query = Query.newEntityQueryBuilder().setKind(CM_CodeType.class.getSimpleName()).setLimit(size).setOffset(offset).build();
		}
		QueryResults<Entity> results = datastore.run(query);
		while (results.hasNext()) {
			Entity entity = results.next();
			ret.add((new CM_CodeType()).fromEntity(entity));
		}
		return ret;
	}

	public CM_Paging selectCodeTypePaging() {
		CM_Paging ret = new CM_Paging();
		ret.setTotalCount(ObjectifyService.run(() -> {
			return ObjectifyService.ofy().load().type(CM_CodeType.class).count();
		}));
		return ret;
	}

	public CM_CodeType selectCodeTypeByType(String type) {
		Entity entity = datastore.get(CM_CodeType.toKey(datastore, CM_CodeType.class, type));
		if (entity != null) {
			return (new CM_CodeType()).fromEntity(entity);
		}
		return null;
	}

	public void insertCodeType(CM_CodeType codeType) {
		datastore.put(codeType.toEntity(datastore));
	}

	public void updateCodeType(CM_CodeType codeType) {
		datastore.put(codeType.toEntity(datastore));
	}

	public void deleteCodeType(CM_CodeType codeType) {
		datastore.delete(codeType.toKey(datastore));
	}
}
