package kr.nesystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_Paging extends Model {
	private long totalCount;
	private long page;
	public CM_Paging() {
		super();
	}
	public CM_Paging(long totalCount, long page) {
		this.totalCount = totalCount;
		this.page = page;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
}
