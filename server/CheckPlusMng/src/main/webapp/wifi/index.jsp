<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
var $gridLayout
startFuncs[startFuncs.length] = function() {
	fillUpSelect('${contextPath}', 'company_status', 'COMPANYSTATUS')
	
	var buttons = [
			{type:'search', callback: 'detailOne'},
			{type:'del'   , callback: 'deleteOne'},
	]
	$gridLayout = initializeGrid({
			id:"gridLayout",
			container:"listLayout",
			showCheckBox: false,
			colModel: [
					{ name: 'idKey'               , hidden: true, },
					{ name: 'no'                  , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'companyId'           , label: '아이디'  , width: 100, align: 'center',},
					{ name: 'name'                , label: '이름'    , width: 100, align: 'center',},
					{ name: 'telephone1'          , label: '전화번호', width: 120, align: 'center',},
					{ name: 'mainOfficer'         , label: '주담당자', width: 100, align: 'center',},
					{ name: 'mainOfficerPosition' , label: '직책'    , width: 80 , align: 'center',},
					{ name: 'mainOfficerTelephone', label: '전화번호', width: 120, align: 'center',},
					{ name: 'mainOfficerEmail'    , label: '이메일'  , width: 140, align: 'center',},
					{ name: 'memo'                , label: '메모'    , width: 280, align: 'center',},
					{ name: 'action'              , label: 'ACTION'  ,             align: 'center', formatter: getGridButtonClosure(buttons)},
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
<h1>업체관리</h1>
<div id="listLayout">
	<table id="gridLayout" style="width:100%;"></table>
</div>
<div class="btn_box">
	<button class="btn_type normal" onclick="openPopupForRegist()">업체추가</button><br>
</div>

<jsp:include page="/common/paging.jsp"/>

<jsp:include page="/common/footer.jsp"/>

<script>
function deleteOne(rowId, rowData) {
	if (false == confirm("업체를 삭제 처리하시겠습니까? - 삭제처리를 추후 복구가 불가능합니다. 임시처리는 사용중지를 해주세요.")) {
		return;
	}
	var url = "${contextPath}/svc/v1/wifi/" + rowData.idKey + "?q=" + getAuthToken()
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

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/wifi/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("업체정보수정")
				openPopup('defaultPopupLayout', 600, 600)
				$("#company_idKey").val(data.idKey)
				$("#company_companyId").val(data.companyId)
				$("#company_companyId").prop('readonly', true)
				$("#company_name").val(data.name)
				$("#company_address1").val(data.address1)
				$("#company_telephone1").val(data.telephone1)
				$("#company_address2").val(data.address2)
				$("#company_telephone2").val(data.telephone2)
				$("#company_mainOfficer").val(data.mainOfficer)
				$("#company_mainOfficerPosition").val(data.mainOfficerPosition)
				$("#company_mainOfficerTelephone").val(data.mainOfficerTelephone)
				$("#company_mainOfficerEmail").val(data.mainOfficerEmail)
				$("#company_subOfficer").val(data.subOfficer)
				$("#company_subOfficerPosition").val(data.subOfficerPosition)
				$("#company_subOfficerTelephone").val(data.subOfficerTelephone)
				$("#company_subOfficerEmail").val(data.subOfficerEmail)
				$("#company_memo").val(data.memo)
				$("#company_status").val(data.status)
			},
			function(data) {
				alert("조회에 실패했습니다.")
			},
			"GET")
}

function openPopupForRegist() {
	$("#layertitle").html("업체추가");
	resetEdit()
	openPopup('defaultPopupLayout', 600, 600);
}

function resetEdit() {
	$("#company_idKey").val('')
	$("#company_companyId").val('')
	$("#company_companyId").prop('readonly', false)
	$("#company_name").val('')
	$("#company_address1").val('')
	$("#company_telephone1").val('')
	$("#company_address2").val('')
	$("#company_telephone2").val('')
	$("#company_mainOfficer").val('')
	$("#company_mainOfficerPosition").val('')
	$("#company_mainOfficerTelephone").val('')
	$("#company_mainOfficerEmail").val('')
	$("#company_subOfficer").val('')
	$("#company_subOfficerPosition").val('')
	$("#company_subOfficerTelephone").val('')
	$("#company_subOfficerEmail").val('')
	$("#company_memo").val('')
	$("#company_status").val('1')
}

function cancelEdit() {
	resetEdit()
	closePopup('defaultPopupLayout')
}

function saveEdit() {
	if ($("#company_companyId").val().trim() == "") {
		alert("아이디를 입력해 주세요.")
		$("#company_companyId").focus()
		return
	}
	if ($("#company_name").val().trim() == "") {
		alert("이름을 입력해 주세요.")
		$("#company_name").focus()
		return
	}
	if ($("#company_address1").val().trim() == "") {
		alert("주소를 입력해 주세요.")
		$("#company_address1").focus()
		return;
	}
	if ($("#company_telephone1").val().trim() == "") {
		alert("전화번호를 입력해 주세요.")
		$("#company_telephone1").focus()
		return;
	}
	if ($("#company_mainOfficer").val().trim() == "") {
		alert("주 담당자를 입력해 주세요.")
		$("#company_mainOfficer").focus()
		return;
	}
	
	var company = {};
	company.authToken = getAuthToken()
	company.idKey      = $("#company_idKey").val().trim()
	company.companyId  = $("#company_companyId").val().trim()
	company.name       = $("#company_name").val().trim()
	company.address1   = $("#company_address1").val().trim()
	company.telephone1 = $("#company_telephone1").val().trim()
	company.address2   = $("#company_address2").val().trim()
	company.telephone2 = $("#company_telephone2").val().trim()
	company.mainOfficer          = $("#company_mainOfficer").val().trim()
	company.mainOfficerPosition  = $("#company_mainOfficerPosition").val().trim()
	company.mainOfficerTelephone = $("#company_mainOfficerTelephone").val().trim()
	company.mainOfficerEmail     = $("#company_mainOfficerEmail").val().trim()
	company.subOfficer           = $("#company_subOfficer").val().trim()
	company.subOfficerPosition   = $("#company_subOfficerPosition").val().trim()
	company.subOfficerTelephone  = $("#company_subOfficerTelephone").val().trim()
	company.subOfficerEmail      = $("#company_subOfficerEmail").val().trim()
	company.memo   = $("#company_memo").val().trim()
	company.status = $("#company_status").val().trim()
	
	var url = ""
	var method = ""
	if (company.idKey == "") {
		url = "${contextPath}/svc/v1/wifi"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/wifi/" + company.idKey
		method = "PUT"
	}
	nesAjax(url,
			JSON.stringify(company),
			function(data) {
				cancelEdit()
				refreshList()
			},
			function(data) {
				if (data.status == 409) {
					alert("동일한 아이디의 업체가 존재합니다.")
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
			<input type="hidden" name="company_idKey" id="company_idKey">
			<table class="tbsty">
				<tr>
					<th>사용자ID</th>
					<td><input type="text" name="company_companyId" id="company_companyId" style="width:90%"></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="company_name" id="company_name" style="width:90%"></td>
				</tr>
				<tr>
					<th>주소1</th>
					<td><input name="company_address1" id="company_address1" style="width:90%"></td>
				</tr>
				<tr>
					<th>전화번호1</th>
					<td><input name="company_telephone1" id="company_telephone1" style="width:90%"></td>
				</tr>
				<tr>
					<th>주소2</th>
					<td><input name="company_address2" id="company_address2" style="width:90%"></td>
				</tr>
				<tr>
					<th>전화번호2</th>
					<td><input name="company_telephone2" id="company_telephone2" style="width:90%"></td>
				</tr>
				<tr>
					<th>주담당자이름</th>
					<td><input name="company_mainOfficer" id="company_mainOfficer" style="width:90%"></td>
				</tr>
				<tr>
					<th>주담당자직책</th>
					<td><input name="company_mainOfficerPosition" id="company_mainOfficerPosition" style="width:90%"></td>
				</tr>
				<tr>
					<th>주담당자전화번호</th>
					<td><input name="company_mainOfficerTelephone" id="company_mainOfficerTelephone" style="width:90%"></td>
				</tr>
				<tr>
					<th>주담당자이메일</th>
					<td><input name="company_mainOfficerEmail" id="company_mainOfficerEmail" style="width:90%"></td>
				</tr>
				<tr>
					<th>부담당자이름</th>
					<td><input name="company_subOfficer" id="company_subOfficer" style="width:90%"></td>
				</tr>
				<tr>
					<th>부담당자직책</th>
					<td><input name="company_subOfficerPosition" id="company_subOfficerPosition" style="width:90%"></td>
				</tr>
				<tr>
					<th>부담당자전화번호</th>
					<td><input name="company_subOfficerTelephone" id="company_subOfficerTelephone" style="width:90%"></td>
				</tr>
				<tr>
					<th>부담당자이메일</th>
					<td><input name="company_subOfficerEmail" id="company_subOfficerEmail" style="width:90%"></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="company_memo" id="company_memo" style="width:90%" rows="4"></textarea>
				</tr>
				<tr>
					<th>사용자상태</th>
					<td><select name="company_status" id="company_status" style="width:90%"></select></td>
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
