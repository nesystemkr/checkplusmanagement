<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<script>
function initServer() {
	if (false == confirm("초기화하시겠습니까?")) {
		return
	}
	var url = "${contextPath}/svc/v1/init/server"
	nesAjax(url, null,
		function(data) {
			alert("기본설정되었습니다.")
			document.location = "${contextPath}/"
		},
		function(data) {
			alert("기본설정에 실패했습니다.")
		},
		"POST")
}
</script>
<div>
	<h1>서버 기본 초기화 작업</h1>
	<p>서버 기동을 위한 테이블과 기본데이터를 생성합니다. 생성되는 데이터는 아래와 같습니다.</p>
	<div class="btn_box" style="text-align:left;">
		<button class="btn_type normal" onclick="initServer()">초기화</button><br>
	</div>
	<p></p>
	<h2>생성테이블</h2>
	<p>
		CM_User<br>
		CM_Device<br>
		CM_Version<br>
		CM_Code<br>
		CM_Menu<br>
		CM_MenuAuth<br>
		CM_Attachment<br>
		CM_AttachmentGroup<br>
		CM_Board<br>
		CM_BoardContent<br>
		CM_BoardAuth<br>
		CM_BoardContentReply<br>
		CM_L10N<br>
		CM_L10NLocale<br>
		CM_Daemon<br>
		PS_PushMsg<br>
	</p>
	<h2>생성데이터</h2>
	<p>
		최고관리자생성(admin/1234)<br>
		기본메뉴생성<br>
		기본코드생성<br>
	</p>
</div>
</body>
</html>
