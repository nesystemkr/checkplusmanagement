<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
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
					{ name: 'no'        , label: 'NO'        , width: 50 , align: 'center',},
					{ name: 'idString'  , label: '아이디'    , width: 100, align: 'center',},
					{ name: 'modelName' , label: '모델명'    , width: 100, align: 'center',},
					{ name: 'serialNo'  , label: 'Serial No' , width: 100, align: 'center',},
					{ name: 'macAddress', label: 'MAC Addr'  , width: 120, align: 'center',},
					{ name: 'gateId'    , label: 'AP_GATE_ID', width: 100, align: 'center',},
					{ name: 'gatePw'    , label: 'AP_GATE_PW', width: 100, align: 'center',},
					{ name: 'wifiId'    , label: 'AP_WIFI_ID', width: 100, align: 'center',},
					{ name: 'wifiPw'    , label: 'AP_WIFI_PW', width: 100, align: 'center',},
					{ name: 'memo'      , label: '메모'      , width: 280, align: 'center',},
					{ name: 'action'    , label: 'ACTION'    ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	refreshList()
}

function refreshList() {
	getDefaultList(__currentPage)
}

function getDefaultList(page) {
	var url = "${contextPath}/svc/v1/wifi/list/" + page + "?q=" + getAuthToken();
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
<h1>WIFI기기관리</h1>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">WIFI기기추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteOne(rowId, rowData) {
	if (false == confirm("WIFI기기를 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/wifi/" + rowData.idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("WIFI기기 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/wifi/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("WIFI기기정보수정")
				openPopup('defaultPopupLayout', 600, 600)
				$("#wifi_idKey").val(data.idKey)
				$("#wifi_idString").val(data.idString)
				$("#wifi_idString").prop('readonly', true)
				$("#wifi_modelName").val(data.modelName)
				$("#wifi_serialNo").val(data.serialNo)
				$("#wifi_macAddress").val(data.macAddress)
				$("#wifi_gateId").val(data.gateId)
				$("#wifi_gatePw").val(data.gatePw)
				$("#wifi_wifiId").val(data.wifiId)
				$("#wifi_wifiPw").val(data.wifiPw)
				$("#wifi_memo").val(data.memo)
				$("#wifi_orderSeq").val(data.orderSeq)
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegist() {
	$("#layertitle").html("WIFI기기추가");
	resetEdit()
	openPopup('defaultPopupLayout', 600, 600);
	getNewId()
}

function getNewId() {
	nesAjax("${contextPath}/svc/v1/wifi/newId?q=" + getAuthToken(),
			null,
			function(data) {
				$("#wifi_idString").val(data.idString)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function resetEdit() {
	$("#wifi_idKey").val('')
	$("#wifi_idString").val('')
	$("#wifi_idString").prop('readonly', false)
	$("#wifi_modelName").val('')
	$("#wifi_serialNo").val('')
	$("#wifi_macAddress").val('')
	$("#wifi_gateId").val('')
	$("#wifi_gatePw").val('')
	$("#wifi_wifiId").val('')
	$("#wifi_wifiPw").val('')
	$("#wifi_memo").val('')
	$("#wifi_orderSeq").val('')
}

function cancelEdit() {
	resetEdit()
	closePopup('defaultPopupLayout')
}

function saveEdit() {
	if ($("#wifi_idString").val().trim() == "") {
		alert("아이디를 입력해 주세요.")
		$("#wifi_idString").focus()
		return
	}
	if ($("#wifi_modelName").val().trim() == "") {
		alert("모델명을 입력해 주세요.")
		$("#wifi_modelName").focus()
		return
	}
	
	var wifi = {};
	wifi.authToken = getAuthToken()
	wifi.idKey      = $("#wifi_idKey").val().trim()
	wifi.idString   = $("#wifi_idString").val().trim()
	wifi.modelName  = $("#wifi_modelName").val().trim()
	wifi.serialNo   = $("#wifi_serialNo").val().trim()
	wifi.macAddress = $("#wifi_macAddress").val().trim()
	wifi.gateId   = $("#wifi_gateId").val().trim()
	wifi.gatePw   = $("#wifi_gatePw").val().trim()
	wifi.wifiId   = $("#wifi_wifiId").val().trim()
	wifi.wifiPw   = $("#wifi_wifiPw").val().trim()
	wifi.memo       = $("#wifi_memo").val().trim()
	wifi.orderSeq   = $("#wifi_orderSeq").val().trim()
	
	var url = ""
	var method = ""
	if (wifi.idKey == "") {
		url = "${contextPath}/svc/v1/wifi"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/wifi/" + wifi.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(wifi),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 WIFI기기가 존재합니다.")
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
			<input type="hidden" name="wifi_idKey" id="wifi_idKey">
			<table class="tbsty">
				<tr>
					<th>WIFI ID</th>
					<td><input type="text" name="wifi_idString" id="wifi_idString" style="width:90%"></td>
				</tr>
				<tr>
					<th>모델명</th>
					<td><input type="text" name="wifi_modelName" id="wifi_modelName" style="width:90%"></td>
				</tr>
				<tr>
					<th>Serial No</th>
					<td><input type="text" name="wifi_serialNo" id="wifi_serialNo" style="width:90%"></td>
				</tr>
				<tr>
					<th>MAC Addr</th>
					<td><input type="text" name="wifi_macAddress" id="wifi_macAddress" style="width:90%"></td>
				</tr>
				<tr>
					<th>AP_GATE_ID</th>
					<td><input type="text" name="wifi_gateId" id="wifi_gateId" style="width:90%"></td>
				</tr>
				<tr>
					<th>AP_GATE_PW</th>
					<td><input type="text" name="wifi_gatePw" id="wifi_gatePw" style="width:90%"></td>
				</tr>
				<tr>
					<th>AP_WIFI_ID</th>
					<td><input type="text" name="wifi_wifiId" id="wifi_wifiId" style="width:90%"></td>
				</tr>
				<tr>
					<th>AP_WIFI_PW</th>
					<td><input type="text" name="wifi_wifiPw" id="wifi_wifiPw" style="width:90%"></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="wifi_memo" id="wifi_memo" style="width:90%" rows="4"></textarea>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="wifi_orderSeq" id="wifi_orderSeq" style="width:90%"></td>
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
