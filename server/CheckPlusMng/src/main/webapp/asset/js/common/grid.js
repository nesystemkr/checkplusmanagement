function initializeGrid(options) {
	if (!options.id || !options.colModel) {
		return
	}
	
	for (var ii=0; ii<options.colModel.length; ii++) {
		if (options.colModel[ii].autowidth == undefined) {
			options.colModel[ii].autowidth = false;
		}
		if (options.colModel[ii].shrinkToFit == undefined) {
			options.colModel[ii].shrinkToFit = false;
		}
		if (options.colModel[ii].sortable == undefined) {
			options.colModel[ii].sortable = false;
		}
	}
	
	if (options.cellEdit == true) {
		options.colModel[options.colModel.length] = { name: 'touch', label: 'TOUCH', hidden: true,}
	}
	
	var retGrid = $("#" + options.id).jqGrid({
		colModel: options.colModel,  // 컬럼정보 assign
		multiselect: options.showCheckbox == undefined ? true : options.showCheckbox, // checkbox 표시여부
		multiselectWidth: 30, // checkbox width
		autowidth: options.stretchColumn ? false : true,
		shrinkToFit: false,
		cellEdit: options.cellEdit ? options.cellEdit : false,
		onSortCol: function (columnName, columnIndex, sortOrder) {
			if (this.p.lastsort >= 0 && this.p.lastsort !== columnIndex &&
				this.p.colModel[this.p.lastsort].sortable !== false) {
				// show the icons of last sorted column
				$(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
//				$(this.grid.headers[this.p.lastsort].el).removeClass('sortedColumnHeader');
			}
//			$(this.grid.headers[idxcol].el).addClass('sortedColumnHeader');
			if (options.onSortCol) {
				options.onSortCol(columnName, columnIndex, sortOrder);
			}
			return 'stop';
		},
		onSelectRow: function(rowId) {
			if (options.onSelectRow) {
				options.onSelectRow(rowId, retGrid.getRowData(rowId));
			}
		},
		ondblClickRow: function(rowId) {
			if (options.ondblClickRow) {
				options.ondblClickRow(rowId, retGrid.getRowData(rowId));
			}
		},
		onCellSelect: function(rowId, iCol) {
			if (options.onCellSelect) {
				options.onCellSelect(rowId, iCol, retGrid.getRowData(rowId));
			}
		},
		loadComplete: function() {
			if (options.container) {
				resizeToFitGridContainer($(this), options)
			}
			if (options.loadComplete) {
				options.loadComplete()
			}
		},
		beforeSelectRow: function (rowId, e) {		// checkbox column만 클릭되게 만든다. 이 event를 빼면, row를 클릭해도 check가 toggle된다.
			var iCol = $.jgrid.getCellIndex($(e.target).closest('td')[0]);
			var cm = retGrid.jqGrid('getGridParam', 'colModel');
			return cm[iCol].name === 'cb'
		},
		afterSaveCell: function (rowId, nm, tmp, iRow, iCol) {
			var rowData = retGrid.getRowData(rowId);
			if (!rowData.touch && rowData.touch != "I") {
				rowData.touch = "U"
				retGrid.setRowData(rowId, rowData)
			}
		},
//		beforeEditCell: function (rowid, cellname, value, iRow, iCol) {
//			alert("beforeEditCell " + rowid + " " + cellname + " " + value + " " + iRow + " " + iCol)
//		},
//		afterEditCell: function (rowid, nm, tmp, iRow, iCol) {
//			alert(rowid + " " + nm + " " + tmp + " " + iRow + " " + iCol)
//		},
//		afterEditCell: function (rowid, cellname, value, iRow, iCol) {
//			alert("afterEditCell " + rowid + " " + cellname + " " + value + " " + iRow + " " + iCol)
//		},
//		afterRestoreCell: function (rowid, value, iRow, iCol) {
//			var data = retGrid.getRowData(rowid)
//			if (!data.touch) {
//				data.touch = "U"
//			} else if (data.touch != "I") {
//				data.touch = "U"
//			}
//		},
	});
	retGrid.setDataList = function(list) {
		$(this).clearGridData()
		$(this).jqGrid('setGridParam',{datatype: 'local', gridview: true, data: list}).trigger("reloadGrid")
	}
	if (options.container) {
		resizeToFitGridContainer(retGrid, options)
	}
	
	return retGrid;
}

function resizeToFitGridContainer(gridLayout, options) {
	setTimeout(function() {
			if (options.stretchColumn) {
				var newWidth = $("#" + options.container).width()
				if (options.showCheckbox == undefined || options.showCheckbox == true) {
					newWidth -= 30
				}
				for (var ii=0; ii<options.colModel.length; ii++) {
					if (options.colModel[ii].name == options.stretchColumn) {
						continue
					}
					if (options.colModel[ii].hidden == true) {
						continue
					}
					if (!options.colModel[ii].width) {
						continue
					}
					newWidth -= options.colModel[ii].width
				}
				gridLayout.jqGrid('setColProp', options.stretchColumn, {'widthOrg': newWidth})
			}
			gridLayout.jqGrid('setGridWidth', $("#" + options.container).width(), true)
	}, 20)
}

function getGridEditableFunc(options) {
	var rowData = getGridDataByRowId($(this), options.rowid);
	return rowData.touch == "I"
}

function getGridButtonClosure(buttons) {
	return function (cellvalue, opts, rowdata) {
		var ret = '<div rowId="' + opts.rowId + '" class="fonticon-wrap grid-detail-button">'
		for (var ii=0; ii<buttons.length; ii++) {
			if (buttons[ii].preSet == -1) {
				ret += '&nbsp;&nbsp;&nbsp;'
				continue;
			}
			ret += '<i class=' 
			if (buttons[ii].type == "del") {
				ret += '"far fa-trash-alt"' 
			} else if (buttons[ii].type == "search") {
				ret += '"fas fa-search"'
			} else if (buttons[ii].type == "edit") {
				ret += '"fas fa-edit"'
			} else if (buttons[ii].type == "key") {
				ret += '"fas fas fa-key"'
			} else if (buttons[ii].type == "stop") {
				ret += '"fas fa-stop"'
			} else if (buttons[ii].type == "start") {
				ret += '"fas fa-play"'
			} else {
				if (buttons[ii].preSet == 1) {
					ret += '"fas fa-search"'
				} else if (buttons[ii].preSet == 2) {
					ret += '"fas fa-edit"'
				} else if (buttons[ii].preSet == 3) {
					ret += '"far fa-trash-alt"'
				} else {
					if (buttons[ii].iconCls) {
						ret += '"' + buttons[ii].iconCls + '"'
					} else {
						ret += '""'
					}
				}
			}
			if (buttons[ii].callback) {
				ret += 'onclick=\'' + buttons[ii].callback + '("' +  opts.rowId + '", getGridRowDataWithGridIdNRowId("' + $(this).attr('id') + '","' + opts.rowId + '"), "' + $(this).attr('id') + '")\''
			} else {
				ret += 'onclick=\'FragmentBuilder.gridCallback("' +  opts.rowId + '", getGridRowDataWithGridIdNRowId("' + $(this).attr('id') + '","' + opts.rowId + '"), "' + $(this).attr('id') + '",' + (buttons[ii].preSet ? buttons[ii].preSet : 0) + ')\''
			}
			ret += 'style=\'cursor:pointer;\''
			ret += '></i>'
			ret += '&nbsp;&nbsp;'
		}
		ret += '</div>'
		return ret
	}
}

function getGridRowDataWithGridIdNRowId(gridId, rowId) {
	return $("#" + gridId).jqGrid().getRowData(rowId)
}

function getGridFullDateFormatClosure() {
	return function (cellvalue, opts, rowdata) {
		return dateTimeToFullString(cellvalue)
	}
}

function getGridDateFormatClosure() {
	return function (cellvalue, opts, rowdata) {
		return dateTimeToString(cellvalue)
	}
}

function getGridCommaNumberFormatClosure() {
	return function (cellvalue, opts, rowdata) {
		return numberWithCommas(cellvalue)
	}
}

function finalizeEditingGrid($grid) {
	var rowId = $grid.getGridParam('selrow')
	var iCol = $grid.getGridParam('iCol')
	$grid.saveCell(rowId, iCol)
}

function getGridLastRowId($grid) {
	var dataIds = $grid.getDataIDs()
	if (dataIds && dataIds.length > 0) {
		return dataIds[dataIds.length - 1]
	}
}

function getGridLastRowIndex($grid) {
	var dataIds = $grid.getDataIDs()
	if (dataIds && dataIds.length > 0) {
		return dataIds.length
	}
}

function getGridTouchedList($grid) {
	var list = []
	var dataIds = $grid.getDataIDs()
	for (var ii=0; ii<dataIds.length; ii++) {
		var rowData = $grid.getRowData(dataIds[ii])
		if (rowData.touch) {
			if (rowData.touch == "I" || rowData.touch == "U") {
				list[list.length] = rowData
			}
		}
	}
	return list
}

function getGridSelectedRowIds($grid) {
	return $grid.getGridParam('selarrrow')
}

function getGridSelectedList($grid) {
	var list = []
	var selectedDataIds = $grid.getGridParam('selarrrow')
	for (var ii=0; ii<selectedDataIds.length; ii++) {
		var rowData = $grid.getRowData(selectedDataIds[ii])
		list[list.length] = rowData
	}
	return list
}

function getGridDataByRowId($grid, rowId) {
	var datas = $grid.getGridParam('data')
	for (var ii=0; ii<datas.length; ii++) {
		if (rowId == datas[ii].id) {
			return datas[ii]
		}
	}
}
