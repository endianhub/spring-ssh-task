package com.xh.ssh.web.task.common.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>Title: 日期格式化</b>
 * <p>Description: </p>
 * <p>
 * 正则函数
 * 根据日期返回指定的日期格式
 * Date转String
 * String转Date
 * 
 * </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年8月28日
 */
@SuppressWarnings("all")
public class DateFormatTool {

	private final static Map<String, String> FORMAT_MAP = new HashMap<String, String>();
	public final static String DATA_FORMATTER1_1 = "yyyyMMdd";
	public final static String DATA_FORMATTER1_2 = "yyyy/MM/dd";
	public final static String DATA_FORMATTER1_3 = "yyyy-MM-dd";
	public final static String DATA_FORMATTER1_4 = "yyyy年MM月dd日";

	public final static String DATA_FORMATTER2_1 = "yyyyMMddHHmm";
	public final static String DATA_FORMATTER2_2 = "yyyy/MM/dd HH:mm";
	public final static String DATA_FORMATTER2_3 = "yyyy-MM-dd HH:mm";
	public final static String DATA_FORMATTER2_4 = "yyyy年MM月dd日 HH时mm分";

	public final static String DATA_FORMATTER3_1 = "yyyyMMddHHmmss";
	public final static String DATA_FORMATTER3_2 = "yyyy/MM/dd HH:mm:ss";
	public final static String DATA_FORMATTER3_3 = "yyyy-MM-dd HH:mm:ss";
	public final static String DATA_FORMATTER3_4 = "yyyy年MM月dd日 HH时mm分ss秒";

	public final static String DATA_FORMATTER4_1 = "EEE MMM dd HH:mm:ss zzz yyyy";
	static {
		FORMAT_MAP.put("\\d{8}$", DATA_FORMATTER1_1);
		FORMAT_MAP.put("\\d{12}$", DATA_FORMATTER2_1);
		FORMAT_MAP.put("\\d{14}$", DATA_FORMATTER3_1);

		FORMAT_MAP.put("\\d{4}/\\d{1,2}/\\d{1,2}", DATA_FORMATTER1_2);
		FORMAT_MAP.put("\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{2}:\\d{2}", DATA_FORMATTER2_2);
		FORMAT_MAP.put("\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}", DATA_FORMATTER3_2);

		FORMAT_MAP.put("\\d{4}-\\d{1,2}-\\d{1,2}", DATA_FORMATTER1_3);
		FORMAT_MAP.put("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}", DATA_FORMATTER2_3);
		FORMAT_MAP.put("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}", DATA_FORMATTER3_3);

		FORMAT_MAP.put("\\d{4}年\\d{1,2}月\\d{1,2}日", DATA_FORMATTER1_4);
		FORMAT_MAP.put("\\d{4}年\\d{1,2}月\\d{1,2}日\\s\\d{2}时\\d{2}分", DATA_FORMATTER2_4);
		FORMAT_MAP.put("\\d{4}年\\d{1,2}月\\d{1,2}日\\s\\d{2}时\\d{2}分\\d{2}秒", DATA_FORMATTER3_4);

		FORMAT_MAP.put("[A-Za-z]{3}\\s[A-Za-z]{3}\\s\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s[A-Za-z]{3}\\s\\d{4}", DATA_FORMATTER4_1);
		// FORMAT_MAP.put("", "");
	}

	/**
	 * <b>Title: 正则函数</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param regex
	 * @param orginal
	 * @return
	 */
	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	/**
	 * <b>Title: 根据日期返回指定的日期格式</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param orginal
	 * @return
	 */
	public static String getDateFormat(String orginal) {
		String formatStr = "";
		for (Map.Entry<String, String> entry : FORMAT_MAP.entrySet()) {
			boolean flag = DateFormatTool.isMatch(entry.getKey(), orginal);
			if (flag) {
				formatStr = entry.getValue();
				break;
			}
		}
		return formatStr;
	}

	/**
	 * <b>Title: Date转String</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param date
	 * @param formatter
	 * @return
	 */
	public static String parseDateToString(Date date, String formatter) {
		DateFormat sd = new SimpleDateFormat(formatter);
		return sd.format(date);
	}

	/**
	 * <b>Title: String转Date</b>
	 * <p>Description: 根据输入的日期格式转入对应的日期</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param orginal
	 * @return
	 */
	public static Date parseStringToDate(String orginal) {
		try {
			String formatStr = "";
			for (Map.Entry<String, String> entry : FORMAT_MAP.entrySet()) {
				boolean flag = DateFormatTool.isMatch(entry.getKey(), orginal);
				if (flag) {
					formatStr = entry.getValue();
					break;
				}
			}
			if (formatStr.equals("")) {
				return null;
			}
			return (formatStr.equals("EEE MMM dd HH:mm:ss zzz yyyy"))//
					? new SimpleDateFormat(formatStr, java.util.Locale.ENGLISH).parse(orginal)//
					: new SimpleDateFormat(formatStr).parse(orginal);//
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws ParseException {
		ArrayList array = new ArrayList();
		array.add("20180829");
		array.add("201808291032");
		array.add("20180829103232");
		array.add("2018-08-29");
		array.add("2018-08-29 10:32");
		array.add("2018-08-29 10:32:32");
		array.add("2018/08/29");
		array.add("2018/08/29 10:32");
		array.add("2018/08/29 10:32:32");
		array.add("2018年08月29日");
		array.add("2018年08月29日 10时32分");
		array.add("2018年08月29日 10时32分32秒");
		array.add("Wed Aug 29 10:32:32 CST 2018");

		// for (Object obj : array) {
		// String formatStr = getDateFormat((String) obj);
		// System.out.println(formatStr);
		//// DateFormat formatter = new SimpleDateFormat(formatStr);
		//// formatter.format(date)
		// }

		String date = "Wed Aug 29 00:00:00 CST 2018";
		// String formatStr = getDateFormat(date);
		// System.out.println(formatStr);
		// DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// SimpleDateFormat sfStart = new SimpleDateFormat(formatStr, java.util.Locale.ENGLISH);
		// System.out.println(sfStart.format(sfStart.parse(date)));

		// String date = DateFormatTool.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
		// System.out.println(date);
		//
		// Date date2 = DateFormatTool.getDateFormat(date, "yyyy-MM-dd HH:mm:ss");
		// System.out.println(date2);

		Date daa = parseStringToDate(date);
		System.out.println(parseDateToString(daa, "yyyy-MM-dd HH:mm:ss"));
	}

}
