package kr.nesystem.appengine.common.model;

public class CM_Paging extends Model {
	private long totalCount;
	public CM_Paging() {
		super();
	}
	public CM_Paging(long totalCount) {
		this.totalCount = totalCount;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}
