function numberWithCommas(x) {
	if (x == undefined) {
		return "0"
	}
	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function convert8String2DateFormat(str) {
	if (str == undefined) {
		return ""
	}
	return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8)
}

function S82S(str) {
	return convert8String2DateFormat(str)
}
