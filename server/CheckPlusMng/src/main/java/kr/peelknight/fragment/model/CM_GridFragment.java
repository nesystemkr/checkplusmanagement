package kr.peelknight.fragment.model;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.peelknight.common.model.Model;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CM_GridFragment extends Model {
	private long fragmentIdKey;
	private String serviceUrl;
	private String needAuth;
	private String jqGrid;
	private String showCheckBox;
	private String stretchColumn;
	private String paging;
	private String popupFragmentId;
	private String gridAddButton;
	private String gridDelButton;
	private String gridSaveButton;
	private String gridSaveUrl;
	private String gridRefreshButton;
	private String searchArea;
	private List<CM_GridColModel> colModel;
	private List<CM_GridColModel> list;
	private CM_PopupFragment popupFragment;
	public void init() {
		needAuth = "Y";
		jqGrid = "Y";
		showCheckBox = "N";
		paging = "Y";
		gridAddButton = "N";
		gridDelButton = "N";
		gridSaveButton = "N";
		gridRefreshButton = "N";
		searchArea = "N";
	}
	public long getFragmentIdKey() {
		return fragmentIdKey;
	}
	public void setFragmentIdKey(long fragmentIdKey) {
		this.fragmentIdKey = fragmentIdKey;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public String getNeedAuth() {
		return needAuth;
	}
	public void setNeedAuth(String needAuth) {
		this.needAuth = needAuth;
	}
	public String getJqGrid() {
		return jqGrid;
	}
	public void setJqGrid(String jqGrid) {
		this.jqGrid = jqGrid;
	}
	public String getShowCheckBox() {
		return showCheckBox;
	}
	public void setShowCheckBox(String showCheckBox) {
		this.showCheckBox = showCheckBox;
	}
	public String getStretchColumn() {
		return stretchColumn;
	}
	public void setStretchColumn(String stretchColumn) {
		this.stretchColumn = stretchColumn;
	}
	public String getPaging() {
		return paging;
	}
	public void setPaging(String paging) {
		this.paging = paging;
	}
	public String getPopupFragmentId() {
		return popupFragmentId;
	}
	public void setPopupFragmentId(String popupFragmentId) {
		this.popupFragmentId = popupFragmentId;
	}
	public String getGridAddButton() {
		return gridAddButton;
	}
	public void setGridAddButton(String gridAddButton) {
		this.gridAddButton = gridAddButton;
	}
	public String getGridDelButton() {
		return gridDelButton;
	}
	public void setGridDelButton(String gridDelButton) {
		this.gridDelButton = gridDelButton;
	}
	public String getGridSaveButton() {
		return gridSaveButton;
	}
	public void setGridSaveButton(String gridSaveButton) {
		this.gridSaveButton = gridSaveButton;
	}
	public String getGridSaveUrl() {
		return gridSaveUrl;
	}
	public void setGridSaveUrl(String gridSaveUrl) {
		this.gridSaveUrl = gridSaveUrl;
	}
	public String getGridRefreshButton() {
		return gridRefreshButton;
	}
	public void setGridRefreshButton(String gridRefreshButton) {
		this.gridRefreshButton = gridRefreshButton;
	}
	public String getSearchArea() {
		return searchArea;
	}
	public void setSearchArea(String searchArea) {
		this.searchArea = searchArea;
	}
	public List<CM_GridColModel> getColModel() {
		return colModel;
	}
	public void setColModel(List<CM_GridColModel> colModel) {
		this.colModel = colModel;
	}
	public List<CM_GridColModel> getList() {
		return list;
	}
	public void setList(List<CM_GridColModel> list) {
		this.list = list;
	}
	public CM_PopupFragment getPopupFragment() {
		return popupFragment;
	}
	public void setPopupFragment(CM_PopupFragment popupFragment) {
		this.popupFragment = popupFragment;
	}
	public void l10n(HttpSession session) {
		if (colModel != null) {
			for (int ii=0; ii<colModel.size(); ii++) {
				colModel.get(ii).l10n(session);
			}
		}
		if (popupFragment != null) {
			popupFragment.l10n(session);
		}
		
	}
}
