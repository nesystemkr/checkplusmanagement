package kr.nesystem.appengine.messagequeue.model;

import kr.nesystem.appengine.common.model.Model;

public class MQ_SMS extends Model {
	private String target;
	private String title;
	private String content;
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
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
