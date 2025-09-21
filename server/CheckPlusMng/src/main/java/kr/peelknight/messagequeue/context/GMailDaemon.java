package kr.peelknight.messagequeue.context;

import java.util.List;

import kr.peelknight.common.model.Model;
import kr.peelknight.messagequeue.model.MQ_EMail;
import kr.peelknight.util.EMail;

public class GMailDaemon extends ConsumerBaseDaemon {
	public GMailDaemon() {
		super(MQ_EMail.class.getName());
	}
	
	@Override
	public String getTopicName() {
		return "gmails";
	}

	@Override
	public String getGroupId() {
		return "gmail";
	}

	@Override
	public void handleItem(Model item) {
		MQ_EMail email = (MQ_EMail)item;
		EMail.sendEmailWithHTMLNGMail(email.getTo(), email.getTitle(), email.getContent());
	}

	@Override
	public void handleList(List<Model> item) {
	}
}
