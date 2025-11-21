package kr.nesystem.appengine.messagequeue.context;

import java.util.List;

import kr.nesystem.appengine.common.model.Model;
import kr.nesystem.appengine.messagequeue.common.EMail;
import kr.nesystem.appengine.messagequeue.model.MQ_EMail;

public class SendMailDaemon extends ConsumerBaseDaemon {
	public SendMailDaemon() {
		super(MQ_EMail.class.getName());
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
		MQ_EMail email = (MQ_EMail)item;
		EMail.sendEmailWithSendMail(email.getTo(), email.getTitle(), email.getContent());
	}

	@Override
	public void handleList(List<Model> item) {
	}
}
