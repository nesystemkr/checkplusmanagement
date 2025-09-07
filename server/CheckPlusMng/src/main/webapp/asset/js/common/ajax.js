function nesAjax($url, $data, $func, $error, $method, $timeout, $beforeSend) {
	$.ajax({
		type: $method,
		url: $url ,
		timeout: $timeout ? $timeout : (600 * 1000),
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
		},
		beforeSend : function(xhr){
			if ($beforeSend) {
				$beforeSend(xhr)
			}
		}
	});
}
