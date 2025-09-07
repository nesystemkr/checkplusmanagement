function nesAjax($url, $data, $func, $error, $method, $timeout) {
	$.ajax({
		type: $method,
		url: $url ,
		timeout: (($timeout != undefined) ? $timeout : (600 * 1000)),
		data: $data ,
		contentType:"application/json; charset=utf-8",
		dataType:"json",
		xhrFields: {
			withCredentials: true
		},
		success: function(data) {
			$func(data);
		},
		error : function(data) {
			try {
				$error(data);
			} catch(e) {
			}
		}
	});
}

function getDateFromString(str) {
	return Date.parse(str);
}

function dateTimeToString(lTime) {
	if (lTime == undefined) {
		return ""
	}
	var d = new Date(lTime);
	var mm = d.getMonth() + 1; // getMonth() is zero-based
	var dd = d.getDate();

	return [d.getFullYear(),
			(mm>9 ? '' : '0') + mm,
			(dd>9 ? '' : '0') + dd].join('.');
}

function dateTimeToFullString(lTime) {
	if (lTime == undefined) {
		return ""
	}
	var d = new Date(lTime);
	var mm = d.getMonth() + 1; // getMonth() is zero-based
	var dd = d.getDate();
	var hh = d.getHours();
	var min = d.getMinutes();
	var ss = d.getSeconds();
	return [d.getFullYear(),
			(mm>9 ? '' : '0') + mm,
			(dd>9 ? '' : '0') + dd].join('.') + " " +
			[(hh>9 ? '' : '0') + hh,
			 (min>9 ? '' : '0') + min,
			 (ss>9 ? '' : '0') + ss].join(':');
}

function openPopup(layerId, popwidth, popheight) {
	var $obj = $('#'+layerId+'');
	$obj.css({'display':'block', 'width':$(window).width()+'px','height':$(window).height()+'px'});
	$obj.find('.layerpop').css({'display':'block', 'width':popwidth+'px', 'height':popheight+'px', 'margin-top':-parseInt(popheight/2)+'px','margin-left':-parseInt(popwidth/2)+'px'});
	var body_HT = $obj.find('.pop_head').outerHeight() + $obj.find('.pop_foot').outerHeight();
	$obj.find('.layerpop .pop_body').css({'height':popheight - body_HT-20 +'px'});
}

function closePopup(ayerId) {
	var $obj = $('#'+ayerId+'');
	$obj.css('display','none');
	$obj.find('.layerpop').css('display','none');
}

function saveLoginInfo(authToken, userIdKey, userId, userType, userName, menuInfo) {
	setAuthToken(authToken)
	setUserIdKey(userIdKey)
	setUserId(userId)
	setUserType(userType)
	setUserName(userName)
	setMenuInfo(menuInfo)
}

function eraseLoginInfo() {
	eraseCookie(gPrefPrefix + "AuthToken")
	getLocalStorage().removeItem(gPrefPrefix + "UserIdKey")
	getLocalStorage().removeItem(gPrefPrefix + "UserId")
	getLocalStorage().removeItem(gPrefPrefix + "UserType")
	getLocalStorage().removeItem(gPrefPrefix + "UserName")
	getLocalStorage().removeItem(gPrefPrefix + "MenuInfo")
}

function setAuthToken(authToken) {
	createCookie(gPrefPrefix + "AuthToken", authToken)
}

function setUserIdKey(userIdKey) {
	getLocalStorage().setItem(gPrefPrefix + "UserIdKey", userIdKey)
}

function setUserId(userId) {
	getLocalStorage().setItem(gPrefPrefix + "UserId", userId)
}

function setUserType(userType) {
	getLocalStorage().setItem(gPrefPrefix + "UserType", userType)
}

function setUserName(userName) {
	getLocalStorage().setItem(gPrefPrefix + "UserName", userName)
}

function setMenuInfo(menuInfo) {
	getLocalStorage().setItem(gPrefPrefix + "MenuInfo", menuInfo)
}

function getAuthToken() {
	return readCookie(gPrefPrefix + "AuthToken")
}

function getUserIdKey() {
	return getLocalStorage().getItem(gPrefPrefix + "UserIdKey")
}

function getUserId() {
	return getLocalStorage().getItem(gPrefPrefix + "UserId")
}

function getUserType() {
	return getLocalStorage().getItem(gPrefPrefix + "UserType")
}

function getUserName() {
	return getLocalStorage().getItem(gPrefPrefix + "UserName")
}

function getMenuInfo() {
	return getLocalStorage().getItem(gPrefPrefix + "MenuInfo")
}

function getLocalStorage() {
	return window.localStorage
}

function isLogined() {
	if (getAuthToken() == null || getAuthToken() == "") {
		return false
	}
	return true
}

function createCookie(name, value, days) {
	var expires;

	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		expires = "; expires=" + date.toGMTString();
	} else {
		expires = "";
	}
	document.cookie = encodeURIComponent(name) + "=" + encodeURIComponent(value) + expires + "; path=/";
}

function readCookie(name) {
	var nameEQ = encodeURIComponent(name) + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) === ' ')
			c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) === 0)
			return decodeURIComponent(c.substring(nameEQ.length, c.length));
	}
	return null;
}

function eraseCookie(name) {
	createCookie(name, "", -1);
}

function disableSelect(jqInput) {
	jqInput.attr("disabled", true);
	jqInput.addClass("lightgrayBackground");
}

function readonlyInput(jqInput) {
	jqInput.attr("readonly", true);
	jqInput.addClass("lightgrayBackground");
}

function enableInput(jqInput) {
	jqInput.attr("readonly", false);
	jqInput.attr("disabled", false);
	jqInput.removeClass("lightgrayBackground");
}

function numberWithCommas(x) {
	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function convert8String2DateFormat(str) {
	return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8)
}