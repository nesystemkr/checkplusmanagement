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
					{ name: 'idKey'     , hidden: true, },
					{ name: 'no'        , label: 'NO'      , width: 50 , align: 'center',},
					{ name: 'idString'  , label: '아이디'  , width: 100, align: 'center',},
					{ name: 'name'      , label: '이름'    , width: 100, align: 'center',},
					{ name: 'telephone' , label: '전화번호', width: 120, align: 'center',},
					{ name: 'eEmail'    , label: '이메일'  , width: 140, align: 'center',},
					{ name: 'officer'   , label: '주담당자', width: 100, align: 'center',},
					{ name: 'officerTel', label: '담당자T' , width: 120, align: 'center',},
					{ name: 'memo'      , label: '메모'    , width: 280, align: 'center',},
					{ name: 'action'    , label: 'ACTION'  ,             align: 'center', formatter: getGridButtonClosure(buttons)},
			],
			stretchColumn:"action",
	})
	refreshList()
}

function refreshList() {
	getDefaultList(__currentPage)
}

function getDefaultList(page) {
	var url = "${contextPath}/svc/v1/company/list/" + page + "?q=" + getAuthToken();
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
	var url = "${contextPath}/svc/v1/company/" + rowData.idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				refreshList()
			},
			function(data) {
				alert("업체 삭제에 실패했습니다.")
			},
			"DELETE")
}

function detailOne(rowId, rowData) {
	openPopupForUpdate(rowData.idKey)
}

function openPopupForUpdate(idKey) {
	var url = "${contextPath}/svc/v1/company/" + idKey + "?q=" + getAuthToken()
	nesAjax(url,
			null,
			function(data) {
				$("#layertitle").html("업체정보수정")
				openPopup('defaultPopupLayout', 600, 600)
				$("#company_idKey").val(data.idKey)
				$("#company_idString").val(data.idString)
				$("#company_idString").prop('readonly', true)
				$("#company_name").val(data.name)
				$("#company_address").val(data.address)
				$("#company_telephone").val(data.telephone)
				$("#company_email").val(data.email)
				$("#company_officer").val(data.officer)
				$("#company_officerTel").val(data.officerTel)
				$("#company_memo").val(data.memo)
				$("#company_orderSeq").val(data.orderSeq)
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
	getNewId()
}

function getNewId() {
	nesAjax("${contextPath}/svc/v1/company/newId?q=" + getAuthToken(),
			null,
			function(data) {
				$("#company_idString").val(data.idString)
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST")
}

function resetEdit() {
	$("#company_idKey").val('')
	$("#company_idString").val('')
	$("#company_idString").prop('readonly', false)
	$("#company_name").val('')
	$("#company_address").val('')
	$("#company_telephone").val('')
	$("#company_email").val('')
	$("#company_officer").val('')
	$("#company_officerTel").val('')
	$("#company_memo").val('')
	$("#company_orderSeq").val('')
}

function cancelEdit() {
	resetEdit()
	closePopup('defaultPopupLayout')
}

function saveEdit() {
	if ($("#company_idString").val().trim() == "") {
		alert("아이디를 입력해 주세요.")
		$("#company_idString").focus()
		return
	}
	if ($("#company_name").val().trim() == "") {
		alert("이름을 입력해 주세요.")
		$("#company_name").focus()
		return
	}
	if ($("#company_address").val().trim() == "") {
		alert("주소를 입력해 주세요.")
		$("#company_address").focus()
		return;
	}
	if ($("#company_telephone").val().trim() == "") {
		alert("전화번호를 입력해 주세요.")
		$("#company_telephone").focus()
		return;
	}
	if ($("#company_officer").val().trim() == "") {
		alert("주 담당자를 입력해 주세요.")
		$("#company_officer").focus()
		return;
	}
	
	var company = {};
	company.authToken = getAuthToken()
	company.idKey      = $("#company_idKey").val().trim()
	company.idString   = $("#company_idString").val().trim()
	company.name       = $("#company_name").val().trim()
	company.address    = $("#company_address").val().trim()
	company.telephone  = $("#company_telephone").val().trim()
	company.email      = $("#company_email").val().trim()
	company.officer    = $("#company_officer").val().trim()
	company.officerTel = $("#company_officerTel").val().trim()
	company.memo   = $("#company_memo").val().trim()
	company.orderSeq = $("#company_orderSeq").val().trim()
	
	var url = ""
	var method = ""
	if (company.idKey == "") {
		url = "${contextPath}/svc/v1/company"
		method = "POST"
	} else {
		url = "${contextPath}/svc/v1/company/" + company.idKey
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
					<th>업체ID</th>
					<td><input type="text" name="company_idString" id="company_idString" style="width:90%"></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="company_name" id="company_name" style="width:90%"></td>
				</tr>
				<tr>
					<th>주소</th>
					<td><input name="company_address" id="company_address" style="width:90%"></td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input name="company_telephone" id="company_telephone" style="width:90%"></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input name="company_email" id="company_email" style="width:90%"></td>
				</tr>
				<tr>
					<th>주담당자</th>
					<td><input name="company_officer" id="company_officer" style="width:90%"></td>
				</tr>
				<tr>
					<th>주담당자전화번호</th>
					<td><input name="company_officerTel" id="company_officerTel" style="width:90%"></td>
				</tr>
				<tr>
					<th>메모</th>
					<td><textarea name="company_memo" id="company_memo" style="width:90%" rows="4"></textarea>
				</tr>
				<tr>
					<th>순서</th>
					<td><input name="company_orderSeq" id="company_orderSeq" style="width:90%"></td>
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
