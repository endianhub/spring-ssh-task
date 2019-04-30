function loadDataGrid(dataGridObj) {
	dataGridObj.height = dataGridObj.height || "auto";
	dataGridObj.type = dataGridObj.type || "POST";
	dataGridObj.datatype = dataGridObj.datatype || "JSON";
	$(dataGridObj.selectorId).datagrid({
		title : dataGridObj.title,
		iconCls : "icon-edit",// 图标
		width : dataGridObj.width, // 高度
		height : dataGridObj.height, // 宽度自动
		loadMsg : "正在加载中，请稍等... ",
		type : dataGridObj.type,
		datatype : dataGridObj.datatype,
		url : dataGridObj.url, // 请求地址
		nowrap : true,
		striped : true,
		border : true,
		// collapsible : false,//是否可折叠的
		fit : true,// 自动大小
		fitColumns : true,// 宽度自适应
		// sortName: "primaryId",
		// sortOrder: "desc",
		remoteSort : false,
		idField : "taskId",
		singleSelect : false,// 是否单选
		// pagination : true,//分页控件
		rownumbers : true,// 行号
	});
}

function messager(data) {
	if (data != null && data != "") {
		return (data.msg != null) ? (data.obj != null) ? data.msg + "：" + data.obj : data.msg : "操作异常";
	}
}
