package com.xh.ssh.web.task.common.tool;

import java.beans.PropertyEditorSupport;

import org.springframework.web.util.HtmlUtils;

/**
 * <p>Title: 防止XSS攻击</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年4月8日
 * 
 */
public class StringEscapeEditorUtils extends PropertyEditorSupport {

	public StringEscapeEditorUtils() {

	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? value.toString() : "";
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		} else {
			setValue(HtmlUtils.htmlEscape(text));
		}
	}

}
