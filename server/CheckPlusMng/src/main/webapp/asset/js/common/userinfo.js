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

function storeData(key, value) {
	if (value == null || value == undefined) {
		getLocalStorage().removeItem(gPrefPrefix + key)	
	} else {
		getLocalStorage().setItem(gPrefPrefix + key, value)
	}
}

function retrieveData(key, defValue) {
	var ret = getLocalStorage().getItem(gPrefPrefix + key)
	if (ret == null || ret == undefined || ret == "") {
		if (defValue) {
			return defValue
		} else {
			return null;
		}
	}
	return ret
}

function getAuthToken() {
	return readCookie(gPrefPrefix + "AuthToken")
}

function getUserIdKey() {
	if (getAuthToken() == null || getAuthToken() == "") {
		return null
	}
	return getLocalStorage().getItem(gPrefPrefix + "UserIdKey")
}

function getUserId() {
	if (getAuthToken() == null || getAuthToken() == "") {
		return null
	}
	return getLocalStorage().getItem(gPrefPrefix + "UserId")
}

function getUserType() {
	if (getAuthToken() == null || getAuthToken() == "") {
		return null
	}
	return getLocalStorage().getItem(gPrefPrefix + "UserType")
}

function getUserName() {
	if (getAuthToken() == null || getAuthToken() == "") {
		return null
	}
	return getLocalStorage().getItem(gPrefPrefix + "UserName")
}

function getMenuInfo() {
	if (getAuthToken() == null || getAuthToken() == "") {
		return null
	}
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
