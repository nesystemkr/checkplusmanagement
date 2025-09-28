package kr.nesystem.appengine.common.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_CodeType;

public class CodeDao extends BaseDao {
	public List<CM_Code> selectCodes(int offset, int size) {
		List<CM_Code> ret = new ArrayList<>();
		QueryResults<Entity> results = datastore.run(Query.newEntityQueryBuilder().setKind(CM_Code.class.getSimpleName()).build());
		while (results.hasNext()) {
			Entity entity = results.next();
			ret.add((new CM_Code()).fromEntity(entity));
		}
		return ret;
	}

	public List<CM_Code> selectCodeByType(String type) {
		List<CM_Code> ret = new ArrayList<>();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(CM_Code.class.getSimpleName())
				.setFilter(StructuredQuery.PropertyFilter.eq("type", type)).build();
		QueryResults<Entity> results = datastore.run(query);
		while (results.hasNext()) {
			Entity entity = results.next();
			ret.add((new CM_Code()).fromEntity(entity));
		}
		return ret;
	}

	public CM_Code selectCodeByTypeNCode(String type, String code) {
		Entity entity = datastore.get(CM_CodeType.toKey(datastore, CM_CodeType.class, type + "__" + code));
		if (entity != null) {
			return (new CM_Code()).fromEntity(entity);
		}
		return null;
	}

	public void insertCode(CM_Code code) {
		datastore.put(code.toEntity(datastore));
	}

//	public void insertCodes(List<CM_Code> list) {
//		FullEntity entities = new FullEntity();
//		for (int ii = 0; ii < list.size(); ii++) {
//			entities.add(list.get(ii).toEntity(datastore));
//		}
//		datastore.put(entities);
//	}

	public void updateCode(CM_Code code) {
		datastore.put(code.toEntity(datastore));
	}

	public void deleteCode(CM_Code code) {
		datastore.delete(code.toKey(datastore));
	}
}
