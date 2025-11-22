<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelect('${contextPath}', 'user_userType', 'USERTYPE')
	fillUpSelect('${contextPath}', 'user_status', 'USERSTATUS')
	
	var buttons = [
			{type:'search', callback: 'detailUser'    },
			{type:'key'   , callback: 'updatePassword'},
			{type:'del'   , callback: 'deleteUser'    },
	]
	$gridLayout = initializeGrid({
			id:"gridLayout",
			container:"listLayout",
			showCheckBox: false,
			colModel: [
					{ name: 'idKey'   , hidden: true, },
					{ name: 'userType', hidden: true, },
					{ name: 'status'  , hidden: true, },
					{ name: 'no'            , label: 'NO'            , width: 50 , align: 'center',},
					{ name: 'userId'        , label: '아이디'        , width: 150, align: 'center',},
					{ name: 'userName'      , label: '이름'          , width: 150, align: 'center',},
					{ name: 'userTypeName'  , label: '사용자유형'    , width: 100, align: 'center',},
					{ name: 'statusName'    , label: '상태'          , width: 100, align: 'center',},
					{ name: 'lastLoginDate' , label: '최종로그인일시', width: 180, align: 'center', formatter: getGridFullDateFormatClosure()},
					{ name: 'loginFailCount', label: '로긴실패횟수'  , width: 100, align: 'center',},
					{ name: 'createDate'    , label: '생성일'        , width: 180, align: 'center', formatter: getGridFullDateFormatClosure()},
					{ name: 'action'        , label: 'ACTION'        ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	
	refreshList()
}

function refreshList() {
	getUserList(__currentPage)
}

function getUserList(page) {
	var url = "${contextPath}/svc/v1/user/list/" + page + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				$gridLayout.setDataList(data.list)
				pagingInfo_success(data.paging, refreshList)
			},
			function(data) {
				alert(JSON.stringify(data));
			},
			"GET");
}
</script>
<h1>업체관리</h1>
<div id="listLayout">
	<h1>작업예정임 - 기다리삼</h1>
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegistUser()">사용자추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteUser(rowId, rowData) {
	if (false == confirm("사용자를 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다. 임시처리는 사용중지를 해주세요.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/user/" + rowData.idKey + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("사용자 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailUser(rowId, rowData) {
	openPopupForUpdateUser(rowData.idKey)
}

function openPopupForUpdateUser(idKey) {
	var url = "${contextPath}/svc/v1/user/" + idKey + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("사용자수정");
				openPopup('userLayout', 500, 400);
				$("#user_idKey").val(data.idKey);
				$("#user_userId").val(data.userId);
				$("#user_password").val('');
				$("#user_password").prop('readonly', true)
				$("#user_userName").val(data.userName);
				$("#user_userType").val(data.userType);
				$("#user_status").val(data.status);
				$("#user_loginFailCount").val(data.loginFailCount);
			},
			function(data) {
				alert("조회에 실패했습니다.");
			},
			"GET");
}

function openPopupForRegistUser() {
	$("#layertitle").html("사용자추가");
	resetEdit()
	openPopup('userLayout', 500, 400);
}

function resetEdit() {
	$("#user_idKey").val('');
	$("#user_userId").val('');
	$("#user_password").val('');
	$("#user_password").prop('readonly', false)
	$("#user_userName").val('');
	$("#user_userType").val('3');
	$("#user_status").val('1');
}

function cancelEdit() {
	resetEdit()
	closePopup('userLayout');
}

function saveEdit() {
	if ($("#user_userId").val().trim() == "") {
		alert("아이디를 입력해 주세요.");
		return;
	}
	if ($("#user_idKey").val().trim() == "") {
		if ($("#user_password").val().trim() == "") {
			alert("비밀번호를 입력해 주세요.");
			return;
		}
	}
	if ($("#user_userName").val().trim() == "") {
		alert("이름을 입력해 주세요.");
		return;
	}
	if ($("#user_userType").val().trim() == "") {
		alert("사용자 유형을 입력해 주세요. (1, 2, 3 중 하나를 입력)");
		return;
	}
	if ($("#user_userType").val().trim() != "1" &&
		$("#user_userType").val().trim() != "2" &&
		$("#user_userType").val().trim() != "3") {
		alert("사용자 유형을 정확히 입력해 주세요. (1, 2, 3 중 하나를 입력)");
		return;
	}
	
	var user = {};
	user.reqToken = getAuthToken();
	user.idKey = $("#user_idKey").val().trim();
	user.userId = $("#user_userId").val().trim();
	user.password = $("#user_password").val().trim();
	user.userName = $("#user_userName").val().trim();
	user.userType = $("#user_userType").val().trim();
	user.status = $("#user_status").val().trim();
	
	var url = "";
	var method = "";
	if (user.idKey == "") {
		url = "${contextPath}/svc/v1/user";
		method = "POST";
	} else {
		url = "${contextPath}/svc/v1/user/" + user.idKey;
		method = "PUT";
	}
	nesAjax(url,
			JSON.stringify(user),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 사용자가 존재합니다.");
				} else {
					alert("입력에 실패했습니다.");
				}
			},
			method);
}

