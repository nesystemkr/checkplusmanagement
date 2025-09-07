<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
var __currentPage = 1
var __refreshFunc
function pagingInfo(url, refreshFunc) {
	nesAjax(url,
			null,
			function(data) {
				pagingInfo_success(data, refreshFunc)
			},
			function(data) {
			},
			"GET");
}

function pagingInfo_success(data, refreshFunc) {
	if (!data.totalCount) {
		data.totalCount = 0;
	}
	var pageCount = Math.ceil(data.totalCount / 10);
	if (pageCount == 0) {
		$("#page_body").hide(); 
		return;
	}
	var startPage = (Math.floor((__currentPage - 1) / 10) * 10) + 1;
	var endPage = startPage + 9;
	if (endPage > pageCount) {
		endPage = pageCount;
	}
	if (startPage == 1) {
		$("#page_first").hide();
		$("#page_start").hide();
	} else {
		$("#page_first").show();
		$("#page_start").show();
		$("#page_start").attr("href", "javascript:goPage(" + (startPage - 1) + ");");
	}
	for (var ii=0; ii<10; ii++) {
		var tempPage = startPage + ii
		if (tempPage == __currentPage) {
			$("#page_num" + ii).addClass("on");
		} else {
			$("#page_num" + ii).removeClass("on");
		}
		if (tempPage <= endPage) {
			$("#page_num" + ii).show();
			$("#page_num" + ii).html(tempPage);
			$("#page_num" + ii).attr("href", "javascript:goPage(" + tempPage + ");");
		} else {
			$("#page_num" + ii).hide();
		}
	}
	if (endPage >= pageCount) {
		$("#page_end").hide();
		$("#page_last").hide();
	} else {
		$("#page_end").show();
		$("#page_last").show();
		$("#page_end").attr("href", "javascript:goPage(" + (endPage + 1) + ");");
		$("#page_last").attr("href", "javascript:goPage(" + pageCount + ");");
	}
	$("#page_body").show();
	__refreshFunc = refreshFunc
}

function goPage(page) {
	__currentPage = page
	if (__refreshFunc) {
		__refreshFunc();
	} else {
		if (refreshList) {
			refreshList()
		}
	}
}
</script>
<div id="page_body" class="paging" style="display:none;">
	<a id="page_first" class="etc" href='javascript:goPage(1);'>&lt;&lt;</a>
	<a id="page_start" class="etc">&lt;</a>
	<a id="page_num0"></a>
	<a id="page_num1"></a>
	<a id="page_num2"></a>
	<a id="page_num3"></a>
	<a id="page_num4"></a>
	<a id="page_num5"></a>
	<a id="page_num6"></a>
	<a id="page_num7"></a>
	<a id="page_num8"></a>
	<a id="page_num9"></a>
	<a id="page_end" class="etc">&gt;</a>
	<a id="page_last" class="etc">&gt;&gt;</a>
</div>
