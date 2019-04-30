//时间格式化
function formatterDate(value, row, index) {
	if (value != null) {
		return TimeObjectUtil.longMsTimeConvertToDateTime(value);
	}
	return null;
}

function sex(value, row, index) {
	return (value == 0) ? "男" : "女";
}
function opened(value, row, index) {
	return (value == 1) ? "打开" : "关闭";
}
function resourceType(value, row, index) {
	return (value == 0) ? "菜单" : "按钮";
}
function status(value, row, index) {
	return (value == 0) ? "正常" : "停用";
}

function userType(value, row, index) {
	if (value == 0) {
		return "管理员";
	} else if (value == 1) {
		return "普通用户";
	}
	return "未知类型";
}

//请假状态
function formatterLeaveState(value, row, index) {
	switch (value) {
	case 0:
		return "未提交";
	case 1:
		return "审批通过";
	case 2:
		return "审批拒绝";
	case 3:
		return "审批中";
	case 4:
		return "驳回";
	default:
		return "审批中";
	}
}
