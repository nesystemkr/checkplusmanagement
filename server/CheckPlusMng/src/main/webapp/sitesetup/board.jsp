<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
startFuncs[startFuncs.length] = function() {
	fillUpSelect("${contextPath}", "board_status", "BOARDSTATUS", "1")
	getUserTypes();
	refreshList();
}

var __userTypes
function getUserTypes() {
	var url = "${contextPath}/svc/v1/code/USERTYPE"
	nesAjax(url,
			null,
			function(data) {
				__userTypes = data.list
			},
			function(data) {
			},
			"GET");
}

function refreshList() {
	getList(__currentPage)
}

function getList(page) {
	var url = "${contextPath}/svc/v1/board/list/" + page + "?q=" + getAuthToken();
	nesAjax(url, null,
		function(data) {
			$("#listLayout").html(TrimPath.processDOMTemplate("boardlist_jst", data));
			pagingInfo_success(data.paging, refreshList)
		},
		function(data) {
			alert(JSON.stringify(data));
		},
		"GET");
}
</script>
<h1>게시판목록</h1>
<div id="listLayout">
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForInsertBoard()">게시판추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<script type="text/template" id="boardlist_jst">
<table class="tbsty list">
	<tr>
		<th class="p-2">NO</th>
		<th class="p-2">게시판</th>
		<th class="p-2">게시판ID</th>
		<th class="p-2">상태</th>
		<th class="p-2">옵션</th>
		<th class="p-2">권한</th>
		<th class="p-2">Action</th>
	</tr>
	{for board in list}
	<tr>
		<td class="p-2">{{board.no}</td>
		<td class="p-2">{{board.boardName}</td>
		<td class="p-2">{{board.boardId}</td>
		<td class="p-2">{{board.statusName}</td>
		<td class="p-2">
			<table cellpadding=0 cellspacing=0 boarder=0 style="width:100%;">
				<tr><th>첨부파일</th>
					<td>
					{if board.attachmentYN == "Y"}
						<i class='far fa-circle' style='font-size:24px'></i>
					{else}
						<i class="fa fa-times" style="font-size:24px"></i>
					{/if}
					</td>
					<th>답글</th>
					<td>
					{if board.answerYN == "Y"}
						<i class='far fa-circle' style='font-size:24px'></i>
					{else}
						<i class="fa fa-times" style="font-size:24px"></i>
					{/if}
					</td>
				</tr>
				<tr><th>상단고정</th>
					<td>
					{if board.topYN == "Y"}
						<i class='far fa-circle' style='font-size:24px'></i>
					{else}
						<i class="fa fa-times" style="font-size:24px"></i>
					{/if}
					</td>
					<th>댓글</th>
					<td>
					{if board.replyYN == "Y"}
						<i class='far fa-circle' style='font-size:24px'></i>
					{else}
						<i class="fa fa-times" style="font-size:24px"></i>
					{/if}
					</td>
				</tr>
				<tr><th>로그인조회</th>
					<td>
					{if board.loginViewYN == "Y"}
						<i class='far fa-circle' style='font-size:24px'></i>
					{else}
						<i class="fa fa-times" style="font-size:24px"></i>
					{/if}
					</td>
					<th>비밀글</th>
					<td>
					{if board.secretYN == "Y"}
						<i class='far fa-circle' style='font-size:24px'></i>
					{else}
						<i class="fa fa-times" style="font-size:24px"></i>
					{/if}
					</td>
				</tr>
			</table>
		</td>
		<td class="p-2">
			<table cellpadding=0 cellspacing=0 boarder=0 style="width:100%;">
				<tbody>
					<tr>
						<th></th>
						<th>비공개조회</th>
						<th>입력가능</th>
						<th>타인수정</th>
					</tr>
				{for boardAuth in board.boardAuths}
					<tr>
						<th style="border:1px solid #cccccc">{{boardAuth.userTypeName}</th>
						<td style="border:1px solid #cccccc">
							{if boardAuth.allowPrivateViewYN == 'Y'}
								<i class='far fa-circle' style='font-size:24px'></i>
							{else}
								<i class="fa fa-times" style="font-size:24px"></i>
							{/if}
						</td>
						<td style="border:1px solid #cccccc">
							{if boardAuth.allowUpdateYN == 'Y'}
								<i class='far fa-circle' style='font-size:24px'></i>
							{else}
								<i class="fa fa-times" style="font-size:24px"></i>
							{/if}
						</td>
						<td style="border:1px solid #cccccc">
							{if boardAuth.allowUpdateOthersYN == 'Y'}
								<i class='far fa-circle' style='font-size:24px'></i>
							{else}
								<i class="fa fa-times" style="font-size:24px"></i>
							{/if}
						</td>
					</tr>
				{/for}
				</tbody>
			</table>
		</td>
		<td class="p-2">
			<i class="fas fa-search" style="cursor:pointer;" onclick="openPopupForUpdateBoard({{board.idKey})"></i>&nbsp;
			<i class="far fa-trash-alt" style="cursor:pointer;" onclick="deleteBoard({{board.idKey})"></i> 
		</td>
	</tr>
	{forelse}
	<tr><td colspan="10">No Data</td></tr>
	{/for}
</table>
</script>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteBoard(idKey) {
	if (false == confirm("삭제하시겠습니까?")) {
		return;
	}
	var url = "${contextPath}/svc/v1/board/" + idKey + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("게시판 삭제에 실패했습니다.");
			},
			method);
}

function openPopupForUpdateBoard(boardIdKey) {
	var url = "${contextPath}/svc/v1/board/" + boardIdKey + "?q=" + getAuthToken();
	var method = "GET";
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("게시판수정");
				openPopup('boardLayout', 600, 600);
				$("#board_idKey").val(data.idKey);
				$("#board_boardName").val(data.boardName);
				$("#board_boardId").val(data.boardId);
				$("#board_attachmentYN").prop("checked", data.attachmentYN == "Y");
				$("#board_answerYN").prop("checked", data.answerYN == "Y");
				$("#board_replyYN").prop("checked", data.replyYN == "Y");
				$("#board_topYN").prop("checked", data.topYN == "Y");
				$("#board_loginViewYN").prop("checked", data.loginViewYN == "Y");
				$("#board_secretYN").prop("checked", data.secretYN == "Y");
				$("#board_status").val(data.status);
				var tableHtml = "<table class='tbsty'>"
				var boardAuth
				for (var ii=0; ii<__userTypes.length; ii++) {
					tableHtml += "<tr>"
					tableHtml += "<th>" + __userTypes[ii].codeNameLocale + "</th>"
					tableHtml += "<td>"
					boardAuth = undefined
					for (var jj=0; jj<data.boardAuths.length; jj++) {
						if (__userTypes[ii].code == data.boardAuths[jj].userType) {
							boardAuth = data.boardAuths[jj]
							break;
						}
					}
					tableHtml += "<input id='auth_" + __userTypes[ii].code + "_allowPrivateViewYN' "
					if (boardAuth != undefined && boardAuth.allowPrivateViewYN == 'Y') {
						tableHtml += "checked "
					}
					tableHtml += "type='checkbox'>비공개조회<br>"
					tableHtml += "<input id='auth_" + __userTypes[ii].code + "_allowUpdateYN' "
					if (boardAuth != undefined && boardAuth.allowUpdateYN == 'Y') {
						tableHtml += "checked "
					}
					tableHtml += "type='checkbox'>입력가능<br>"
					tableHtml += "<input id='auth_" + __userTypes[ii].code + "_allowUpdateOthersYN' "
					if (boardAuth != undefined && boardAuth.allowUpdateOthersYN == 'Y') {
						tableHtml += "checked "
					}
					tableHtml += "type='checkbox'>타인수정<br>"
					tableHtml += "</td>"
					tableHtml += "</tr>"
				}
				tableHtml += "</table>"
				$("#board_auths").html(tableHtml);
			},
			function(data) {
				alert("조회에 실패했습니다.");
			},
			method);
}

function openPopupForInsertBoard() {
	$("#layertitle").html("게시판추가");
	resetEdit();
	openPopup('boardLayout', 600, 600);
}

function resetEdit() {
	$("#board_idKey").val('');
	$("#board_boardName").val('');
	$("#board_boardId").val('');
	$("#board_attachmentYN").prop("checked", false);
	$("#board_answerYN").prop("checked", false);
	$("#board_replyYN").prop("checked", false);
	$("#board_topYN").prop("checked", false);
	$("#board_loginViewYN").prop("checked", false);
	$("#board_secretYN").prop("checked", false);
	$("#board_status").val('1');
	var tableHtml = "<table class='tbsty'>"
	for(var ii=0; ii<__userTypes.length; ii++) {
		tableHtml += "<tr>"
		tableHtml += "<th>" + __userTypes[ii].codeNameLocale + "</th>"
		tableHtml += "<td>"
		tableHtml += "<input id='auth_" + __userTypes[ii].code + "_allowPrivateViewYN' type='checkbox'>비공개조회<br>"
		tableHtml += "<input id='auth_" + __userTypes[ii].code + "_allowUpdateYN' type='checkbox'>입력가능<br>"
		tableHtml += "<input id='auth_" + __userTypes[ii].code + "_allowUpdateOthersYN' type='checkbox'>타인수정<br>"
		tableHtml += "</td>"
		tableHtml += "</tr>"
	}
	tableHtml += "</table>"
	$("#board_auths").html(tableHtml);
}

function cancelEdit() {
	resetEdit();
	closePopup('boardLayout');
}

function saveEdit() {
	if ($("#board_boardName").val().trim() == "") {
		alert("이름을 입력해 주세요.");
		return;
	}
	if ($("#board_boardId").val().trim() == "") {
		alert("아이디를 입력해 주세요.");
		return;
	}
	if ($("#board_status").val().trim() == "") {
		alert("상태를 선택해 주세요.");
		return;
	}
	
	var board = {};
	board.reqToken = getAuthToken();
	board.idKey = $("#board_idKey").val().trim();
	board.boardType = "1"
	board.boardName = $("#board_boardName").val().trim();
	board.boardId = $("#board_boardId").val().trim();
	board.attachmentYN = $("#board_attachmentYN").prop("checked") ? "Y" : "N";
	board.answerYN = $("#board_answerYN").prop("checked") ? "Y" : "N";
	board.replyYN = $("#board_replyYN").prop("checked") ? "Y" : "N";
	board.topYN = $("#board_topYN").prop("checked") ? "Y" : "N";
	board.loginViewYN = $("#board_loginViewYN").prop("checked") ? "Y" : "N";
	board.secretYN = $("#board_secretYN").prop("checked") ? "Y" : "N";
	board.status = $("#board_status").val().trim();
	board.boardAuths = []
	var boardAuth
	for (var ii=0; ii<__userTypes.length; ii++) {
		boardAuth = {}
		boardAuth.boardIdKey = $("#board_idKey").val().trim();
		boardAuth.userType = __userTypes[ii].code
		boardAuth.allowPrivateViewYN = $("#auth_" + __userTypes[ii].code + "_allowPrivateViewYN").prop("checked") ? "Y" : "N"
		boardAuth.allowUpdateYN = $("#auth_" + __userTypes[ii].code + "_allowUpdateYN").prop("checked") ? "Y" : "N"
		boardAuth.allowUpdateOthersYN = $("#auth_" + __userTypes[ii].code + "_allowUpdateOthersYN").prop("checked") ? "Y" : "N"
		board.boardAuths[board.boardAuths.length] = boardAuth
	}
	
	var url = "";
	var method = "";
	if ($("#board_idKey").val() == "") {
		url = "${contextPath}/svc/v1/board";
		method = "POST";
	} else {
		url = "${contextPath}/svc/v1/board/" + board.idKey;
		method = "PUT";
	}
	nesAjax(url,
			JSON.stringify(board),
			function(data) {
				cancelEdit();
				refreshList();
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 게시판이 존재합니다.");
				} else {
					alert("입력에 실패했습니다.");
				}
			},
			method);
}
</script>
<div class="layer_bg" id="boardLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="layertitle"></span>
			<a href="#none" class="pop_close white" onClick="cancelEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="board_idKey" id="board_idKey">
			<table class="tbsty">
				<tr>
					<th>게시판이름</th>
					<td><input type="text" name="board_boardName" id="board_boardName" style="width:95%"></td>
				</tr>
				<tr>
					<th>게시판아이디</th>
					<td><input type="text" name="board_boardId" id="board_boardId" style="width:95%"></td>
				</tr>
				<tr>
					<th>옵션</th>
					<td>
						<input type="checkbox" name="board_attachmentYN" id="board_attachmentYN">첨부파일사용<br>
						<input type="checkbox" name="board_answerYN" id="board_answerYN">답글사용<br>
						<input type="checkbox" name="board_replyYN" id="board_replyYN">댓글사용<br>
						<input type="checkbox" name="board_topYN" id="board_topYN">상단고정사용<br>
						<input type="checkbox" name="board_loginViewYN" id="board_loginViewYN">로그인조회<br>
						<input type="checkbox" name="board_secretYN" id="board_secretYN">비밀글사용<br>
					</td>
				</tr>
				<tr>
					<th>권한</th>
					<td id="board_auths"></td>
				</tr>
				<tr>
					<th>상태</th>
					<td><select id="board_status" style="width:95%"></select></td>
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
