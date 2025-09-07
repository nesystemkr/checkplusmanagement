CKEDITOR.plugins.add('hcard', {
	requires: 'widget',
	init: function(editor) {
		editor.widgets.add('hcard', {
			allowedContent: 'div(!h-card)',
			requiredContent: 'div(h-card)',
			pathName: 'hcard',
			upcast: function(el) {
				return el.name == 'div' && el.hasClass('h-card');
			}
		});
		editor.addFeature(editor.widgets.registered.hcard);
	}
});

function initializeCKEditor(options) {
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
	
	CKEDITOR.replace(options.id, {
		width: options.width,
		height: options.height,
		allowedContent: true,
		extraPlugins: 'hcard',
	});
	var editor = CKEDITOR.instances[options.id]
 	editor.addCommand("insertImageUrl", {
		exec: function(editor, datas) {
			var html = '<div class="h-card"><img src="' + datas[0] + '"></div>'
			html += '<p></p>'
			editor.insertHtml(html)
		}
	})
	editor.setData('<p><div class="h-card"></div></p>', {
		callback: function() {
			editor.setData('')
		}
	})
	editor.insertImageUrl = function(imageUrl) {
		var range = editor.createRange()
		range.moveToElementEditEnd(range.root)
		editor.getSelection().selectRanges([range]);
		editor.execCommand('insertImageUrl', [imageUrl])
	}
	return editor
}
