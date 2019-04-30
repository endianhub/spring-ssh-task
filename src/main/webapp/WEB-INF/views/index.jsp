<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>首页</title>
<%@ include file="/commons/base.jsp"%>
<link rel="stylesheet" type="text/css" href="${staticPath }/static/css/main.css">
<script type="text/javascript" src="${staticPath }/static/js/main.js"></script>
</head>

<body>
	<div id="mainLayout" class="easyui-layout" data-options="fit:true, border:false">
		<div data-options="region:'north',border:false, collapsedSize:0" style="height: 50px;">
			<div class="head">
				<table>
					<tr>
						<td width="50%" style="font-size: 14px;">Activiti</td>
						<td width="50%" align="right" style="font-size: 12px;">
							<div class="easyui-panel rtool" data-options="border:false"
								style="text-align: right; background: #09C; color: white; margin-top: -1px;">
								<a href="#" class="easyui-menubutton" data-options="menu:'#mm1'">
									<shiro:principal></shiro:principal>
								</a>
							</div>
							<div id="mm1" style="width: 150px;">
								<div data-options="iconCls:'glyphicon-pencil'" onclick='editUserPwd()'>修改密码</div>
								<div class="menu-sep"></div>
								<div data-options="iconCls:'glyphicon-log-out'" onclick="loginOut()">退出</div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="west" class="scrollbar" region="west" hide="true" split="true" title=""
			style="width: 180px; background-color: #424f63; color: white">
			<div class="west_menu">
				<div class="menu_head">菜单导航</div>
				<ul id="treeDemo" class="easyui-tree ztree"></ul>
			</div>
		</div>
		<div id="mainPanle" region="center">
			<iframe id="iframe" class="easyui-panel" frameborder="0" scrolling="no" width="100%" height="100%" src="https://www.baidu.com/"
				style="overflow: hidden;"></iframe>
		</div>
	</div>
</body>
</html>
