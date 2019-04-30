package com.xh.ssh.web.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>Title: 定时任务</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-09-01
 */
@Entity
@Table(name = "web_task")
public class WebTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Integer taskId;
	// 任务名称
	@Column(name = "task_name")
	private String taskName;
	// 任务类名
	@Column(name = "task_class")
	private String taskClass;
	// 任务描述
	@Column(name = "task_desc")
	private String taskDesc;
	// 发送地址
	@Column(name = "task_url")
	private String taskUrl;
	// 计划执行次数
	@Column(name = "plan_exec")
	private Integer planExec;
	// 执行次数
	@Column(name = "executed")
	private Integer executed;
	// cron表达式
	@Column(name = "cron")
	private String cron;
	// cron表达式说明
	@Column(name = "cron_desc")
	private String cronDesc;
	// 通知单（邮件通知）
	@Column(name = "notice_list")
	private String noticeList;
	// 0无效，1有效，2执行次数到达计划执行次数，3部署
	@Column(name = "status")
	private Integer status;
	@Column(name = "create_time", updatable = false)
	private Date createTime;

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	/** 任务名称 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/** 任务名称 */
	public String getTaskName() {
		return taskName;
	}

	/** 任务类名 */
	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}

	/** 任务类名 */
	public String getTaskClass() {
		return taskClass;
	}

	/** 任务描述 */
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	/** 任务描述 */
	public String getTaskDesc() {
		return taskDesc;
	}

	/** 任务请求地址 */
	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	/** 任务请求地址 */
	public String getTaskUrl() {
		return taskUrl;
	}

	/** 计划执行次数 */
	public void setPlanExec(Integer planExec) {
		this.planExec = planExec;
	}

	/** 计划执行次数 */
	public Integer getPlanExec() {
		return planExec;
	}

	/** 执行次数 */
	public void setExecuted(Integer executed) {
		this.executed = executed;
	}

	/** 执行次数 */
	public Integer getExecuted() {
		return executed;
	}

	/** cron表达式 */
	public void setCron(String cron) {
		this.cron = cron;
	}

	/** cron表达式 */
	public String getCron() {
		return cron;
	}

	/** cron表达式描述 */
	public void setCronDesc(String cronDesc) {
		this.cronDesc = cronDesc;
	}

	/** cron表达式描述 */
	public String getCronDesc() {
		return cronDesc;
	}

	/** 通知单（邮件通知） */
	public void setNoticeList(String noticeList) {
		this.noticeList = noticeList;
	}

	/** 通知单（邮件通知） */
	public String getNoticeList() {
		return noticeList;
	}

	/** 0无效, 1未部署, 2已部署, 3已暂停, 4设上限(执行次数到达计划执行次数) */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 0无效, 1未部署, 2已部署, 3已暂停, 4设上限(执行次数到达计划执行次数) */
	public Integer getStatus() {
		return status;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}
}
