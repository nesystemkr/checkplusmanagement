var FragmentBuilder
if (!FragmentBuilder) {
	FragmentBuilder = {};
}

FragmentBuilder.MapFragment = {}

FragmentBuilder.newGridBuilder = function(options) {
	var ret = new FragmentBuilder.GridBuilder(options)
	FragmentBuilder.MapFragment[ret.getGridId()] = ret
	return ret
}

FragmentBuilder.newPopupBuilder = function(options) {
	var ret = new FragmentBuilder.PopupBuilder(options)
	FragmentBuilder.MapFragment[ret.getPopupId()] = ret
	return ret
}

FragmentBuilder.gridCallback = function(rowId, rowData, gridId, preSet) {
	if (FragmentBuilder.MapFragment[gridId]) {
		FragmentBuilder.MapFragment[gridId].callback(rowId, rowData, preSet)
	}
}

FragmentBuilder.GridBuilder = function(options) {
	var vContextPath = ""
	var vFragmentUrl
	var vFragmentModel
	var vButtons
	var vParentElement
	var vGridId = "grid_" + (Math.floor(Math.random() * 100000) + 1)
	var vCurrentPage = 1
	var vJQGridTable
	var vPageBody
	var vPageFirst
	var vPageStart
	var vPageNum
	var vPageEnd
	var vPageLast
	var vPopupBuilder
	var vParamData
	var vSelf
	var vSearchText
	var vPopupChangeCallBack
	var vPopupOpenCallBack
	var vCreatedCallBack
	
	if (options.contextPath) {
		vContextPath = options.contextPath
	}
	if (options.fragmentId) {
		vFragmentUrl = vContextPath + "/svc/v1/fragment/id/" + options.fragmentId
	} else if (options.fragmentUrl) {
		vFragmentUrl = vContextPath + options.fragmentUrl
	} else {
		vFragmentModel = options.fragmentModel
	}
	if (options.parentElement) {
		vParentElement = options.parentElement
	} else {
		if (options.parentId) {
			vParentElement = $("#" + options.parentId)
		}
	}
	if (options.buttons) {
		vButtons = options.buttons
	}
	if (options.paramData) {
		vParamData = options.paramData
	}
	if (options.popupChangeCallBack) {
		vPopupChangeCallBack = options.popupChangeCallBack
	}
	if (options.popupOpenCallBack) {
		vPopupOpenCallBack = options.popupOpenCallBack
	}
	if (options.createdCallBack) {
		vCreatedCallBack = options.createdCallBack
	}
	vSelf = this
	if (vFragmentUrl) {
		//metaUrl에서 데이터를 가져와서 MetalModel을 만든다.
		nesAjax(vFragmentUrl,
				null,
				function(data) {
					vFragmentModel = data
					build()
					refreshList()
				},
				function(data) {
					alert(JSON.stringify(data))
				},
				"GET");
	} else {
		build()
		refreshList()
	}
	
	this.getGridId = function() {
		return vGridId
	}
	
	this.refreshGrid = function() {
		refreshList()
	}
	
	this.getJQGridTable = function() {
		return vJQGridTable
	}
	
	this.getPopupBuilder = function() {
		return vPopupBuilder
	}
	
	this.getPopupBuilderId = function() {
		if (vPopupBuilder) {
			return vPopupBuilder.getPopupId()
		}
	}
	
	this.getJQGridData = function() {
		finalizeEditingGrid(vJQGridTable);
		var touchedList = getGridTouchedList(vJQGridTable)
		if (vDelList) {
			for (var ii=0; ii<vDelList.length; ii++) {
				vDelList[ii].touch = "D"
				touchedList[touchedList.length] = vDelList[ii]
			}
		}
		return touchedList
	}
	
	function refreshList() {
		getList(vCurrentPage)
	}
	
	function retrieveUrl(url, page) {
		var ret = url
		if (page) {
 			ret = ret.replace("{{page}", page)
		}
		if (vParamData) {
			for (var key in vParamData) {
				if (ret.indexOf("{{" + key + "}") >= 0) {
					ret = ret.replaceAll("{{" + key + "}", vParamData[key]);
				}
			}
		}
		ret = vContextPath + ret
		if (vFragmentModel.needAuth == "Y") {
		 	ret += "?q=" + getAuthToken();
		}
		return ret
	}
	
	function getList(page) {
		vDelList = []
		vCurrentPage = page
		var url = retrieveUrl(vFragmentModel.serviceUrl, page)
		if (vSearchText && vSearchText.trim().length > 0) {
			if (getAuthToken()) {
				url += "&"
			} else {
				url += "?"
			}
			url += "search=" + encodeURIComponent(vSearchText)
		}
		nesAjax(url,
				null,
				function(data) {
					if (vFragmentModel.jqGrid == "Y") {
						setTimeout(function() { vJQGridTable.setDataList(data.list) }, 1000)
						//vJQGridTable.setDataList(data.list)
					} else {
						vParentElement.html(TrimPath.processDOMTemplate(vTemplateId, data))
					}
					if (vFragmentModel.paging == "Y") {
						pagingInfo_success(data.paging)
					}
				},
				function(data) {
					alert(JSON.stringify(data))
				},
				"GET")
	}

	function getListClosure(page) {
		return function() {
			getList(page)
		}
	}

	function pagingInfo_success(data) {
		if (!data.totalCount) {
			data.totalCount = 0
		}
		var pageCount = Math.ceil(data.totalCount / 10)
		if (pageCount == 0) {
			vPageBody.hide()
			return
		}
		var startPage = (Math.floor((vCurrentPage - 1) / 10) * 10) + 1
		var endPage = startPage + 9
		if (endPage > pageCount) {
			endPage = pageCount
		}
		if (startPage == 1) {
			vPageFirst.hide()
			vPageStart.hide()
		} else {
			vPageFirst.show()
			vPageStart.show()
			vPageFirst.click(getListClosure(1))
			vPageStart.click(getListClosure(startPage - 1))
		}
		for (var ii=0; ii<10; ii++) {
			var tempPage = startPage + ii
			if (tempPage == vCurrentPage) {
				vPageNum[ii].addClass("on")
			} else {
				vPageNum[ii].removeClass("on")
			}
			if (tempPage <= endPage) {
				vPageNum[ii].show()
				vPageNum[ii].html(tempPage)
				vPageNum[ii].click(getListClosure(tempPage))
			} else {
				vPageNum[ii].hide()
			}
		}
		if (endPage >= pageCount) {
			vPageEnd.hide()
			vPageLast.hide()
		} else {
			vPageEnd.show()
			vPageLast.show()
			vPageEnd.click(getListClosure(endPage + 1))
			vPageLast.click(getListClosure(pageCount))
		}
		vPageBody.show()
	}
	
	function getAlignProp(nAlign) {
		if (nAlign == 2) {
			return "right"
		} else if (nAlign == 1) {
			return "center"
		}
		return "left"
	}
	
	function newGridRow() {
		vJQGridTable.addRowData(undefined, {touch: "I"}, "last")
	}
	
	function delGridRows() {
		finalizeEditingGrid(vJQGridTable);
		var selectedRowIds = getGridSelectedRowIds(vJQGridTable)
		var selectedList = getGridSelectedList(vJQGridTable)
		if (!selectedList || selectedList.length == 0) {
			return
		}
		if (!vDelList) {
			vDelList = []
		}
		for (var ii=0; ii<selectedList.length; ii++) {
			vDelList[vDelList.length] = selectedList[ii]
			$("#" + selectedRowIds[ii]).hide()
		}
		vJQGridTable.resetSelection()
	}
	
	function saveGridRows() {
		finalizeEditingGrid(vJQGridTable);
		var touchedList = getGridTouchedList(vJQGridTable)
		if (vDelList) {
			for (var ii=0; ii<vDelList.length; ii++) {
				vDelList[ii].touch = "D"
				touchedList[touchedList.length] = vDelList[ii]
			}
		}
		if (!touchedList || touchedList.length == 0) {
			return
		}
		var url = vContextPath + vFragmentModel.gridSaveUrl
		var paging = {}
		paging.reqToken = getAuthToken()
		paging.list = touchedList;
		nesAjax(url,
				JSON.stringify(paging),
				function(data) {
					refreshList()
				},
				function(data) {
					alert(JSON.stringify(data))
				},
				"POST")
	}
	
	function getSearchButtonClosure(searchInput) {
		return function() {
			vSearchText = searchInput.val()
			getList(1)
		}
	}
	
	function build() {
		if (!vFragmentModel) {
			alert("Fragment model is not defined!")
			return
		}
		if (!vParentElement) {
			alert("parent element is not defiend!")
		}
		if (vFragmentModel.gridAddButton == "Y" || vFragmentModel.gridDelButton == "Y" ||
			vFragmentModel.gridSaveButton == "Y" || vFragmentModel.gridRefreshButton == "Y" ||
			vFragmentModel.searchArea == "Y") {
			var buttonDiv = addDiv(vParentElement, "btn_box")
			buttonDiv.css({"padding-top":"3px","padding-bottom":"1px"})
			var buttonTmp
			if (vFragmentModel.gridAddButton == "Y") {
				buttonTmp = addEle(buttonDiv, "button", "btn_type small").html("New")
				buttonTmp.click(function() {
					newGridRow()
				});
			}
			if (vFragmentModel.gridDelButton == "Y") {
				buttonTmp = addEle(buttonDiv, "button", "btn_type small").html("Del")
				buttonTmp.click(function() {
					delGridRows()
				});
			}
			if (vFragmentModel.gridSaveButton == "Y") {
				buttonTmp = addEle(buttonDiv, "button", "btn_type small").html("Save")
				buttonTmp.click(function() {
					saveGridRows()
				});
			}
			if (vFragmentModel.gridRefreshButton == "Y") {
				buttonTmp = addEle(buttonDiv, "button", "btn_type small").html("Refresh")
				buttonTmp.click(function() {
					refreshList()
				});
			}
			if (vFragmentModel.searchArea == "Y") {
				var searchInput = addEle(buttonDiv, "input", "inputSelect").css({float:"left",width:"30%",height:"22px"}).attr("type","text")
				buttonTmp = addEle(buttonDiv, "button", "btn_type small").html("Search").css({float:"left"})
				buttonTmp.click(getSearchButtonClosure(searchInput))
			}
		}
		if (vFragmentModel.jqGrid == "Y") {
			gridOptions = {}
			gridOptions.colModel = [];
			if (vFragmentModel.colModel) {
				for (var ii=0; ii<vFragmentModel.colModel.length; ii++) {
					gridOptions.colModel[ii] = {}
					gridOptions.colModel[ii].name = vFragmentModel.colModel[ii].name
					gridOptions.colModel[ii].hidden = (vFragmentModel.colModel[ii].hidden == "Y")
					gridOptions.colModel[ii].label = vFragmentModel.colModel[ii].label
					gridOptions.colModel[ii].width = vFragmentModel.colModel[ii].width
					gridOptions.colModel[ii].align = getAlignProp(vFragmentModel.colModel[ii].align)
					if (vFragmentModel.colModel[ii].codeType) {
						gridOptions.colModel[ii].cellsubmit = 'clientArray'
						gridOptions.colModel[ii].formatter = 'select'
						gridOptions.colModel[ii].edittype = 'select'
						gridOptions.colModel[ii].editoptions = { value : vFragmentModel.colModel[ii].selectValues }
					} else {
						gridOptions.colModel[ii].edittype = 'text'
					}
					if (vFragmentModel.colModel[ii].editType == 0) { //DISABLED
						gridOptions.colModel[ii].editable = false
					} else if (vFragmentModel.colModel[ii].editType == 1) { //수정가능
						gridOptions.colModel[ii].editable = true
					} else if (vFragmentModel.colModel[ii].editType == 2) { //입력만 가능
						gridOptions.colModel[ii].editable = getGridEditableFunc
					} else {
						gridOptions.colModel[ii].editable = false
					}
					if (vFragmentModel.colModel[ii].mandatory == "Y") {
						gridOptions.colModel[ii].editrules = {required:true}
					}
					if (vFragmentModel.colModel[ii].format) {
						// public static final int COLFORMAT_NONE = 0;
						// public static final int COLFORMAT_DATE = 1;
						// public static final int COLFORMAT_DATETIME = 2;
						// public static final int COLFORMAT_COMMANUMBER = 3;
						if (vFragmentModel.colModel[ii].format == 1) {
							gridOptions.colModel[ii].formatter = getGridDateFormatClosure()
						} else if (vFragmentModel.colModel[ii].format == 2) {
							gridOptions.colModel[ii].formatter = getGridFullDateFormatClosure()
						} else if (vFragmentModel.colModel[ii].format == 3) {
							gridOptions.colModel[ii].formatter = getGridCommaNumberFormatClosure()
						} else if (vFragmentModel.colModel[ii].format == 4) {
							gridOptions.colModel[ii].formatter = function(cellvalue, options, rowObject) {
								return cellvalue;
							}
						}
					}
					if (vButtons) {
						for (var jj=vButtons.length-1; jj>=0; jj--) {
							if (vFragmentModel.colModel[ii].name == vButtons[jj]["column"]) {
								if (!vFragmentModel.colModel[ii].buttons) {
									vFragmentModel.colModel[ii].buttons = []
								}
								vFragmentModel.colModel[ii].buttons.unshift(vButtons[jj])
							} 
						}
					}
					if (vFragmentModel.colModel[ii].buttons && vFragmentModel.colModel[ii].buttons.length > 0) {
						gridOptions.colModel[ii].formatter = getGridButtonClosure(vFragmentModel.colModel[ii].buttons)
					}
				}
			}
			gridOptions.showCheckBox = (vFragmentModel.showCheckBox == "Y")
			gridOptions.stretchColumn = vFragmentModel.stretchColumn 
			gridOptions.id = vGridId
			gridOptions.container = "container_" + (Math.floor(Math.random() * 100000) + 1)
			gridOptions.cellEdit = true
			var containerDiv = addDiv(vParentElement).attr("id", gridOptions.container);
			addTable(containerDiv).attr("id", gridOptions.id);
			vJQGridTable = initializeGrid(gridOptions)
		} else {
			var template = ""
			template += '<table class="tbsty">'
			template += '<thead><tr>'
			if (vFragmentModel.colModel) {
				for (var ii=0; ii<vFragmentModel.colModel.length; ii++) {
					template += '<th'
					if (vFragmentModel.colModel[ii].width) {
						template += ' style="'
						if (vFragmentModel.colModel[ii].width) {
							template += 'width:' + vFragmentModel.colModel[ii].width + "px;"
						}
						template += '"'
					}
					template += '>'
					template += vFragmentModel.colModel[ii].label
					template += '</th>'
				}
			}
			template += '</tr></thead>'
			template += '<tbody>'
			template += '{for item in list}'
			template += '<tr>'
			if (vFragmentModel.colModel) {
				for (var ii=0; ii<vFragmentModel.colModel.length; ii++) {
					template += '<td'
					if (vFragmentModel.colModel[ii].align) {
						template += ' style="'
						if (vFragmentModel.colModel[ii].align) {
							template += 'text-align:' + getAlignProp(vFragmentModel.colModel[ii].align) + ";"
						}
						template += '"'
					}
					template += '>'
					if (vFragmentModel.colModel[ii].buttons) {
					} else {
						// public static final int COLFORMAT_NONE = 0;
						// public static final int COLFORMAT_DATE = 1;
						// public static final int COLFORMAT_DATETIME = 2;
						// public static final int COLFORMAT_COMMANUMBER = 3;
						if (vFragmentModel.colModel[ii].format) {
							if (vFragmentModel.colModel[ii].format == 1) {
								//FIXME
							} else if (vFragmentModel.colModel[ii].format == 2) {
								//FIXME
								template += "{{item." + vFragmentModel.colModel[ii].name + "}"
							} else if (vFragmentModel.colModel[ii].format == 3) {
								//FIXME
							} else if (vFragmentModel.colModel[ii].format == 4) {
								template += "{{item." + vFragmentModel.colModel[ii].name + "}"
							}
						} else {
							if (vFragmentModel.colModel[ii].name) {
								template += "{{item." + vFragmentModel.colModel[ii].name + "}"
							}
						}
					}
					template += '</td>'
				}
			}
			template += '</tr>'
			template += '{for else}'
			if (vFragmentModel.colModel) {
				template += '<tr><td colspan="' + vFragmentModel.colModel.length + '">No Data</tr>'
			} else {
				template += '<tr><td>No Data</tr>'
			}
			template += '{/for}'
			template += '</tbody>'
			template += '</table>'
			$("#" + vTemplateId).html(template);
		}
		if (vFragmentModel.paging == "Y") {
			vPageBody = addDiv(vParentElement, "paging").hide()
			vPageFirst = addEle(vPageBody, "a", "etc").html("&lt;&lt;")
			vPageStart = addEle(vPageBody, "a", "etc").html("&lt;")
			vPageNum = []
			for (var ii=0; ii<10; ii++) {
				vPageNum[vPageNum.length] = addEle(vPageBody, "a").html(ii+1)
			}
			vPageEnd = addEle(vPageBody, "a", "etc").html("&gt;")
			vPageLast = addEle(vPageBody, "a", "etc").html("&gt;&gt;")
		}
		if (vFragmentModel.popupFragmentId && vFragmentModel.popupFragment) {
			vPopupBuilder = new FragmentBuilder.PopupBuilder({
					contextPath: vContextPath,
					fragmentModel: vFragmentModel.popupFragment,
					parentGroudBuilder: vSelf,
					paramData: vParamData,
					changeCallBack: vPopupChangeCallBack,
					openCallBack: vPopupOpenCallBack,
			})
			if (vFragmentModel.popupFragment.insertUrl) {
				var buttonDiv = addDiv(vParentElement, "btn_box")
				var buttonTmp = addEle(buttonDiv, "button", "btn_type normal").html("Add")
				buttonTmp.click(function() {
					vPopupBuilder.openInsert()
				});
			}
			if (vCreatedCallBack) {
				vCreatedCallBack()
			}
		} else {
			if (vCreatedCallBack) {
				vCreatedCallBack()
			}
		}
	}
	
	this.callback = function(rowId, rowData, preSet) {
		if (!vPopupBuilder) {
			return
		}
		if (preSet == 1) {
			vPopupBuilder.openDetail(rowData)
		} else if (preSet == 2) {
			vPopupBuilder.openUpdate(rowData)
		} else if (preSet == 3) {
			vPopupBuilder.deleteData(rowData)
		}
	}
}

