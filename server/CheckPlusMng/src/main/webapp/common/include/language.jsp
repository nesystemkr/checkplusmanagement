<script>
function changeLanguage(lang) {
	createCookie("Language", lang, 365)
	if (getMenuInfo) {
		if (getMenuInfo()) {
			var url = "${contextPath}/svc/v1/login/menus?lang=" + lang + "&q=" + getAuthToken()
			nesAjax(url,
					null,
					function(data) {
						setMenuInfo(JSON.stringify(data.menus))
						window.location.reload()
					},
					function(data) {
						alert(JSON.stringfy(data))
					},
					"GET")
		} else {
			window.location.reload()
		}
	} else {
		window.location.reload()
	}
}
</script>
