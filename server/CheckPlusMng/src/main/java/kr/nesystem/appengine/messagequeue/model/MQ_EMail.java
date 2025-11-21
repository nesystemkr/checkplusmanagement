package kr.nesystem.appengine.messagequeue.model;

import kr.nesystem.appengine.common.model.Model;

public class MQ_EMail extends Model {
	private String to;
	private String title;
	private String content;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
