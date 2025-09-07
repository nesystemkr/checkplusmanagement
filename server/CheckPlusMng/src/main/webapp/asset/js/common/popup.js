function openPopup(layerId, popwidth, popheight) {
	var $obj = $('#'+layerId+'');
	$obj.css({'display':'block', 'width':$(window).width()+'px','height':$(window).height()+'px'});
	$obj.find('.layerpop').css({'display':'block', 'width':popwidth+'px', 'height':popheight+'px', 'margin-top':-parseInt(popheight/2)+'px','margin-left':-parseInt(popwidth/2)+'px'});
	var body_HT = $obj.find('.pop_head').outerHeight() + $obj.find('.pop_foot').outerHeight();
	$obj.find('.layerpop .pop_body').css({'height':popheight - body_HT-20 +'px'});
}

function closePopup(ayerId) {
	var $obj = $('#'+ayerId+'');
	$obj.css('display','none');
	$obj.find('.layerpop').css('display','none');
}
