package kr.nesystem.appengine.daemon.model;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.model.GAEAutoIncModel;
import kr.nesystem.appengine.common.util.L10N;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

public class CM_Daemon extends GAEAutoIncModel {
	private String daemonName;
	private String className;
	private int orderSeq;
	private String status;
	private String autoStartYN;
	private String running;
	private String statusName;
	private String autoStartNm;
	private String runningName;
	public String getDaemonName() {
		return daemonName;
	}
	public void setDaemonName(String daemonName) {
		this.daemonName = daemonName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAutoStartYN() {
		return autoStartYN;
	}
	public void setAutoStartYN(String autoStartYN) {
		this.autoStartYN = autoStartYN;
	}
	public String getRunning() {
		return running;
	}
	public void setRunning(String running) {
		this.running = running;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getAutoStartNm() {
		return autoStartNm;
	}
	public void setAutoStartNm(String autoStartNm) {
		this.autoStartNm = autoStartNm;
	}
	public String getRunningName() {
		return runningName;
	}
	public void setRunningName(String runningName) {
		this.runningName = runningName;
	}
	public void l10n(HttpSession session) {
		statusName = L10N.get(statusName, session);
		autoStartNm = L10N.get(autoStartNm, session);
		runningName = L10N.get(runningName, session);
	}
	@Override
	public FullEntity<?> toEntity(KeyFactory keyFactory) {
		return Entity.newBuilder(keyFactory.newKey())
				.set("daemonName", N2Z(daemonName))
				.set("className", N2Z(className))
				.set("orderSeq", orderSeq)
				.set("status", N2Z(status))
				.set("autoStartYN", N2Z(autoStartYN))
				.set("running", N2Z(running))
				.build();
	}
	@Override
	public Entity toEntity(Entity existOne) {
		return Entity.newBuilder(existOne.getKey(), existOne)
				.set("daemonName", N2Z(daemonName))
				.set("className", N2Z(className))
				.set("orderSeq", orderSeq)
				.set("status", N2Z(status))
				.set("autoStartYN", N2Z(autoStartYN))
				.set("running", N2Z(running))
				.build();
	}
	@Override
	public CM_Daemon fromEntity(Entity entity) {
		super.fromEntity(entity);
		setDaemonName(entity.getString("daemonName"));
		setClassName(entity.getString("className"));
		setOrderSeq((int)entity.getLong("orderSeq"));
		setStatus(entity.getString("status"));
		setAutoStartYN(entity.getString("autoStartYN"));
		setRunning(entity.getString("running"));
		return this;
	}
}
