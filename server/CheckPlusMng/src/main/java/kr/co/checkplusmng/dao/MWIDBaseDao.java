package kr.co.checkplusmng.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.GAEModel;

public class MWIDBaseDao<T extends GAEModel> extends MWBaseDao<T> {
	public MWIDBaseDao(Class<T> clazz) {
		super(clazz);
	}

	public T selectByIdString(HttpSession session, String idString) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("idString", idString);
		List<T> list = super.list(session, filter, -1, 0);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
