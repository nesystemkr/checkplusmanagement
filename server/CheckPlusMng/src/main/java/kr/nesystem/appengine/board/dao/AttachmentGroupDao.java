package kr.nesystem.appengine.board.dao;

import kr.nesystem.appengine.board.model.CM_AttachmentGroup;
import kr.nesystem.appengine.common.dao.BaseDao;

public class AttachmentGroupDao extends BaseDao<CM_AttachmentGroup> {
	public AttachmentGroupDao() {
		super(CM_AttachmentGroup.class);
	}
}
