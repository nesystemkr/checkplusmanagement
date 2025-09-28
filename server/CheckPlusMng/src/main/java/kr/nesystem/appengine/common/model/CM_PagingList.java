package kr.nesystem.appengine.common.model;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import kr.peelknight.common.model.CM_Paging;
import kr.peelknight.common.model.Model;

public class CM_PagingList<T>  {
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
	public void l10n(HttpSession session) {
		if (list == null) {
			return;
		}
		Model item;
		for (int ii=0; ii<list.size(); ii++) {
			item = (Model)list.get(ii);
			item.l10n(session);
		}
	}
}
