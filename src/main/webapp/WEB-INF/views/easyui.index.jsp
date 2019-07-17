<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>首页</title>
<%@ include file="/commons/base.jsp"%>
</head>
<script type="text/javascript">
	$(function() {
		var obj = new Object();
		obj.title = "任务列表";
		obj.width = "700";
		obj.url = "query.action";
		obj.selectorId = "#taskDataGrid";
		obj.type = "GET";
		loadDataGrid(obj);
	});

	function formatterPath(value, row, index) {
		value = (value == null) ? "" : value;
		return "<span title="+value+">" + value + "</span>"
	}
	function formatterInfo(value, row, index) {
		return "<span title="+value+">" + value + "</span>"
	}
	function formatDate(value) {
		var date = new Date(value);//如果date为10位不需要乘1000
		var Y = date.getFullYear() + "-";
		var M = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) + "-";
		var D = (date.getDate() < 10 ? "0" + (date.getDate()) : date.getDate()) + " ";
		var h = (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
		var m = (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) + ":";
		var s = (date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds());
		return Y + M + D + h + m + s;
	}

	var url = "";

	function saveDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加");
		$("#fm").form("clear");
		url = "save.action";
	}
	function removeDialog() {
		var rows = $("#taskDataGrid").datagrid("getSelections");
		if (rows) {
			var paramId = "";
			for (var i = 0; i < rows.length; i++) {
				paramId += rows[i].taskId + ",";
			}
			$.messager.confirm("系统提示：", "您确定要删除吗?", function(r) {
				if (r) {
					$.post("remove.action", {
						taskId : paramId.substring(paramId.length - 1, -1)
					}, function(data) {
						$("#taskDataGrid").datagrid("reload"); // reload the user data
						if (data.code == 200) {
							$.messager.show({
								title : "系统提示",
								msg : "删除成功！"
							});
						} else {
							$.messager.show({
								title : "系统提示",
								msg : messager(data)
							});
						}
					}, "json");
				}
			});
		} else {
			$.messager.alert("系统提示", "未选中要修改的数据！", "warning");
		}
	}
	function editDialog() {
		var row = $("#taskDataGrid").datagrid("getSelected");
		console.info(row);
		if (row) {
			$("#dlg").dialog("open").dialog("setTitle", "修改");
			$("#fm").form("load", row);
			url = "edit.action";
		} else {
			$.messager.alert("系统提示", "未选中要修改的数据！", "warning");
		}
	}

	function saveData() {
		$('#fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(data) {
				data = (data != null && data != "") ? JSON.parse(data) : null;
				if (data != null && data.code == 200) {
					$("#dlg").dialog("close");
					$("#taskDataGrid").datagrid("reload"); // reload the user data
					$.messager.show({
						title : "系统提示",
						msg : "更新成功！"
					});
				} else {
					$.messager.show({
						title : "系统提示",
						msg : messager(data)
					});
				}
			}
		});
	}

	function executeTask() {
		ajaxTask("execute.action");
	}
	function deployTask() {
		ajaxTask("deploy.action");
	}
	function undeployTask() {
		ajaxTask("undeploy.action");
	}
	function pauseTask() {
		ajaxTask("pause.action");
	}
	function shutdownTask() {
		ajaxTask("shutdown.action");
	}
	function restoreTask() {
		ajaxTask("restore.action");
	}
	function restarTask() {
		ajaxTask("restore.action");
	}

	function ajaxTask(url) {
		var rows = $("#taskDataGrid").datagrid("getSelections");
		if (rows) {
			var paramId = "";
			for (var i = 0; i < rows.length; i++) {
				paramId += rows[i].taskId + ",";
			}
			$.post(url, {
				taskId : paramId
			}, function(data) {
				$("#taskDataGrid").datagrid("reload"); // reload the user data
				if (data.code == 200) {
					$.messager.show({
						title : "系统提示",
						msg : data.msg
					});
				} else {
					$.messager.show({
						title : "系统提示",
						msg : messager(data)
					});
				}
			}, "json");
		}
	}
</script>
<body>
	<table id="taskDataGrid" title="任务列表" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="taskId" width="40">编号</th>
				<th field="taskName" width="140">任务名称</th>
				<th field="taskClass" width="220">任务类名</th>
				<th field="taskDesc" width="220">任务描述</th>
				<!-- <th field="taskUrl" width="40">发送地址</th> -->
				<th field="executed" width="80">执行次数</th>
				<th field="cron" width="140">cron表达式</th>
				<th field="cronDesc" width="180">cron表达式说明</th>
				<!-- <th field="noticeList" width="220">通知单（邮件通知）</th> -->
				<th field="status" width="80">状态</th>
				<th field="createTime" width="140" formatter="formatDate">创建时间</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="deployTask()">部署</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="executeTask()">执行</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="undeployTask()">取消部署</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="pauseTask()">暂停</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="restoreTask()">恢复</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="shutdownTask()">关闭</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="restarTask()">重启</a>

		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="saveDialog()">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="removeDialog()">删除</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="editDialog()">更新</a>
	</div>

	<div id="dlg" class="easyui-dialog" style="width: 510px; height: 450px; padding: 10px 20px;" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">任务信息</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<input id="taskId" name="taskId" type="hidden">
			</div>
			<div class="fitem">
				<label>任务名称:</label>
				<input id="taskName" name="taskName" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>任务类名:</label>
				<input id="taskClass" name="taskClass" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>任务描述:</label>
				<input id="taskDesc" name="taskDesc" class="easyui-textbox">
			</div>
			<div class="fitem">
				<label>发送地址:</label>
				<input id="taskUrl" name="taskUrl" class="easyui-textbox">
			</div>
			<div class="fitem">
				<label>执行次数:</label>
				<input id="executed" name="executed" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>cron表达式:</label>
				<input id="cron" name="cron" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>cron表达式说明:</label>
				<input id="cronDesc" name="cronDesc" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>通知单（邮件通知）:</label>
				<input id="noticeList" name="noticeList" class="easyui-textbox">
			</div>
		</form>

	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveData()">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>
</html>
