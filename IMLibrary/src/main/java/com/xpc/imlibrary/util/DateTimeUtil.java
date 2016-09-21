package com.xpc.imlibrary.util;

import android.content.Context;
import android.text.TextUtils;

import com.xpc.imlibrary.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间日期处理类
 * 
 * @author qiaocb
 * @time 2015-10-26 下午3:14:55
 */
public class DateTimeUtil {

	/** 默认日期格式 */
	public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** 日期时间格式到年(yyyy) */
	public static final String FORMAT_YEAR = "yyyy";
	/** 日期时间格式到月(yyyy-MM) */
	public static final String FORMAT_MONTH = "yyyy-MM";
	/** 日期时间格式到日(yyyy-MM-dd) */
	public static final String FORMAT_DAY = "yyyy-MM-dd";
	/** 日期时间格式到日(yyyy-MM-dd) */
	public static final String FORMAT_DAY_TWO = "yyyy年MM月dd日";
	/** 日期时间格式到分(yyyy-MM-dd HH:mm) */
	public static final String FORMAT_MINUTE_ONE = "yyyy-MM-dd HH:mm";
	/** 日期时间格式到分(yyyy年MM月dd日HH点mm分) */
	public static final String FORMAT_MINUTE_TWO = "yyyy年MM月dd日HH点mm分";
	/** 日期时间格式到毫秒(yyyy-MM-dd-HH-mm-ss-SSS) */
	public static final String FORMAT_MILLISECOND = "yyyy-MM-dd-HH-mm-ss-SSS";
	/** 日期时间格式到时分(HH:mm) */
	public static final String FORMAT_HOUR_MINUTE = "HH:mm";
	/** 日期时间格式到时分秒(HH:mm:ss) */
	public static final String FORMAT_HOUR_MINUTE_SECOND = "HH:mm:ss";
	/** 日期时间格式到星期(EEEE) */
	public static final String FORMAT_WEEK = "EEEE";
	/** 日期时间格式到日包括星期*/
	public static final String FORMAT_DAY_WEEK="yyyy-MM-dd (EEEE)";

