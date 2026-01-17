<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelect('${contextPath}', 'daemon_status', 'DAEMONSTATUS')
	fillUpSelect('${contextPath}', 'daemon_autoStartYN', 'DAEMONAUTOSTART')
	
	var buttons = [
		{type:'search', callback: 'detailDaemon'},
		{type:'del'   , callback: 'deleteDaemon'},
		{type:'stop'  , callback: 'stopDaemon'  },
		{type:'start' , callback: 'startDaemon' },
	]
	$gridLayout = initializeGrid({
			id:"gridLayout",
			container:"listLayout",
			showCheckbox: false,
			colModel: [
					{ name: 'idKey'      , hidden: true, },
					{ name: 'status'     , hidden: true, },
					{ name: 'running'    , hidden: true, },
					{ name: 'autoStartYN', hidden: true, },
					{ name: 'no'         , label: 'NO'        , width: 50 , align: 'center',},
					{ name: 'daemonName' , label: '데몬이름'  , width: 150, align: 'center',},
					{ name: 'className'  , label: '클래스경로', width: 500, align: 'left'  ,},
					{ name: 'orderSeq'   , label: '순서'      , width: 40 , align: 'center',},
					{ name: 'statusName' , label: '상태'      , width: 80 , align: 'center',},
					{ name: 'autoStartNm', label: '자동실행'  , width: 80 , align: 'center',},
					{ name: 'runningName', label: '실행여부'  , width: 80 , align: 'center',},
					{ name: 'action'     , label: 'ACTION'    ,             align: 'center',
							formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	
	refreshList()
}

function refreshList() {
	getList(__currentPage)
}

function getList(page) {
	var url = "${contextPath}/svc/v1/daemon/list/" + page + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				$gridLayout.setDataList(data.list)
				pagingInfo_success(data.paging, refreshList);
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET");
}
</script>

<h1>데몬관리</h1>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegistDaemon()">데몬추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function startDaemon(rowId, rowData) {
	var url = "${contextPath}/svc/v1/daemon/" + rowData.idKey + "/start?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("데몬실행에 실패했습니다.")
			},
			"PUT")
}

function stopDaemon(rowId, rowData) {
	var url = "${contextPath}/svc/v1/daemon/" + rowData.idKey + "/stop?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("데몬중지에 실패했습니다.")
			},
			"PUT")
}

function deleteDaemon(rowId, rowData) {
	if (false == confirm("데몬 삭제 처리하시겠습니까?")) {
		return;
	}
	var url = "${contextPath}/svc/v1/daemon/" + rowData.idKey + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("데몬삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailDaemon(rowId, rowData) {
	openPopupForUpdateDaemon(rowData.idKey)
}

function openPopupForUpdateDaemon(idKey) {
	var url = "${contextPath}/svc/v1/daemon/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("데몬수정")
				openPopup('daemonLayout', 700, 300)
				$("#daemon_idKey").val(data.idKey)
				$("#daemon_daemonName").val(data.daemonName)
				$("#daemon_className").val(data.className)
				$("#daemon_orderSeq").val(data.orderSeq)
				$("#daemon_status").val(data.status)
				$("#daemon_autoStartYN").val(data.autoStartYN)
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegistDaemon() {
	$("#layertitle").html("데몬추가")
	resetEdit()
	openPopup('daemonLayout', 700, 300)
}

function resetEdit() {
	$("#daemon_idKey").val('')
	$("#daemon_daemonName").val('')
	$("#daemon_className").val('')
	$("#daemon_orderSeq").val('')
	$("#daemon_status").val('1')
	$("#daemon_autoStartYN").val('Y')
}

function cancelEdit() {
	resetEdit()
	closePopup('daemonLayout')
}

function saveEdit() {
	if ($("#daemon_daemonName").val().trim() == "") {
		alert("이름을 입력해 주세요.")
		return;
	}
	if ($("#daemon_className").val().trim() == "") {
		alert("클래스를 입력해 주세요.")
		return;
	}
	if ($("#daemon_orderSeq").val().trim() == "") {
		alert("순서를 입력해 주세요.")
		return;
	}
	
	var daemon = {}
	daemon.authToken = getAuthToken()
	daemon.idKey = $("#daemon_idKey").val().trim()
	daemon.daemonName = $("#daemon_daemonName").val().trim()
	daemon.className = $("#daemon_className").val().trim()
	daemon.orderSeq = $("#daemon_orderSeq").val().trim()
	daemon.status = $("#daemon_status").val().trim()
	daemon.autoStartYN = $("#daemon_autoStartYN").val().trim()
	if (daemon.autoStartYN == '') {
		daemon.autoStartYN = 'N'
	}
	var url = ""
	var method = ""
	if (daemon.idKey == "") {
		url = "${contextPath}/svc/v1/daemon"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/daemon/" + daemon.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(daemon),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				alert("입력에 실패했습니다.")
			},
			method)
}
</script>
<div class="layer_bg" id="daemonLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="layertitle"></span>
			<a href="#none" class="pop_close white" onClick="cancelEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="daemon_idKey" id="daemon_idKey">
			<table class="tbsty">
				<tr>
					<th style="width:100px;">데몬이름</th>
					<td><input type="text" name="daemon_daemonName" id="daemon_daemonName" style="width:95%"></td>
				</tr>
				<tr>
					<th>클래스경로</th>
					<td><input type="text" name="daemon_className" id="daemon_className" style="width:95%"></td>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="daemon_orderSeq" id="daemon_orderSeq" style="width:95%"></td>
				</tr>
				<tr>
					<th>상태</th>
					<td><select name="daemon_status" id="daemon_status" style="width:95%"></select></td>
				</tr>
				<tr>
					<th>자동실행</th>
					<td><select name="daemon_autoStartYN" id="daemon_autoStartYN" style="width:95%"></select></td>
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
