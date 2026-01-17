<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelectByUrl("${contextPath}/svc/v1/company/list/0?q=" + getAuthToken(), "project_brokerIdKey", "idKey", "name")
	fillUpSelectByUrl("${contextPath}/svc/v1/company/list/0?q=" + getAuthToken(), "project_customerIdKey", "idKey", "name")
	createDatePicker('project_contractDate')
	var buttons = [
			{type:'search', callback: 'detailOne'},
			{type:'del'   , callback: 'deleteOne'},
	]
	$gridLayout = initializeGrid({
			id:"gridLayout",
			container:"listLayout",
			showCheckbox: false,
			colModel: [
					{ name: 'idKey'        , hidden: true, },
					{ name: 'brokerIdKey'  , hidden: true, },
					{ name: 'customerIdKey', hidden: true, },
					{ name: 'no'           , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'     , label: '아이디'  , width: 200, align: 'center',},
					{ name: 'name'         , label: '이름'    , width: 200, align: 'center',},
					{ name: 'brokerName'   , label: '판매업체', width: 120, align: 'center',},
					{ name: 'customerName' , label: '설치업체', width: 100, align: 'center',},
					{ name: 'contractDate' , label: '계약일'  , width: 80 , align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'memo'         , label: '메모'    , width: 380, align: 'center',},
					{ name: 'action'       , label: 'ACTION'  ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	refreshList()
}

function refreshList() {
	getDefaultList(__currentPage)
}

function getDefaultList(page) {
	var url = "${contextPath}/svc/v1/project/list/" + page + "?q=" + getAuthToken();
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
<h1>프로젝트관리</h1>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">프로젝트추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteOne(rowId, rowData) {
	if (false == confirm("프로젝트를 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/project/" + rowData.idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("프로젝트 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/project/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("프로젝트수정")
				openPopup('defaultPopupLayout', 600, 600)
				$("#project_idKey").val(data.idKey)
				$("#project_idString").val(data.idString)
				$("#project_idString").prop('readonly', true)
				$("#project_name").val(data.name)
				$("#project_brokerIdKey").val(data.brokerIdKey)
				$("#project_customerIdKey").val(data.customerIdKey)
				$("#project_contractDate").datepicker('setDate', data.contractDate)
				$("#project_memo").val(data.memo)
				$("#project_orderSeq").val(data.orderSeq)
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegist() {
	$("#layertitle").html("프로젝트추가");
	resetEdit()
	openPopup('defaultPopupLayout', 600, 600);
	getNewId()
}

function getNewId() {
	nesAjax("${contextPath}/svc/v1/project/newId?q=" + getAuthToken(),
			null,
			function(data) {
				$("#project_idString").val(data.idString)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function resetEdit() {
	$("#project_idKey").val('')
	$("#project_idString").val('')
	$("#project_idString").prop('readonly', false)
	$("#project_name").val('')
	$("#project_brokerIdKey").val('')
	$("#project_customerIdKey").val('')
	$("#project_contractDate").val('')
	$("#project_memo").val('')
	$("#project_orderSeq").val('')
}

function cancelEdit() {
	resetEdit()
	closePopup('defaultPopupLayout')
}

function saveEdit() {
	if ($("#project_idString").val().trim() == "") {
		alert("프로젝트아이디를 입력해 주세요.")
		$("#project_idString").focus()
		return
	}
	if ($("#project_name").val().trim() == "") {
		alert("프로젝트이름을 입력해 주세요.")
		$("#project_name").focus()
		return
	}
	if ($("#project_customerIdKey").val() == undefined || $("#project_customerIdKey").val().trim() == "" || $("#project_customerIdKey").val() == "0") {
		alert("설치업체를 선택해 주세요.")
		$("#project_contractCompanyIdKey").focus()
		return;
	}
	if ($("#project_contractDate").val().trim() == "") {
		alert("계약일을 선택해 주세요.")
		$("#project_contractDate").focus()
		return;
	}
	
	var project = {};
	project.authToken = getAuthToken()
	project.idKey         = $("#project_idKey").val().trim()
	project.idString      = $("#project_idString").val().trim()
	project.name          = $("#project_name").val().trim()
	if ($("#project_brokerIdKey").val()) {
		project.brokerIdKey   = $("#project_brokerIdKey").val().trim()
	}
	project.customerIdKey = $("#project_customerIdKey").val()
	project.contractDate  = $("#project_contractDate").datepicker('getDate')
	project.memo          = $("#project_memo").val().trim()
	project.orderSeq      = $("#project_orderSeq").val().trim()
	
	var url = ""
	var method = ""
	if (project.idKey == "") {
		url = "${contextPath}/svc/v1/project"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/project/" + project.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(project),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 프로젝트체가 존재합니다.")
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
			<input type="hidden" name="project_idKey" id="project_idKey">
			<table class="tbsty">
				<tr>
					<th>프로젝트ID</th>
					<td><input type="text" name="project_idString" id="project_idString" style="width:90%"></td>
				</tr>
				<tr>
					<th>프로젝트이름</th>
					<td><input type="text" name="project_name" id="project_name" style="width:90%"></td>
				</tr>
				<tr>
					<th>판매업체</th>
					<td><select name="project_brokerIdKey" id="project_brokerIdKey"></select></td>
				</tr>
				<tr>
					<th>설치업체</th>
					<td><select name="project_customerIdKey" id="project_customerIdKey"></select></td>
				</tr>
				<tr>
					<th>계약일</th>
					<td><input type="text" class="form-control" id="project_contractDate" /></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="project_memo" id="project_memo" style="width:90%" rows="4"></textarea>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="project_orderSeq" id="project_orderSeq" style="width:90%"></td>
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
