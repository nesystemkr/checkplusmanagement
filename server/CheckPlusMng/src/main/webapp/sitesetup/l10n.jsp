<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridL10N
var $gridL10NLocale
startFuncs[startFuncs.length] = function() {
	$gridL10N = initializeGrid({
			id:"L10NGrid",
			container:"L10NLayout",
			cellEdit: true,
			colModel: [
				{ name: 'idKey'        , hidden: true,},
				{ name: 'no'           , label: 'NO'          , width: 50, align: 'center',},
				{ name: 'idString'     , label: '문자열아이디', width: 400, align: 'left',
						editable: getGridEditableFunc , edittype:'text', editrules:{required:true}},
				{ name: 'defaultString', label: '문자열'      , align: 'left',
						editable: true, edittype:'text', editrules:{required:true}},
			],
			onCellSelect: function (rowId, iCol, rowData) {
				getL10NLocaleList(rowData.idKey, rowData.idString)
			},
			stretchColumn: 'defaultString',
	})

	initCommonCodeForGridSelect('${contextPath}', [{'type':'LOCALE'}],
			function(data) {
				$gridL10NLocale = initializeGrid({
						id:"L10NLocaleGrid",
						container:"L10NLocaleLayout",
						cellEdit: true,
						colModel: [
							{ name: 'idKey'        , hidden: true,},
								{ name: 'no'          , label: 'NO'           , width: 50 , align: 'center',},
								{ name: 'idString'    , label: '문자열아이디,', width: 300, align: 'left',},
								{ name: 'locale'      , label: '지역코드'     , width: 100 , align: 'center',
										editable: true, cellsubmit: 'clientArray', formatter: 'select', edittype: 'select',
										editoptions:{ value : data.LOCALE },
								},
								{ name: 'localeString', label: '지역문자열'   , width: 200, align: 'left',
										editable: true, edittype:'text', editrules:{required:true}},
						],
						stretchColumn: 'localeString',
				})
	})
	refreshList()
}

function refreshList() {
	getL10NList(__currentPage)
	clearL10NLocaleList()
}

function getL10NList(page) {
	var url = "${contextPath}/svc/v1/l10n/list/" + page + "?q=" + getAuthToken()
	nesAjax(url, null,
		function(data) {
			$gridL10N.setDataList(data.list)
			pagingInfo_success(data.paging, refreshList)
		},
		function(data) {
			alert(JSON.stringify(data))
		},
		"GET")
}

function addL10NRow() {
	$gridL10N.addRowData(undefined, {touch: "I"}, "last")
	var lastRowIndex = getGridLastRowIndex($gridL10N)
	if (lastRowIndex) {
		$gridL10N.editCell(lastRowIndex, 2, true)
	}
}

function saveL10Ns() {
	finalizeEditingGrid($gridL10N);
	var touchedList = getGridTouchedList($gridL10N)
	for (var ii=0; ii<touchedList.length; ii++) {
		if (!touchedList[ii].idString || touchedList[ii].idString.trim() == "") {
			touchedList.splice(ii, 1)
			ii--
		}
	}
	if (!touchedList || touchedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/l10n/list"
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

function delL10Ns() {
	finalizeEditingGrid($gridL10N);
	var selectedList = getGridSelectedList($gridL10N)
	if (!selectedList || selectedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/l10n/list/delete"
	var paging = {}
	paging.reqToken = getAuthToken()
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

var selectedIdKey
var selectedIdString
function clearL10NLocaleList() {
	if ($gridL10NLocale) {
		$gridL10NLocale.setDataList([])
	}
	selectedIdKey = undefined
	selectedIdString = undefined
}

function refreshL10NLocaleList() {
	getL10NLocaleList(selectedIdKey, selectedIdString)
}

function getL10NLocaleList(idKey, idString) {
	if (!idString || idString.trim() == "") {
		return
	}
	if (!idKey || idKey == 0 || idKey.trim() == "") {
		clearL10NLocaleList()
		return
	}
	var url = "${contextPath}/svc/v1/l10n/" + idKey + "/locale/list?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				selectedIdKey = idKey
				selectedIdString = idString
				$gridL10NLocale.setDataList(data.list)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET")
	clearL10NLocaleList()
}

function addL10NLocaleRow() {
	if (selectedIdString == undefined) {
		return
	}
	$gridL10NLocale.addRowData(undefined, {idString: selectedIdString, touch: "I"}, "last")
	var lastRowIndex = getGridLastRowIndex($gridL10NLocale)
	if (lastRowIndex) {
		$gridL10NLocale.editCell(lastRowIndex, 3, true)
	}
}

function saveL10NLocales() {
	finalizeEditingGrid($gridL10NLocale)
	var touchedList = getGridTouchedList($gridL10NLocale)
	for (var ii=0; ii<touchedList.length; ii++) {
		if (!touchedList[ii].locale || touchedList[ii].locale.trim() == "") {
			touchedList.splice(ii, 1)
			ii--
		}
	}
	if (!touchedList || touchedList.length == 0) {
		return
	}
	if (selectedIdString == undefined) {
		return
	}
	var url = "${contextPath}/svc/v1/l10n/" + selectedIdKey + "/locale/list"
	var paging = {}
	paging.reqToken = getAuthToken()
	paging.list = touchedList;
	nesAjax(url,
			JSON.stringify(paging),
			function(data) {
				refreshL10NLocaleList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 지역코드의 문자열이 존재합니다.");
				} else {
					alert(JSON.stringify(data))
				}
			},
			"POST")
}

function delL10NLocales() {
	finalizeEditingGrid($gridL10NLocale)
	var selectedList = getGridSelectedList($gridL10NLocale)
	if (!selectedList || selectedList.length == 0) {
		return
	}
	if (selectedIdString == undefined) {
		return
	}
	var url = "${contextPath}/svc/v1/l10n/" + selectedIdKey + "/locale/list/delete"
	var paging = {}
	paging.reqToken = getAuthToken()
	paging.list = selectedList;
	nesAjax(url,
			JSON.stringify(paging),
			function(data) {
				refreshL10NLocaleList()
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function refreshL10N() {
	var url = "${contextPath}/svc/v1/l10n/refresh?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET")
}

function reloadFromFileL10N() {
	var url = "${contextPath}/svc/v1/l10n/reloadfromfile?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET")
}
</script>
<div style="font-size:20px;margin-top:15px;"><strong>언어별문자열관리</strong></div>
<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<span style="float:left;font-weight:700;padding:5px;">코드리스트</span>
	<button class="btn_type small" onclick="refreshL10N()">재로드</button>
	<button class="btn_type small" onclick="reloadFromFileL10N()">파일에서재로드</button>
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="addL10NRow()">추가</button>
	<button class="btn_type small" onclick="delL10Ns()">삭제</button>
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="saveL10Ns()">저장</button>
</div>
<div id="L10NLayout">
	<table id="L10NGrid"></table>
</div>
<jsp:include page="/common/paging.jsp"/>

<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<span style="float:left;font-weight:700;padding:5px;">지역별언어</span>
	<button class="btn_type small" onclick="addL10NLocaleRow()">추가</button>
	<button class="btn_type small" onclick="delL10NLocales()">삭제</button>
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="saveL10NLocales()">저장</button>
</div>
<div id="L10NLocaleLayout">
	<table id="L10NLocaleGrid" style="width:100%;"></table>
</div>

<jsp:include page="/common/footer.jsp"/>
