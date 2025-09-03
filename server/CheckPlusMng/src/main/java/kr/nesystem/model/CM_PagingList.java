package kr.nesystem.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_PagingList<T> extends Model {
	private CM_Paging paging;
	private List<T> list;
	public CM_Paging getPaging() {
		return paging;
	}
	public void setPaging(CM_Paging paging) {
		this.paging = paging;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public void numbering(int offset) {
		if (paging == null) {
			return;
		}
		if (list == null) {
			return;
		}
		if (list.size() == 0) {
			return;
		}
		int idx = 0;
		if (offset > 0) {
			idx = offset;
		}
		if (list.get(0) instanceof Model) {
			Model item;
			for (int ii=0; ii<list.size(); ii++) {
				item = (Model)list.get(ii);
				item.setNo((long)idx + ii + 1);
			}
		}
	}
	public void reverse_numbering(int offset) {
		if (paging == null) {
			return;
		}
		if (list == null) {
			return;
		}
		if (list.size() == 0) {
			return;
		}
		int idx = 0;
		if (offset > 0) {
			idx = offset;
		}
		if (list.get(0) instanceof Model) {
			Model item;
			for (int ii=0; ii<list.size(); ii++) {
				item = (Model)list.get(ii);
				item.setNo(paging.getTotalCount() - ii - idx);
			}
		}
	}
}
