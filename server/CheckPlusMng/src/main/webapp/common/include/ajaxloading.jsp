<script>
var __useLoadingDiv = true
$(document).ajaxStart(function () {
	if (__useLoadingDiv == true) {
		startLoading()
	}
});
$(document).ajaxStop(function () {
	stopLoading()
});
function startLoading() {
	$('#loadingBgDiv').show()
	$('#loadingDiv').show()
}

function stopLoading() {
	$('#loadingBgDiv').hide()
	$('#loadingDiv').hide()
}
</script>
<div class="loader_bg" id="loadingBgDiv">
<div class="loader" id="loadingDiv" style="display:hidden;"></div>
</div>
