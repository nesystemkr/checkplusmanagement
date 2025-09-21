package kr.peelknight.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.common.model.CM_Attachment;
import kr.peelknight.common.model.CM_AttachmentGroup;
import kr.peelknight.mybatis.MySqlSessionFactory;

public class AttachmentDao {
	public SqlSessionFactory factory =  null;

	public AttachmentDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public void insertAttachmentGroup(CM_AttachmentGroup attachmentGroup) {
		SqlSession session = factory.openSession();
		try {
			CM_Attachment attachment;
			session.insert("kr.peelknight.mybatis.attachmentMapper.insertAttachmentGroup", attachmentGroup);
			for (int ii=0; ii<attachmentGroup.getAttachments().size(); ii++) {
				attachment = attachmentGroup.getAttachments().get(ii);
				attachment.setGroupIdKey(attachmentGroup.getIdKey());
				session.insert("kr.peelknight.mybatis.attachmentMapper.insertAttachment", attachment);
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateAttachmentGroup(CM_AttachmentGroup attachmentGroup) {
		SqlSession session = factory.openSession();
		try {
			CM_Attachment attachment;
			session.update("kr.peelknight.mybatis.attachmentMapper.updateAttachmentGroup", attachmentGroup);
			if (attachmentGroup.getAttachments() != null) {
				for (int ii=0; ii<attachmentGroup.getAttachments().size(); ii++) {
					attachment = attachmentGroup.getAttachments().get(ii);
					attachment.setGroupIdKey(attachmentGroup.getIdKey());
					session.insert("kr.peelknight.mybatis.attachmentMapper.insertAttachment", attachment);
				}
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	public CM_AttachmentGroup selectAttachmentGroupByIdKey(long groupIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", groupIdKey);
			return session.selectOne("kr.peelknight.mybatis.attachmentMapper.selectAttachmentGroup", param);
		} finally {
			session.close();
		}
	}
	
	public List<CM_Attachment> selectAttachments(long groupIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("groupIdKey", groupIdKey);
			return session.selectList("kr.peelknight.mybatis.attachmentMapper.selectAttachment", param);
		} finally {
			session.close();
		}
	}

	public CM_Attachment selectAttachmentByIdKey(long attachmentIdKey) {
		SqlSession session = factory.openSession();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idKey", attachmentIdKey);
			return session.selectOne("kr.peelknight.mybatis.attachmentMapper.selectAttachment", param);
		} finally {
			session.close();
		}
	}

	public void updateAttachment(CM_Attachment attachment) {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.attachmentMapper.updateAttachment", attachment);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void deleteAttachment(CM_Attachment attachment) {
		SqlSession session = factory.openSession();
		try {
			session.delete("kr.peelknight.mybatis.attachmentMapper.deleteAttachment", attachment);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void deleteAttachmentGroup(List<CM_Attachment> listAttachments, CM_AttachmentGroup attachmentGroup) {
		SqlSession session = factory.openSession();
		try {
			for (int ii=0; ii<listAttachments.size(); ii++) {
				session.delete("kr.peelknight.mybatis.attachmentMapper.deleteAttachment", listAttachments.get(ii));
			}
			session.delete("kr.peelknight.mybatis.attachmentMapper.deleteAttachmentGroup", attachmentGroup);
			session.commit();
		} finally {
			session.close();
		}
	}
}
