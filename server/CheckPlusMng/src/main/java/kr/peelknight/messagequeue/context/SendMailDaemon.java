package kr.peelknight.messagequeue.context;

import java.util.List;

import kr.peelknight.common.model.Model;
import kr.peelknight.messagequeue.model.MQ_EMail;
import kr.peelknight.util.EMail;

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
