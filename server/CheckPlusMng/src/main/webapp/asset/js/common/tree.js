function initializeTree(options) {
	if (!options.id || !options.idField || !options.parentIdField) {
		return
	}
	
	var plgs = [];
	if (options.dnd) {
		plgs[plgs.length] = "dnd"
	}
	if (options.checkbox) {
		plgs[plgs.length] = "checkbox"
	}
	
	var retTree = $('#' + options.id).jstree({
		"core": {
			"check_callback" : true
		},
		"plugins" : plgs,
		"extraColModel" : options.extraColModel
	});
	
	retTree.setTreeDataList = function(list) {
		$('#' + options.id).jstree(true).delete_node($('#' + options.id).jstree(true).get_node("#").children);
		var nd;
		for (var ii=0; ii<list.length; ii++) {
			nd = {}
			nd.data = list[ii];
			nd.id = list[ii][options.idField];
			if (options.captionFunc) {
				nd.text = options.captionFunc(list[ii]);
			} else {
				nd.text = list[ii][options.captionField];
			}
			nd.state = {opened : true};
			$('#' + options.id).jstree().create_node(
					(list[ii][options.parentIdField]  && list[ii][options.parentIdField] > 0) ? list[ii][options.parentIdField]  : '#',
					nd, "last", function() {});
		}
	}
	
	retTree.getSelected = function() {
		return $('#' + options.id).jstree(true).get_selected()
	}
	
	retTree.getNode = function(nodeId) {
		return $('#' + options.id).jstree(true).get_node(nodeId)
	}
	
	retTree.getJson = function(obj, opts, flat) {
		return $('#' + options.id).jstree(true).get_json(obj, opts, flat)
	}
	
	$('#' + options.id).on("select_node.jstree",
			function(evt, data) {
				if (options.onSelectNode) {
					options.onSelectNode(data.node.data);
				}
			}
	)
	
	retTree.bind("move_node.jstree",
		function (evt, dropdata) {
			if (dropdata.parent == "#") {
				dropdata.node.data[options.parentIdField] = 0
				dropdata.node.data['touch'] = 'U'
			} else {
				dropdata.node.data[options.parentIdField] = parseInt(dropdata.parent)
				dropdata.node.data['touch'] = 'U'
			}
		}
	)
	
	return retTree;
}
