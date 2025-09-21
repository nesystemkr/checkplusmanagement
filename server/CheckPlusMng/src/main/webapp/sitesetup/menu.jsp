<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $menuTree
startFuncs[startFuncs.length] = function() {
	fillUpSelect("${contextPath}", "menu_status", "MENUSTATUS", "1")
	getUserTypeForAuth()
	var url = "${contextPath}/svc/v1/code/USERTYPE"
	fillUpSelectByUrl(url, "userTypeSelect", "code", "codeNameLocale", "", "", "전체")
	$menuTree = initializeTree({
			id: 'menuTreeLayout',
			dnd: true,
			onSelectNode: function(data) {
				getMenuDataForUpdate(data.idKey)
			},
			idField: 'idKey',
			parentIdField: 'parentIdKey',
			captionField: 'menuName',
			captionFunc: function(data) {
					var ret = "<span style='"
					ret += "color:" + ((data.status == "1") ? "#000000" : "#888888") 
					ret += "'>"
					ret += data.menuName + "(" + data.menuLocale + ") - "
					ret += "<strong>" + (data.menuUrl ?  data.menuUrl : "NOURL") + "</strong> - "
					//ret += data.statusName + " - ("
					ret += "("
					var authRet = ""
					for (var ii=0; ii<data.menuAuths.length; ii++) {
						if (data.menuAuths[ii].allowYN == 'Y') {
							if (authRet.length > 0) {
								authRet += "/"
							}
							authRet += data.menuAuths[ii].userTypeName
						}
					}
					ret += authRet
					ret += ")"
					ret += "</span>"
					return ret;
				},
	})
	refreshList()
}

var currentUserType =""
function refreshList() {
	getList(currentUserType)
}

function getList(userType) {
	var url = "${contextPath}/svc/v1/menu/listbyusertype/" + userType + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				currentUserType = userType
				$menuTree.setTreeDataList(data.list)
			},
			function(data) {
				alert(JSON.stringify(data));
			},
			"GET")
}

function onChangeUserType() {
	currentUserType = $("#userTypeSelect").val()
	refreshList()
}

function getUserTypeForAuth() {
	var url = "${contextPath}/svc/v1/code/USERTYPE"
	nesAjax(url,
			null,
			function(data) {
				for (var ii=0; ii<data.list.length; ii++) {
					$("#menu_menuAuth").append("<input type='checkbox' class='checkboxMenuAuth' " +
							"id='menuAuth_" + data.list[ii].code + "' value='" + data.list[ii].code + "'>" +
							data.list[ii].codeNameLocale + "<br>")
				}
			},
			function(data) {
				alert(JSON.stringify(data));
			},
			"GET")
}

function getMenuDataForUpdate(menuIdKey) {
	var url = "${contextPath}/svc/v1/menu/" + menuIdKey + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				$("#menu_idKey").val(data.idKey);
				$("#menu_parentIdKey").val(data.parentIdKey != undefined ? data.parentIdKey : "0");
				$("#menu_menuName").val(data.menuName);
				$("#menu_menuLocale").val(data.menuLocale);
				$("#menu_menuUrl").val(data.menuUrl);
				$("#menu_orderSeq").val(data.orderSeq);
				$("#menu_status").val(data.status);
				$(".checkboxMenuAuth").prop("checked", false)
				if (data.menuAuths) {
					for (var ii=0; ii<data.menuAuths.length; ii++) {
						if (data.menuAuths[ii].allowYN == "Y") {
							$("#menuAuth_" + data.menuAuths[ii].userType).prop("checked", true)
						}
					}
				}
			},
			function(data) {
				alert("조회에 실패했습니다.");
			},
			"GET");
}

function newMenu() {
	resetEdit()
}

function resetEdit() {
	$("#menu_idKey").val('');
	$("#menu_parentIdKey").val('0');
	$("#menu_menuName").val('');
	$("#menu_menuLocale").val('');
	$("#menu_menuUrl").val('');
	$("#menu_orderSeq").val('');
	$("#menu_status").val("1");
	$(".checkboxMenuAuth").prop("checked", false)
}

function delMenu() {
	if ($("#menu_idKey").val() == "") {
		return;
	}
	if (false == confirm("삭제시 복원할 수 없습니다. 계속하시겠습니까?")) {
		return;
	}
	var url = "${contextPath}/svc/v1/menu/" + $("#menu_idKey").val() + "?q=" + getAuthToken();
	nesAjax(url,
			null,
			function(data) {
				resetEdit()
				refreshList()
			},
			function(data) {
				alert("조회에 실패했습니다.");
			},
			"DELETE");
	
}

