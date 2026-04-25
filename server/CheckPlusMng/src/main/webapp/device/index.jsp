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
					{ name: 'idKey'      , hidden: true, },
					{ name: 'no'         , label: 'NO'          , width: 50 , align: 'center',},
					{ name: 'idString'   , label: '아이디'      , width: 200, align: 'center',},
					{ name: 'modelType'  , label: '모델종류'    , width: 240, align: 'center',},
					{ name: 'modelName'  , label: '모델명'      , width: 200, align: 'center',},
					{ name: 'weldType'   , label: '용접종류'    , width: 140, align: 'center',},
					{ name: 'customized' , label: '커스터마이징', width: 400, align: 'center',},
					{ name: 'memo'       , label: '메모'        , width: 580, align: 'center',},
					{ name: 'action'     , label: 'ACTION'      ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	refreshList()
}

function refreshList() {
	getDefaultList(__currentPage)
}

function getDefaultList(page) {
	var url = "${contextPath}/svc/v1/device/list/" + page + "?q=" + getAuthToken()
	if ($("#search_keyword").val().trim() != "") {
		url = url + "&search=" + $("#search_keyword").val()
	}
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
<h1>디바이스관리</h1>
<div class="search-area">
	<input type="text" class="search-keyword" id="search_keyword">
	<button class="btn_type small" onclick="__currentPage=1; refreshList()">검색</button>
</div>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">디바이스추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteOne(rowId, rowData) {
	if (false == confirm("디바이스를 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/device/" + rowData.idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("디바이스 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/device/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("디바이스정보수정")
				openPopup('defaultPopupLayout', 600, 600)
				$("#device_idKey").val(data.idKey)
				$("#device_idString").val(data.idString)
				$("#device_idString").prop('readonly', true)
				$("#device_modelType").val(data.modelType)
				$("#device_modelName").val(data.modelName)
				$("#device_weldType").val(data.weldType)
				$("#device_customized").val(data.customized)
				$("#device_memo").val(data.memo)
				$("#device_orderSeq").val(data.orderSeq)
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegist() {
	$("#layertitle").html("디바이스추가");
	resetEdit()
	openPopup('defaultPopupLayout', 600, 600);
	getNewId()
}

function getNewId() {
	nesAjax("${contextPath}/svc/v1/device/newId?q=" + getAuthToken(),
			null,
			function(data) {
				$("#device_idString").val(data.idString)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function resetEdit() {
	$("#device_idKey").val('')
	$("#device_idString").val('')
	$("#device_idString").prop('readonly', false)
	$("#device_modelType").val('')
	$("#device_modelName").val('')
	$("#device_weldType").val('')
	$("#device_customized").val('')
	$("#device_memo").val('')
	$("#device_orderSeq").val('')
}

function cancelEdit() {
	resetEdit()
	closePopup('defaultPopupLayout')
}

function saveEdit() {
	if ($("#device_idString").val().trim() == "") {
		alert("아이디를 입력해 주세요.")
		$("#device_idString").focus()
		return
	}
	if ($("#device_modelType").val().trim() == "") {
		alert("모델종류를 입력해 주세요.")
		$("#device_modelType").focus()
		return
	}
	if ($("#device_modelName").val().trim() == "") {
		alert("모델명을 입력해 주세요.")
		$("#device_modelName").focus()
		return
	}
	
	var device = {};
	device.authToken = getAuthToken()
	device.idKey      = $("#device_idKey").val().trim()
	device.idString   = $("#device_idString").val().trim()
	device.modelType  = $("#device_modelType").val().trim()
	device.modelName  = $("#device_modelName").val().trim()
	device.weldType   = $("#device_weldType").val().trim()
	device.customized = $("#device_customized").val().trim()
	device.memo       = $("#device_memo").val().trim()
	device.orderSeq   = $("#device_orderSeq").val().trim()
	
	var url = ""
	var method = ""
	if (device.idKey == "") {
		url = "${contextPath}/svc/v1/device"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/device/" + device.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(device),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 디바이스가 존재합니다.")
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
			<input type="hidden" name="device_idKey" id="device_idKey">
			<table class="tbsty">
				<tr>
					<th>디바이스ID</th>
					<td><input type="text" name="device_idString" id="device_idString" style="width:90%"></td>
				</tr>
				<tr>
					<th>모델종류</th>
					<td><input type="text" name="device_modelType" id="device_modelType" style="width:90%"></td>
				</tr>
				<tr>
					<th>모델명</th>
					<td><input type="text" name="device_modelName" id="device_modelName" style="width:90%"></td>
				</tr>
				<tr>
					<th>용접종류</th>
					<td><input type="text" name="device_weldType" id="device_weldType" style="width:90%"></td>
				</tr>
				<tr>
					<th>커스터마이징</th>
					<td><input type="text" name="device_customized" id="device_customized" style="width:90%"></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="device_memo" id="device_memo" style="width:90%" rows="4"></textarea>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="device_orderSeq" id="device_orderSeq" style="width:90%"></td>
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
