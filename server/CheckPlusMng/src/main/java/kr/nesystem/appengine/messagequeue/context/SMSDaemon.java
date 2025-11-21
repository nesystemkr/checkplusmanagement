package kr.nesystem.appengine.messagequeue.context;

import java.util.List;

import kr.nesystem.appengine.common.model.Model;
import kr.nesystem.appengine.messagequeue.common.SMS;
import kr.nesystem.appengine.messagequeue.model.MQ_SMS;

public class SMSDaemon extends ConsumerBaseDaemon {
	public SMSDaemon() {
		super(MQ_SMS.class.getName());
	}
	
	@Override
	public String getTopicName() {
		return "mails";
	}

	@Override
	public String getGroupId() {
		return "mail";
	}

	@Override
	public void handleItem(Model item) {
		MQ_SMS sms = (MQ_SMS)item;
		SMS.sendSMS(sms.getTarget(), sms.getTitle(), sms.getContent());
	}

	@Override
	public void handleList(List<Model> item) {
	}
}