function saveMenu() {
	if ($("#menu_menuName").val().trim() == "") {
		alert("이름을 입력해 주세요.");
		return;
	}
	
	var menu = {};
	menu.reqToken = getAuthToken();
	menu.idKey = $("#menu_idKey").val().trim();
	menu.parentIdKey = $("#menu_parentIdKey").val().trim();
	menu.menuName = $("#menu_menuName").val().trim();
	menu.menuUrl = $("#menu_menuUrl").val().trim();
	menu.status = $("#menu_status").val().trim();
	if (menu.status == '') {
		menu.status = "1"
	}
	menu.menuAuths = []
	var checkMenuAuths = $(".checkboxMenuAuth")
	for (var ii=0; ii<checkMenuAuths.length; ii++) {
		var menuAuth = {}
		menuAuth.userType = $(checkMenuAuths[ii]).val()
		if ($(checkMenuAuths[ii]).prop("checked") == true) {
			menuAuth.allowYN = "Y"
		} else {
			menuAuth.allowYN = "N"
		}
		menu.menuAuths[menu.menuAuths.length] = menuAuth
	}
	
	var url = "";
	var method = "";
	if ($("#menu_idKey").val() == "") {
		url = "${contextPath}/svc/v1/menu";
		method = "POST";
	} else {
		url = "${contextPath}/svc/v1/menu/" + menu.idKey;
		method = "PUT";
	}
	nesAjax(url,
			JSON.stringify(menu),
			function(data) {
				resetEdit();
				refreshList();
			},
			function(data) {
				alert("입력에 실패했습니다.");
			},
			method);
}

function saveMenus() {
	checkOrderSeq($menuTree.getJson("#"))
	var allflat = $menuTree.getJson("#", {flat: true});
	var changed = []
	for (var ii=0; ii<allflat.length; ii++) {
		var nodeData = $menuTree.getNode(allflat[ii]).data;
		if (nodeData.touch == 'U' || nodeData.touch == 'I') {
			changed[changed.length] = nodeData
		}
	}
	if (changed.length == 0) {
		return
	}
	
	var paging = {};
	paging.reqToken = getAuthToken();
	paging.list = changed
	
	var url = "${contextPath}/svc/v1/menu/list";
	nesAjax(url,
			JSON.stringify(paging),
			function(data) {
				resetEdit();
				refreshList();
			},
			function(data) {
				alert("수정에 실패했습니다.");
			},
			"PUT");
}

function checkOrderSeq(list) {
	for (var ii=0; ii<list.length; ii++) {
		var nodeData = $menuTree.getNode(list[ii]).data;
		if (nodeData.orderSeq != ii + 1) {
			nodeData.orderSeq = ii + 1
			nodeData.touch = 'U'
		}
		if (list[ii].children) {
			checkOrderSeq(list[ii].children)
		}
	}
}
</script>
<div style="font-size:20px;margin-top:15px;"><strong>메뉴관리</strong></div>
<div class="btn_box" style="padding-top:10px;padding-bottom:5px;vertical-align:middle;">
	<span style="float:left;font-weight:700;padding:5px;">사용자유형 : </span>
	<select id="userTypeSelect" style="float:left;margin-top:5px;" onchange="onChangeUserType()"></select>
</div>
<table>
	<tr>
		<td style="width:700px;vertical-align:top;">
			<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
				<span style="float:left;font-weight:700;padding:5px;">메뉴리스트</span>
				<button class="btn_type small" onclick="saveMenus()">저장</button>
			</div>
			<div id="menuTreeLayout" style="border:1px solid #dfdfdf;padding:5px 5px 5px 5px;height:100%;"></div>
		</td>
		<td style="width:500px;vertical-align:top;">
			<div class="btn_box" style="padding-top:10px;padding-bottom:5px;">
				<span style="float:left;font-weight:700;padding:5px;">메뉴상세</span>
				<button class="btn_type small" onclick="newMenu()">새메뉴</button>
				<button class="btn_type small" onclick="delMenu()">삭제</button>
				&nbsp;&nbsp;
				<button class="btn_type small" onclick="saveMenu()">저장</button>
			</div>
			<input type="hidden" name="menu_idKey" id="menu_idKey">
			<input type="hidden" name="menu_parentIdKey" id="menu_parentIdKey">
			<table class="tbsty">
				<tr>
					<th>메뉴명</th>
					<td><input type="text" name="menu_menuName" id="menu_menuName" style="width:95%"></td>
				</tr>
				<tr>
					<th>메뉴명(로컬)</th>
					<td><input type="text" name="menu_menuLocale" id="menu_menuLocale" style="width:95%" readonly></td>
				</tr>
				<tr>
					<th>URL</th>
					<td><input type="text" name="menu_menuUrl" id="menu_menuUrl" style="width:95%"></td>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="menu_orderSeq" id="menu_orderSeq" style="width:95%" readonly></td>
				</tr>
				<tr>
					<th>상태</th>
					<td><select id="menu_status" style="width:95%"></select></td>
				</tr>
				<tr>
					<th>권한</th>
					<td>
						<div id="menu_menuAuth">
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<jsp:include page="/common/footer.jsp"/>
