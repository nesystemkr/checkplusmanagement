<script>
var loginExemptionList = [
	"/login.jsp",
	"/init.jsp",
	"/example.jsp",
	"/generate.jsp",
	"/index.jsp",
	"/"
]

function isExemptionUrl(curUrl) {
	for (var ii=0; ii<loginExemptionList.length; ii++) {
		if (loginExemptionList[ii].lastIndexOf("*") != -1 &&
			loginExemptionList[ii].lastIndexOf("*") == (loginExemptionList[ii].length - 1)) {
			var startUrl = loginExemptionList[ii].replace("*", "")
			if (curUrl.indexOf(startUrl) == 0) {
				return true
			}
		} else {
			if (curUrl == "${contextPath}" + loginExemptionList[ii]) {
				return true
			}
		}
	}
	return false;
}
</script>
