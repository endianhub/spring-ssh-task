//zTree start
var setting = {
	open : true,// 只展开第一个菜单
	url : null,// 编辑（添加、修改）
	submit : true,// 表单提交状态(默认可正常提交)
	async : {
		enable : true,
		url : basePath + '/admin/resource/homeTree',
		autoParam : [ "id" ],
		otherParam : {
			"type" : "checkbox"
		},
		dataFilter : filter
	},
	view : {
		dblClickExpand : true
	},
	check : {
		enable : false,
		chkStyle : "checkbox"
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : onClick,
		onAsyncSuccess : zTreeOnAsyncSuccess
	},

	// 自定义参数
	post : function(data) {
		$.post(data.url, data.data, function(result) {
			successCallback(result);
		}, data.returnType);
	}

}

function filter(treeId, parentNode, childNodes) {
	if (!childNodes)
		return null;
	for (var i = 0, l = childNodes.length; i < l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}

function onClick(e, treeId, treeNode) {
	if (treeNode.attributes != "") {
		$("#iframe").attr("src", basePath + treeNode.attributes);
	}
}

function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	if (setting.open) {
		// 获得树形图对象
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		// 获取根节点个数,getNodes获取的是根节点的集合
		var nodeList = zTree.getNodes();
		// 展开第一个根节点
		zTree.expandNode(nodeList[0], true);
		// 当再次点击节点时条件不符合,直接跳出方法
		setting.open = false;
	}
}
// zTree end

function editGrid(grid, dialog, formId, keyId, url) {
	setting.url = url;
	var row = $(grid).datagrid('getSelected');
	if (row && keyId != "") {
		$(dialog).dialog('open').dialog('setTitle', '修改');
		$(formId).form('load', row);
		$(keyId).val(row.id);
	} else {
		$(dialog).dialog('open').dialog('setTitle', '添加');
		$(formId).form('clear');
	}
}

// 执行保存和更新
function saveAndUpdate(fromId) {
	if (setting.url == null) {
		return false;
	}
	if (!setting.submit) {
		return false;
	}
	$(fromId).form('submit', {
		url : basePath + setting.url,
		onSubmit : function() {
			return $(this).form('validate');
		},
		success : function(result) {
			if (result != "")
				successCallback(eval('(' + result + ')'));
		}
	});
}

// 根据ID删除数据
function remove(grid, url) {
	// 获取选中数据
	var row = $(grid).datagrid('getSelected');
	if (row) {
		var msg = (typeof(row.name) =="undefined") ? "您确定删除该记录吗?" : "您确定删除 [" + row.name + "] 吗?";
		$.messager.confirm("删除", msg, function(res) {
			if (res) {
				var data = {
					url : basePath + url,
					data : {
						paramId : row.id
					},
					returnType : "json"
				};
				setting.post(data);
			}
		});
	}
}

function validateFile(obj) {
	setting.submit = true;
	// 返回 KB，保留小数点后两位
	var file = obj.value;
	if (!/.(bpmn)$/.test(file)) {
		$.messager.alert("提示", "文件类型必须是bpmn类型");
		setting.submit = false;
		return false;
	} else {
		// 返回Byte(B),保留小数点后两位
		if (((obj.files[0].size).toFixed(2)) >= (5 * 1024 * 1024)) {
			$.messager.alert("提示", "文件不能大于5M");
			setting.submit = false;
			return false;
		}
	}
}
