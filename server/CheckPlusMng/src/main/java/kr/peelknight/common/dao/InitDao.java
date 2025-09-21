package kr.peelknight.common.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.peelknight.mybatis.MySqlSessionFactory;

public class InitDao {
	public SqlSessionFactory factory =  null;

	public InitDao() {
		factory = MySqlSessionFactory.getSqlSessionFactory();
	}

	public void createTables() {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.initMapper.createCMUser");
			session.update("kr.peelknight.mybatis.initMapper.createIdxUserIdCMUser");
			session.update("kr.peelknight.mybatis.initMapper.createIdxUserTypeCMUser");
			session.update("kr.peelknight.mybatis.initMapper.createCMDevice");
//			session.update("kr.peelknight.mybatis.initMapper.createIdxUniqueIdCMDevice");
//			session.update("kr.peelknight.mybatis.initMapper.createIdxPushKeyCMDevice");
			session.update("kr.peelknight.mybatis.initMapper.createIdxUserIdKeyCMDevice");
			session.update("kr.peelknight.mybatis.initMapper.createCMVersion");
			session.update("kr.peelknight.mybatis.initMapper.createIdxVersionCMVersion");
			session.update("kr.peelknight.mybatis.initMapper.createCMCodeType");
			session.update("kr.peelknight.mybatis.initMapper.createCMCode");
			session.update("kr.peelknight.mybatis.initMapper.createCMMenu");
			session.update("kr.peelknight.mybatis.initMapper.createIdxParentIdKeyCMMenu");
			session.update("kr.peelknight.mybatis.initMapper.createCMMenuAuth");
			session.update("kr.peelknight.mybatis.initMapper.createIdxuserTypeCMMenuAuth");
			session.update("kr.peelknight.mybatis.initMapper.createIdxMenuIdKeyCMMenuAuth");
			session.update("kr.peelknight.mybatis.initMapper.createCMAttachment");
			session.update("kr.peelknight.mybatis.initMapper.createIdxGroupIdKeyCMAttachment");
			session.update("kr.peelknight.mybatis.initMapper.createCMAttachmentGroup");
			session.update("kr.peelknight.mybatis.initMapper.createCMBoard");
			session.update("kr.peelknight.mybatis.initMapper.createIdxBoardIdCMBoard");
			session.update("kr.peelknight.mybatis.initMapper.createCMBoardContent");
			session.update("kr.peelknight.mybatis.initMapper.createIdxBoardIdKeyCMBoardContent");
			session.update("kr.peelknight.mybatis.initMapper.createIdxTopYNCMBoardContent");
			session.update("kr.peelknight.mybatis.initMapper.createIdxCreateDateCMBoardContent");
			session.update("kr.peelknight.mybatis.initMapper.createIdxStatusCMBoardContent");
			session.update("kr.peelknight.mybatis.initMapper.createCMBoardAuth");
			session.update("kr.peelknight.mybatis.initMapper.createCMBoardContentReply");
			session.update("kr.peelknight.mybatis.initMapper.createIdxBoardContentIdKeyCMBoardContentReply");
			session.update("kr.peelknight.mybatis.initMapper.createIdxParentIdKeyCMBoardContentReply");
			session.update("kr.peelknight.mybatis.initMapper.createIdxStatusCMBoardContentReply");
			session.update("kr.peelknight.mybatis.initMapper.createCML10N");
			session.update("kr.peelknight.mybatis.initMapper.createIdxIdStringCML10N");
			session.update("kr.peelknight.mybatis.initMapper.createCML10NLocale");
			session.update("kr.peelknight.mybatis.initMapper.createIdxIdStringLocaleCML10NLocale");
			session.update("kr.peelknight.mybatis.initMapper.createCMDaemon");
			session.update("kr.peelknight.mybatis.initMapper.createPSMsg");
			session.update("kr.peelknight.mybatis.initMapper.createIdxUserIdKeyPSMsg");
			session.update("kr.peelknight.mybatis.initMapper.createIdxMsgGroupIdKeyPSMsg");
			session.update("kr.peelknight.mybatis.initMapper.createIdxReserveDatePSMsg");
			session.update("kr.peelknight.mybatis.initMapper.createIdxSendDatePSMsg");
			session.update("kr.peelknight.mybatis.initMapper.createIdxPmsStatusPSMsg");
			
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void createFragmentTables() {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.initFragmentMapper.createCMFragment");
			session.update("kr.peelknight.mybatis.initFragmentMapper.createCMFragmentCachedJson");
			session.update("kr.peelknight.mybatis.initFragmentMapper.createCMGridFragment");
			session.update("kr.peelknight.mybatis.initFragmentMapper.createCMGridColModel");
			session.update("kr.peelknight.mybatis.initFragmentMapper.createCMGridColButton");
			session.update("kr.peelknight.mybatis.initFragmentMapper.createCMPopupFragment");
			session.update("kr.peelknight.mybatis.initFragmentMapper.createCMPopupRow");
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void recreateCMCodes() {
		SqlSession session = factory.openSession();
		try {
			session.update("kr.peelknight.mybatis.initMapper.dropCMCodeType");
			session.update("kr.peelknight.mybatis.initMapper.dropCMCode");
			session.update("kr.peelknight.mybatis.initMapper.createCMCodeType");
			session.update("kr.peelknight.mybatis.initMapper.createCMCode");
			session.commit();
		} finally {
			session.close();
		}
	}
}
