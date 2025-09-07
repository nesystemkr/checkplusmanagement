<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
function logout() {
	eraseLoginInfo()
	document.location = "${contextPath}/"
}

startFuncs[startFuncs.length] = function() {
	$("#header_userName").html(getUserName())
	
	var menuInfo = getMenuInfo()
	try {
		menus = JSON.parse(menuInfo)
	} catch(e) {
		console.log(e)
	}
	if (menus) {
		var menuon
		for (var ii=0; ii<menus.length; ii++) {
			var ele = createDiv("menuitem")
			ele.attr("id", "menu_" + menus[ii].idKey)
			ele.html(menus[ii].menuLocale)
			if (menus[ii].subMenus) {
				var subDiv = createDiv("menuitem-dropdown")
				ele.append(subDiv)
				for (var jj=0; jj<menus[ii].subMenus.length; jj++) {
					var subEle = createDiv("submenuitem")
					subEle.attr("id", "submenu_" + menus[ii].subMenus[jj].idKey)
					subEle.html(menus[ii].subMenus[jj].menuLocale)
					subEle.click(getMenuClickClosure(menus[ii].subMenus[jj].menuUrl))
					subDiv.append(subEle)
				}
			} else {
				ele.click(getMenuClickClosure(menus[ii].menuUrl))
			}
			$("#topmenudiv").append(ele)
		}
	}
}

function getMenuClickClosure(menuUrl) {
	return function() {
		moveTo(menuUrl);
	}
}

function moveTo(url) {
	if (url) {
		document.location = "${contextPath}" + url
	}
}
</script>

<div class="header">
	<a href="${contextPath}/"><img src="${contextPath}/asset/images/logo.png" style="width:120px;height:60px;" /></a>
	<div class="log_info">
		<a href="javascript:openPopupForUpdateMyInfo()"><strong id="header_userName"></strong> 님</a>
		<span>
			<a href="javascript:logout();" class="btn_set sm_smt">로그아웃</a>
		</span>
	</div>
	<div id="topmenudiv" class="topmenu">
	</div>
	<div class="topmenuline"></div>
</div>
<script>
function openPopupForUpdateMyInfo() {
	var url = "${contextPath}/svc/v1/user/" + getUserIdKey() + "?q=" + getAuthToken();
	var method = "GET";
	nesAjax(url,
			null,
			function(data) {
				$("#myInfoTitle").html("내정보수정");
				openPopup('myInfoLayout', 600, 400);
				$("#myInfo_idKey").val(data.idKey);
				$("#myInfo_userId").val(data.userId);
				$("#myInfo_userName").val(data.userName);
				$("#myInfo_userType").val(data.userType);
				$("#myInfo_telNo").val(data.mobileNumber);
				$("#myInfo_address").val(data.address);
				$("#myInfo_comment").val(data.comment);
			},
			function(data) {
				alert("조회에 실패했습니다.");
			},
			method);
}

function cancelMyInfo() {
	$("#myInfo_idKey").val('');
	$("#myInfo_userId").val('');
	$("#myInfo_userName").val('');
	$("#myInfo_userType").val('3');
	$("#myInfo_telNo").val('');
	$("#myInfo_address").val('');
	$("#myInfo_comment").val('');
	
	closePopup('myInfoLayout');
}

