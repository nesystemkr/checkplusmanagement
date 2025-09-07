<script>
var startFuncs = []
$(document).ready(function() {
	var bRunStart = true
	if (isExemptionUrl(document.location.pathname) == false) {
		if (getAuthToken() == undefined || getAuthToken() == null || getAuthToken() == "") {
			document.location = "${contextPath}/login.jsp"
			bRunStart = false
		}
	}
	if (bRunStart) {
		for (var ii=0; ii<startFuncs.length; ii++) {
			startFuncs[ii]();
		}
	}
});
</script>
