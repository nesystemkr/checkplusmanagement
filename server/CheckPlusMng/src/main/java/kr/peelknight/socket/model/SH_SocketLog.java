package kr.peelknight.socket.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SH_SocketLog extends Model {
	private String baseDate;
	private long idKey;
	private Date logDate;
	private long socketSeq;
	private int dataLen;
	private String data;
	public String getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}
	public long getIdKey() {
		return idKey;
	}
	public void setIdKey(long idKey) {
		this.idKey = idKey;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public long getSocketSeq() {
		return socketSeq;
	}
	public void setSocketSeq(long socketSeq) {
		this.socketSeq = socketSeq;
	}
	public int getDataLen() {
		return dataLen;
	}
	public void setDataLen(int dataLen) {
		this.dataLen = dataLen;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
