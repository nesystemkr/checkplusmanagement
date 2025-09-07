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

function createEle(eleName, className) {
	var ele = $("<" + eleName + "></" + eleName + ">")
	if (className) {
		ele.addClass(className)
	}
	return ele
}

function addEle(parent, eleName, className) {
	var ele = createEle(eleName, className)
	if (parent) {
		parent.append(ele)
	}
	return ele
}

function createDiv(className) {
	return createEle("div", className)
}

function addDiv(parent, className) {
	var div = createDiv(className)
	if (parent) {
		parent.append(div)
	}
	return div
}

function addTable(parent, className) {
	var table = createTable(className);
	if (parent) {
		parent.append(table)
	}
	return table
}

function createTable(className) {
	var ele = createEle("table")
	if (className) {
		ele.addClass(className)
	}
	ele.attr("cellSpacing", "0")
	ele.attr("cellPadding", "0")
	ele.attr("border", "0")
	return ele
}

function addTr(table, className) {
	var ele = createEle("tr")
	if (className) {
		ele.addClass(className)
	}
	table.append(ele)
	return ele
}

function addTd(tr, tdWidth, className) {
	var ele = createEle("td")
	if (tdWidth) {
		ele.css("width", tdWidth + "px")
	}
	if (className) {
		ele.addClass(className)
	}
	tr.append(ele)
	return ele
}

function addInputText(parent, name, value, className) {
	var ele = createEle("input")
	ele.attr("type", "text")
	ele.attr("name", name)
	ele.attr("value", value)
	if (className) {
		ele.addClass(className)
	}
	if (parent) {
		parent.append(ele)
	}
	return ele
}

function addInputRadio(parent, name, value, className) {
	var ele = createEle("input")
	ele.attr("type", "radio")
	ele.attr("name", name)
	ele.attr("value", value)
	if (className) {
		ele.addClass(className)
	}
	if (parent) {
		parent.append(ele)
	}
	return ele
}

function addInputFile(parent, className) {
	var ele = createEle("input")
	ele.attr("type", "file")
	if (className) {
		ele.addClass(className)
	}
	if (parent) {
		parent.append(ele)
	}
	return ele
}
