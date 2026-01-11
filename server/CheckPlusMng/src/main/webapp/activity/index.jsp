<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
var $elementGridLayout
var $invoiceGridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelectByUrl("${contextPath}/svc/v1/project/list/0?q=" + getAuthToken(), "projectIdKey", "idKey", "name", "", "", "선택")
	createDatePicker('activity_deliveryDate')
	createDatePicker('element_startDate')
	createDatePicker('element_endDate')
	createDatePicker('invoice_issueDate')
	createDatePicker('invoice_entryDate')
7	
	var buttons = [
			{type:'search', callback: 'detailOne'},
			{type:'del'   , callback: 'deleteOne'},
	]
	$gridLayout = initializeGrid({
			id:"gridLayout",
			container:"listLayout",
			showCheckBox: false,
			colModel: [
					{ name: 'idKey'          , hidden: true, },
					{ name: 'brokerIdKey'    , hidden: true, },
					{ name: 'customerIdKey'  , hidden: true, },
					{ name: 'no'             , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'       , label: '아이디'  , width: 200, align: 'center',},
					{ name: 'projectIdString', label: '프로젝트', width: 200, align: 'center',},
					{ name: 'brokerName'     , label: '판매업체', width: 200, align: 'center',},
					{ name: 'customerName'   , label: '설치업체', width: 200, align: 'center',},
					{ name: 'deliveryDate'   , label: '납품일'  , width: 120, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'memo'           , label: '메모'    , width: 580, align: 'center',},
					{ name: 'action'         , label: 'ACTION'  ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	var buttons2 = [
			{type:'search', callback: 'detailElement'},
			{type:'del'   , callback: 'deleteElement'},
	]
	$elementGridLayout = initializeGrid({
			id:"elementGridLayout",
			container:"elementListLayout",
			showCheckBox: true,
			colModel: [
					{ name: 'idKey'        , hidden: true, },
					{ name: 'activityIdKey', hidden: true, },
					{ name: 'elementType'  , hidden: true, },
					{ name: 'elementIdKey' , hidden: true, },
					{ name: 'orderSeq    ' , hidden: true, },
					{ name: 'no'           , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'     , label: '아이디'  , width: 200, align: 'center',},
					{ name: 'elementTitle' , label: '설명'    , width: 580, editable: true, edittype:'text', align: 'center',},
					{ name: 'unitPrice'    , label: '단가'    , width: 120, editable: true, edittype:'text', align: 'center',},
					{ name: 'startDate'    , label: '시작일자', width: 80 , editable: true, edittype:'text', align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'endDate'      , label: '종료일자', width: 80 , editable: true, edittype:'text', align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'memo'         , label: '메모'    , width: 440, editable: true, edittype:'text', align: 'center',},
					{ name: 'action'       , label: 'ACTION'  ,                             align: 'center', formatter: getGridButtonClosure(buttons2)},
			],
			stretchColumn:"action",
	})
	var buttons3 = [
			{type:'search', callback: 'detailInvoice'},
			{type:'del'   , callback: 'deleteInvoice'},
	]
	$invoiceGridLayout = initializeGrid({
			id:"invoiceGridLayout",
			container:"invoiceListLayout",
			showCheckBox: true,
			colModel: [
					{ name: 'idKey'        , hidden: true, },
					{ name: 'activityIdKey', hidden: true, },
					{ name: 'orderSeq    ' , hidden: true, },
					{ name: 'no'           , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'     , label: '아이디'  , width: 200, align: 'center',},
					{ name: 'invoiceType'  , label: '종류'    , width: 250, align: 'center',},
					{ name: 'issueDate'    , label: '발행일'  , width: 120, editable: true, edittype:'text', align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'issueAmount'  , label: '발행금액', width: 100, editable: true, edittype:'text', align: 'center',},
					{ name: 'approvalNo'   , label: '발행번호', width: 160, editable: true, edittype:'text', align: 'center',},
					{ name: 'entrydate '   , label: '입금일'  , width: 80 , editable: true, edittype:'text', align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'memo'         , label: '메모'    , width: 590, editable: true, edittype:'text', align: 'center',},
					{ name: 'action'       , label: 'ACTION'  ,                             align: 'center', formatter: getGridButtonClosure(buttons3)},
			],
			stretchColumn:"action",
	})
}

function onChangeProject() {
	resetEdit();
	$("#activity_projectIdKey").val($("#projectIdKey").val())
	$gridLayout.clearGridData()
	$elementGridLayout.clearGridData()
	$invoiceGridLayout.clearGridData()
	refreshList()
}

function refreshList() {
	getDefaultList()
}

function getDefaultList() {
	if (!$("#projectIdKey").val()) {
		return
	}
	if ($("#projectIdKey").val() == "") {
		return
	}
	var url = "${contextPath}/svc/v1/activity/list/0?q=" + getAuthToken() + "&projectIdKey=" + $("#projectIdKey").val();
	nesAjax(url,
			null,
			function(data) {
				$gridLayout.setDataList(data.list)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET")
}
</script>

<h1>거래내역관리</h1>
<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<span style="float:left;font-weight:700;font-size:1.3em;padding:5px;">프로젝트 : <select name="projectIdKey" id="projectIdKey" style="font-size:0.9em;" onchange="onChangeProject()"></select></span>
</div>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">새 거래내역</button><br>
</div>
<div class="btn_box" style="padding-top:5px;">
	<input type="hidden" name="activity_idKey" id="activity_idKey">
	<input type="hidden" name="activity_projectIdKey" id="activity_projectIdKey">
	<table class="tbsty">
		<tr>
			<th>아이디</th>
			<td><input type="text" name="activity_idString" id="activity_idString" style="width:90%;"></td>
			<th>납품일</th>
			<td><input type="text" name="activity_deliveryDate" id="activity_deliveryDate" style="width:90%;"></td>
			<th>일련번호</th>
			<td><input type="text" name="activity_orderSeq" id="activity_orderSeq" style="width:90%;"></td>
			<th></th><td></td>
		</tr>
		<tr>
			<th>H/W총액</th>
			<td><input type="text" name="activity_hwTotalAmount" id="activity_hwTotalAmount" style="width:90%;"></td>
			<th>H/W실제금액</th>
			<td><input type="text" name="activity_hwActualAmount" id="activity_hwActualAmount" style="width:90%;"></td>
			<th>S/W총액</th>
			<td><input type="text" name="activity_swTotalAmount" id="activity_swTotalAmount" style="width:90%;"></td>
			<th>S/W실제금액</th>
			<td><input type="text" name="activity_swActualAmount" id="activity_swActualAmount" style="width:90%;"></td>
		</tr>
		<tr>
			<th>메모</th>
			<td colspan="7"><textarea name="activity_memo" id="activity_memo" style="width:98%" rows="4"></textarea></td>
		</tr>
	</table>
</div>
<div class="btn_box" style="padding-top:5px;">
	<a href="#none" class="btn_type submit" onClick="saveEdit();return false;">거래내역 저장</a>
</div>

<div style="height:2px;background-color:#000;margin-top:25px;"></div>

<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<button class="btn_type small" style="float:left;margin-left:5px;" onclick="openServicePopup()" >설치용역추가  </button>
	<button class="btn_type small" style="float:left;margin-left:5px;" onclick="openWifiPopup()"    >WIFI추가      </button>
	<button class="btn_type small" style="float:left;margin-left:5px;" onclick="openLTEPopup()"     >LTE추가       </button>
	<button class="btn_type small" style="float:left;margin-left:5px;" onclick="openWelderPopup()"  >용접기추가    </button>
	<button class="btn_type small" style="float:left;margin-left:5px;" onclick="openSoftwarePopup()">소프트웨어추가</button>
	<button class="btn_type small" onclick="sumElements()">총액재계산</button>
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="saveElements()">저장</button>
</div>
<div id="elementListLayout">
	<table id="elementGridLayout" style="width:100%;"></table>
</div>

<div style="height:2px;background-color:#000;margin-top:25px;"></div>

<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<button class="btn_type small" style="float:left;margin-left:5px;" onclick="openPopupForInvoiceRegist()">추가</button>
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="saveInvoices()">저장</button>
</div>
<div id="invoiceListLayout">
	<table id="invoiceGridLayout" style="width:100%;"></table>
</div>



<jsp:include page="/common/footer.jsp"/>

<script>
function deleteOne(rowId, rowData) {
	if (false == confirm("거래내역을 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/activity/" + rowData.idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("거래내역 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/activity/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#activity_idKey").val(data.idKey)
				$("#activity_projectIdKey").val(data.projectIdKey)
				$("#activity_idString").val(data.idString)
				$("#activity_idString").prop('readonly', true)
				$("#activity_deliveryDate").datepicker('setDate', (data.deliveryDate))
				$("#activity_hwTotalAmount").val(data.hwTotalAmount)
				$("#activity_hwActualAmount").val(data.hwActualAmount)
				$("#activity_swTotalAmount").val(data.swTotalAmount)
				$("#activity_swActualAmount").val(data.swActualAmount)
				$("#activity_memo").val(data.memo)
				$("#activity_orderSeq").val(data.orderSeq)
				//Element 리스트 조회를 호출한다.
				//Invoice 리스트 조회를 호출한다.
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegist() {
	resetEdit()
	getNewId()
}

function getNewId() {
	nesAjax("${contextPath}/svc/v1/activity/newId?q=" + getAuthToken(),
			null,
			function(data) {
				$("#activity_idString").val(data.idString)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function resetEdit() {
	if (!$("#projectIdKey").val()) {
		return
	}
	$("#activity_idKey").val('')
	$("#activity_projectIdKey").val($("#projectIdKey").val())
	$("#activity_idString").val('')
	$("#activity_idString").prop('readonly', false)
	$("#activity_deliveryDate").val('')
	$("#activity_hwTotalAmount").val('')
	$("#activity_hwActualAmount").val('')
	$("#activity_swTotalAmount").val('')
	$("#activity_swActualAmount").val('')
	$("#activity_memo").val('')
	$("#activity_orderSeq").val('')
}

function cancelEdit() {
	resetEdit()
}

function saveEdit() {
	if ($("#activity_idString").val().trim() == "") {
		alert("거래내역아이디를 입력해 주세요.")
		$("#project_idString").focus()
		return
	}
	if (!$("#projectIdKey").val()) {
		alert("프로젝트를 선택 해 주세요(1).")
		$("#projectIdKey").focus()
		return
	}
	if ($("#activity_projectIdKey").val().trim() == "") {
		alert("프로젝트를 선택 해 주세요(2).")
		$("#projectIdKey").focus()
		return
	}
	
	var activity = {};
	activity.authToken = getAuthToken()
	activity.idKey          = $("#activity_idKey").val().trim()
	activity.projectIdKey   = $("#activity_projectIdKey").val().trim()
	activity.idString       = $("#activity_idString").val().trim()
	activity.deliveryDate   = $("#activity_deliveryDate").datepicker('getDate')
	activity.hwTotalAmount  = $("#activity_hwTotalAmount").val().trim()
	activity.hwActualAmount = $("#activity_hwActualAmount").val().trim()
	activity.swTotalAmount  = $("#activity_swTotalAmount").val()
	activity.swActualAmount = $("#activity_swActualAmount").val()
	activity.memo           = $("#activity_memo").val().trim()
	activity.orderSeq       = $("#activity_orderSeq").val().trim()
	
	var url = ""
	var method = ""
	if (activity.idKey == "") {
		url = "${contextPath}/svc/v1/activity"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/activity/" + activity.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(activity),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 거래내역이 존재합니다.")
				} else {
					alert("입력에 실패했습니다.")
				}
			},
			method)
}

function addElementRow() {
	$elementGridLayout.addRowData(undefined, {touch: "I"}, "last")
	var lastRowIndex = getGridLastRowIndex($elementGridLayout)
	if (lastRowIndex) {
		$elementGridLayout.editCell(lastRowIndex, 2, true)
	}
}

function saveElements() {
	finalizeEditingGrid($elementGridLayout);
	var touchedList = getGridTouchedList($elementGridLayout)
	for (var ii=0; ii<touchedList.length; ii++) {
		if (!touchedList[ii].type || touchedList[ii].type.trim() == "") {
			touchedList.splice(ii, 1)
			ii--
		}
	}
	if (!touchedList || touchedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/activity/list"
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

function delElements() {
	finalizeEditingGrid($elementGridLayout);
	var selectedList = getGridSelectedList($elementGridLayout)
	if (!selectedList || selectedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/activity/list/delete"
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


function addInvoiceRow() {
	$invoiceGridLayout.addRowData(undefined, {touch: "I"}, "last")
	var lastRowIndex = getGridLastRowIndex($invoiceGridLayout)
	if (lastRowIndex) {
		$invoiceGridLayout.editCell(lastRowIndex, 2, true)
	}
}

function saveInvoices() {
	finalizeEditingGrid($invoiceGridLayout);
	var touchedList = getGridTouchedList($invoiceGridLayout)
	for (var ii=0; ii<touchedList.length; ii++) {
		if (!touchedList[ii].type || touchedList[ii].type.trim() == "") {
			touchedList.splice(ii, 1)
			ii--
		}
	}
	if (!touchedList || touchedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/invoice/list"
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

function delInvoices() {
	finalizeEditingGrid($invoiceGridLayout);
	var selectedList = getGridSelectedList($invoiceGridLayout)
	if (!selectedList || selectedList.length == 0) {
		return
	}
	var url = "${contextPath}/svc/v1/invoice/list/delete"
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

var __wifiList;
var __lteList;
var __welderList;

function openServicePopup() {
}

function openWifiPopup() {
	if ($("#activity_projectIdKey").val().trim() == "") {
		alert("프로젝트를 선택 해 주세요(3).")
		$("#projectIdKey").focus()
		return
	}

	var url = "${contextPath}/svc/v1/wifi/list/0?q=" + getAuthToken()
	var method = "GET";
	nesAjax(url,
			null,
			function(data) {
				openPopup('wifiListLayout', 600, 600)
				__wifiList = data.list
				$("#listLayoutForWifi").html(TrimPath.processDOMTemplate("wifilist_jst", data))
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			method);
}

function closeWifiPopup() {
	closePopup('wifiListLayout')
}

function selectWifiElement(idKey) {
	if (!__wifiList) {
		return
	}
	var selected;
	for (var ii = 0; ii < __wifiList.length; ii++) {
		if (__wifiList[ii].idKey == idKey) {
			selected = __wifiList[ii];
		}
	}
	if (!selected) {
		return
	}
	
	var element = {}
	element.touch = "I"
	element.activityIdKey = $("#activity_projectIdKey").val()
	element.elementType = "2" //WIFI
	element.elementIdKey = idKey
	element.idString = selected.idString
	element.elementTitle = "WIFI " + selected.idString
	
	$elementGridLayout.addRowData(undefined, element, "last")
	var lastRowIndex = getGridLastRowIndex($elementGridLayout)
	if (lastRowIndex) {
		$elementGridLayout.editCell(lastRowIndex, 4, true)
	}
	closeWifiPopup()
}

function openLTEPopup() {
	if ($("#activity_projectIdKey").val().trim() == "") {
		alert("프로젝트를 선택 해 주세요(3).")
		$("#projectIdKey").focus()
		return
	}

	var url = "${contextPath}/svc/v1/lte/list/0?q=" + getAuthToken()
	var method = "GET";
	nesAjax(url,
			null,
			function(data) {
				openPopup('lteListLayout', 600, 600)
				__lteList = data.list
				$("#listLayoutForLTE").html(TrimPath.processDOMTemplate("ltelist_jst", data))
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			method);
}

function closeLTEPopup() {
	closePopup('lteListLayout')
}

function selectLTEElement(idKey) {
	if (!__lteList) {
		return
	}
	var selected;
	for (var ii = 0; ii < __lteList.length; ii++) {
		if (__lteList[ii].idKey == idKey) {
			selected = __lteList[ii];
		}
	}
	if (!selected) {
		return
	}
	
	var element = {}
	element.touch = "I"
	element.activityIdKey = $("#activity_projectIdKey").val()
	element.elementType = "3" //LTE
	element.elementIdKey = idKey
	element.idString = selected.idString
	element.elementTitle = "LTE " + selected.idString
	
	$elementGridLayout.addRowData(undefined, element, "last")
	var lastRowIndex = getGridLastRowIndex($elementGridLayout)
	if (lastRowIndex) {
		$elementGridLayout.editCell(lastRowIndex, 4, true)
	}
	closeLTEPopup()
}

function openWelderPopup() {
	if ($("#activity_projectIdKey").val().trim() == "") {
		alert("프로젝트를 선택 해 주세요(3).")
		$("#projectIdKey").focus()
		return
	}

	var url = "${contextPath}/svc/v1/welder/list/0?q=" + getAuthToken()
	var method = "GET";
	nesAjax(url,
			null,
			function(data) {
				openPopup('welderListLayout', 600, 600)
				__welderList = data.list
				$("#listLayoutForWelder").html(TrimPath.processDOMTemplate("welderlist_jst", data))
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			method);
}

function closeWelderPopup() {
	closePopup('welderListLayout')
}

function selectWelderElement(idKey) {
	if (!__welderList) {
		return
	}
	var selected;
	for (var ii = 0; ii < __welderList.length; ii++) {
		if (__welderList[ii].idKey == idKey) {
			selected = __welderList[ii];
		}
	}
	if (!selected) {
		return
	}
	
	var element = {}
	element.touch = "I"
	element.activityIdKey = $("#activity_projectIdKey").val()
	element.elementType = "4" //Welder
	element.elementIdKey = idKey
	element.idString = selected.idString
	element.elementTitle = "Welder " + selected.idString
	
	$elementGridLayout.addRowData(undefined, element, "last")
	var lastRowIndex = getGridLastRowIndex($elementGridLayout)
	if (lastRowIndex) {
		$elementGridLayout.editCell(lastRowIndex, 4, true)
	}
	closeWelderPopup()
}

function openSoftwarePopup() {
}
</script>

<div class="layer_bg" id="wifiListLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title">WIFI기기 선택</span>
			<a href="#none" class="pop_close white" onClick="closeWifiPopup();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<div id="listLayoutForWifi"></div>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<button type="button" class="btn btn-secondary" onClick="closeWifiPopup();return false;">취소</button>
			</div>
		</div>
	</div>
</div>

<script type="text/template" id="wifilist_jst">
<table class="tbsty">
	<thead>
		<tr>
			<th class="p-2" style="width:110px;">WIFI아이디</th>
			<th class="p-2">메모</th>
			<th class="p-2" style="width:60px;">Action</th>
		</tr>
	</thead>
	<tbody>
	{for item in list}
		<tr class="bg-dark">
			<td class="p-2" style="vertical-align:middle;">{{item.idString}</td>
			<td class="p-2" style="vertical-align:middle;">{{item.memo}</td>
			<td class="p-2">
				<button type="button" class="btn btn-secondary" onclick="selectWifiElement({{item.idKey})">선택</button>
			</td>
		</tr>
	{forelse}
		<tr><td colspan="3">데이터 없음</tr>
	{/for}
	</tbody>
</table>
</script>

<div class="layer_bg" id="lteListLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title">LTE기기 선택</span>
			<a href="#none" class="pop_close white" onClick="closeLTEPopup();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<div id="listLayoutForLTE"></div>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<button type="button" class="btn btn-secondary" onClick="closeLTEPopup();return false;">취소</button>
			</div>
		</div>
	</div>
</div>

<script type="text/template" id="ltelist_jst">
<table class="tbsty">
	<thead>
		<tr>
			<th class="p-2" style="width:110px;">LTE아이디</th>
			<th class="p-2">메모</th>
			<th class="p-2" style="width:60px;">Action</th>
		</tr>
	</thead>
	<tbody>
	{for item in list}
		<tr class="bg-dark">
			<td class="p-2" style="vertical-align:middle;">{{item.idString}</td>
			<td class="p-2" style="vertical-align:middle;">{{item.memo}</td>
			<td class="p-2">
				<button type="button" class="btn btn-secondary" onclick="selectLTEElement({{item.idKey})">선택</button>
			</td>
		</tr>
	{forelse}
		<tr><td colspan="3">데이터 없음</tr>
	{/for}
	</tbody>
</table>
</script>

<div class="layer_bg" id="welderListLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title">용접기 선택</span>
			<a href="#none" class="pop_close white" onClick="closeWelderPopup();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<div id="listLayoutForWelder"></div>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<button type="button" class="btn btn-secondary" onClick="closeWelderPopup();return false;">취소</button>
			</div>
		</div>
	</div>
</div>

<script type="text/template" id="welderlist_jst">
<table class="tbsty">
	<thead>
		<tr>
			<th class="p-2" style="width:110px;">용접기아이디</th>
			<th class="p-2">메모</th>
			<th class="p-2" style="width:60px;">Action</th>
		</tr>
	</thead>
	<tbody>
	{for item in list}
		<tr class="bg-dark">
			<td class="p-2" style="vertical-align:middle;">{{item.idString}</td>
			<td class="p-2" style="vertical-align:middle;">{{item.memo}</td>
			<td class="p-2">
				<button type="button" class="btn btn-secondary" onclick="selectWelderElement({{item.idKey})">선택</button>
			</td>
		</tr>
	{forelse}
		<tr><td colspan="3">데이터 없음</tr>
	{/for}
	</tbody>
</table>
</script>

<script>
function detailElement(rowId, rowData) {
	openPopupForElement(rowId, rowData)
}

var __elementRowId
var __elementRowData
function openPopupForElement(rowId, rowData) {
	__elementRowId = rowId
	__elementRowData = rowData
	$("#element_idKey").val(rowData.idKey)
	$("#element_idString").val(rowData.idString)
	$("#element_unitPrice").val(rowData.unitPrice)
	$("#element_startDate").val(rowData.startDate)
	$("#element_endDate").val(rowData.endDate)
	$("#element_memo").val(rowData.memo)
	$("#element_orderSeq").val(rowData.memo)
	openPopup('elementPopupLayout', 600, 600)
}

function resetElement() {
	__elementRowId = undefined
	__elementRowData = undefined
	$("#element_idKey").val('')
	$("#element_idString").val('')
	$("#element_unitPrice").val('')
	$("#element_startDate").val('')
	$("#element_endDate").val('')
	$("#element_memo").val('')
	$("#element_orderSeq").val('')
}

function cancelElement() {
	resetElement()
	closePopup('elementPopupLayout')
}

function saveElement() {
	if (!__elementRowData) {
		return
	}
	__elementRowData.unitPrice = $("#element_unitPrice").val().trim()
	__elementRowData.startDate = $("#element_startDate").val().trim()
	__elementRowData.endDate   = $("#element_endDate").val().trim()
	__elementRowData.memo      = $("#element_memo").val().trim()
	__elementRowData.orderSeq  = $("#element_orderSeq").val().trim()
	if (__elementRowData.idKey) {
		__elementRowData.touch = "U"
	} else {
		__elementRowData.touch = "I"
	}
	$elementGridLayout.setRowData(__elementRowId, __elementRowData)
	cancelElement()
}
</script>

<div class="layer_bg" id="elementPopupLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="elementPopuptitle"></span>
			<a href="#none" class="pop_close white" onClick="cancelElement();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="element_idKey" id="element_idKey">
			<table class="tbsty">
				<tr>
					<th>아이디</th>
					<td><input type="text" name="element_idString" id="element_idString" style="width:90%" readonly></td>
				</tr>
				<tr>
					<th>단가</th>
					<td><input type="text" name="element_unitPrice" id="element_unitPrice" style="width:90%"></td>
				</tr>
				<tr>
					<th>시작일</th>
					<td><input type="text" class="form-control" id="element_startDate" /></td>
				</tr>
				<tr>
					<th>종료일</th>
					<td><input type="text" class="form-control" id="element_endDate" /></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="element_memo" id="element_memo" style="width:90%" rows="4"></textarea></td>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="element_orderSeq" id="element_orderSeq" style="width:90%"></td>
				</tr>
			</table>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<a href="#none" class="btn_type submit" onClick="saveElement();return false;">확인</a>
				<a href="#none" class="btn_type normal" onClick="cancelElement();return false;">취소</a>
			</div>
		</div>
	</div>
</div>

<script>
function openPopupForInvoiceRegist() {
	resetInvoice()
	openPopup('invoicePopupLayout', 600, 600);
	getInvoiceNewId()
}

function detaiInvoice(rowId, rowData) {
	openPopupForInvoice(rowId, rowData)
}

var __invoiceRowId
var __invoiceRowData
function openPopupForInvoice(rowId, rowData) {
	__invoiceRowId = rowId
	__invoiceRowData = rowData
	$("#invoice_idKey").val(rowData.idKey)
	$("#invoice_idString").val(rowData.idString)
	$("#invoice_invoiceType").val(rowData.unitPrice)
	$("#invoice_issueAmount").val(rowData.startDate)
	$("#invoice_issueDate").val(rowData.endDate)
	$("#invoice_entryDate").val(rowData.memo)
	$("#invoice_memo").val(rowData.memo)
	$("#invoice_orderSeq").val(rowData.memo)
	openPopup('invoicePopupLayout', 600, 600)
}

function resetInvoice() {
	__invoiceRowId = undefined
	__invoiceRowData = undefined
	$("#invoice_idKey").val('')
	$("#invoice_idString").val('')
	$("#invoice_invoiceType").val('')
	$("#invoice_issueAmount").val('')
	$("#invoice_issueDate").val('')
	$("#invoice_entryDate").val('')
	$("#element_memo").val('')
	$("#element_orderSeq").val('')
}

function cancelInvoice() {
	resetInvoice()
	closePopup('invoicePopupLayout')
}

function saveInvoice() {
	if (!__invoiceRowData) {
		__invoiceRowData = {}
		__invoiceRowData.idString = $("#invoice_idString").val()
	}
	__invoiceRowData.invoiceType = $("#invoice_invoiceType").val().trim()
	__invoiceRowData.issueAmount = $("#invoice_issueAmount").val().trim()
	__invoiceRowData.issueDate   = $("#invoice_issueDate").val().trim()
	__invoiceRowData.entryDate   = $("#invoice_entryDate").val().trim()
	__invoiceRowData.memo        = $("#invoice_memo").val().trim()
	__invoiceRowData.orderSeq    = $("#invoice_orderSeq").val().trim()
	if (__invoiceRowId) {
		$invoiceGridLayout.setRowData(__invoiceRowId, __invoiceRowData)
		__invoiceRowData.touch = "U"
	} else {
		__invoiceRowData.touch = "I"
		$invoiceGridLayout.addRowData(undefined, __invoiceRowData, "last")
		var lastRowIndex = getGridLastRowIndex($invoiceGridLayout)
		if (lastRowIndex) {
			$invoiceGridLayout.editCell(lastRowIndex, 2, true)
		}

	}
	cancelInvoice()
}

function getInvoiceNewId() {
	nesAjax("${contextPath}/svc/v1/invoice/newId?q=" + getAuthToken(),
			null,
			function(data) {
				$("#invoice_idString").val(data.idString)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}
</script>
<div class="layer_bg" id="invoicePopupLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="invoicePopupTitle"></span>
			<a href="#none" class="pop_close white" onClick="cancelInvoice();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="invoice_idKey" id="invoice_idKey">
			<table class="tbsty">
				<tr>
					<th>아이디</th>
					<td><input type="text" name="invoice_idString" id="invoice_idString" style="width:90%" readonly></td>
				</tr>
				<tr>
					<th>종류</th>
					<td><input type="text" name="invoice_invoiceType" id="invoice_invoiceType" style="width:90%"></td>
				</tr>
				<tr>
					<th>발행금액</th>
					<td><input type="text" class="form-control" id="invoice_issueAmount" /></td>
				</tr>
				<tr>
					<th>발행일</th>
					<td><input type="text" class="form-control" id="invoice_issueDate" /></td>
				</tr>
				<tr>
					<th>입금일</th>
					<td><input type="text" class="form-control" id="invoice_entryDate" /></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="invoice_memo" id="invoice_memo" style="width:90%" rows="4"></textarea></td>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="invoice_orderSeq" id="invoice_orderSeq" style="width:90%"></td>
				</tr>
			</table>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<a href="#none" class="btn_type submit" onClick="saveInvoice();return false;">확인</a>
				<a href="#none" class="btn_type normal" onClick="cancelInvoice();return false;">취소</a>
			</div>
		</div>
	</div>
</div>