FragmentBuilder.PopupBuilder = function(options) {
	var vContextPath = ""
	var vFragmentUrl
	var vFragmentModel
	var vParentGridBuilder
	var vPopupMode
	var vQueriedData
	var vPopupId = "popup_" + (Math.floor(Math.random() * 100000) + 1)
	var vParamData
	var vOpenAfterBuild
	var vSelf
	var vGridBuilder
	var vPrevButton
	var vNextButton
	var vChangeCallBack
	var vOpenCallBack
	
	if (options.contextPath) {
		vContextPath = options.contextPath
	}
	if (options.fragmentId) {
		vFragmentUrl = vContextPath + "/svc/v1/fragment/id/" + options.fragmentId
	} else if (options.fragmentUrl) {
		vFragmentUrl = vContextPath + options.fragmentUrl
	} else {
		vFragmentModel = options.fragmentModel
	}
	if (options.parentGroudBuilder) {
		vParentGridBuilder = options.parentGroudBuilder
	}
	if (options.paramData) {
		vParamData = options.paramData
	}
	if (options.openAfterBuild) {
		vOpenAfterBuild = options.openAfterBuild
	}
	if (options.changeCallBack) {
		vChangeCallBack = options.changeCallBack
	}
	if (options.openCallBack) {
		vOpenCallBack = options.openCallBack
	}
	vSelf = this
	if (vFragmentUrl) {
		//metaUrl에서 데이터를 가져와서 MetalModel을 만든다.
		nesAjax(vFragmentUrl,
				null,
				function(data) {
					vFragmentModel = data
					build()
					if (vOpenAfterBuild) {
						vSelf.openProper()
					}
				},
				function(data) {
					alert(JSON.stringify(data))
				},
				"GET");
	} else {
		build()
		if (vOpenAfterBuild) {
			vSelf.openProper()
		}
	}
	
	this.getPopupId = function() {
		return vPopupId
	}
	
	var vPopupROOT
	var vPopupTitle
	var vInsertPopupHeight
	var vDetailPopupHeight
	var vUpdatePopupHeight
	function build() {
		if (!vFragmentModel) {
			alert("Fragment model is not defined!")
			return
		}
		vPopupROOT = addDiv($('body'), "layer_bg").attr("id", vPopupId)
		var popupDiv = addDiv(vPopupROOT, "layerpop")
		var popupHead = addDiv(popupDiv, "pop_head")
		vPopupTitle = addEle(popupHead, "soan", "title")
		var closeButton = addEle(popupHead, "a", "pop_close white").attr("href", "#none")
		addEle(closeButton, "span").html("Close")
		closeButton.click(function() {
			closePopup()
		})
		var popupBody = addDiv(popupDiv, "pop_body")
		var inputTmp
		var rowTmp
		var bodyTable = addTable(popupBody, "tbsty")
		var trTmp
		var tdTmp
		var rowSeqTmp
		var maxColSpan = 0
		var maxColSpanTmp = 0
		vInsertPopupHeight = 45 + 68 + 21
		vDetailPopupHeight = 45 + 68 + 21
		vUpdatePopupHeight = 45 + 68 + 21
		for (var ii=0; ii<vFragmentModel.rows.length; ii++) {
			rowTmp = vFragmentModel.rows[ii];
			inputTmp = undefined
			if (rowTmp.hidden == "Y") {
				inputTmp = addEle(popupBody, "input", "inputText").attr("type", "hidden")
				rowTmp.inputElement = inputTmp
			} else {
				if (rowSeqTmp != rowTmp.rowSeq || !trTmp) {
					if ((rowTmp.showType & 1) == 1) { //SHOWTYPE_DETAIL
						vDetailPopupHeight += 33
						if (rowTmp.dataType == 10 || rowTmp.dataType == 11) {
							vDetailPopupHeight += 111
						} else if (rowTmp.dataType == 12) {
							vDetailPopupHeight += 93
						} else if (rowTmp.dataType == 13) {
							vDetailPopupHeight += 213
						}
					}
					if ((rowTmp.showType & 2) == 2) { //SHOWTYPE_INSERT
						vInsertPopupHeight += 33
						if (rowTmp.dataType == 10 || rowTmp.dataType == 11) {
						if ((rowTmp.showType & 8) == 8) {
								vInsertPopupHeight += 111
							} else {
								vInsertPopupHeight += 134
							}
						} else if (rowTmp.dataType == 12) {
							vInsertPopupHeight += 93
						} else if (rowTmp.dataType == 13) {
							vInsertPopupHeight += 213
						}
					}
					if ((rowTmp.showType & 4) == 4) { //SHOWTYPE_UPDATE
						vUpdatePopupHeight += 33
						if (rowTmp.dataType == 10 || rowTmp.dataType == 11) {
							if ((rowTmp.showType & 16) == 16) {
								vUpdatePopupHeight += 111
							} else {
								vUpdatePopupHeight += 134
							}
						} else if (rowTmp.dataType == 12) {
							vUpdatePopupHeight += 93
						} else if (rowTmp.dataType == 13) {
							vUpdatePopupHeight += 213
						}
					}
					trTmp = addTr(bodyTable)
					rowSeqTmp = rowTmp.rowSeq
					if (maxColSpanTmp > maxColSpan) {
						maxColSpan = maxColSpanTmp
					}
					maxColSpanTmp = 0
				}
				tdTmp = addEle(trTmp, "th").html(rowTmp.title)
				maxColSpanTmp++
				if (rowTmp.titleColSpan) {
					tdTmp.attr("colSpan", rowTmp.titleColSpan)
				}
				if (rowTmp.titleWidth) {
					tdTmp.css({"width":rowTmp.titleWidth})
				}
				tdTmp = addEle(trTmp, "td")
				maxColSpanTmp++
				if (rowTmp.valueColSpan) {
					tdTmp.attr("colSpan", rowTmp.valueColSpan)
				}
				if (rowTmp.valueWidth) {
					tdTmp.css({"width":rowTmp.valueWidth})
				}
				if (rowTmp.dataType == 1) { //DATATYPE_STRING
					inputTmp = addEle(tdTmp, "input", "inputText").attr("type", "text")
				} else if (rowTmp.dataType == 2) { //DATATYPE_CODETYPE
					inputTmp = addEle(tdTmp, "select", "inputSelect")
				} else if (rowTmp.dataType == 3 || //DATATYPE_INTEGER
						   rowTmp.dataType == 4 || //DATATYPE_LONG
						   rowTmp.dataType == 5 || //DATATYPE_FLOAT
						   rowTmp.dataType == 6) { //DATATYPE_DOUBLE
					inputTmp = addEle(tdTmp, "input", "inputText").attr("type", "number")
				} else if (rowTmp.dataType == 7) { //DATATYPE_DATE
					inputTmp = addEle(tdTmp, "input", "inputText").attr("type", "text")
				} else if (rowTmp.dataType == 8) { //DATATYPE_DATETIME
					inputTmp = addEle(tdTmp, "input", "inputText").attr("type", "text")
				} else if (rowTmp.dataType == 9) { //DATATYPE_LOCATION
					inputTmp = addEle(tdTmp, "input", "inputText").attr("type", "text")
					rowTmp.buttonElement = addEle(tdTmp, "button", "btn_type small").html("지도열기").css({"margin-left":"10px"})
					rowTmp.buttonElement.click(function() {
						openMapForLocation()
					})
				} else if (rowTmp.dataType == 10) { //DATATYPE_ATTACHMENT
					inputTmp = addEle(popupBody, "input", "inputText").attr("type", "hidden")
					rowTmp.fileUploadDiv = addDiv(tdTmp).attr("id", vPopupId + "_" + rowTmp.name + "_fileupload")
					rowTmp.fileUpload = initializeFileUpload({
											id: vPopupId + "_" + rowTmp.name + "_fileupload",
											isImage: false,
											contextPath: vContextPath,
											maxFileNameLen: 30,
										})
				} else if (rowTmp.dataType == 11) { //DATATYPE_IMAGE_ATTACHMENT
					inputTmp = addEle(popupBody, "input", "inputText").attr("type", "hidden")
					rowTmp.fileUploadDiv = addDiv(tdTmp).attr("id", vPopupId + "_" + rowTmp.name + "_fileupload")
					rowTmp.fileUpload = initializeFileUpload({
											id: vPopupId + "_" + rowTmp.name + "_fileupload",
											isImage: true,
											contextPath: vContextPath,
											maxFileNameLen: 30,
										})
				} else if (rowTmp.dataType == 12) {
					inputTmp = addEle(tdTmp, "textarea", "inputText").css({"height":"80px"})
				} else if (rowTmp.dataType == 13) {
					inputTmp = addEle(tdTmp, "textarea", "inputTextForNE").css({"height":"200px"})
				}
				if (inputTmp) {
					inputTmp.attr("id", vPopupId + "_" + rowTmp.name)
				}
				if (rowTmp.dataType == 7) { //DATATYPE_DATE
					createDatePicker(inputTmp)
				} else if (rowTmp.dataType == 8) { //DATATYPE_DATETIME
					createDateTimePicker(inputTmp)
				}
				
				if (rowTmp.dataType == 2 && rowTmp.codeType && rowTmp.codes) {
					_fillUpSelect(inputTmp, 'code', 'codeNameLocale', rowTmp.defaultValue, rowTmp.codes)
				}
				rowTmp.inputElement = inputTmp
				rowTmp.trElement = trTmp
			}
		}
		if (vFragmentModel.gridFragmentId) {
			vInsertPopupHeight += 400
			vDetailPopupHeight += 400
			vUpdatePopupHeight += 400
			trTmp = addTr(bodyTable)
			tdTmp = addEle(trTmp, "td")
			tdTmp.attr("colspan", maxColSpan)
			tdTmp.css({"padding":"0px 0px 0px 0px"})
			vGridBuilder = FragmentBuilder.newGridBuilder({
				contextPath: vContextPath,
				fragmentId: vFragmentModel.gridFragmentId,
				parentElement: tdTmp,
				paramData: vParamData,
			})
		}
		if (vInsertPopupHeight > 800) {
			vInsertPopupHeight = 800
		}
		if (vDetailPopupHeight > 800) {
			vDetailPopupHeight = 800
		}
		if (vUpdatePopupHeight > 800) {
			vUpdatePopupHeight = 800
		}
		
		var popupFoot = addDiv(popupDiv, "pop_foot")
		var buttonBox = addDiv(popupFoot, "btn_box")
		if (vFragmentModel.prevUrl) {
			vPrevButton = addEle(buttonBox, "a", "btn_type prev").attr("href", "#none").html("Prev").css({"float":"left"})
			vPrevButton.click(function() {
				queryPrev()
			})
		}
		if (vFragmentModel.nextUrl) {
			vNextButton = addEle(buttonBox, "a", "btn_type next").attr("href", "#none").html("Next").css({"float":"right"})
			vNextButton.click(function() {
				queryNext()
			})
		}
		if (vFragmentModel.confirmButton) {
			var confirmButton = addEle(buttonBox, "a", "btn_type submit").attr("href", "#none").html(vFragmentModel.confirmButton)
			confirmButton.click(function() {
				if (vPopupMode == 'INSERT') {
					insertData()
				} else if (vPopupMode == 'UPDATE') {
					updateData()
				} else {
					closePopup()
				}
			})
		}
		if (vFragmentModel.cancelButton) {
			var cancelButton = addEle(buttonBox, "a", "btn_type normal").attr("href", "#none").html(vFragmentModel.cancelButton)
			cancelButton.click(function() {
				closePopup()
			})
		}
	}
	
	function getSelectDefaultClosure(inputTmp, defaultVal) {
		return function() {
			inputTmp.val(defaultVal)
		}
	}
	
	function resetPopup() {
		vQueriedData = undefined
		var rowTmp
		if (vPopupMode == 'INSERT' || vPopupMode == 'UPDATE') {
			if (vPrevButton) {
				vPrevButton.hide()
			}
			if (vNextButton) {
				vNextButton.hide()
			}
		}
		if (vPopupMode == 'DETAIL') {
			if (vPrevButton) {
				vPrevButton.show()
			}
			if (vNextButton) {
				vNextButton.show()
			}
		}
		for (var ii=0; ii<vFragmentModel.rows.length; ii++) {
			rowTmp = vFragmentModel.rows[ii]
			if (rowTmp.inputElement) {
				if (rowTmp.defaultValue) {
					if (rowTmp.defaultValue.indexOf("{{") == 0) {
						var ret
						if (vParamData) {
							ret = rowTmp.defaultValue
							for (var key in vParamData) {
								if (ret.indexOf("{{" + key + "}") >= 0) {
									ret = ret.replaceAll("{{" + key + "}", vParamData[key]);
								}
							}
						} else {
							ret = ''
						}
						rowTmp.inputElement.val(ret)
					} else {
						rowTmp.inputElement.val(rowTmp.defaultValue)
					}
				} else {
					rowTmp.inputElement.val('')
				}
				rowTmp.inputElement.removeAttr('data')
			}
			if (rowTmp.inputElement && rowTmp.trElement) {
				if ((vPopupMode == 'DETAIL' && (rowTmp.showType & 1) == 1) ||
					(vPopupMode == 'INSERT' && (rowTmp.showType & 2) == 2) ||
					(vPopupMode == 'UPDATE' && (rowTmp.showType & 4) == 4)) {
					rowTmp.trElement.show()
				} else {
					rowTmp.trElement.hide()
				}
				if ((vPopupMode == 'DETAIL') ||
					(vPopupMode == 'INSERT' && (rowTmp.showType & 8) == 8) ||
					(vPopupMode == 'UPDATE' && (rowTmp.showType & 16) == 16)) {
					if (rowTmp.inputElement) {
						rowTmp.inputElement.prop("readonly", true)
						rowTmp.inputElement.prop("disabled", true)
					}
					if (rowTmp.dataType == 9) { //Location
						rowTmp.buttonElement.hide()
						rowTmp.inputElement.css("width", "")
					} else if (rowTmp.dataType == 10 || rowTmp.dataType == 11) { //FileUpload
						rowTmp.fileUpload.setEditable(false)
					} else if (rowTmp.dataType == 13) {
						if (!rowTmp.neEditor) {
							rowTmp.neEditor = initializeNaverEditor({
												id: rowTmp.inputElement.attr("id"),
												contextPath: vContextPath,
												callback: getNeEditorDisableClosure(rowTmp),
											})
						} else {
							rowTmp.neEditor.resetHtml()
							rowTmp.neEditor.disable()
						}
					}
				} else {
					if (rowTmp.inputElement) {
						rowTmp.inputElement.prop("readonly", false)
						rowTmp.inputElement.prop("disabled", false)
					}
					if (rowTmp.dataType == 9) { //Location
						rowTmp.buttonElement.show()
						rowTmp.buttonElement.css({"width":"120px"})
						rowTmp.inputElement.css({"width":"calc(100% - 138px)"})
					} else if (rowTmp.dataType == 10 || rowTmp.dataType == 11) { //FileUpload
						rowTmp.fileUpload.setEditable(true)
					} else if (rowTmp.dataType == 13) {
						if (!rowTmp.neEditor) {
							rowTmp.neEditor = initializeNaverEditor({
												id: rowTmp.inputElement.attr("id"),
												contextPath: vContextPath,
												callback: getNeEditorEnableClosure(rowTmp),
											})
						} else {
							rowTmp.neEditor.resetHtml()
							rowTmp.neEditor.enable()
						}
					}
				}
			}
		}
	}
	
	function getNeEditorEnableClosure(rowTmp) {
		return function() {
			rowTmp.neEditor.resetHtml()
			rowTmp.neEditor.enable()
		}
	}
	
	function getNeEditorDisableClosure(rowTmp) {
		return function() {
			rowTmp.neEditor.resetHtml()
			rowTmp.neEditor.disable()
		}
	}
	
	function openMapForLocation() {
		alert("지도를 연다.")
	}
	
	function checkData() {
		var rowTmp
		for (var ii=0; ii<vFragmentModel.rows.length; ii++) {
			rowTmp = vFragmentModel.rows[ii]
			if (rowTmp.inputElement && rowTmp.mandatory == "Y") {
				if (rowTmp.inputElement.val() == '' || rowTmp.inputElement.val() == null || rowTmp.inputElement.val() == undefined) {
					alert(rowTmp.mandatoryErrorMsg)
					return false
				}
			}
		}
		return true
	}
	
	function insertData() {
		if (false == checkData()) {
			return
		}
		if (vChangeCallBack) {
			if (false == vChangeCallBack('INSERT', vSelf)) {
				return
			}
		}
		uploadAttachment(
				function() {
					nesAjax(retrieveUrl(vFragmentModel.insertUrl),
							JSON.stringify(retrieveData()),
							function(data) {
								if (vParentGridBuilder) {
									vParentGridBuilder.refreshGrid()
								}
								closePopup()
							},
							function(data) {
								if (vFragmentModel.insertErrorMsg) {
									alert(vFragmentModel.insertErrorMsg)
								} else {
									alert(JSON.stringify(data))
								}
							},
							vFragmentModel.insertMethod)
				}, 
				function(data) {
					alert('파일업로드에 실패했습니다.' + JSON.stringify(data))
				})
	}
	
	function uploadAttachment(succ, fail) {
		var attachCount = 0
		var currentIndex = 0;
		var rowTmp
		for (var ii=0; ii<vFragmentModel.rows.length; ii++) {
			rowTmp = vFragmentModel.rows[ii]
			if (rowTmp.fileUpload) {
				attachCount++
			}
		}
		if (attachCount == 0) {
			succ()
			return
		}
		for (var ii=0; ii<vFragmentModel.rows.length; ii++) {
			rowTmp = vFragmentModel.rows[ii]
			if (rowTmp.fileUpload) {
				uploadAttachmentClosure(rowTmp,
						function(data, rowEle) {
							if (data) {
								rowEle.inputElement.val(data.idKey)
								rowEle.fileUpload.resetFile()
							}
							currentIndex++
							if (currentIndex >= attachCount) {
								succ()
							}
						},
						function(data) {
							fail(data)
						})
			}
		}
	}
	
	function uploadAttachmentClosure(rowTmp, succ, fail) {
		if (rowTmp.fileUpload) {
			rowTmp.fileUpload.uploadFile(
					function(data) {
						succ(data, rowTmp)
					},
					function(data) {
						fail(data)
					})
		}
	}
	
	function updateData() {
		if (false == checkData()) {
			return
		}
		if (vChangeCallBack) {
			if (false == vChangeCallBack('UPDATE', vSelf)) {
				return
			}
		}
		uploadAttachment(
				function() {
					nesAjax(retrieveUrl(vFragmentModel.updateUrl),
							JSON.stringify(retrieveData()),
							function(data) {
								if (vParentGridBuilder) {
									vParentGridBuilder.refreshGrid()
								}
								closePopup()
							},
							function(data) {
								if (vFragmentModel.updateErrorMsg) {
									alert(vFragmentModel.updateErrorMsg)
								} else {
									alert(JSON.stringify(data))
								}
							},
							vFragmentModel.updateMethod)
				}, 
				function(data) {
					alert('파일업로드에 실패했습니다.' + JSON.stringify(data))
				})
	}
	
	function displayData(data) {
		var rowTmp
		for (var ii=0; ii<vFragmentModel.rows.length; ii++) {
			rowTmp = vFragmentModel.rows[ii]
			if (rowTmp.inputElement) {

				if (rowTmp.dataType == 7) { //DATATYPE_DATE
					rowTmp.inputElement.datepicker('setDate', data[rowTmp.name])
				} else if (rowTmp.dataType == 8) { //DATATYPE_DATETIME
					rowTmp.inputElement.datetimepicker('setDate', data[rowTmp.name])
				} else {
					rowTmp.inputElement.val(data[rowTmp.name])
					if (data[rowTmp.name]) {
						rowTmp.inputElement.attr('data', data[rowTmp.name])
					}
					if (rowTmp.dataType == 2) {
						rowTmp.inputElement.change()
					}
				}
			}
			if (rowTmp.neEditor) {
				rowTmp.neEditor.pasteHtml(data[rowTmp.name])
			}			
			if (rowTmp.fileUpload) {
				rowTmp.fileUpload.resetFile()
				rowTmp.fileUpload.setGroupIdKey(data[rowTmp.name])
			}
		}
		vQueriedData = data
	}
	
	function retrieveData() {
		var ret = {}
		var rowTmp
		for (var ii=0; ii<vFragmentModel.rows.length; ii++) {
			rowTmp = vFragmentModel.rows[ii]
			if (rowTmp.neEditor) {
				rowTmp.neEditor.updateContentsField()
			}			
			if (rowTmp.inputElement) {
				if (rowTmp.dataType == 7) { //DATATYPE_DATE
					if (rowTmp.inputElement.datepicker('getDate')) {
						ret[rowTmp.name] = rowTmp.inputElement.datepicker('getDate').getTime()
					}
				} else if (rowTmp.dataType == 8) { //DATATYPE_DATETIME
					if (rowTmp.inputElement.datetimepicker('getDate')) {
						ret[rowTmp.name] = rowTmp.inputElement.datetimepicker('getDate').getTime()
					}
				} else {
					ret[rowTmp.name] = rowTmp.inputElement.val()
				}
			}
		}
		ret.reqToken = getAuthToken()
		if (vGridBuilder && vGridBuilder.getJQGridTable()) {
			if (ret.list) {
				alert("그리드 데이터를 가져올 수 없음 List변수 중복!!!")
				return
			}
			ret.list = vGridBuilder.getJQGridData()
		}
		return ret
	}
	
	function retrieveUrl(url, rowData) {
		var ret = url
		if (rowData) {
			for (var key in rowData) {
				if (ret.indexOf("{{" + key + "}") >= 0) {
					ret = ret.replaceAll("{{" + key + "}", rowData[key]);
				}
			}
		}
		if (vQueriedData) {
			for (var key in vQueriedData) {
				if (ret.indexOf("{{" + key + "}") >= 0) {
					ret = ret.replaceAll("{{" + key + "}", vQueriedData[key]);
				}
			}
		}
		if (vParamData) {
			for (var key in vParamData) {
				if (ret.indexOf("{{" + key + "}") >= 0) {
					ret = ret.replaceAll("{{" + key + "}", vParamData[key]);
				}
			}
		}
		ret = vContextPath + ret
		if (getAuthToken()) {
			ret = ret + "?q=" + getAuthToken()
		}
		return ret
	}
	
	function queryDetail(rowData) {
		innerQueryDetail(retrieveUrl(vFragmentModel.selectUrl, rowData))
	}
	
	function queryPrev() {
		innerQueryDetail(retrieveUrl(vFragmentModel.prevUrl))
	}
	
	function queryNext() {
		innerQueryDetail(retrieveUrl(vFragmentModel.nextUrl))
	}
	
	function innerQueryDetail(url) {
		nesAjax(url,
				null,
				function(data) {
					displayData(data)
				},
				function(data) {
					if (data.status == 428) {
						alert("최초 데이터입니다.")
					} else if (data.status == 429) {
						alert("취종 데이터입니다.")
					} else {
						if (vFragmentModel.selectErrorMsg) {
							alert(vFragmentModel.selectErrorMsg)
						} else {
							alert(JSON.stringify(data))
						}
					}
				},
				"GET")
	}
	
	function openPopup(popwidth, popheight) {
		vPopupROOT.css({'display':'block', 'width':$(window).width()+'px','height':$(window).height()+'px'});
		vPopupROOT.find('.layerpop').css({'display':'block'})
		if (popwidth) {
			vPopupROOT.find('.layerpop').css({'width':popwidth+'px','margin-left':-parseInt(popwidth/2)+'px'});
		}
		if (popheight) {
			vPopupROOT.find('.layerpop').css({'height':popheight+'px','margin-top':-parseInt(popheight/2)+'px'});
		}
		var body_HT = vPopupROOT.find('.pop_head').outerHeight() + vPopupROOT.find('.pop_foot').outerHeight();
		vPopupROOT.find('.layerpop .pop_body').css({'height':popheight - body_HT-20 +'px'});
	}
	
	function closePopup() {
		vPopupROOT.css('display','none');
		vPopupROOT.find('.layerpop').css('display','none');
		if (FragmentBuilder.MapFragment[vSelf.getPopupId()]) {
			vPopupROOT.remove()
			delete FragmentBuilder.MapFragment[vSelf.getPopupId()]
		}
	}
	
	this.openProper = function() {
		if (vParamData) {
			nesAjax(retrieveUrl(vFragmentModel.selectUrl),
					null,
					function(data) {
						vPopupMode = 'UPDATE'
						resetPopup()
						vPopupTitle.html(vFragmentModel.updateTitle)
						displayData(data)
						openPopup(vFragmentModel.width, vFragmentModel.height > 0 ? vFragmentModel.height : vUpdatePopupHeight)
					},
					function(data) {
						if (data.status == 404) {
							vSelf.openInsert()
						} else {
							alert(JSON.stringify(data))
						}
					},
					"GET")
		} else {
			vSelf.openInsert()
		}
	}
	
	this.openInsert = function() {
		if (vOpenCallBack) {
			if (false == vOpenCallBack('INSERT', vSelf)) {
				return
			}
		}
		vPopupMode = 'INSERT'
		resetPopup()
		vPopupTitle.html(vFragmentModel.insertTitle)
		openPopup(vFragmentModel.width, vFragmentModel.height > 0 ? vFragmentModel.height : vInsertPopupHeight)
	}
	
	this.openDetail = function(rowData) {
		if (vOpenCallBack) {
			if (false == vOpenCallBack('DETAIL', vSelf)) {
				return
			}
		}
		vPopupMode = 'DETAIL'
		resetPopup()
		vPopupTitle.html(vFragmentModel.selectTitle)
		queryDetail(rowData)
		openPopup(vFragmentModel.width, vFragmentModel.height > 0 ? vFragmentModel.height : vDetailPopupHeight)
	}
	
	this.openUpdate = function(rowData) {
		if (vOpenCallBack) {
			if (false == vOpenCallBack('UPDATE', vSelf)) {
				return
			}
		}
		vPopupMode = 'UPDATE'
		resetPopup()
		vPopupTitle.html(vFragmentModel.updateTitle)
		queryDetail(rowData)
		openPopup(vFragmentModel.width, vFragmentModel.height > 0 ? vFragmentModel.height : vUpdatePopupHeight)
	}
	
	this.deleteData = function(rowData) {
		if (vChangeCallBack) {
			if (false == vChangeCallBack('DELETE', vSelf)) {
				return
			}
		}
		if (false == confirm(vFragmentModel.deleteConfirm)) {
			return
		}
		nesAjax(retrieveUrl(vFragmentModel.deleteUrl, rowData),
				null,
				function(data) {
					if (vParentGridBuilder) {
						vParentGridBuilder.refreshGrid()
					}
				},
				function(data) {
					if (vFragmentModel.deleteErrorMsg) {
						alert(vFragmentModel.deleteErrorMsg)
					} else {
						alert(JSON.stringify(data))
					}
				},
				vFragmentModel.deleteMethod)
	}
}
