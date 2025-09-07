<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("contextPath", request.getContextPath());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>사진 첨부하기 :: SmartEditor2</title>
<link rel="stylesheet" href="${contextPath}/asset/css/fileuploader.css" />
<style type="text/css">
/* NHN Web Standard 1Team JJS 120106 */ 
/* Common */
body,p,h1,h2,h3,h4,h5,h6,ul,ol,li,dl,dt,dd,table,th,td,form,fieldset,legend,input,textarea,button,select{margin:0;padding:0}
body,input,textarea,select,button,table{font-family:'돋움',Dotum,Helvetica,sans-serif;font-size:12px}
img,fieldset{border:0}
ul,ol{list-style:none}
em,address{font-style:normal}
a{text-decoration:none}
a:hover,a:active,a:focus{text-decoration:underline}

/* Contents */
.blind{visibility:hidden;position:absolute;line-height:0}
#pop_wrap{width:100%}
#pop_header{height:26px;padding:14px 0 0 20px;border-bottom:1px solid #ededeb;background:#f4f4f3}
.pop_container{padding:11px 20px 0;height: 151px;}
#pop_footer{margin:21px 20px 0;padding:10px 0 16px;border-top:1px solid #e5e5e5;text-align:center}
h1{color:#333;font-size:14px;letter-spacing:-1px}
.btn_area{word-spacing:2px}
.pop_container .drag_area{overflow:hidden;overflow-y:auto;position:relative;width:341px;height:129px;margin-top:4px;border:1px solid #eceff2}
.pop_container .drag_area .bg{display:block;position:absolute;top:0;left:0;width:341px;height:129px;background:#fdfdfd url(./img/bg_drag_image.png) 0 0 no-repeat}
.pop_container .nobg{background:none}
.pop_container .bar{color:#e0e0e0}
.pop_container .lst_type li{overflow:hidden;position:relative;padding:7px 0 6px 8px;border-bottom:1px solid #f4f4f4;vertical-align:top}
.pop_container :root .lst_type li{padding:6px 0 5px 8px}
.pop_container .lst_type li span{float:left;color:#222}
.pop_container .lst_type li em{float:right;margin-top:1px;padding-right:22px;color:#a1a1a1;font-size:11px}
.pop_container .lst_type li a{position:absolute;top:6px;right:5px}
.pop_container .dsc{margin-top:6px;color:#666;line-height:18px}
.pop_container .dsc_v1{margin-top:12px}
.pop_container .dsc em{color:#13b72a}
.pop_container2{padding:46px 60px 20px}
.pop_container2 .dsc{margin-top:6px;color:#666;line-height:18px}
.pop_container2 .dsc strong{color:#13b72a}
.upload{margin:0 4px 0 0;_margin:0;padding:6px 0 4px 6px;border:solid 1px #d5d5d5;color:#a1a1a1;font-size:12px;border-right-color:#efefef;border-bottom-color:#efefef;length:300px;}
:root  .upload{padding:6px 0 2px 6px;}
</style>

<!-- 외부 자바스크립트 -->
<!-- script src='/js/jquery/jquery-1.11.0.min.js'></script -->
<script src="${contextPath}/asset/js/jquery.js"></script>
<script src="${contextPath}/asset/js/common/constant.js"></script>
<script src="${contextPath}/asset/js/common/cookie.js"></script>
<script src="${contextPath}/asset/js/common/userinfo.js"></script>
<script src="${contextPath}/asset/js/common/element.js"></script>
<script src="${contextPath}/asset/js/common/fileupload.js"></script>
<script>
var fileupload
$(document).ready(function() {
	fileupload = initializeFileUpload({
		id: "fileuploadDiv",
		isImage: true,
		contextPath: '${contextPath}',
	})

	$("#btn_cancel").click(function() {
		close();
	})

	$("#btn_confirm").click(function() {
		fileupload.uploadFile(
				function(data) {
					var oFileInfos = []
					for (var ii=0; ii<data.attachments.length; ii++) {
						oFileInfos[oFileInfos.length] = {
							bNewLine: true,
							sFileName: data.attachments[ii].fileName,
							sFileURL: "${contextPath}/svc/v1/attachment/" + data.attachments[ii].idKey,
						}
					}
					setPhotoToEditor(oFileInfos)
					fileupload.resetFile()
					close();
				},
				function(data) {
					alert("업로드에 실패했습니다. " + JSON.stringify(data))
				})
		})
});

function setPhotoToEditor(oFileInfo){
	if (!!opener && !!opener.nhn && !!opener.nhn.husky && !!opener.nhn.husky.PopUpManager) {
		opener.nhn.husky.PopUpManager.setCallback(window, 'SET_PHOTO', [oFileInfo]);
		//본문에 바로 tag를 넣는 방법 (oFileInfo는 String으로 <img src=....> )
		//opener.nhn.husky.PopUpManager.setCallback(window, 'PASTE_HTML', [oFileInfo]);
	}
}

</script>

</head>
<body>
<div id="pop_wrap">
	<!-- header -->
	<div id="pop_header">
		<h1>사진 첨부하기</h1>
	</div>
	<!-- //header -->
	<!-- container -->
	<div id="fileuploadDiv" class="pop_container"></div>
	<!-- //container -->
	<!-- footer -->
	<div id="pop_footer">
		<div class="btn_area">
			<a href="#"><img src="./img/btn_confirm.png" width="49" height="28" alt="확인" id="btn_confirm"></a>
			<a href="#"><img src="./img/btn_cancel.png" width="48" height="28" alt="취소" id="btn_cancel"></a>
		</div>
	</div>
	<!-- //footer -->
</div>

</body>
</html>