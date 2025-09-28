package kr.nesystem.appengine.common.model;

public class CM_Paging {
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
