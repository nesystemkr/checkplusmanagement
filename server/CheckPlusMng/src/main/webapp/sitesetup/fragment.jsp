<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/include.jsp"/>
<jsp:include page="/common/header.jsp"/>
<script>
startFuncs[startFuncs.length] = function() {
	FragmentBuilder.newGridBuilder({
		contextPath: "${contextPath}",
		fragmentId: "GR999001",
		parentId: "parentDiv",
		buttons: [
			{"column":"action","iconCls":"fas fa-receipt","callback":"openDetailPopup"},
			{"column":"action","preSet":-1}
		],
	})
}

function openDetailPopup(rowId, rowData, gridId) {
	if (rowData.fragmentType == 1) {
		FragmentBuilder.newPopupBuilder({
			contextPath: "${contextPath}",
			fragmentId: "PO999002",
			paramData: rowData,
			openAfterBuild: true,
		})
	} else if (rowData.fragmentType == 2) {
		FragmentBuilder.newPopupBuilder({
			contextPath: "${contextPath}",
			fragmentId: "PO999003",
			paramData: rowData,
			openAfterBuild: true,
		})
	} else {
		alert("지원하지 않는 FramentType입니다.")
	}
}
</script>
<div id="parentDiv"></div>

<jsp:include page="/common/footer.jsp"/>
