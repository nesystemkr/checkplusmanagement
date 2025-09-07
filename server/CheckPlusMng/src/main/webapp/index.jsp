<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<script>
startFuncs[startFuncs.length] = function() {
	document.location = "${contextPath}/sitemng/user.jsp"
}
</script>
</body>
</html>