function saveMyInfo() {
	if ($("#myInfo_userId").val().trim() == "") {
		alert("아이디를 입력해 주세요.");
		return;
	}
	if ($("#myInfo_userName").val().trim() == "") {
		alert("이름을 입력해 주세요.");
		return;
	}
	if ($("#myInfo_userType").val().trim() == "") {
		alert("사용자 유형을 입력해 주세요. (1, 2, 3 중 하나를 입력)");
		return;
	}
	if ($("#myInfo_userType").val().trim() != "1" &&
		$("#myInfo_userType").val().trim() != "2" &&
		$("#myInfo_userType").val().trim() != "3") {
		alert("사용자 유형을 정확히 입력해 주세요. (1, 2, 3 중 하나를 입력)");
		return;
	}
	
	var user = {};
	user.reqToken = getAuthToken();
	user.idKey = $("#myInfo_idKey").val().trim();
	user.userId = $("#myInfo_userId").val().trim();
	user.userName = $("#myInfo_userName").val().trim();
	user.userType = $("#myInfo_userType").val().trim();
	user.mobileNumber = $("#myInfo_telNo").val().trim();
	user.address = $("#myInfo_address").val().trim();
	user.comment = $("#myInfo_comment").val().trim();
	user.usingServices = "welder";
	
	var url = "/svc/v1/user/" + user.idKey;
	var method = "PUT";
	nesAjax(url,
			JSON.stringify(user),
			function(data) {
				cancelMyInfo();
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
<div class="layer_bg" id="myInfoLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="myInfoTitle">사용자추가</span>
			<a href="#none" class="pop_close white" onClick="cancelMyInfo();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="myInfo_idKey" id="myInfo_idKey">
			<input type="hidden" name="myInfo_userType" id="myInfo_userType">
			<table class="tbsty">
				<tr>
					<th>사용자ID</th>
					<td><input type="text" name="myInfo_userId" id="myInfo_userId" style="width:60%;background:#eee;" readonly></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="button" name="myInfo_password_button" id="myInfo_password_button" style="width:30%" value="비밀번호변경" onclick="openPopupForChangeMyPwd()"></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="myInfo_userName" id="myInfo_userName" style="width:60%"></td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input type="text" name="myInfo_telNo" id="myInfo_telNo" style="width:60%"></td>
				</tr>
				<tr>
					<th>현장주소</th>
					<td><textarea name="myInfo_address" id="myInfo_address" style="width:60%"></textarea></td>
				</tr>
				<tr>
					<th>비고</th>
					<td><textarea name="myInfo_comment" id="myInfo_comment" style="width:60%"></textarea></td>
				</tr>
			</table>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<a href="#none" class="btn_type submit" onClick="saveMyInfo();return false;">확인</a>
				<a href="#none" class="btn_type normal" onClick="cancelMyInfo();return false;">취소</a>
			</div>
		</div>
	</div>
</div>
<script>
function openPopupForChangeMyPwd() {
	$("#changeMyPw_idKey").val(getUserIdKey());
	$("#changeMyPw_userId").val(getUserId());
	openPopup('changeMyPwLayout', 600, 300);
}

function cancelMyPwEdit() {
	$("#changeMyPw_idKey").val('');
	$("#changeMyPw_userId").val('');
	$("#changeMyPw_oldpassword").val('');
	$("#changeMyPw_password").val('');
	$("#changeMyPw_password2").val('');
	
	closePopup('changeMyPwLayout');
}

function saveMyPwEdit() {
	if ($("#changeMyPw_oldpassword").val().trim() == "") {
		alert("이전비밀번호를 입력해 주세요.");
		return;
	}
	if ($("#changeMyPw_password").val().trim() == "") {
		alert("비밀번호를 입력해 주세요.");
		return;
	}
	if ($("#changeMyPw_password").val() != $("#changeMyPw_password2").val()) {
		alert("비밀번호가 일치하지 않습니다.");
		return;
	}
	
	var user = {};
	user.reqToken = getAuthToken();
	user.idKey = $("#changeMyPw_idKey").val().trim();
	user.userId = $("#changeMyPw_userId").val().trim();
	user.zipCode = $("#changeMyPw_oldpassword").val().trim();
	user.password = $("#changeMyPw_password").val().trim();
	
	var url = "${contextPath}/svc/v1/user/" + $("#changeMyPw_idKey").val().trim() + "/password";
	var method = "PUT";
	nesAjax(url,
			JSON.stringify(user),
			function(data) {
				alert("비밀번호가 변경되었습니다.");
				cancelMyPwEdit();
			},
			function(data) {
				alert("비밀번호 변경에 실패했습니다.");
			},
			method);
}
</script>
<div class="layer_bg" id="changeMyPwLayout">
	<div class="layerpop">
		<div class="pop_head">
			<span class="title" id="MyPwTitle">비밀번호변경</span>
			<a href="#none" class="pop_close white" onClick="cancelMyPwEdit();return false;"><span>닫기</span></a>
		</div>
		<div class="pop_body">
			<input type="hidden" name="changeMyPw_idKey" id="changeMyPw_idKey">
			<input type="hidden" name="changeMyPw_userId" id="changeMyPw_userId">
			<table class="tbsty">
				<tr>
					<th>이전비밀번호</th>
					<td><input type="password" name="changeMyPw_oldpassword" id="changeMyPw_oldpassword" style="width:60%"></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" name="changeMyPw_password" id="changeMyPw_password" style="width:60%"></td>
				</tr>
				<tr>
					<th>비밀번호확인</th>
					<td><input type="password" name="changeMyPw_password2" id="changeMyPw_password2" style="width:60%"></td>
				</tr>
			</table>
		</div>
		<div class="pop_foot">
			<div class="btn_box">
				<a href="#none" class="btn_type submit" onClick="saveMyPwEdit();return false;">확인</a>
				<a href="#none" class="btn_type normal" onClick="cancelMyPwEdit();return false;">취소</a>
			</div>
		</div>
	</div>
</div>
<div class="body">
