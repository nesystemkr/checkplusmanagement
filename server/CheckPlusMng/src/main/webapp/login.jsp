<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>

<script>
function login() {
	if ($("#userId").val().trim() == "") {
		alert("아이디를 입력하세요.");
		return
	}
	
	if ($("#userPw").val().trim() == "") {
		alert("비밀번호를 입력하세요.");
		return
	}

	var login = {}
	login.userId = $("#userId").val().trim();
	login.password = $("#userPw").val().trim();
	login.deviceType = "WEB"
	
	var url = "${contextPath}/svc/v1/login"
	nesAjax(url,
			JSON.stringify(login),
			function(data) {
				saveLoginInfo(data.authToken, data.userIdKey, data.userId,
							  data.userType, data.userName, JSON.stringify(data.menus))
				document.location = "${contextPath}/"
			},
			function(data) {
				alert("로그인에 실패했습니다.");
			},
			"POST");
}

function openSignup() {
	openPopup('signupLayout', 600, 400);
}

function resetSignup() {
	$("#user_idKey").val('')
	$("#user_userId").val('')
	$("#user_password").val('')
	$("#user_password2").val('')
	$("#user_userName").val('')
	$("#user_userType").val('')
}

function cancelSignup() {
	resetSignup()
	closePopup('signupLayout')
}

function signup() {
	if ($("#user_userId").val().trim() == "") {
		alert("아이디를 입력해 주세요.");
		return;
	}
	if ($("#user_password").val().trim() == "") {
		alert("비밀번호를 입력해 주세요.");
		return;
	}
	if ($("#user_password").val().trim() != $("#user_password2").val().trim()) {
		alert("비밀번호가 일치하지 않습니다.");
		return;
	}
	if ($("#user_userName").val().trim() == "") {
		alert("이름을 입력해 주세요.");
		return;
	}
	
	var user = {};
	user.reqToken = "signupbyuser";
	user.idKey = $("#user_idKey").val().trim();
	user.userId = $("#user_userId").val().trim();
	user.password = $("#user_password").val().trim();
	user.userName = $("#user_userName").val().trim();
	user.userType = $("#user_userType").val().trim();
	
	var url = "";
	var method = "";
	url = "${contextPath}/svc/v1/user";
	method = "POST";
	nesAjax(url,
			JSON.stringify(user),
			function(data) {
				alert("회원가입이 완료되었습니다. 로그인 해 주세요.");
				cancelSignup();
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
</script>

<div class="login_area">
	<div class="login_box">
		<h2>NESystem AppEngine Base</h2>
		<ul>
			<li>
				<span>${L10N.get("SIGNIN_LOGINID")}</span>
				<input type="text" id="userId" name="userId" class="text1"
						title="아이디를 입력하세요." maxlength="12"/>
			</li>
			<li>
				<span>${L10N.get("SIGNIN_PASSWORD")}</span>
				<input type="password" id="userPw" name="userPw" class="text1"
						title="비밀번호를 입력하세요." maxlength="12" value="" />
			</li>
		</ul>
		<div class="btn_box">
			<a class="btn_type submit" href="javascript:openSignup()">${L10N.get("SIGNIN_SIGNUPBTN")}</a>&nbsp;
			<a class="btn_type submit" href="javascript:login()">${L10N.get("SIGNIN_LOGINBTN")}</a>
		</div>
	</div>
</div>

<div class="layer_bg" id="signupLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="layertitle">회원가입</span>
			<a href="#none" class="pop_close" onClick="cancelEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="user_idKey" id="user_idKey">
			<table class="tbsty">
				<tr>
					<th>사용자ID</th>
					<td><input type="text" name="user_userId" id="user_userId" style="width:60%"></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" name="user_password" id="user_password" style="width:60%"></td>
				</tr>
				<tr>
					<th>비밀번호확인</th>
					<td><input type="password" name="user_password2" id="user_password2" style="width:60%"></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="user_userName" id="user_userName" style="width:60%"></td>
				</tr>
				<tr>
					<th>유형</th>
					<td><input type="text" name="user_userType" id="user_userType" style="width:60%"></td>
				</tr>
			</table>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<a href="#none" class="btn_type submit" onClick="signup();return false;">확인</a>
				<a href="#none" class="btn_type normal" onClick="cancelSignup();return false;">취소</a>
			</div>
		</div>
	</div>
</div>

</body>
</html>
