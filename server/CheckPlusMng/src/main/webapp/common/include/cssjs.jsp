<%@ page import = "kr.nesystem.appengine.common.Constant" %>
<link rel="stylesheet" href="${contextPath}/asset/css/jstree.css" />
<link rel="stylesheet" href="${contextPath}/asset/css/jquery-ui.css" />
<link rel="stylesheet" href="${contextPath}/asset/css/jquery-ui-timepicker-addon.css" />
<link rel="stylesheet" href="${contextPath}/asset/css/jqgrid.css" />
<link rel="stylesheet" href="${contextPath}/asset/css/common.css" media="screen" type="text/css" />
<link rel='stylesheet' href='${contextPath}/asset/fullcalendar/main.css' />
<link rel="stylesheet" href="${contextPath}/asset/css/fileuploader.css" />
<script src="${contextPath}/asset/js/jquery.js"></script>
<script src="${contextPath}/asset/js/jquery-ui.js"></script>
<script src="${contextPath}/asset/js/jquery.ui.monthpicker.js"></script>
<script src="${contextPath}/asset/js/jquery-ui-timepicker-addon.js"></script>
<script defer src="${contextPath}/asset/fontawesome/js/all.js"></script>
<script src="${contextPath}/asset/js/jstree.js"></script>
<script src="${contextPath}/asset/js/jqgrid.js"></script>
<script src="${contextPath}/asset/js/template.js"></script>
<script src='${contextPath}/asset/fullcalendar/main.js'></script>
<script src="${contextPath}/asset/js/common/constant.js"></script>
<script src="${contextPath}/asset/js/common/ajax.js"></script>
<script src="${contextPath}/asset/js/common/date.js"></script>
<script src="${contextPath}/asset/js/common/cmcode.js"></script>
<script src="${contextPath}/asset/js/common/cookie.js"></script>
<script src="${contextPath}/asset/js/common/userinfo.js"></script>
<script src="${contextPath}/asset/js/common/popup.js"></script>
<script src="${contextPath}/asset/js/common/format.js"></script>
<script src="${contextPath}/asset/js/common/element.js"></script>
<script src="${contextPath}/asset/js/common/grid.js"></script>
<script src="${contextPath}/asset/js/common/datepicker.js"></script>
<script src="${contextPath}/asset/js/common/tree.js"></script>
<script src="${contextPath}/asset/js/common/fileupload.js"></script>
<script src="${contextPath}/asset/js/common/screenbuilder.js"></script>
<%if (Constant.MODE_OFFLINE == true) { %>
<script type="text/javascript" src="${contextPath}/asset/charts/loader.js"></script>
<%} else {%>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<%}%>
