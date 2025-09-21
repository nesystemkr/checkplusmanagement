package kr.peelknight.fragment.model;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;
import kr.peelknight.util.L10N;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_PopupFragment extends Model {
	private long fragmentIdKey;
	private int width;
	private int height;
	private String selectTitle;
	private String selectUrl;
	private String prevUrl;
	private String nextUrl;
	private String selectErrorMsg;
	private String insertTitle;
	private String insertUrl;
	private String insertMethod;
	private String insertErrorMsg;
	private String updateTitle;
	private String updateUrl;
	private String updateMethod;
	private String updateErrorMsg;
	private String deleteConfirm;
	private String deleteUrl;
	private String deleteMethod;
	private String deleteErrorMsg;
	private String confirmButton;
	private String cancelButton;
	private String gridFragmentId;
	private List<CM_PopupRow> rows;
	private List<CM_PopupRow> list;
	private CM_GridFragment gridFragment;
	public void init() {
		selectTitle = "COMMON_SELECT_TITLE";
		selectErrorMsg = "COMMON_SELECT_ERROR";
		insertTitle = "COMMON_INSERT_TITLE";
		insertMethod = "POST";
		insertErrorMsg = "COMMON_INSERT_ERROR";
		updateTitle = "COMMON_UPDATE_TITLE";
		updateMethod = "PUT";
		updateErrorMsg = "COMMON_UPDATE_ERROR";
		deleteConfirm = "COMMON_CONFIRM_DELETE";
		deleteMethod = "DELETE";
		deleteErrorMsg = "COMMON_DELETE_ERROR";
		confirmButton = "COMMON_CONFIRM";
		cancelButton = "COMMON_CANCEL";
	}
	public long getFragmentIdKey() {
		return fragmentIdKey;
	}
	public void setFragmentIdKey(long fragmentIdKey) {
		this.fragmentIdKey = fragmentIdKey;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getSelectTitle() {
		return selectTitle;
	}
	public void setSelectTitle(String selectTitle) {
		this.selectTitle = selectTitle;
	}
	public String getSelectUrl() {
		return selectUrl;
	}
	public void setSelectUrl(String selectUrl) {
		this.selectUrl = selectUrl;
	}
	public String getPrevUrl() {
		return prevUrl;
	}
	public void setPrevUrl(String prevUrl) {
		this.prevUrl = prevUrl;
	}
	public String getNextUrl() {
		return nextUrl;
	}
	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}
	public String getSelectErrorMsg() {
		return selectErrorMsg;
	}
	public void setSelectErrorMsg(String selectErrorMsg) {
		this.selectErrorMsg = selectErrorMsg;
	}
	public String getInsertTitle() {
		return insertTitle;
	}
	public void setInsertTitle(String insertTitle) {
		this.insertTitle = insertTitle;
	}
	public String getInsertUrl() {
		return insertUrl;
	}
	public void setInsertUrl(String insertUrl) {
		this.insertUrl = insertUrl;
	}
	public String getInsertMethod() {
		return insertMethod;
	}
	public void setInsertMethod(String insertMethod) {
		this.insertMethod = insertMethod;
	}
	public String getInsertErrorMsg() {
		return insertErrorMsg;
	}
	public void setInsertErrorMsg(String insertErrorMsg) {
		this.insertErrorMsg = insertErrorMsg;
	}
	public String getUpdateTitle() {
		return updateTitle;
	}
	public void setUpdateTitle(String updateTitle) {
		this.updateTitle = updateTitle;
	}
	public String getUpdateUrl() {
		return updateUrl;
	}
	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	public String getUpdateMethod() {
		return updateMethod;
	}
	public void setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
	}
	public String getUpdateErrorMsg() {
		return updateErrorMsg;
	}
	public void setUpdateErrorMsg(String updateErrorMsg) {
		this.updateErrorMsg = updateErrorMsg;
	}
	public String getDeleteConfirm() {
		return deleteConfirm;
	}
	public void setDeleteConfirm(String deleteConfirm) {
		this.deleteConfirm = deleteConfirm;
	}
	public String getDeleteUrl() {
		return deleteUrl;
	}
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}
	public String getDeleteMethod() {
		return deleteMethod;
	}
	public void setDeleteMethod(String deleteMethod) {
		this.deleteMethod = deleteMethod;
	}
	public String getDeleteErrorMsg() {
		return deleteErrorMsg;
	}
	public void setDeleteErrorMsg(String deleteErrorMsg) {
		this.deleteErrorMsg = deleteErrorMsg;
	}
	public String getConfirmButton() {
		return confirmButton;
	}
	public void setConfirmButton(String confirmButton) {
		this.confirmButton = confirmButton;
	}
	public String getCancelButton() {
		return cancelButton;
	}
	public void setCancelButton(String cancelButton) {
		this.cancelButton = cancelButton;
	}
	public String getGridFragmentId() {
		return gridFragmentId;
	}
	public void setGridFragmentId(String gridFragmentId) {
		this.gridFragmentId = gridFragmentId;
	}
	public List<CM_PopupRow> getRows() {
		return rows;
	}
	public void setRows(List<CM_PopupRow> rows) {
		this.rows = rows;
	}
	public List<CM_PopupRow> getList() {
		return list;
	}
	public void setList(List<CM_PopupRow> list) {
		this.list = list;
	}
	public CM_GridFragment getGridFragment() {
		return gridFragment;
	}
	public void setGridFragment(CM_GridFragment gridFragment) {
		this.gridFragment = gridFragment;
	}
	public void l10n(HttpSession session) {
		selectTitle = L10N.get(selectTitle, session);
		selectErrorMsg = L10N.get(selectErrorMsg, session);
		insertTitle = L10N.get(insertTitle, session);
		insertErrorMsg = L10N.get(insertErrorMsg, session);
		updateTitle = L10N.get(updateTitle, session);
		updateErrorMsg = L10N.get(updateErrorMsg, session);
		deleteConfirm = L10N.get(deleteConfirm, session);
		deleteErrorMsg = L10N.get(deleteErrorMsg, session);
		confirmButton = L10N.get(confirmButton, session);
		cancelButton = L10N.get(cancelButton, session);
		if (rows != null) {
			for (int ii=0; ii<rows.size(); ii++) {
				rows.get(ii).l10n(session);
			}
		}
	}
}