	/**
	 * 获取当前系统时间
	 * 
	 * @param format
	 *            时间格式
	 * @return 日期时间
	 */
	public static String getCurrDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format,
				Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 获得当前日期的字符串格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return cal2Str(c, format);
	}

	/**
	 * 获取日期时间由指定毫秒
	 * 
	 * @param format
	 *            日期格式
	 * @param time
	 *            毫秒
	 * @return 日期时间
	 */
	public static String getMillon(String format, long time) {
		return new SimpleDateFormat(format, Locale.getDefault()).format(time);
	}
	/**
	 * 判断时间是否是指定时间格式
	 * @param inputTime	时间
	 * @param format	格式
	 * @return	true:是	false:不是
	 */
	public static  boolean isDateFormat(String inputTime,SimpleDateFormat format){
		if (!TextUtils.isEmpty(inputTime)) {
			format.setLenient(false);
	         try {
	        	 format.format(format.parse(inputTime));
	         } catch (Exception e) {
	             return false;
	         }
	         return true;
	     }
		return false;
	}
	/**
	 * @Description 指定年月的最后一天日期
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param simpleDateFormat
	 *            日期格式
	 * @return String 指定格式日期
	 */
	public static String getMonthLastDayDate(int year, int month,
			SimpleDateFormat simpleDateFormat) {
		if (year < 0)
			return null;
		Calendar cal = Calendar.getInstance();
		if (month < 0) {
			cal.set(Calendar.YEAR, year - 1);
			cal.set(Calendar.MONTH, 12 + month % 11);
		} else if (month > 11) {
			cal.set(Calendar.YEAR, year + 1);
			cal.set(Calendar.MONTH, month % 11);
		} else {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
		}
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String date = simpleDateFormat.format(cal.getTime());
		return date;
	}

	/**
	 * @Description 指定年月的第一天日期
	 * @param year
	 * @param month
	 * @param simpleDateFormat
	 * @return String
	 */
	public static String getMonthFirstDayDate(int year, int month,
			SimpleDateFormat simpleDateFormat) {
		if (year < 0)
			return null;
		Calendar cal = Calendar.getInstance();

		if (month < 0) {
			cal.set(Calendar.YEAR, year - 1);
			cal.set(Calendar.MONTH, 12 + month);
		} else if (month > 11) {
			cal.set(Calendar.YEAR, year + 1);
			cal.set(Calendar.MONTH, month % 11);
		} else {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String date = simpleDateFormat.format(cal.getTime());
		return date;
	}

	/**
	 * 获取指定日期当前周的第一天或者最后一天
	 * 
	 * @param cal
	 *            指定日期
	 * @param flag
	 *            true:指定周第一天; false:周最后一天;
	 * @return 日期
	 */
	public static String getFirstLastWeek(Calendar cal, boolean flag) {
		int dw = cal.get(Calendar.DAY_OF_WEEK);
		if (!flag)
			cal.setTimeInMillis(cal.getTimeInMillis() + (7 - dw) * 24 * 60 * 60
					* 1000);
		else
			cal.setTimeInMillis(cal.getTimeInMillis() - (dw - 1) * 24 * 60 * 60
					* 1000);

		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DAY,
				Locale.getDefault());

		String showTime = formatter.format(cal.getTime());
		return showTime.toString();
	}

	/**
	 * 指定日期当月第一天或最后一天
	 * 
	 * @param cal
	 *            指定日期
	 * @param flag
	 *            true月第一天 false 月最后一天
	 * @return 日期
	 */
	public static String getFirstLastMoonth(Calendar cal, boolean flag) {
		cal.set(Calendar.DAY_OF_MONTH, 1);
		if (!flag) {
			cal.roll(Calendar.DAY_OF_MONTH, -1);
		}
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DAY,
				Locale.getDefault());
		String showTime = formatter.format(cal.getTime());
		return showTime.toString();
	}

	/**
	 * cal转string(格式yyyy-MM-dd)
	 * 
	 * @param cal
	 *            指定日期
	 * @return 日期字符串
	 */
	public static String getDateString(Calendar cal) {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DAY,
				Locale.getDefault());
		String showTime = formatter.format(cal.getTime());
		return showTime.toString();
	}

	/**
	 * 获取当前星期几
	 * 
	 * @return 当前星期
	 */
	public static String getWeekTime(Context context) {
		String weekly = "";
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int mWay = c.get(Calendar.DAY_OF_WEEK);
		switch (mWay) {
		case 1:
			weekly = context.getString(R.string.sunday);
			break;
		case 2:
			weekly = context.getString(R.string.monday);
			break;
		case 3:
			weekly = context.getString(R.string.tuesday);
			break;
		case 4:
			weekly = context.getString(R.string.wednesday);
			break;
		case 5:
			weekly = context.getString(R.string.thursday);
			break;
		case 6:
			weekly = context.getString(R.string.friday);
			break;
		case 7:
			weekly = context.getString(R.string.saturday);
			break;
		default:
			break;
		}
		return weekly;
	}

	/**
	 * 日期月日偶数格式，为奇数时在日期的日和月前加0
	 * 
	 * @return 格式后的日期，null格式失败
	 */
	public static String getSimpleDate(String date) {
		if (!TextUtils.isEmpty(date) && !date.equals("")) {
			String[] dates = date.split("-");
			int month = Integer.valueOf(dates[1]);
			int day = Integer.valueOf(dates[2]);
			return dates[0] + "-" + getZeroDate(month) + "-" + getZeroDate(day);

		}
		return null;
	}

	/**
	 * 单数处理，单位数前加0
	 * 
	 * @param temp
	 *            整数
	 * @return 双数
	 */
	public static String getZeroDate(int temp) {
		if (temp < 10) {
			return "0" + temp;
		}
		return temp + "";
	}

	/**
	 * str转Date（默认格式）
	 * 
	 * @param str
	 *            日期
	 * @return Date日期
	 */
	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	/**
	 * str转Date（指定格式）
	 * 
	 * @param str
	 *            日期
	 * @param format
	 *            格式（为空则用默认）
	 * @return Date日期
	 */
	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format,
					Locale.getDefault());
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	/**
	 * str转Calendar（默认格式）
	 * 
	 * @param str
	 * @return 指定日期Calendar
	 */
	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);
	}

	/**
	 * str转Calendar（指定格式）
	 * 
	 * @param str
	 *            日期
	 * @param format
	 *            格式（为空用默认）
	 * @return 指定日期Calendar
	 */
	public static Calendar str2Calendar(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;

	}

	/**
	 * Calendar转字符串（默认格式）
	 * 
	 * @param c
	 *            日期
	 * @return str日期
	 */
	public static String cal2Str(Calendar c) {
		return cal2Str(c, null);
	}

	/**
	 * Calendar转字符串（指定格式）
	 * 
	 * @param c
	 *            日期
	 * @param format
	 *            格式
	 * @return str日期
	 */
	public static String cal2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	/**
	 * Date转字符串（默认格式）
	 * 
	 * @param d
	 * @return
	 */
	public static String date2Str(Date d) {
		return date2Str(d, null);
	}

	/**
	 * Date转字符串（指定格式）
	 * 
	 * @param d
	 *            日期
	 * @param format
	 *            格式（为空用默认）
	 * @return 格式化后String日期
	 */
	public static String date2Str(Date d, String format) {
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String s = sdf.format(d);
		return s;
	}

	/**
	 * 计算两个时间差
	 * 
	 * @param curTime
	 *            当前时间
	 * @param beforeTime
	 *            以前时间
	 * @param format
	 *            时间格式
	 * @return 时间差
	 */
	public static long dateComm(String curTime, String beforeTime, String format) {
		Date curDate = str2Date(curTime, format);
		Date beforeDate = str2Date(beforeTime, format);
		long diff = curDate.getTime() - beforeDate.getTime();// 这样得到的差值是微秒级别

		long days = diff / (1000 * 60 * 60 * 24);
		long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);
		return minutes;
	}

	/**
	 * 当前时间推迟几个小时的时间
	 * 
	 * @param hour
	 *            当前小时
	 * @param simpleDateFormat
	 *            时间格式
	 * @return date 推迟的时间
	 */
	public static String delayHourTime(int hour,
			SimpleDateFormat simpleDateFormat) {
		Calendar cal = Calendar.getInstance();
		int mCurrentHour;
		mCurrentHour = cal.get(Calendar.HOUR_OF_DAY);
		cal.set(Calendar.HOUR_OF_DAY, mCurrentHour + hour);
		String date = simpleDateFormat.format(cal.getTime());
		return date;
	}

	/**
	 * 获取指定日期中的时间（时分）
	 * 
	 * @param dateTime
	 *            日期
	 * @param format
	 *            格式
	 * @return 时间
	 */
	public static String getHourAndMinute(String dateTime, String format) {
		String hourMinute = "";
		SimpleDateFormat houeMinFormat = new SimpleDateFormat(
				FORMAT_HOUR_MINUTE, Locale.US);
		try {
			Date date = new SimpleDateFormat(FORMAT, Locale.US).parse(dateTime);
			hourMinute = houeMinFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(hourMinute)) {
			return hourMinute;
		}
		return dateTime;
	}

	/**
	 * 获取当前时间
	 * 
	 * @param format
	 *            格式
	 * @return 时间
	 */
	public static String getDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format,
				Locale.getDefault());
		String showTime = formatter.format(Calendar.getInstance(
				Locale.getDefault()).getTime());
		return showTime.toString();
	}

	/**
	 * 根据星期int类型数组获取星期
	 * 
	 * @param context
	 *            当前上下文
	 * @param weekInt
	 *            星期int类型标识（7为星期天）
	 * @return 星期
	 */
	public static String getIntToStringWeek(Context context, String weekInt) {
		StringBuffer weekString = new StringBuffer();
		if (!TextUtils.isEmpty(weekInt)) {
			if (weekInt.indexOf(",") != -1) {
				String[] weeks = weekInt.split(",");
				if (weeks.length > 0) {
					for (int i = 0; i < weeks.length; i++) {
						String week = weeks[i];
						int w = Integer.parseInt(week);
						switch (w) {
						case 1:
							weekString.append(context
									.getString(R.string.monday));
							break;
						case 2:
							weekString.append(context
									.getString(R.string.tuesday));
							break;
						case 3:
							weekString.append(context
									.getString(R.string.wednesday));
							break;
						case 4:
							weekString.append(context
									.getString(R.string.thursday));
							break;
						case 5:
							weekString.append(context
									.getString(R.string.friday));
							break;
						case 6:
							weekString.append(context
									.getString(R.string.saturday));
							break;
						case 7:
							weekString.append(context
									.getString(R.string.sunday));
							break;
						default:
							break;
						}
						weekString.append("、");
					}
					weekString.deleteCharAt(weekString.length() - 1);
				}
			} else {
				int w = Integer.parseInt(weekInt);
				switch (w) {
				case 1:
					weekString.append(context.getString(R.string.monday));
					break;
				case 2:
					weekString.append(context.getString(R.string.tuesday));
					break;
				case 3:
					weekString.append(context.getString(R.string.wednesday));
					break;
				case 4:
					weekString.append(context.getString(R.string.thursday));
					break;
				case 5:
					weekString.append(context.getString(R.string.friday));
					break;
				case 6:
					weekString.append(context.getString(R.string.saturday));
					break;
				case 7:
					weekString.append(context.getString(R.string.sunday));
					break;
				default:
					break;
				}
			}
		}
		return weekString.toString();
	}

	/**
	 * 获取指定时间转换为秒
	 * 
	 * @param curTime
	 *            时间
	 * @param format
	 *            格式
	 * @return 秒
	 */
	public static long getDateTimeToSecond(String curTime, String format) {
		if (TextUtils.isEmpty(format)) {
			format = FORMAT_HOUR_MINUTE_SECOND;
		}
		long second = 0;
		Calendar cal = str2Calendar(curTime, format);
		if (cal != null) {
			second = (cal.get(Calendar.HOUR_OF_DAY) * (60 * 60))
					+ (cal.get(Calendar.MINUTE) * 60)
					+ cal.get(Calendar.SECOND);
		}
		return second;
	}

	/**
	 * 获取当前时间换为秒
	 * 
	 * @return 秒
	 */
	public static long getCurrentSecond() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE)
				* 60 + cal.get(Calendar.SECOND);
	}

	/**
	 * 将毫秒转为时间
	 * 
	 * @param l
	 *            毫秒
	 * @return 时间
	 */
	public static String getTimeMillisToDate(long l) {
		Timestamp d = new Timestamp(l);
		return d.toString().substring(0, 19);
	}

	/**
	 * 当前时间
	 * 
	 * @return Timestamp
	 */
	public static Timestamp crunttime() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间的字符串
	 * 
	 * @return String ex:2006-07-07
	 */
	public static String getCurrentDate() {
		Timestamp d = crunttime();
		return d.toString().substring(0, 10);
	}

	/**
	 * 获取当前时间的字符串
	 * 
	 * @return String ex:2006-07-07 22:10:10
	 */
	public static String getCurrentDateTime() {
		Timestamp d = crunttime();
		return d.toString().substring(0, 19);
	}

	/**
	 * 获取当前星期
	 * 
	 * @return 星期
	 */
	public static String getWeekDay() {
		Calendar date = Calendar.getInstance();
		date.setTime(crunttime());
		return new SimpleDateFormat(FORMAT_WEEK, Locale.getDefault())
				.format(date.getTime());
	}

	/**
	 * 获取指定时间的字符串,只到日期
	 * 
	 * @param t
	 *            时间戳Timestamp
	 * 
	 * @return String ex:2006-07-07
	 */
	public static String getStrDate(Timestamp t) {
		return t.toString().substring(0, 10);
	}

	/**
	 * 获取指定时间的字符串
	 * 
	 * @param t
	 *            时间戳Timestamp
	 * 
	 * @return String ex:2006-07-07 22:10:10
	 */
	public static String getStrDateTime(Timestamp t) {
		return t.toString().substring(0, 19);
	}

	/**
	 * 获得当前日期的前几天日期
	 * 
	 * @param days
	 *            相隔天数
	 * @return String 日期
	 */
	public static String getStrIntervalDate(String days) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DAY,
				Locale.getDefault());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -Integer.parseInt(days));
		String strBeforeDays = sdf.format(cal.getTime());
		return strBeforeDays;
	}

	/**
	 * 格式化时间Date(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @param dt
	 *            String -> yyyy-MM-dd hh:mm:ss
	 * @return java.util.Date.Date -> yyyy-MM-dd hh:mm:ss
	 */
	public static Date parseDateTime(String dt) {
		Date jDt = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT,
					Locale.getDefault());
			if (dt.length() > 10) {
				jDt = sdf.parse(dt);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jDt;
	}

	/**
	 * 格式化时间(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param date
	 *            java.util.Date
	 * @return String -> yyyy-MM-dd HH:mm:ss
	 */
	public static String parseDateTime(Date date) {
		String s = null;
		if (date != null) {
			try {
				SimpleDateFormat f = new SimpleDateFormat(FORMAT,
						Locale.getDefault());
				s = f.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	/**
	 * 格式化日期(yyyy-MM-dd)
	 * 
	 * @param dt
	 *            String -> yyyy-MM-dd
	 * @return java.util.Date.Date -> yyyy-MM-dd
	 */
	public static Date parseDate(String dt) {
		Date jDt = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DAY,
					Locale.getDefault());
			if (dt.length() >= 8) {
				jDt = sdf.parse(dt);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jDt;
	}
	
	/**
	 * 格式化日期
	 * @param dt 日期
	 * @param format 日期格式
	 * @return 格式化后的日期
	 */
	public static Date parseDate(String dt,String format) {
		Date jDt = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format,
					Locale.getDefault());
			if (dt.length() >= 8) {
				jDt = sdf.parse(dt);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jDt;
	}

	/**
	 * 格式化时间(yyyy-MM-dd)
	 * 
	 * @param date
	 *            java.util.Date
	 * @return String -> yyyy-MM-dd
	 */
	public static String parseDate(Date date) {
		String s = null;
		try {
			if (date != null) {
				SimpleDateFormat f = new SimpleDateFormat(FORMAT_DAY,
						Locale.getDefault());
				s = f.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 格式化时间(yyyy-MM-dd(星期))
	 * @param date
	 * @return
	 */
	public static String parseDateWeek(Date date){
		String s=null;
		try {
			if(date!=null){
				SimpleDateFormat f=new SimpleDateFormat(FORMAT_DAY_WEEK, Locale.getDefault());
				s=f.format(date);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 格式化时间(yyyy-MM-dd 00:00:00)
	 * 
	 * @param dt
	 *            String时间
	 * @return String -> yyyy-MM-dd 00:00:00
	 */
	public static String getLongDateFromShortDate(String dt) {
		String strDT = dt;
		try {
			if (strDT != null && strDT.length() <= 10) {
				strDT = dt.trim() + " 00:00:00";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strDT;
	}

	/**
	 * 格式化时间(yyyy-MM-dd 00:00)
	 * 
	 * @param dt
	 *            String时间
	 * @return String -> yyyy-MM-dd 00:00
	 */
	public static String getShortDateToHHMM(String dt) {
		String jDt = dt;
		try {
			if (jDt != null && jDt.length() <= 10) {
				jDt = jDt + " 00:00";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MINUTE_ONE,
					Locale.getDefault());
			jDt = sdf.parse(jDt).toLocaleString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jDt;
	}

	/**
	 * 格式化日期时间(yyyy-MM-dd HH:mm)
	 * 
	 * @param dateStr
	 *            指定日期
	 * @return 日期 -> yyyy-MM-dd HH:mm
	 */
	public static String formatDateToHHMM(String dateStr) {
		String resultDate = null;
		try {
			if (dateStr.length() > 10) {
				SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MINUTE_ONE,
						Locale.getDefault());
				Date date = sdf.parse(dateStr);
				resultDate = sdf.format(date);
			} else
				resultDate = dateStr;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultDate;
	}

	/**
	 * 获取相隔日期 格式:2006-07-05(距离1900)
	 * 
	 * @param str
	 *            当前时间
	 * @return Timestamp
	 */
	public static Timestamp getDiffdate(String str) {
		Timestamp tp = null;
		if (str.length() <= 10) {
			String[] string = str.trim().split("-");
			int one = Integer.parseInt(string[0]) - 1900;
			int two = Integer.parseInt(string[1]) - 1;
			int three = Integer.parseInt(string[2]);
			tp = new Timestamp(one, two, three, 0, 0, 0, 0);
		}
		return tp;
	}

	/**
	 * 获取指定日期之后的日期字符串 如(2007-04-15 后一天 就是 2007-04-16)
	 * 
	 * @param strDate
	 *            指定日期
	 * @param day
	 *            间隔天数
	 * @return 间隔日期
	 */
	public static String getNextDay(String strDate, int day) {
		if (strDate != null && !strDate.equals("")) {
			Calendar cal1 = Calendar.getInstance();
			String[] string = strDate.trim().split("-");
			int one = Integer.parseInt(string[0]) - 1900;
			int two = Integer.parseInt(string[1]) - 1;
			int three = Integer.parseInt(string[2]);
			cal1.setTime(new Date(one, two, three));
			cal1.add(Calendar.DAY_OF_MONTH, day);
			SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DAY,
					Locale.getDefault());
			return formatter.format(cal1.getTime());
		} else {
			return null;
		}
	}

	/**
	 * 获取指定日期年之后的日期年字符串 (如 2007-02-28 后一年 就是 2008-02-29 （含闰年）)
	 * 
	 * @param strDate
	 *            指定日期
	 * @return 间隔年日期
	 */
	public static String getNextYear(String strDate, int year) {
		Calendar cal1 = Calendar.getInstance();
		String[] string = strDate.trim().split("-");
		int one = Integer.parseInt(string[0]) - 1900;
		int two = Integer.parseInt(string[1]) - 1;
		int three = Integer.parseInt(string[2]);
		cal1.setTime(new Date(one, two, three));
		cal1.add(Calendar.YEAR, year);
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DAY,
				Locale.getDefault());
		return formatter.format(cal1.getTime());
	}

	/**
	 * 返回时间和日期 (格式:2006-07-05 22:10:10)
	 * 
	 * @param str
	 *            日期
	 * @return Timestamp 时间戳
	 */
	public static Timestamp datetime(String str) {
		Timestamp tp = null;
		if (str != null && str.length() > 10) {
			String[] string = str.trim().split(" ");
			String[] date = string[0].split("-");
			String[] time = string[1].split(":");
			int date1 = Integer.parseInt(date[0]) - 1900;
			int date2 = Integer.parseInt(date[1]) - 1;
			int date3 = Integer.parseInt(date[2]);
			int time1 = Integer.parseInt(time[0]);
			int time2 = Integer.parseInt(time[1]);
			int time3 = Integer.parseInt(time[2]);
			tp = new Timestamp(date1, date2, date3, time1, time2, time3, 0);
		}
		return tp;
	}

	/**
	 * 返回日期和时间(没有秒) (格式:2006-07-05 22:10)
	 * 
	 * @param str
	 *            指定日期
	 * @return Timestamp 时间戳
	 */
	public static Timestamp datetimeHm(String str) {
		Timestamp tp = null;
		if (str.length() > 10) {
			String[] string = str.trim().split(" ");
			String[] date = string[0].split("-");
			String[] time = string[1].split(":");
			int date1 = Integer.parseInt(date[0]) - 1900;
			int date2 = Integer.parseInt(date[1]) - 1;
			int date3 = Integer.parseInt(date[2]);
			int time1 = Integer.parseInt(time[0]);
			int time2 = Integer.parseInt(time[1]);
			tp = new Timestamp(date1, date2, date3, time1, time2, 0, 0);
		}
		return tp;
	}

	/**
	 * 获得当前系统日期与本周一相差的天数
	 * 
	 * @return int
	 */
	private static int getMondayPlus() {
		Calendar calendar = Calendar.getInstance();
		// 获得今天是一周的第几天，正常顺序是星期日是第一天，星期一是第二天......
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 星期日是第一天
		return (dayOfWeek == 1) ? -6 : 2 - dayOfWeek;
	}

	/**
	 * 获得距当前时间所在某星期的周一的日期 例： 0-本周周一日期 -1-上周周一日期 1-下周周一日期
	 * 
	 * @param week
	 *            int 星期
	 * @return java.util.Date
	 */
	public static Date getMondayOfWeek(int week) {
		int mondayPlus = getMondayPlus(); // 相距周一的天数差
		GregorianCalendar current = new GregorianCalendar();
		current.add(GregorianCalendar.DATE, mondayPlus + 7 * week);
		return current.getTime();
	}

	/**
	 * 获得某日前后的某一天
	 * 
	 * @param date
	 *            java.util.Date
	 * @param day
	 *            int
	 * @return java.util.Date
	 */
	public static Date getDay(Date date, int day) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(GregorianCalendar.DATE, day);
		return c.getTime();
	}

	/**
	 * 获得距当前周的前后某一周的日期
	 * 
	 * @param week
	 *            int
	 * @return String[]
	 */
	public static String[] getDaysOfWeek(int week) {
		String[] days = new String[7];
		Date monday = getMondayOfWeek(week); // 获得距本周前或后的某周周一
		Timestamp t = new Timestamp(monday.getTime());
		days[0] = getStrDate(t);
		for (int i = 1; i < 7; i++) {
			t = new Timestamp(getDay(monday, i).getTime());
			days[i] = getStrDate(t);
		}
		return days;
	}

	/***
	 * MCC的UTC时间转换，MCC的UTC不是到毫秒的
	 * 
	 * @param utc
	 * @return java.util.Date
	 */
	public static Date mccUTC2Date(long utc) {
		Date d = new Date();
		d.setTime(utc * 1000); // 转成毫秒
		return d;
	}

	/**
	 * 将长时间格式字符串转换为时间( yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = (Date) formatter.parse(strDate, pos);
		if (strtodate == null) {
			formatter = new SimpleDateFormat(FORMAT_DAY, Locale.getDefault());
			strtodate = (Date) formatter.parse(strDate, pos);
		}
		return strtodate;
	}

	/**
	 * 将 yyyy-MM-dd HH:mm 格式字符串转换为时间
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateTime(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_MINUTE_ONE,
				Locale.getDefault());
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = (Date) formatter.parse(strDate, pos);
		if (strtodate == null) {
			formatter = new SimpleDateFormat(FORMAT, Locale.getDefault());
			strtodate = (Date) formatter.parse(strDate, pos);
		}
		return strtodate;
	}

	/**
	 * 根据输入的字符串返回日期字符串(2006-07-07 22:10 2006-07-07)
	 * 
	 * @param str
	 * @return
	 */
	public static String getStrDate(String str) {
		if (str.length() > 10) {
			String[] string = str.trim().split(" ");
			return string[0];
		} else {
			return getCurrentDate();
		}
	}

	/**
	 * 获取当前时间的指定格式字符串 (2006-07-07 22:10:10转为2006-07-07_221010)
	 * 
	 * @return
	 */
	public static String getStrDateTime() {
		Timestamp d = crunttime();
		return d.toString().substring(0, 19).replace(":", "").replace(" ", "_");
	}

	/**
	 * 根据日期字符串，返回今天，昨天或日期
	 * 
	 * @param str
	 * @return
	 */
	public static String getDayOrDate(Context context, String str) {
		if (str != null && !str.equals("")) {
			if (getNextDay(str, 0).equals(getCurrentDate())) {
				str = context.getString(R.string.today);
			} else if (getNextDay(str, 1).equals(getCurrentDate())) {
				str = context.getString(R.string.yesterday);
			}
		}
		return str;
	}

	/**
	 * 返回当前日期所在星期，2对应星期一
	 * 
	 * @return 星期
	 */
	public static int getMonOfWeek() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		return cal1.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取当前日期之前的日期字符串 (如 2007-04-15 前5月 就是 2006-11-15)
	 * 
	 * @param month
	 *            间隔月数
	 * @return 日期
	 */
	public static String getPreviousMonth(int month) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.add(Calendar.MONTH, -month);
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DAY,
				Locale.getDefault());
		return formatter.format(cal1.getTime());

	}

	/**
	 * 获取当前间隔年份
	 * 
	 * @param year
	 *            间隔年数
	 * @return 年
	 */
	public static String getStrYear(Context context, int year) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.add(Calendar.YEAR, -year);
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YEAR,
				Locale.getDefault());
		return formatter.format(cal1.getTime())
				+ context.getString(R.string.years);
	}

	/**
	 * 比较两个日期前后 可以大于或等于
	 * 
	 * @param starDate
	 *            起始时间
	 * @param endDate
	 *            结束日期
	 * @return true:endDate>startDate false:endDate<startDate
	 */
	public static boolean compareTwoDays(String starDate, String endDate) {
		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();
		cal_start.setTime(parseDate(starDate));
		cal_end.setTime(parseDate(endDate));
		return cal_end.after(cal_start);
	}
	
	/**
	 * 比较两个日期前后 可以大于或等于
	 * @param starDate 起始时间
	 * @param endDate 结束日期
	 * @param format 日期格式
	 * @return true:endDate>startDate false:endDate<startDate
	 */
	public static boolean compareTwoDays(String starDate, String endDate, String format) {
		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();
		cal_start.setTime(parseDate(starDate,format));
		cal_end.setTime(parseDate(endDate,format));
		return cal_end.after(cal_start);
	}
	
	/**
	 * @Description 比较两个日期大小
	 * @param startTime
	 * @param endTime
	 * @param format
	 *            时间要为24小时制，否则大于12点时会判断错误
	 * @return boolean true:开始时间小于结束时间，false：其他
	 */
	public static boolean compareDate(String startTime, String endTime,
			SimpleDateFormat format) {
		if (format == null) {
			format = new SimpleDateFormat(FORMAT,Locale.getDefault());
		}
		try {
			Date startDate = format.parse(startTime);
			Date endDate = format.parse(endTime);
			if (startDate.getTime() < endDate.getTime()) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 两日期间隔天数
	 * 
	 * @param d1
	 *            日期1
	 * @param d2
	 *            日期2
	 * @return 间隔天数
	 */
	public static int getDaysBetween(Calendar d1,
			Calendar d2) {
		if (d1.after(d2)) { // d1>d2
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR)
				- d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 得到两个日期之间的间隔年数
	 * 
	 * @param starDate
	 *            日期1
	 * @param endDate
	 *            日期2
	 * @return 间隔年数
	 */
	public static int dateDiffYear(String starDate, String endDate) {
		int result = 0;
		Calendar d1 = Calendar.getInstance();
		Calendar d2 = Calendar.getInstance();
		d1.setTime(parseDate(starDate));
		d2.setTime(parseDate(endDate));

		// 日期大小翻转
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int yy = d2.get(Calendar.YEAR) - d1.get(Calendar.YEAR);
		int mm = d2.get(Calendar.MONTH) - d1.get(Calendar.MONTH);
		if (mm < 0) {
			result = yy - 1;
		}
		if (mm > 0) {
			result = yy;
		}
		if (mm == 0) {
			if ((d2.getTimeInMillis() - d1.getTimeInMillis()) >= 0) {
				result = yy;
			} else {
				result = yy - 1;
			}
		}
		return result;
	}

	/**
	 * 获取指定日期和当前日期的间隔年数
	 * 
	 * @param starDate
	 *            指定日期
	 * @return 间隔年数
	 */
	public static int getAgeByBirth(String starDate) {
		return dateDiffYear(starDate, getCurrentDate());
	}
	
	/**
	 * 获取上个月年月
	 * 
	 * @param format
	 *            显示格式
	 * @return
	 */
	public static String getLastMonthDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return sdf.format(cal.getTime());
	}
}
