<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<br>
<h1 class="tit">테이블 기반 소스파일 생성</h1>
데이터베이스명<input type="text" class="form-control" id="databasename" size="80" value=""/><br>
테이블명<input type="text" class="form-control" id="tablename" size="25" value=""/><br>
PREFIX<input type="text" class="form-control" id="prefix" size="25" value=""/><br>
패키지명<input type="text" class="form-control" id="packagename" size="25" value=""/><br>
<input type="button" value="생성" onclick="generate()"><br>
model<br>
<textarea id="modelTextarea" cols="120" rows="10">
</textarea><button onclick="saveTextAsFile('modelTextarea')">download</button><br>
xml<br>
<textarea id="xmlTextarea" cols="120" rows="10">
</textarea><button onclick="saveTextAsFile('xmlTextarea')">download</button><br>
dao<br>
<textarea id="daoTextarea" cols="120" rows="10">
</textarea><button onclick="saveTextAsFile('daoTextarea')">download</button><br>
service<br>
<textarea id="serviceTextarea" cols="120" rows="10">
</textarea><button onclick="saveTextAsFile('serviceTextarea')">download</button><br>
jsp<br>
<textarea id="jspTextarea" cols="120" rows="10">
</textarea><button onclick="saveTextAsFile('jspTextarea')">download</button><br>
<!-- Script -->
<script>
$(document).ready(function(){
});

function saveTextAsFile(textareaId, fileNameToSaveAs) {
	var textToWrite = $("#" + textareaId).val()
	var textFileAsBlob = new Blob([textToWrite], {type:'text/plain'}); 
	var downloadLink = document.createElement("a");
	downloadLink.download = fileNameToSaveAs;
	downloadLink.innerHTML = "Download File";
	if (window.webkitURL != null) {
		downloadLink.href = window.webkitURL.createObjectURL(textFileAsBlob);
	} else {
		downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
		downloadLink.onclick = destroyClickedElement;
		downloadLink.style.display = "none";
		document.body.appendChild(downloadLink);
	}
	downloadLink.click();
}

function generate() {
	var url = "${contextPath}/svc/1/generate/bytable"
	var gen = {}
	gen.databaseName = $("#databasename").val()
	gen.tableName = $("#tablename").val()
	gen.prefix = $("#prefix").val()
	gen.packageName = $("#packagename").val()
	nesAjax(url,
			JSON.stringify(gen),
			function(data) {
				$("#modelTextarea").val(data.model)
				$("#xmlTextarea").val(data.xml)
				$("#daoTextarea").val(data.dao)
				$("#serviceTextarea").val(data.service)
				$("#jspTextarea").val(data.jsp)
			},
			function(data) {
				alert("생성에 실패했습니다.");
			},
			"POST");
}
</script>
</body>
</html>