package com.xpc.imlibrary.util;

import android.content.Context;

import com.xpc.imlibrary.R;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 时间日期格式化类
 * @author qiaocbao
 * @time 2014-10-7 上午9:27:36
 */
public class PrettyDateFormat extends SimpleDateFormat {
	private static final long serialVersionUID = 1L;
	private Pattern pattern = Pattern.compile("('*)(#{1,2}|@)");
	private FormatType formatType = FormatType.DEAFULT;
	private SimpleDateFormat simpleDateFormat;
	private Context context;
	private enum FormatType {
		DEAFULT, TIME, DAY
	};

	/**
	 * 构造器
	 * <p>
	 * format中的@表示[XXX秒前,XXX分钟前,XXX小时前(最多是23小时前)]
	 * <p>
	 * format中的#表示[空字串(表示今天),昨天,前天]
	 * <p>
	 * format中的##表示[今天,昨天,前天]
	 * 
	 * @param format
	 *            和SimpleDateFormat中的格式设置基本上是一样的,只是多的@格式 #格式和##格式
	 * @param fullFormat
	 *            和SimpleDateFormat中的格式设置是一样的
	 */
	public PrettyDateFormat(String format, String fullFormat, Context context) {
		super(fullFormat);
		this.context = context;
		Matcher m = pattern.matcher(format);
		while (m.find()) {
			if (m.group(1).length() % 2 == 0) {
				if ("@".equals(m.group(2))) {
					if (formatType == FormatType.DAY) {
						throw new IllegalArgumentException(context.getString(R.string.util_date_user));
					}
					formatType = FormatType.TIME;
				} else {
					if (formatType == FormatType.TIME) {
						throw new IllegalArgumentException(context.getString(R.string.util_date_user));
					}
					formatType = FormatType.DAY;
				}
			}
		}
		this.simpleDateFormat = new SimpleDateFormat(format.replace("'", "''"));
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		throw new UnsupportedOperationException(context.getString(R.string.util_date_no_operate));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.text.SimpleDateFormat#format(java.util.Date,
	 * java.lang.StringBuffer, java.text.FieldPosition)
	 */
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
		if (formatType == FormatType.DEAFULT) {
			return super.format(date, toAppendTo, pos);
		}

		long curTime = System.currentTimeMillis();

		long diffDay = 0L;
		long diffSecond = 0L;
		if (formatType == FormatType.TIME) {
			diffSecond = (curTime - date.getTime()) / 1000L;
			if (diffSecond < 0 || diffSecond >= 86400) {
				return super.format(date, toAppendTo, pos);
			}
		}
		if (formatType == FormatType.DAY) {
			Calendar curDate = new GregorianCalendar();
			curDate.setTime(new Date(curTime));
			curDate.set(Calendar.HOUR_OF_DAY, 23);
			curDate.set(Calendar.MINUTE, 59);
			curDate.set(Calendar.SECOND, 59);
			curDate.set(Calendar.MILLISECOND, 999);
			diffDay = (curDate.getTimeInMillis() - date.getTime()) / 86400000L;
			if (diffDay < 0 || diffDay > 2) {
				return super.format(date, toAppendTo, pos);
			}
		}
		StringBuffer sb = new StringBuffer();
		Matcher m = pattern.matcher(simpleDateFormat.format(date));
		if (m.find()) {
			String group2 = m.group(2);
			String replacement = "";
			while (true) {
				if ("@".equals(group2)) {
					if (diffSecond < 60) {
						replacement = diffSecond == 0 ? context.getString(R.string.util_date_one_second) : diffSecond + context.getString(R.string.util_date_second);
					} else if (diffSecond < 3600) {
						replacement = diffSecond / 60 + context.getString(R.string.util_date_minute);
					} else if (diffSecond < 86400) {
						replacement = diffSecond / 3600 + context.getString(R.string.util_date_hour);
					}
				} else {
					if (diffDay == 0) {
						replacement = group2.length() == 2 ? context.getString(R.string.util_date_today) : "";
					} else if (diffDay == 1) {
						replacement = context.getString(R.string.util_date_yestoday);
					} else {
						replacement = context.getString(R.string.util_date_qiantian);
					}
				}
				m.appendReplacement(sb, replacement);
				if (!m.find()) {
					break;
				}
			}
			m.appendTail(sb);
		}
		return toAppendTo.append(sb.toString());
	}
}
