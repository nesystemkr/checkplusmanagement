<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelectByUrl("${contextPath}/svc/v1/project/list/0?q=" + getAuthToken(), "lte_projectIdKey", "idKey", "projectName")
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
			showCheckBox: false,
			colModel: [
					{ name: 'idKey'              , hidden: true, },
					{ name: 'projectIdKey'       , hidden: true, },
					{ name: 'no'                 , label: 'NO'          , width: 50 , align: 'center',},
					{ name: 'lteId'              , label: '아이디'      , width: 100, align: 'center',},
					{ name: 'projectName'        , label: '프로젝트'    , width: 100, align: 'center',},
					{ name: 'contractCompanyName', label: '설치업체'    , width: 100, align: 'center',},
					{ name: 'modelName'          , label: '모델명'      , width: 100, align: 'center',},
					{ name: 'deviceSerialNo'     , label: '일련번호'    , width: 100, align: 'center',},
					{ name: 'usimSerialNo'       , label: '유심일련번호', width: 120, align: 'center',},
					{ name: 'telephoneNo'        , label: '전화번호'    , width: 120, align: 'center',},
					{ name: 'lteGateId'          , label: 'LTE_GATE_ID' , width: 100, align: 'center',},
					{ name: 'lteGatePw'          , label: 'LTE_GATE_PW' , width: 100, align: 'center',},
					{ name: 'lteWifiId'          , label: 'LTE_WIFI_ID' , width: 100, align: 'center',},
					{ name: 'lteWifiPw'          , label: 'LTE_WIFI_PW' , width: 100, align: 'center',},
					{ name: 'registDate'         , label: '가입일'      , width: 100, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'startDate'          , label: '시작일'      , width: 100, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'endDate'            , label: '만기일'      , width: 100, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'contract'           , label: '약정'        , width: 100, align: 'center',},
					{ name: 'memo'               , label: '메모'        , width: 280, align: 'center',},
					{ name: 'action'             , label: 'ACTION'      ,             align: 'center', formatter: getGridButtonClosure(buttons)},
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
				$("#lte_lteId").val(data.lteId)
				$("#lte_lteId").prop('readonly', true)
				$("#lte_projectIdKey").val(data.projectIdKey)
				$("#lte_modelName").val(data.modelName)
				$("#lte_deviceSerialNo").val(data.deviceSerialNo)
				$("#lte_usimSerialNo").val(data.usimSerialNo)
				$("#lte_telephoneNo").val(data.telephoneNo)
				$("#lte_lteGateId").val(data.lteGateId)
				$("#lte_lteGatePw").val(data.lteGatePw)
				$("#lte_lteWifiId").val(data.lteWifiId)
				$("#lte_lteWifiPw").val(data.lteWifiPw)
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
}

function resetEdit() {
	$("#lte_idKey").val('')
	$("#lte_lteId").val('')
	$("#lte_lteId").prop('readonly', false)
	$("#lte_projectIdKey").val('')
	$("#lte_modelName").val('')
	$("#lte_deviceSerialNo").val('')
	$("#lte_usimSerialNo").val('')
	$("#lte_telephoneNo").val('')
	$("#lte_lteGateId").val('')
	$("#lte_lteGatePw").val('')
	$("#lte_lteWifiId").val('')
	$("#lte_lteWifiPw").val('')
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
	if ($("#lte_lteId").val().trim() == "") {
		alert("아이디를 입력해 주세요.")
		$("#lte_lteId").focus()
		return
	}
	if ($("#lte_projectIdKey").val() == undefined || $("#lte_projectIdKey").val().trim() == "" || $("#lte_projectIdKey").val() == "0") {
		alert("프로젝트를 선택해 주세요.")
		$("#lte_projectIdKey").focus()
		return;
	}
	if ($("#lte_modelName").val().trim() == "") {
		alert("모델명을 입력해 주세요.")
		$("#lte_modelName").focus()
		return
	}
	
	var lte = {};
	lte.authToken = getAuthToken()
	lte.idKey          = $("#lte_idKey").val().trim()
	lte.lteId          = $("#lte_lteId").val().trim()
	lte.projectIdKey   = $("#lte_projectIdKey").val().trim()
	lte.modelName      = $("#lte_modelName").val().trim()
	lte.deviceSerialNo = $("#lte_deviceSerialNo").val().trim()
	lte.usimSerialNo   = $("#lte_usimSerialNo").val().trim()
	lte.telephoneNo    = $("#lte_telephoneNo").val().trim()
	lte.lteGateId      = $("#lte_lteGateId").val().trim()
	lte.lteGatePw      = $("#lte_lteGatePw").val().trim()
	lte.lteWifiId      = $("#lte_lteWifiId").val().trim()
	lte.lteWifiPw      = $("#lte_lteWifiPw").val().trim()
	lte.registDate     = $("#lte_registDate").datepicker('getDate')
	lte.startDate      = $("#lte_startDate").datepicker('getDate')
	lte.endDate        = $("#lte_endDate").datepicker('getDate')
	lte.contract       = $("#lte_contract").val().trim()
	lte.memo           = $("#lte_memo").val().trim()
	lte.orderSeq       = $("#lte_orderSeq").val().trim()
	
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
					<td><input type="text" name="lte_lteId" id="lte_lteId" style="width:90%"></td>
				</tr>
				<tr>
					<th>프로젝트</th>
					<td><select name="lte_projectIdKey" id="lte_projectIdKey"></select></td>
				</tr>
				<tr>
					<th>모델명</th>
					<td><input type="text" name="lte_modelName" id="lte_modelName" style="width:90%"></td>
				</tr>
				<tr>
					<th>기기일련번호</th>
					<td><input type="text" name="lte_deviceSerialNo" id="lte_deviceSerialNo" style="width:90%"></td>
				</tr>
				<tr>
					<th>USIM일련번호</th>
					<td><input type="text" name="lte_usimSerialNo" id="lte_usimSerialNo" style="width:90%"></td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input type="text" name="lte_telephoneNo" id="lte_telephoneNo" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_GATE_ID</th>
					<td><input type="text" name="lte_lteGateId" id="lte_lteGateId" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_GATE_PW</th>
					<td><input type="text" name="lte_lteGatePw" id="lte_lteGatePw" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_WIFI_ID</th>
					<td><input type="text" name="lte_lteWifiId" id="lte_lteWifiId" style="width:90%"></td>
				</tr>
				<tr>
					<th>LTE_WIFI_PW</th>
					<td><input type="text" name="lte_lteWifiPw" id="lte_lteWifiPw" style="width:90%"></td>
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
					<td><input name="lte_contract" id="lte_contract" style="width:90%"></td>
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
