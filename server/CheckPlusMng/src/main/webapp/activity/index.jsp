<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
var $elementGridLayout
var $invoiceGridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelectByUrl("${contextPath}/svc/v1/project/list/0?q=" + getAuthToken(), "projectIdKey", "idKey", "name")
//	fillUpSelectByUrl("${contextPath}/svc/v1/company/list/0?q=" + getAuthToken(), "project_brokerIdKey", "idKey", "name")
//	fillUpSelectByUrl("${contextPath}/svc/v1/company/list/0?q=" + getAuthToken(), "project_customerIdKey", "idKey", "name")
//	createDatePicker('project_contractDate')
	var buttons = [
			{type:'search', callback: 'detailOne'},
			{type:'del'   , callback: 'deleteOne'},
	]
	$gridLayout = initializeGrid({
			id:"gridLayout",
			container:"listLayout",
			showCheckBox: false,
			colModel: [
					{ name: 'idKey'        , hidden: true, },
					{ name: 'brokerIdKey'  , hidden: true, },
					{ name: 'customerIdKey', hidden: true, },
					{ name: 'no'           , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'     , label: '아이디'  , width: 200, align: 'center',},
					{ name: 'name'         , label: '이름'    , width: 200, align: 'center',},
					{ name: 'brokerName'   , label: '판매업체', width: 120, align: 'center',},
					{ name: 'customerName' , label: '설치업체', width: 100, align: 'center',},
					{ name: 'contractDate' , label: '계약일'  , width: 80 , align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'memo'         , label: '메모'    , width: 380, align: 'center',},
					{ name: 'action'       , label: 'ACTION'  ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	refreshList()
	
	$elementGridLayout = initializeGrid({
			id:"elementGridLayout",
			container:"elementGridLayout",
			showCheckBox: true,
			colModel: [
					{ name: 'idKey'        , hidden: true, },
					{ name: 'activityIdKey', hidden: true, },
					{ name: 'elementType'  , hidden: true, },
					{ name: 'elementIdKey' , hidden: true, },
					{ name: 'no'           , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'     , label: '아이디'  , width: 200, align: 'center',},
					{ name: 'elementTitle' , label: '설명'    , width: 200, align: 'center',},
					{ name: 'unitPrice '   , label: '단가'    , width: 120, align: 'center',},
					{ name: 'startDate'    , label: '시작일자', width: 80 , align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'endDate'      , label: '종료일자', width: 80 , align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'memo'         , label: '메모'    , width: 380, align: 'center',},
					{ name: 'action'       , label: 'ACTION'  ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	$invoiceGridLayout = initializeGrid({
			id:"invoiceGridLayout",
			container:"invoiceListLayout",
			showCheckBox: true,
			colModel: [
					{ name: 'idKey'        , hidden: true, },
					{ name: 'activityIdKey', hidden: true, },
					{ name: 'no'           , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'     , label: '아이디'  , width: 200, align: 'center',},
					{ name: 'invoiceType'  , label: '종류'    , width: 200, align: 'center',},
					{ name: 'issueDate'    , label: '발행일'  , width: 120, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'issueAmount'  , label: '발행금액', width: 100, align: 'center',},
					{ name: 'approvalNo'   , label: '발행번호', width: 160, align: 'center',},
					{ name: 'entrydate '   , label: '입금일'  , width: 80 , align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'memo'         , label: '메모'    , width: 380, align: 'center',},
					{ name: 'action'       , label: 'ACTION'  ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
}

function refreshList() {
	getDefaultList()
}

function getDefaultList() {
	if (!$("#projectIdKey").val()) {
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

// 먼저 프로젝트를 선택해야 한다. 프로젝트를 선택하면 해당 프로젝트에 속하는 거래내역을 가져와서 뿌린다.
// 해당 거래내역에 속하는 기기들을 추가한다. 각각의 단가와 정보를 입력한다.
// 해당 내역을 자동으로 합쳐서 금액을 추가한다.
// 해당 금액에 대해서 세금계산서를 발행하는 내용을 추가한다.
// 자동으로 금액을 분배하는 기능이 필요한가?
 

<h1>거래내역관리</h1>
<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<span style="float:left;font-weight:700;padding:5px;">프로젝트 <select name="projectIdKey" id="projectIdKey"></select></span>
</div>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">새 거래내역</button><br>
</div>
<div class="btn_box">
<input type="hidden" name="activity_idKey" id="activity_idKey" style="width:90%">
<input type="hidden" name="activity_projectIdKey" id="activity_projectIdKey" style="width:90%">
아이디 : <input type="text" name="activity_idString" id="activity_idString" style="width:90%" readonly>
납품일 : <input type="text" name="activity_deliveryDate" id="activity_deliveryDate" style="width:90%" readonly>
하드웨어금액 : <input type="text" name="activity_hwTotalAmount" id="activity_hwTotalAmount" style="width:90%" readonly>
               <input type="text" name="activity_hwActualAmount" id="activity_hwActualAmount" style="width:90%">
소프트웨어금액 : <input type="text" name="activity_swTotalAmount" id="activity_swTotalAmount" style="width:90%" readonly>
                 <input type="text" name="activity_swActualAmount" id="activity_swActualAmount" style="width:90%">
메모 : <textarea name="activity_memo" id="activity_memo" style="width:90%" rows="4"></textarea>
일련번호 : <input type="text" name="activity_orderSeq" id="activity_orderSeq" style="width:90%" readonly>
</div>

//아래에 WIFI추가한다.(팜업)
//아래에 LTE를 추가한다.
//아래에 용접기를 추가한다.
//혹은 내역을 삭제한다. (이미 다른 거래내역에 포함된 기기는 추가 할 수 없다.) 혀냊 Activity에 속하는 것을 파악할 수 있어야 한다.
//각각의 단가를 입력하고 수정할 수 있다.
//모든 하드웨어와 소프트 웨어를 추가할 수 있다.
//기타 용역과 소프트웨어 사용금액 등을 추가할 수 있다.
<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<button class="btn_type small" onclick="addElementRow()">추가</button>
	<button class="btn_type small" onclick="delElements()">삭제</button>
	&nbsp;&nbsp;
	<button class="btn_type small" onclick="saveElements()">저장</button>
</div>
<div id="elementListLayout">
	<table id="elementGridLayout" style="width:100%;"></table>
</div>

//아래에 invoice라인을 추가할 수 있다.
//invoice를 삭제할 수 있으며
//이미 처리가 완료된 것은 삭제할 수 없다. (처리 미완료로 변경후 삭제)
<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
	<button class="btn_type small" onclick="addInvoiceRow()">추가</button>
	<button class="btn_type small" onclick="delInvoices()">삭제</button>
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
	nesAjax("${contextPath}/svc/v1/activity/newId?format=ACTV_%2504d&q=" + getAuthToken(),
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
	activity.projectIdKey   = @("#activity_projectIdKey").val().trim()
	activity.idString       = $("#activity_idString").val().trim()
	activity.deliveryDate   = $("#project_contractDate").datepicker('getDate')
	activity.hwTotalAmount  = $("#activity_hwTotalAmount").val().trim()
	activity.hwActualAmount = $("#activity_hwActualAmount").val().trim()
	activity.swTotalAmount  = $("#activity_swTotalAmount").val()
	activity.swActualAmount = $("#activity_swActualAmount").val()
	activity.memo           = $("#project_memo").val().trim()
	activity.orderSeq       = $("#project_orderSeq").val().trim()
	
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
</script>
<div class="layer_bg" id="defaultPopupLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="layertitle"></span>
			<a href="#none" class="pop_close white" onClick="cancelEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="project_idKey" id="project_idKey">
			<table class="tbsty">
				<tr>
					<th>프로젝트ID</th>
					<td><input type="text" name="project_idString" id="project_idString" style="width:90%"></td>
				</tr>
				<tr>
					<th>프로젝트이름</th>
					<td><input type="text" name="project_name" id="project_name" style="width:90%"></td>
				</tr>
				<tr>
					<th>판매업체</th>
					<td><select name="project_brokerIdKey" id="project_brokerIdKey"></select></td>
				</tr>
				<tr>
					<th>설치업체</th>
					<td><select name="project_customerIdKey" id="project_customerIdKey"></select></td>
				</tr>
				<tr>
					<th>계약일</th>
					<td><input type="text" class="form-control" id="project_contractDate" /></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="project_memo" id="project_memo" style="width:90%" rows="4"></textarea></td>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="project_orderSeq" id="project_orderSeq" style="width:90%"></td>
				</tr>
			</table>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<a href="#none" class="btn_type submit" onClick="saveEdit();return false;">확인</a>
				<a href="#none" class="btn_type normal" onClick="cancelEdit();return false;">취소</a>
			</div>
		</div>
	</div>
</div>
