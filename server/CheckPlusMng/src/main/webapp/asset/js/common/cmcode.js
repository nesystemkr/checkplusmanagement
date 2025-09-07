function fillUpSelect(contextPath, selectId, type, selectedValue, callbackFunc) {
	var url = contextPath + "/svc/v1/code/" + type
	fillUpSelectByUrl(url, selectId, 'code', 'codeNameLocale', selectedValue, '', '선택', callbackFunc)
}

function fillUpSelect2(contextPath, selectId, type, topValue, selectedValue, callbackFunc, allowCodes) {
	var url = contextPath + "/svc/v1/code/" + type
	fillUpSelectByUrl(url, selectId, 'code', 'codeNameLocale', selectedValue, '', topValue, callbackFunc, allowCodes)
}

function fillUpSelectByUrl(url, selectId, keyField, valueField, selectedValue, topKey, topValue, callbackFunc, allowCodes) {
	nesAjax(url,
			null,
			function(data) {
				var $selectObj = $("#" + selectId).html("");
				if (topKey != undefined && topValue != undefined) {
					$selectObj.append("<option value='" + topKey + "'>" + topValue + "</option>")
				}
				if (data) {
					if (data.list) {
						_fillUpSelect($selectObj, keyField, valueField, selectedValue, data.list, allowCodes)
					} else {
						_fillUpSelect($selectObj, keyField, valueField, selectedValue, data, allowCodes)
					}
					if (callbackFunc) {
						callbackFunc(data)
					}
				}
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"GET");
}

function _fillUpSelect($selectObj, keyField, valueField, selectedValue, list, allowCodes) {
	var el;
	if (list) {
		for (var ii=0; ii<list.length; ii++) {
			if (allowCodes && !allowCodes.includes(list[ii][keyField])) {
				continue
			}
			el = "<option value='" + list[ii][keyField] + "'";
			if (list[ii][keyField] == selectedValue) {
				el += " selected"
			}
			el += ">" + list[ii][valueField] + "</option>"
			$selectObj.append(el)
		}
	}
}

function initCommonCode(contextPath, types, callbackFunc) {
	var url = contextPath + "/svc/v1/code/types/"
	var paging = {}
	paging.list = types
	nesAjax(url,
			JSON.stringify(paging),
			function(data) {
				if (callbackFunc) {
					callbackFunc(data)
				}
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST");
}

function initCommonCodeForGridSelect(contextPath, types, callbackFunc) {
	var url = contextPath + "/svc/v1/code/types/forselect"
	var paging = {}
	paging.list = types
	nesAjax(url,
			JSON.stringify(paging),
			function(data) {
				if (callbackFunc) {
					callbackFunc(data)
				}
			},
			function(data) {
				alert(JSON.stringify(data))
			},
			"POST");
}
