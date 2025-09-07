/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	
	// %REMOVE_START%
	// The configuration options below are needed when running CKEditor from source files.
	config.plugins = 'toolbar,basicstyles,wysiwygarea,magicline,widget'
		+ ',image,indent,indentlist,fakeobjects,link,list,maximize'
		+ ',htmlwriter'	
		+ ',dialogui,dialog,about,a11yhelp,blockquote,notification,button'
		+ ',clipboard,panel,floatpanel,menu,contextmenu,enterkey,entities'
		+ ',popup,filetools,filebrowser,floatingspace,listblock,richcombo,format,horizontalrule'
		+ ',pastetext,pastetools,pastefromgdocs,pastefromword,removeformat,showborders,sourcearea'
		+ ',specialchar,menubutton,scayt,stylescombo,tab,table,tabletools,tableselection,undo'
		+ ',lineutils,widgetselection,notificationaggregator,uploadwidget,uploadimage,wsc'
		//+ ',elementspath,resize'
//	config.plugins = 'dialogui,dialog,basicstyles,toolbar,clipboard,enterkey,magicline,undo,lineutils,button,widget';
//	config.plugins = 'dialogui,dialog,basicstyles,toolbar' //,button,widget'
	
//	basicstyles only : 아무것도 표시안됨
//	toolbar only : toolbar만 버튼없이 표시됨
//	toolbar, basicstyles : 버표시되나 disabled됨
		//
		
	config.skin = 'moono-lisa';
	// %REMOVE_END%

	// Define changes to default configuration here.
	// For complete reference see:
	// https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_config.html

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbarGroups = [
//		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
//		{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
//		{ name: 'insert' },
//		{ name: 'forms' },
//		{ name: 'tools' },
//		{ name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
//		{ name: 'others' },
//		'/',
//		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'basicstyles', groups: [ 'basicstyles' ] },
		{ name: 'links', groups: [ 'links' ] },
//		{ name: 'insert' },
//		{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
//		{ name: 'styles' },
//		{ name: 'colors' },
//		{ name: 'about' }
	];

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript,NumberedList,BulletedList,Anchor';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

//	config.removePlugins = 'elementspath';
	config.resize_enabled = false
	
	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';
};