function initLoginFailCount() {
	var url = "${contextPath}/svc/v1/user/" + $("#user_idKey").val().trim() + "/initLoginFaileCount";
	var method = "PUT";
	var user = {};
	user.reqToken = getAuthToken();
	user.idKey = $("#user_idKey").val().trim();
	
	nesAjax(url,
			JSON.stringify(user),
			function(data) {
				$("#user_loginFailCount").val(data.loginFailCount);
			},
			function(data) {
				alert("초기화에 실패했습니다.");
			},
			method);
}
</script>
<div class="layer_bg" id="userLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="layertitle"></span>
			<a href="#none" class="pop_close white" onClick="cancelEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="user_idKey" id="user_idKey">
			<table class="tbsty">
				<tr>
					<th>사용자ID</th>
					<td><input type="text" name="user_userId" id="user_userId" style="width:90%"></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" name="user_password" id="user_password" style="width:90%"></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="user_userName" id="user_userName" style="width:90%"></td>
				</tr>
				<tr>
					<th>사용자유형</th>
					<td><select name="user_userType" id="user_userType" style="width:90%"></select></td>
				</tr>
				<tr>
					<th>사용자상태</th>
					<td><select name="user_status" id="user_status" style="width:90%"></select></td>
				</tr>
				<tr>
					<th>비번오류횟수</th>
					<td>
						<input type="text" name="user_loginFailCount" id="user_loginFailCount" style="width:55%" readonly>
						<a href="#none" class="btn_type small" onClick="initLoginFailCount();return false;">오류횟수초기화</a>
					</td>
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

<script>
function updatePassword(rowId, rowData, gridId) {
	openPopupForChangePwd(rowData.idKey, rowData.userId)
}

function openPopupForChangePwd(idKey, userId) {
	$("#changePw_idKey").val(idKey);
	$("#changePw_userId").val(userId);
	openPopup('changePwLayout', 500, 230);
}

function resetPwEdit() {
	$("#changePw_idKey").val('');
	$("#changePw_userId").val('');
	$("#changePw_password").val('');
	$("#changePw_password2").val('');
}

function cancelPwEdit() {
	resetPwEdit()
	closePopup('changePwLayout');
	refreshList()
}

function savePwEdit() {
	if ($("#changePw_password").val().trim() == "") {
		alert("비밀번호를 입력해 주세요.");
		return;
	}
	if ($("#changePw_password").val() != $("#changePw_password2").val()) {
		alert("비밀번호가 일치하지 않습니다.");
		return;
	}
	
	var user = {};
	user.reqToken = getAuthToken();
	user.idKey = $("#changePw_idKey").val().trim();
	user.userId = $("#changePw_userId").val().trim();
	user.password = $("#changePw_password").val().trim();
	
	var url = "${contextPath}/svc/v1/user/" + $("#changePw_idKey").val().trim() + "/password";
	var method = "PUT";
	nesAjax(url,
			JSON.stringify(user),
			function(data) {
				cancelPwEdit();
			},
			function(data) {
				alert("비밀번호 변경에 실패했습니다.");
			},
			method);
}
</script>
<div class="layer_bg" id="changePwLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title">비밀번호변경</span>
			<a href="#none" class="pop_close white" onClick="cancelPwEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="changePw_idKey" id="changePw_idKey">
			<input type="hidden" name="changePw_userId" id="changePw_userId">
			<table class="tbsty">
				<tr>
					<th>비밀번호</th>
					<td><input type="password" name="changePw_password" id="changePw_password" style="width:90%"></td>
				</tr>
				<tr>
					<th>비밀번호확인</th>
					<td><input type="password" name="changePw_password2" id="changePw_password2" style="width:90%"></td>
				</tr>
			</table>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<a href="#none" class="btn_type submit" onClick="savePwEdit();return false;">확인</a>
				<a href="#none" class="btn_type normal" onClick="cancelPwEdit();return false;">취소</a>
			</div>
		</div>
	</div>
</div>
