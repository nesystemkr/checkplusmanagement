package kr.nesystem.appengine.board.dao;

import java.util.List;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.board.model.CM_Attachment;
import kr.nesystem.appengine.common.dao.BaseDao;

public class AttachmentDao extends BaseDao<CM_Attachment> {
	public AttachmentDao() {
		super(CM_Attachment.class);
	}

	public List<CM_Attachment> selectAttachments(HttpSession session, long groupIdKey) throws Exception {
		PropertyFilter filter = PropertyFilter.eq("groupIdKey", groupIdKey);
		return super.list(session, filter, -1, 0);
	}
}
