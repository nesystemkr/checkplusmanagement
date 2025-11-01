package kr.peelknight.daemon.model;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;
//import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_Daemon extends Model {
	private long idKey;
	private String daemonName;
	private String className;
	private int orderSeq;
	private String status;
	private String autoStartYN;
	private String running;
	private String statusName;
	private String autoStartNm;
	private String runningName;
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
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
//	public void l10n(HttpSession session) {
//		statusName = L10N.get(statusName, session);
//		autoStartNm = L10N.get(autoStartNm, session);
//	}
}
