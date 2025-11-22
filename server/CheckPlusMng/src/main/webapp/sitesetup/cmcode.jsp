<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridCodeType
var $gridCode
startFuncs[startFuncs.length] = function() {
	$gridCodeType = initializeGrid({
			id:"codeTypeGrid",
			container:"codeTypeLayout",
			cellEdit: true,
			colModel: [
					{ name: 'no'          , label: 'NO'        , width: 100, align: 'center',},
					{ name: 'type'        , label: '코드유형'  , width: 200, align: 'center', editable: getGridEditableFunc , edittype:'text', editrules:{required:true}},
					{ name: 'typeName'    , label: '유형명'    , width: 200, align: 'center', editable: true, edittype:'text', editrules:{required:true}},
			],
			onCellSelect: function (rowId, iCol, rowData) {
				getCodeList(rowData.type)
			},
		})

	$gridCode = initializeGrid({
			id:"codeGrid",
			container:"codeLayout",
			cellEdit: true,
			colModel: [
					{ name: 'no'          , label: 'NO'        , width: 100, align: 'center',},
					{ name: 'type'        , label: '코드유형'  , hidden: true,},
					{ name: 'code'        , label: '코드'      , width: 200, align: 'center', editable: true, edittype:'text', editrules:{required:true}},
					{ name: 'codeName'    , label: '이름'      , width: 200, align: 'center', editable: true, edittype:'text', editrules:{required:true}},
					{ name: 'comment'     , label: '비고'      , width: 200, align: 'center', editable: true, edittype:'text', editrules:{required:true}},
			],
		})
	refreshList()
}

function refreshList() {
	getTypeList(__currentPage)
	clearCodeList()
}

function getTypeList(page) {
	var url = "${contextPath}/svc/v1/codetype/list/" + page + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$gridCodeType.setDataList(data.list)
				pagingInfo_success(data.paging, refreshList)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET")
}

function addCodeTypeRow() {
	$gridCodeType.addRowData(undefined, {touch: "I"}, "last")
	var lastRowIndex = getGridLastRowIndex($gridCodeType)
	if (lastRowIndex) {
		$gridCodeType.editCell(lastRowIndex, 2, true)
	}
}

function saveCodeTypes() {
	finalizeEditingGrid($gridCodeType);
	var touchedList = getGridTouchedList($gridCodeType)
	for (var ii=0; ii<touchedList.length; ii++) {
		if (!touchedList[ii].type || touchedList[ii].type.trim() == "") {
			touchedList.splice(ii, 1)
			ii--
		}
	}
	if (!touchedList || touchedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/codetype/list"
	var paging = {}
	paging.authToken = getAuthToken()
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

function delCodeTypes() {
	finalizeEditingGrid($gridCodeType);
	var selectedList = getGridSelectedList($gridCodeType)
	if (!selectedList || selectedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/codetype/list/delete"
	var paging = {}
	paging.authToken = getAuthToken()
	paging.list = selectedList;
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

var selectedType
function clearCodeList() {
	$gridCode.setDataList([])
	selectedType = undefined
}

function refreshCodeList() {
	getCodeList(selectedType)
}

function getCodeList(type) {
	if (!type || type.trim() == "") {
		return
	}
	var url = "${contextPath}/svc/v1/code/" + type + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				selectedType = type
				$gridCode.setDataList(data.list)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET")
	clearCodeList()
}

function addCodeRow() {
	if (selectedType == undefined) {
		return
	}
	$gridCode.addRowData(undefined, {type: selectedType, touch: "I"}, "last")
	var lastRowIndex = getGridLastRowIndex($gridCode)
	if (lastRowIndex) {
		$gridCode.editCell(lastRowIndex, 3, true)
	}
}

function saveCodes() {
	finalizeEditingGrid($gridCode)
	var touchedList = getGridTouchedList($gridCode)
	if (!touchedList || touchedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/code/list"
	var paging = {}
	paging.authToken = getAuthToken()
	paging.list = touchedList;
	nesAjax(url,
			JSON.stringify(paging),
			function(data) {
				refreshCodeList()
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function delCodes() {
	finalizeEditingGrid($gridCode)
	var selectedList = getGridSelectedList($gridCode)
	if (!selectedList || selectedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/code/list/delete"
	var paging = {}
	paging.authToken = getAuthToken()
	paging.list = selectedList;
	nesAjax(url,
			JSON.stringify(paging),
			function(data) {
				refreshCodeList()
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function recreateTable() {
	var url = "${contextPath}/svc/v1/init/serverbase/recreate/cmcode?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"PUT")
}
</script>
<div style="font-size:20px;margin-top:15px;"><strong>공통코드관리</strong></div>
<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<span style="float:left;font-weight:700;padding:5px;">코드유형</span>
	<button class="btn_type small" onclick="recreateTable()">코드재생성</button>	
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="addCodeTypeRow()">추가</button>
	<button class="btn_type small" onclick="delCodeTypes()">삭제</button>
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="saveCodeTypes()">저장</button>
</div>
<div id="codeTypeLayout">
	<table id="codeTypeGrid" style="width:100%;"></table>
</div>
<jsp:include page="/common/paging.jsp"/>

<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<span style="float:left;font-weight:700;padding:5px;">코드</span>
	<button class="btn_type small" onclick="addCodeRow()">추가</button>
	<button class="btn_type small" onclick="delCodes()">삭제</button>
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="saveCodes()">저장</button>
</div>
<div id="codeLayout">
	<table id="codeGrid" style="width:100%;"></table>
</div>

<jsp:include page="/common/footer.jsp"/>
