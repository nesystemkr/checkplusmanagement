function parseDateFromString(str) {
	return Date.parse(str);
}

function getDateFromString(str) {
	var year = 0
	var month = 0
	var day = 0
	var hour = 0
	var minute = 0
	var second = 0
	if (str.length == 6) { //yyMMdd
		year = parseInt(str.substring(0, 2))
		if (year < 70) {
			year = 2000 + year
		} else {
			year = 1900 + year
		}
		month = parseInt(str.substring(2, 4))
		day = parseInt(str.substring(4, 6))
	} else if (str.length == 8) { //yyyyMMdd
		year =  parseInt(str.substring(0, 4))
		month =  parseInt(str.substring(4, 6))
		day =  parseInt(str.substring(6, 8))
	} else if (str.length == 10) { //yyyy.MM.dd
		year =  parseInt(str.substring(0, 4))
		month =  parseInt(str.substring(5, 7))
		day =  parseInt(str.substring(8, 10))
	} else if (str.length == 14) { //yyyyMMddHHmmss
		year =  parseInt(str.substring(0, 4))
		month =  parseInt(str.substring(4, 6))
		day =  parseInt(str.substring(6, 8))
		hour =  parseInt(str.substring(8, 10))
		minute =  parseInt(str.substring(10, 12))
		second =  parseInt(str.substring(12, 14))
	} else if (str.length == 19) { //yyyy-MM-dd HH:mm:ss
		year =  parseInt(str.substring(0, 4))
		month =  parseInt(str.substring(5, 7))
		day =  parseInt(str.substring(8, 10))
		hour =  parseInt(str.substring(11, 13))
		minute =  parseInt(str.substring(14, 16))
		second =  parseInt(str.substring(17, 19))
	} else {
		return null
	}
	var d = new Date();
	d.setFullYear(year)
	d.setMonth(month - 1)
	d.setDate(day)
	d.setHours(hour)
	d.setMinutes(minute)
	d.setSeconds(second)
	d.setMilliseconds(0)
	return d;
}

function getDateFromMonthString(str, isLast) {
	var year = 0
	var month = 0
	var day = 1
	if (str.length == 4) { //yymm
		year = parseInt(str.substring(0, 2))
		if (year < 70) {
			year = 2000 + year
		} else {
			year = 1900 + year
		}
		month = parseInt(str.substring(2, 4))
	} else if (str.length == 5) { //yy.mm
		year = parseInt(str.substring(0, 2))
		if (year < 70) {
			year = 2000 + year
		} else {
			year = 1900 + year
		}
		month = parseInt(str.substring(3, 5))
	} else if (str.length == 6) { //yyyymm
		year =  parseInt(str.substring(0, 4))
		month =  parseInt(str.substring(4, 6))
	} else if (str.length == 7) { //yyyy.mm
		year =  parseInt(str.substring(0, 4))
		month =  parseInt(str.substring(5, 7))
	}
	var d = new Date();
	d.setFullYear(year)
	d.setMonth(month - 1)
	d.setDate(day)
	d.setHours(0)
	d.setMinutes(0)
	d.setSeconds(0)
	d.setMilliseconds(0)
	if (isLast == true) {
		d.setMonth(d.getMonth() + 1)
		d.setDate(d.getDate() - 1)
	}
	return d;
}

function dateTimeToStr8(lTime) {
	if (lTime == undefined) {
		return ""
	}
	if (lTime == "") {
		return ""
	}
	if (typeof(lTime) == 'string') {
		return lTime
	}
	var d = new Date(lTime);
	var mm = d.getMonth() + 1; // getMonth() is zero-based
	var dd = d.getDate();

	return [d.getFullYear(),
			(mm>9 ? '' : '0') + mm,
			(dd>9 ? '' : '0') + dd].join('');
}

function dateTimeToString(lTime) {
	if (lTime == undefined) {
		return ""
	}
	if (lTime == "") {
		return ""
	}
	if (typeof(lTime) == 'string') {
		return lTime
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
	if (lTime == "") {
		return ""
	}
	if (typeof(lTime) == 'string') {
		return lTime
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

function dateTimeToMinuteString(lTime) {
	if (lTime == undefined) {
		return ""
	}
	if (lTime == "") {
		return ""
	}
	if (typeof(lTime) == 'string') {
		return lTime
	}
	var d = new Date(lTime);
	var mm = d.getMonth() + 1; // getMonth() is zero-based
	var dd = d.getDate();
	var hh = d.getHours();
	var min = d.getMinutes();
	return [d.getFullYear(),
			(mm>9 ? '' : '0') + mm,
			(dd>9 ? '' : '0') + dd].join('.') + " " +
			[(hh>9 ? '' : '0') + hh,
			 (min>9 ? '' : '0') + min].join(':');
}

function addDays(date, days) {
	var result = new Date(date);
	result.setDate(result.getDate() + days);
	return result;
}

function D2S(lTime) {
	return dateTimeToString(lTime)
}

function D28(lTime) {
	return dateTimeToStr8(lTime)
}

function S2D(str) {
	return getDateFromString(str)
}

function dateTimeFullForFileName(lTime) {
	if (lTime == undefined) {
		return "0000_00_00_00_00_00"
	}
	var d = new Date(lTime);
	var mm = d.getMonth() + 1; // getMonth() is zero-based
	var dd = d.getDate();
	var hh = d.getHours();
	var min = d.getMinutes();
	var ss = d.getSeconds();
	return [d.getFullYear(),
			(mm>9 ? '' : '0') + mm,
			(dd>9 ? '' : '0') + dd].join('_') + "_" +
			[(hh>9 ? '' : '0') + hh,
			 (min>9 ? '' : '0') + min,
			 (ss>9 ? '' : '0') + ss].join('_');
}

function dateTimeForFileName(lTime) {
	if (lTime == undefined) {
		return ""
	}
	var d = new Date(lTime);
	var mm = d.getMonth() + 1; // getMonth() is zero-based
	var dd = d.getDate();

	return [d.getFullYear(),
			(mm>9 ? '' : '0') + mm,
			(dd>9 ? '' : '0') + dd].join('_');
}

