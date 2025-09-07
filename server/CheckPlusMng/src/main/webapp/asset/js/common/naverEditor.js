var sLang = "ko_KR";	// 언어 (ko_KR/ en_US/ ja_JP/ zh_CN/ zh_TW), default = ko_KR

function initializeNaverEditor(options) {
	if (!options) {
		return
	}
	if (!options.id) {
		return
	}
	if (!options.width) {
		options.width = 800; 
	}
	if (!options.height) {
		options.height = 400
	}
	if (!options.contextPath) {
		options.contextPath = ''
	}
	if (!options.lang) {
		options.lang = 'ko_KR'
	}
	
	var ret = {};
	var oEditors = [];
	nhn.husky.EZCreator.createInIFrame({
		oAppRef: oEditors,
		elPlaceHolder: options.id,
		sSkinURI: options.contextPath + "/asset/naverEditor/SmartEditor2Skin.html",	
		htParams : {
			bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseVerticalResizer : true,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
			fOnBeforeUnload : function(){
			},
			I18N_LOCALE : options.lang
		}, //boolean
		fOnAppLoad : function(){
			if (options.callback) {
				options.callback()
			}
		},
		fCreator: "createSEditor2"
	});
	
	ret.updateContentsField = function() {
		oEditors.getById[options.id].exec("UPDATE_CONTENTS_FIELD", []);
	}
	
	ret.pasteHtml = function(html) {
		if (oEditors.getById == undefined) {
			setTimeout(function() {
				ret.pasteHtml(html)
			}, 250)
		} else {
			oEditors.getById[options.id].exec("PASTE_HTML", [html]);
		}
	}
	
	ret.resetHtml = function() {
		if (oEditors.getById == undefined) {
			setTimeout(function() {
				ret.resetHtml()
			}, 250)
		} else {
			oEditors.getById[options.id].exec("SET_IR", [""]);
		}
	}
	
	ret.enable = function() {
		if (oEditors.getById == undefined) {
			setTimeout(function() {
				ret.enable()
			}, 250)
		} else {
			oEditors.getById[options.id].exec("ENABLE_WYSIWYG");
			oEditors.getById[options.id].exec("ENABLE_ALL_UI");
		}
	}
	
	ret.disable = function() {
		if (oEditors.getById == undefined) {
			setTimeout(function() {
				ret.disable()
			}, 250)
		} else {
			oEditors.getById[options.id].exec("DISABLE_WYSIWYG");
			oEditors.getById[options.id].exec("DISABLE_ALL_UI");
		}
	}
	
	return ret
}
