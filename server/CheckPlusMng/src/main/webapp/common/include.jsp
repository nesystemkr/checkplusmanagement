<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "kr.nesystem.appengine.common.util.L10N" %>
<%
response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
response.setHeader("Pragma", "no-cache"); //HTTP 1.0
response.setDateHeader("Expires", 0); //prevents caching at the proxy server
request.setAttribute("contextPath", request.getContextPath());
Cookie[] cookies = request.getCookies();
Cookie cookie;
String lang = null;
if (cookies != null) {
	for (int ii=0; ii<cookies.length; ii++) {
		cookie = cookies[ii];
		if (cookie.getName().equals("Language")) {
			lang = cookie.getValue();
		}
	}
}
if (session.getAttribute("L10N") == null ||
	session.getAttribute("Locale") == null ||
	!(session.getAttribute("Locale").equals(lang))) {
	session.setAttribute("Locale", lang);
	L10N sessionL10N = new L10N(session);
	session.setAttribute("L10N", sessionL10N);
}
%>
<!DOCTYPE HTML>
<html>
<head>
<title>체크플러스 관리</title>
<%@ include file="/common/include/meta.jsp" %>
<%@ include file="/common/include/cssjs.jsp" %>
<%@ include file="/common/include/exemptionlist.jsp" %>
<%@ include file="/common/include/readychecklogin.jsp" %>
<%@ include file="/common/include/language.jsp" %>
</head>
<body>
<%@ include file="/common/include/ajaxloading.jsp" %>