<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelectByUrl("${contextPath}/svc/v1/project/list/0?q=" + getAuthToken(), "welder_projectIdKey", "idKey", "projectName")
	createDatePicker('welder_installDate')
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
					{ name: 'welderId'           , label: '아이디'      , width: 100, align: 'center',},
					{ name: 'projectName'        , label: '프로젝트'    , width: 100, align: 'center',},
					{ name: 'contractCompanyName', label: '설치업체'    , width: 100, align: 'center',},
					{ name: 'modelName'          , label: '모델명'      , width: 100, align: 'center',},
					{ name: 'weldType'           , label: '용접종류'    , width: 100, align: 'center',},
					{ name: 'subDevice'          , label: '부속장비'    , width: 100, align: 'center',},
					{ name: 'customized'         , label: '커스터마이징', width: 120, align: 'center',},
					{ name: 'installDate'        , label: '설치일'      , width: 100, align: 'center', formatter: getGridDateFormatClosure()},
					{ name: 'installLocation'    , label: '설치장소'    , width: 100, align: 'center',},
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
	var url = "${contextPath}/svc/v1/welder/list/" + page + "?q=" + getAuthToken();
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
<h1>용접기관리</h1>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">용접기추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteOne(rowId, rowData) {
	if (false == confirm("용접기를 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/welder/" + rowData.idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("용접기 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/welder/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("용접기정보수정")
				openPopup('defaultPopupLayout', 600, 600)
				$("#welder_idKey").val(data.idKey)
				$("#welder_welderId").val(data.welderId)
				$("#welder_welderId").prop('readonly', true)
				$("#welder_projectIdKey").val(data.projectIdKey)
				$("#welder_modelName").val(data.modelName)
				$("#welder_weldType").val(data.weldType)
				$("#welder_subDevice").val(data.subDevice)
				$("#welder_customized").val(data.customized)
				$("#welder_installDate").datepicker('setDate', data.installDate)
				$("#welder_installLocation").val(data.installLocation)
				$("#welder_memo").val(data.memo)
				$("#welder_orderSeq").val(data.orderSeq)
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegist() {
	$("#layertitle").html("용접기추가");
	resetEdit()
	openPopup('defaultPopupLayout', 600, 600);
}

function resetEdit() {
	$("#welder_idKey").val('')
	$("#welder_welderId").val('')
	$("#welder_welderId").prop('readonly', false)
	$("#welder_projectIdKey").val('')
	$("#welder_modelName").val('')
	$("#welder_weldType").val('')
	$("#welder_subDevice").val('')
	$("#welder_customized").val('')
	$("#welder_installDate").val('')
	$("#welder_installLocation").val('')
	$("#welder_memo").val('')
	$("#welder_orderSeq").val('')
}

function cancelEdit() {
	resetEdit()
	closePopup('defaultPopupLayout')
}

function saveEdit() {
	if ($("#welder_welderId").val().trim() == "") {
		alert("아이디를 입력해 주세요.")
		$("#welder_welderId").focus()
		return
	}
//	if ($("#welder_projectIdKey").val() == undefined || $("#welder_projectIdKey").val().trim() == "" || $("#welder_projectIdKey").val() == "0") {
//		alert("프로젝트를 선택해 주세요.")
//		$("#welder_projectIdKey").focus()
//		return;
//	}
	if ($("#welder_modelName").val().trim() == "") {
		alert("모델명을 입력해 주세요.")
		$("#welder_modelName").focus()
		return
	}
	
	var welder = {};
	welder.authToken = getAuthToken()
	welder.idKey           = $("#welder_idKey").val().trim()
	welder.welderId        = $("#welder_welderId").val().trim()
	welder.projectIdKey    = $("#welder_projectIdKey").val().trim()
	welder.modelName       = $("#welder_modelName").val().trim()
	welder.serialNo        = $("#welder_weldType").val().trim()
	welder.macAddress      = $("#welder_subDevice").val().trim()
	welder.customized      = $("#welder_customized").val().trim()
	welder.installDate     = $("#welder_installDate").datepicker('getDate')
	welder.installLocation = $("#welder_installLocation").val().trim()
	welder.memo            = $("#welder_memo").val().trim()
	welder.orderSeq        = $("#welder_orderSeq").val().trim()
	
	var url = ""
	var method = ""
	if (welder.idKey == "") {
		url = "${contextPath}/svc/v1/welder"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/welder/" + welder.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(welder),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 용접기가 존재합니다.")
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
			<input type="hidden" name="welder_idKey" id="welder_idKey">
			<table class="tbsty">
				<tr>
					<th>용접기 ID</th>
					<td><input type="text" name="welder_welderId" id="welder_welderId" style="width:90%"></td>
				</tr>
				<tr>
					<th>프로젝트</th>
					<td><select name="welder_projectIdKey" id="welder_projectIdKey"></select></td>
				</tr>
				<tr>
					<th>모델명</th>
					<td><input type="text" name="welder_modelName" id="welder_modelName" style="width:90%"></td>
				</tr>
				<tr>
					<th>용접조건</th>
					<td><input type="text" name="welder_weldType" id="welder_weldType" style="width:90%"></td>
				</tr>
				<tr>
					<th>부속장비</th>
					<td><input type="text" name="welder_subDevice" id="welder_subDevice" style="width:90%"></td>
				</tr>
				<tr>
					<th>커스터마이징</th>
					<td><input type="text" name="welder_customized" id="welder_customized" style="width:90%"></td>
				</tr>
				<tr>
					<th>설치일</th>
					<td><input type="text" name="welder_installDate" id="welder_installDate" style="width:90%"></td>
				</tr>
				<tr>
					<th>설치장소</th>
					<td><input type="text" name="welder_installLocation" id="welder_installLocation" style="width:90%"></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="welder_memo" id="welder_memo" style="width:90%" rows="4"></textarea>
				</tr>
				<tr>
					<th>순서</th>
					<td><input type="text" name="welder_orderSeq" id="welder_orderSeq" style="width:90%"></td>
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
