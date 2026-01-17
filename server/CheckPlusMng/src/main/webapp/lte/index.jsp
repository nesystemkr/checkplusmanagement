<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
	createDatePicker('lte_registDate')
	createDatePicker('lte_startDate')
	createDatePicker('lte_endDate')
	var buttons = [
			{type:'search', callback: 'detailOne'},
			{type:'del'   , callback: 'deleteOne'},
	]
	$gridLayout = initializeGrid({
			id:"gridLayout",
			container:"listLayout",
			showCheckbox: false,
			colModel: [
					{ name: 'idKey'     , hidden: true, },
					{ name: 'no'        , label: 'NO'          , width: 50 , align: 'center',},
					{ name: 'idString'  , label: '아이디'      , width: 100, align: 'center',},
					{ name: 'modelName' , label: '모델명'      , width: 100, align: 'center',},
					{ name: 'serialNo'  , label: '일련번호'    , width: 100, align: 'center',},
					{ name: 'usimNo'    , label: '유심일련번호', width: 120, align: 'center',},
					{ name: 'telephone' , label: '전화번호'    , width: 120, align: 'center',},
					{ name: 'gateId'    , label: 'LTE_GATE_ID' , width: 100, align: 'center',},
					{ name: 'gatePw'    , label: 'LTE_GATE_PW' , width: 100, align: 'center',},
					{ name: 'wifiId'    , label: 'LTE_WIFI_ID' , width: 100, align: 'center',},
					{ name: 'wifiPw'    , label: 'LTE_WIFI_PW' , width: 100, align: 'center',},
					{ name: 'registDate', label: '가입일'      , width: 100, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'startDate' , label: '시작일'      , width: 100, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'endDate'   , label: '만기일'      , width: 100, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'contract'  , label: '약정'        , width: 100, align: 'center',},
					{ name: 'memo'      , label: '메모'        , width: 280, align: 'center',},
					{ name: 'action'    , label: 'ACTION'      ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	refreshList()
}

function refreshList() {
	getDefaultList(__currentPage)
}

function getDefaultList(page) {
	var url = "${contextPath}/svc/v1/lte/list/" + page + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				$gridLayout.setDataList(data.list)
				pagingInfo_success(data.paging, refreshList)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET")
}
</script>
<h1>LTE기기관리</h1>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">LTE기기추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteOne(rowId, rowData) {
	if (false == confirm("LTE기기를 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/lte/" + rowData.idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("LTE기기 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/lte/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("LTE기기정보수정")
				openPopup('defaultPopupLayout', 600, 600)
				$("#lte_idKey").val(data.idKey)
				$("#lte_idString").val(data.idString)
				$("#lte_idString").prop('readonly', true)
				$("#lte_modelName").val(data.modelName)
				$("#lte_serialNo").val(data.serialNo)
				$("#lte_usimNo").val(data.usimNo)
				$("#lte_telephone").val(data.telephone)
				$("#lte_gateId").val(data.gateId)
				$("#lte_gatePw").val(data.gatePw)
				$("#lte_wifiId").val(data.wifiId)
				$("#lte_wifiPw").val(data.wifiPw)
				$("#lte_registDate").datepicker('setDate', data.registDate)
				$("#lte_startDate").datepicker('setDate', data.startDate)
				$("#lte_endDate").datepicker('setDate', data.endDate)
				$("#lte_contract").val(data.contract)
				$("#lte_memo").val(data.memo)
				$("#lte_orderSeq").val(data.orderSeq)
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegist() {
	$("#layertitle").html("LTE기기추가");
	resetEdit()
	openPopup('defaultPopupLayout', 600, 600);
	getNewId()
}

function getNewId() {
	nesAjax("${contextPath}/svc/v1/lte/newId?q=" + getAuthToken(),
			null,
			function(data) {
				$("#lte_idString").val(data.idString)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function resetEdit() {
	$("#lte_idKey").val('')
	$("#lte_idString").val('')
	$("#lte_idString").prop('readonly', false)
	$("#lte_modelName").val('')
	$("#lte_serialNo").val('')
	$("#lte_usimNo").val('')
	$("#lte_telephone").val('')
	$("#lte_gateId").val('')
	$("#lte_gatePw").val('')
	$("#lte_wifiId").val('')
	$("#lte_wifiPw").val('')
	$("#lte_registDate").val('')
	$("#lte_startDate").val('')
	$("#lte_endDate").val('')
	$("#lte_contract").val('')
	$("#lte_memo").val('')
	$("#lte_orderSeq").val('')
}

function cancelEdit() {
	resetEdit()
	closePopup('defaultPopupLayout')
}

function saveEdit() {
	if ($("#lte_idString").val().trim() == "") {
		alert("아이디를 입력해 주세요.")
		$("#lte_idString").focus()
		return
	}
	if ($("#lte_modelName").val().trim() == "") {
		alert("모델명을 입력해 주세요.")
		$("#lte_modelName").focus()
		return
	}
	
	var lte = {};
	lte.authToken = getAuthToken()
	lte.idKey      = $("#lte_idKey").val().trim()
	lte.idString   = $("#lte_idString").val().trim()
	lte.modelName  = $("#lte_modelName").val().trim()
	lte.serialNo   = $("#lte_serialNo").val().trim()
	lte.usimNo     = $("#lte_usimNo").val().trim()
	lte.telephone  = $("#lte_telephone").val().trim()
	lte.gateId     = $("#lte_gateId").val().trim()
	lte.gatePw     = $("#lte_gatePw").val().trim()
	lte.wifiId     = $("#lte_wifiId").val().trim()
	lte.wifiPw     = $("#lte_wifiPw").val().trim()
	lte.registDate = $("#lte_registDate").datepicker('getDate')
	lte.startDate  = $("#lte_startDate").datepicker('getDate')
	lte.endDate    = $("#lte_endDate").datepicker('getDate')
	lte.contract   = $("#lte_contract").val().trim()
	lte.memo       = $("#lte_memo").val().trim()
	lte.orderSeq   = $("#lte_orderSeq").val().trim()
	
	var url = ""
	var method = ""
	if (lte.idKey == "") {
		url = "${contextPath}/svc/v1/lte"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/lte/" + lte.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(lte),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 LTE기기가 존재합니다.")
				} else {
					alert("입력에 실패했습니다.")
				}
			},
			method)
}
</script>
<div class="layer_bg" id="defaultPopupLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="layertitle"></span>
			<a href="#none" class="pop_close white" onClick="cancelEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="lte_idKey" id="lte_idKey">
			<table class="tbsty">
				<tr>
					<th>LTE ID</th>
					<td><input type="text" name="lte_idString" id="lte_idString" style="width:90%"></td>
				</tr>
				<tr>
					<th>모델명</th>
					<td><input type="text" name="lte_modelName" id="lte_modelName" style="width:90%"></td>
				</tr>
				<tr>
					<th>기기일련번호</th>
					<td><input type="text" name="lte_serialNo" id="lte_serialNo" style="width:90%"></td>
				</tr>
				<tr>
					<th>USIM일련번호</th>
					<td><input type="text" name="lte_usimNo" id="lte_usimNo" style="width:90%"></td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input type="text" name="lte_telephone" id="lte_telephone" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_GATE_ID</th>
					<td><input type="text" name="lte_gateId" id="lte_gateId" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_GATE_PW</th>
					<td><input type="text" name="lte_gatePw" id="lte_gatePw" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_WIFI_ID</th>
					<td><input type="text" name="lte_wifiId" id="lte_wifiId" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_WIFI_PW</th>
					<td><input type="text" name="lte_wifiPw" id="lte_wifiPw" style="width:90%"></td>
				</tr>
				<tr>
					<th>등록일</th>
					<td><input type="text" class="form-control" id="lte_registDate" /></td>
				</tr>
				<tr>
					<th>시작일</th>
					<td><input type="text" class="form-control" id="lte_startDate" /></td>
				</tr>
				<tr>
					<th>만기일</th>
					<td><input type="text" class="form-control" id="lte_endDate" /></td>
				</tr>
				<tr>
					<th>약정</th>
					<td><input type="text" name="lte_contract" id="lte_contract" style="width:90%"></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="lte_memo" id="lte_memo" style="width:90%" rows="4"></textarea>
				</tr>
				<tr>
					<th>순서</th>
					<td><input name="lte_orderSeq" id="lte_orderSeq" style="width:90%"></td>
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
