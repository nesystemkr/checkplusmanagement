function createDatePicker(inputId, onSelect, numOfMon) {
	var $ele
	if (inputId instanceof jQuery){
		$ele = inputId
	} else {
		$ele = $("#" + inputId);
	}
	$ele.datepicker({
		closeText: "닫기",
		prevText: "이전",
		nextText: "이후",
		currentText: "오늘",
		monthNames: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
		dayNames: [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
		dayNamesShort: [ "일", "월", "화", "수", "목", "금", "토" ],
		dayNamesMin: [ "일","월","화","수","목","금","토" ],
		dateFormat: "yy.mm.dd",
		numberOfMonths: numOfMon == undefined ? 1 : numOfMon,
		showMonthAfterYear: true,
		yearSuffix: "년",
		onSelect: onSelect ? onSelect : null,
	})
}

function createMonthPicker(inputId, onSelect) {
	var $ele
	if (inputId instanceof jQuery){
		$ele = inputId
	} else {
		$ele = $("#" + inputId);
	}
	$ele.monthpicker({
		monthNames: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
		dateFormat: "yy.mm",
		yearSuffix: "년",
		onSelect: onSelect ? onSelect : null,
	})
}

function createDateTimePicker(inputId, onSelect, numOfMon) {
	var $ele
	if (inputId instanceof jQuery){
		$ele = inputId
	} else {
		$ele = $("#" + inputId);
	}
	$ele.datetimepicker({
		closeText: "닫기",
		prevText: "이전",
		nextText: "이후",
		currentText: "오늘",
		monthNames: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
		dayNames: [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
		dayNamesShort: [ "일", "월", "화", "수", "목", "금", "토" ],
		dayNamesMin: [ "일","월","화","수","목","금","토" ],
		dateFormat: "yy.mm.dd",
		numberOfMonths: numOfMon == undefined ? 1 : numOfMon,
		showMonthAfterYear: true,
		yearSuffix: "년",
		onSelect: onSelect ? onSelect : null,
		controlType: 'select',
		oneLine: true,
		timeFormat: 'HH:mm tt'
	})
}
