package kr.nesystem.appengine.common.dao;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

public class BaseDao {
	protected final Datastore datastore;
	
	public BaseDao() {
		DatastoreOptions options = DatastoreOptions.newBuilder().setProjectId("checkplusmng").setDatabaseId("checkplusmanagement").build();
		this.datastore = options.getService();
	}
}
