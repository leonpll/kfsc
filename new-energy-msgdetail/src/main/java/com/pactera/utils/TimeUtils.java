package com.pactera.utils;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间相关操作工具类
 * 
 * @author：Peill
 * @since：2017年8月21日 下午2:00:22
 */
public class TimeUtils {
	
	/**
	 * 目录：
	 * millis2String            : 将时间戳转为时间字符串
	 * string2Millis            : 将时间字符串转为时间戳
	 * string2Date              : 将时间字符串转为Date类型
	 * date2String              : 将Date类型转为时间字符串
	 * date2Millis              : 将Date类型转为时间戳
	 * millis2Date              : 将时间戳转为Date类型
	 * dateReckon               : 日期+-天数
	 * getNowTimeMills          : 获取当前毫秒时间戳
	 * getNowTimeString         : 获取当前时间字符串
	 * getNowTimeDate           : 获取当前Date
	 * getWeek, getWeekIndex    : 获取星期
	 * getWeekOfMonth           : 获取月份中的第几周
	 * getWeekOfYear            : 获取年份中的第几周
	 * getMonthIndex            : 获取年份中的第几月
	 * parseMillisecone         : 时间差计算 return string 0天0时11分55秒
	 * getDifference			：时间差计算 return int
	 * getChineseZodiac         : 获取生肖
	 * getZodiac                : 获取星座
	 * 
	 * 注意：SimpleDateFormat不是线程安全的，线程安全需用{@code ThreadLocal<SimpleDateFormat>}
	 */
	
	public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	public static final SimpleDateFormat DEFAULT_SDF_CN = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.getDefault());
	
	public static final SimpleDateFormat DEFAULT_SIMPLE_SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	
	public static final SimpleDateFormat DEFAULT_SIMPLE_SDF_CN = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());

	public static final String DEFAULT_TIME = "yyyy-MM-dd HH:mm:ss";
	
	public static final String TIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static final String DEFAULT_TIME_CN = "yyyy年MM月dd日 HH时mm分ss秒";

	public static final String DEFAULT_SIMPLE_TIME = "yyyy-MM-dd";
	
	public static final String DEFAULT_SIMPLE_TIME_NOSS = "yyyy-MM-dd HH:mm";
	
	public static final String DEFAULT_SIMPLE_TIME_CN = "yyyy年MM月dd日";
	
	public static final long DAY = 1000 * 60 * 60 * 24;
		
	public static final long HOUR = 1000 * 60 * 60;
	
	public static final long MINUTE = 1000 * 60;
	
	public static final long SECOND = 1000;
	/**
	 * 将时间戳转为时间字符串 格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return 时间字符串
	 */
	public static String millis2String(long millis) {
		return DEFAULT_SDF.format(new Date(millis));
	}

	/**
	 * 将时间戳转为时间字符串 格式为pattern
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @param pattern
	 *            时间格式
	 * @return 时间字符串
	 */
	public static String millis2String(long millis, String pattern) {
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(millis));
	}

	//获取当前整点时间戳
	public static Long getNowWholeTime(){
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.MINUTE,0);
		instance.set(Calendar.SECOND,0);
		return instance.getTime().getTime();
	}

	//根据传的时间戳获取上一个小时的时间戳
	public static Long getLastHourTime(Long time){
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		instance.add(Calendar.HOUR_OF_DAY,-1);
		return instance.getTime().getTime();
	}

	/**
	 * 将时间字符串转为时间戳
	 * time格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 *            时间字符串
	 * @return 毫秒时间戳
	 */
	public static long string2Millis(String time) {
		return string2Millis(time, DEFAULT_TIME);
	}

	/**
	 * 将时间字符串转为时间戳
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 毫秒时间戳
	 */
	public static long string2Millis(String time, String pattern) {
		try {
			return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 将时间字符串转为Date类型
 	 * time格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 *            时间字符串
	 * @return Date类型
	 */
	public static Date string2Date(String time) {
		return string2Date(time, DEFAULT_TIME);
	}
	
	/**
	 * 将时间字符串转为Date类型
	 * 
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return Date类型
	 */
	public static Date string2Date(String time, String pattern) {
		return new Date(string2Millis(time, pattern));
	}

	/**
	 * 将Date类型转为时间字符串 格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 *            Date类型时间
	 * @return 时间字符串
	 */
	public static String date2String(Date date) {
		return date2String(date, DEFAULT_TIME);
	}

	
	/**
	 * 将Date类型转为时间字符串 格式为pattern
	 *
	 * @param date
	 *            Date类型时间
	 * @param pattern
	 *            时间格式
	 * @return 时间字符串
	 */
	public static String date2String(Date date, String pattern) {
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
	}

	/**
	 * 
	 * @Title: dateToStringCNDate 
	 * @author: 阴展 13426272161@139.com
	 * @Description: yyyy年MM月dd日 
	 * @param: @param date
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String dateToStringCNDate(Date date) {
		return date2String(date, DEFAULT_SIMPLE_TIME_CN);
	}

	/**
	 *
	 * @Title: dateToStringCNDate
	 * @author: 阴展 13426272161@139.com
	 * @Description: yyyy年MM月dd日
	 * @param: @param date
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String dateToStringCNDate2(Date date) {
		return date2String(date, DEFAULT_SIMPLE_TIME);
	}
	/**
	 * 
	 * @Title: dateToStringCNTime 
	 * @author: 阴展 13426272161@139.com
	 * @Description: yyyy年MM月dd日 HH时mm分ss秒
	 * @param: @param date
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String dateToStringCNTime(Date date) {
		return date2String(date, DEFAULT_TIME_CN);
	}
	
	/**
	 * 将Date类型转为时间戳
	 *
	 * @param date
	 *            Date类型时间
	 * @return 毫秒时间戳
	 */
	public static long date2Millis(Date date) {
		return date.getTime();
	}

	/**
	 * 将时间戳转为Date类型
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return Date类型时间
	 */
	public static Date millis2Date(long millis) {
		return new Date(millis);
	}

	
	/**
	 * 日期+-天数
	 * 
	 * @param date
	 * @param num 增加的天数，整数为+，负数-
	 * @return Date
	 */
	public static Date dateReckon(Date date, Integer num) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, num);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}
	
	/**
	 * 日期+-天数
	 * 
	 * @param millis 时间戳
	 * @param num 增加的天数，整数为+，负数-
	 * @return Date
	 */
	public static Date dateReckon(long millis, Integer num) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(millis2Date(millis));
		calendar.add(Calendar.DATE, num);// 把日期往后增加一天.整数往后推,负数往前移动
		Date date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}
	
	/**
	 * 日期+-天数
	 * 
	 * @param time 时间格式字符转
	 * @param num 增加的天数，整数为+，负数-
	 * @return Date
	 */
	public static Date dateReckon(String time, Integer num) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(string2Date(time));
		calendar.add(Calendar.DATE, num);// 把日期往后增加一天.整数往后推,负数往前移动
		Date date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}
	
	
	/**
	 * 获取当前毫秒时间戳
	 *
	 * @return 毫秒时间戳
	 */
	public static long getNowTimeMills() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当前时间字符串
	 * 格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @return 时间字符串
	 */
	public static String getNowTimeString() {
		return millis2String(System.currentTimeMillis(), DEFAULT_TIME);
	}
	
	/**
     * 获取当前Date
     *
     * @return Date类型时间
     */
    public static Date getNowTimeDate() {
        return new Date();
    }
    
	/**
	 * 获取星期
	 * time格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 *            时间字符串
	 * @return 星期
	 */
	public static String getWeek(String time) {
		return getWeek(string2Date(time, DEFAULT_TIME));
	}

	/**
	 * 获取星期
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 星期
	 */
	public static String getWeek(String time, String pattern) {
		return getWeek(string2Date(time, pattern));
	}

	/**
	 * 获取星期
	 *
	 * @param date
	 *            Date类型时间
	 * @return 星期
	 */
	public static String getWeek(Date date) {
		return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
	}

	/**
	 * 获取星期
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return 星期
	 */
	public static String getWeek(long millis) {
		return getWeek(new Date(millis));
	}

	/**
	 * 获取星期
	 * 注意：周日的Index才是1，周六为7
	 * time格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 *            时间字符串
	 * @return 1...5
	 */
	public static int getWeekIndex(String time) {
		return getWeekIndex(string2Date(time, DEFAULT_TIME));
	}

	/**
	 * 获取星期
	 * 注意：周日的Index才是1，周六为7
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 1...7
	 */
	public static int getWeekIndex(String time, String pattern) {
		return getWeekIndex(string2Date(time, pattern));
	}

	/**
	 * 获取星期
	 * 注意：周日的Index才是1，周六为7
	 *
	 * @param date
	 *            Date类型时间
	 * @return 1...7
	 */
	public static int getWeekIndex(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	
	
	/**
	 * 获取星期
	 * 注意：周日的Index才是1，周六为7
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return 1...7
	 */
	public static int getWeekIndex(long millis) {
		return getWeekIndex(millis2Date(millis));
	}
	
	/**
	 * 获取月份
	 * 注意：下标从0开始;
	 *
	 * @param pattern 格式
	 *            time类型时间
	 * @return 0...11
	 */
	public static int getMonthIndex(String time,String pattern) {
		return getMonthIndex(string2Date(time, pattern));
	}
	
	/**
	 * 获取月份
	 * 注意：下标从0开始;
	 *
	 * @param time
	 *            time类型时间yyyy-MM-dd HH:mm:ss
	 * @return 0...11
	 */
	public static int getMonthIndex(String time) {
		return getMonthIndex(string2Date(time, DEFAULT_TIME));
	}
	
	/**
	 * 获取月份
	 * 注意：下标从0开始;
	 *
	 * @param date
	 *            时间戳
	 * @return 0...11
	 */
	public static int getMonthIndex(long date) {
		return getMonthIndex(millis2Date(date));
	}
	
	/**
	 * 获取月份
	 * 注意：下标从0开始;
	 *
	 * @param date
	 *            Date类型时间
	 * @return 0...11
	 */
	public static int getMonthIndex(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}
	
	
	/**
	 * 获取年份
	 * @param time
	 *            time类型时间yyyy-MM-dd HH:mm:ss
	 */
	public static int getYear(String time) {
		return getYear(string2Date(time, DEFAULT_TIME));
	}
	
	/**
	 * 获取年份
	 * @param pattern
	 *            time类型时间yyyy-MM-dd HH:mm:ss
	 */
	public static int getYear(String time,String pattern) {
		return getYear(string2Date(time, pattern));
	}
	
	/**
	 * 获取年份
	 * @param date
	 *            时间戳
	 */
	public static int getYear(long date) {
		return getYear(millis2Date(date));
	}
	
	/**
	 * 获取年份
	 * @param date
	 *            Date类型时间
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 获取月份中的第几周
	 * <p>
	 * 注意：国外周日才是新的一周的开始
	 * </p>
	 * <p>
	 * time格式为yyyy-MM-dd HH:mm:ss
	 * </p>
	 *
	 * @param time
	 *            时间字符串
	 * @return 1...5
	 */
	public static int getWeekOfMonth(String time) {
		return getWeekOfMonth(string2Date(time, DEFAULT_TIME));
	}

	/**
	 * 获取月份中的第几周
	 * 注意：国外周日才是新的一周的开始
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 1...5
	 */
	public static int getWeekOfMonth(String time, String pattern) {
		return getWeekOfMonth(string2Date(time, pattern));
	}

	/**
	 * 获取月份中的第几周
	 * 注意：国外周日才是新的一周的开始
	 *
	 * @param date
	 *            Date类型时间
	 * @return 1...5
	 */
	public static int getWeekOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取月份中的第几周
	 * 注意：国外周日才是新的一周的开始
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return 1...5
	 */
	public static int getWeekOfMonth(long millis) {
		return getWeekOfMonth(millis2Date(millis));
	}

	/**
	 * 获取年份中的第几周
	 * 注意：国外周日才是新的一周的开始
	 * time格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 *            时间字符串
	 * @return 1...54
	 */
	public static int getWeekOfYear(String time) {
		return getWeekOfYear(string2Date(time, DEFAULT_TIME));
	}

	/**
	 * 获取年份中的第几周
	 * 注意：国外周日才是新的一周的开始
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 1...54
	 */
	public static int getWeekOfYear(String time, String pattern) {
		return getWeekOfYear(string2Date(time, pattern));
	}

	/**
	 * 获取年份中的第几周
	 * 注意：国外周日才是新的一周的开始
	 *
	 * @param date
	 *            Date类型时间
	 * @return 1...54
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取年份中的第几周
	 * 注意：国外周日才是新的一周的开始
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return 1...54
	 */
	public static int getWeekOfYear(long millis) {
		return getWeekOfYear(millis2Date(millis));
	}
	
	private static final String[] CHINESE_ZODIAC = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

	/**
	 * 获取生肖
	 * time格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 *            时间字符串
	 * @return 生肖
	 */
	public static String getChineseZodiac(String time) {
		return getChineseZodiac(string2Date(time, DEFAULT_TIME));
	}

	/**
	 * 获取生肖
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 生肖
	 */
	public static String getChineseZodiac(String time, String pattern) {
		return getChineseZodiac(string2Date(time, pattern));
	}

	/**
	 * 获取生肖
	 *
	 * @param date
	 *            Date类型时间
	 * @return 生肖
	 */
	public static String getChineseZodiac(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
	}

	/**
	 * 获取生肖
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return 生肖
	 */
	public static String getChineseZodiac(long millis) {
		return getChineseZodiac(millis2Date(millis));
	}

	/**
	 * 获取生肖
	 *
	 * @param year
	 *            年
	 * @return 生肖
	 */
	public static String getChineseZodiac(int year) {
		return CHINESE_ZODIAC[year % 12];
	}

	private static final String[] ZODIAC = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座",
			"射手座", "魔羯座" };
	private static final int[] ZODIAC_FLAGS = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22 };

	/**
	 * 获取星座
	 * time格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 *            时间字符串
	 * @return 生肖
	 */
	public static String getZodiac(String time) {
		return getZodiac(string2Date(time, DEFAULT_TIME));
	}

	/**
	 * 获取星座
	 * time格式为pattern
	 *
	 * @param time
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 生肖
	 */
	public static String getZodiac(String time, String pattern) {
		return getZodiac(string2Date(time, pattern));
	}

	/**
	 * 获取星座
	 *
	 * @param date
	 *            Date类型时间
	 * @return 星座
	 */
	public static String getZodiac(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return getZodiac(month, day);
	}

	/**
	 * 获取星座
	 *
	 * @param millis
	 *            毫秒时间戳
	 * @return 星座
	 */
	public static String getZodiac(long millis) {
		return getZodiac(millis2Date(millis));
	}

	/**
	 * 获取星座
	 *
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 星座
	 */
	public static String getZodiac(int month, int day) {
		return ZODIAC[day >= ZODIAC_FLAGS[month - 1] ? month - 1 : (month + 10) % 12];
	}
	
	/**
	 * 时间差计算
	 * 
	 * @param time 当前时间
	 * @return string 0天0时11分55秒
	 */
	public static String parseMillisecone(Date time) {
		
		return parseMillisecone(countTimeDiff(date2Millis(time)));
	}
	
	/**
	 * 时间差计算
	 * 
	 * @param time 当前时间
	 * @return string 0天0时11分55秒
	 */
	public static String parseMillisecone(String time) {
		
		return parseMillisecone(countTimeDiff(string2Millis(time)));
	}
	
	/**
	 * 时间差计算
	 * 
	 * @param time 当前时间
	 * @return string 0天0时11分55秒
	 */
	public static String parseMillisecone(String time,String pattern) {
		
		return parseMillisecone(countTimeDiff(string2Millis(time,pattern)));
	}
	
	/**
	 * 时间差计算
	 * 
	 * @param millisecond
	 * @return string 0天0时11分55秒
	 */
	public static String parseMillisecone(long millisecond) {
		String time = null;
		try {
			long yushu_day = millisecond % DAY;
			long yushu_hour = millisecond % (DAY * HOUR);
			long yushu_minute = millisecond % (DAY * HOUR * MINUTE);
			@SuppressWarnings("unused")
			long yushu_second = millisecond % (DAY * HOUR * MINUTE * SECOND);
			if (yushu_day == 0) {
				return (millisecond / DAY) + "天";
			} else {
				if (yushu_hour == 0) {
					return (millisecond / DAY) + "天" + (yushu_day / HOUR) + "时";
				} else {
					if (yushu_minute == 0) {
						return (millisecond / DAY) + "天" + (yushu_day / HOUR) + "时" + (yushu_hour / MINUTE) + "分";
					} else {
						return (millisecond / DAY) + "天" + (yushu_day / HOUR) + "时" + (yushu_hour / MINUTE) + "分"
								+ (yushu_minute / SECOND) + "秒";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 时间差计算
	 * 
	 * @param time 当前时间
	 * @return string 0天0时11+
	 * 分55秒
	 */
	public static long getDifference(Date time, int f) {
		
		return getDifference(countTimeDiff(date2Millis(time)),f);
	}
	
	/**
	 * 时间差计算
	 * 
	 * @param time 当前时间
	 * @return string 0天0时11分55秒
	 */
	public static long getDifference(String time, int f) {
		
		return getDifference(countTimeDiff(string2Millis(time)),f);
	}
	
	/**
	 * 时间差计算
	 * 
	 * @param time 当前时间
	 * @return string 0天0时11分55秒
	 */
	public static long getDifference(String time,String pattern, int f) {
		
		return getDifference(countTimeDiff(string2Millis(time,pattern)),f);
	}
	
	
	/**
	 * 计算时间与当前时间相差多久
	 * 
	 * @param time
	 * @param f
	 * 时间差的形式0:秒,1:分种,2:小时,3:天
	 * @return 返回int类型长度，多出则+1
	 */
	public static long getDifference(long time, int f) {
		long result = 0;
		long yushu_day = time % DAY;
		long yushu_hour = time % (DAY * HOUR);
		long yushu_minute = time % (DAY * HOUR * MINUTE);
		long yushu_second = time % (DAY * HOUR * MINUTE * SECOND);
		try {
			// 日期相减获取日期差X(单位:毫秒)
			long millisecond = System.currentTimeMillis() - time;
			/**
			 * Math.abs((int)(millisecond/1000)); 绝对值 1秒 = 1000毫秒
			 * millisecond/1000 --> 秒 millisecond/1000*60 - > 分钟
			 * millisecond/(1000*60*60) -- > 小时 millisecond/(1000*60*60*24) -->
			 * 天
			 */
			switch (f) {
			case 0: {
				if (yushu_second != 0) {
					return (millisecond / SECOND) + 1;
				}
				return (millisecond / SECOND);
			}
			case 1: {
				if (yushu_minute != 0) {
					return (millisecond / MINUTE) + 1;
				}
				return (millisecond / MINUTE);
			}
			case 2: {
				if (yushu_hour != 0) {
					return (millisecond / HOUR) + 1;
				}
				return (millisecond / HOUR);
			} // hour
			case 3: {
				if (yushu_day != 0) {
					return (millisecond / DAY) + 1;
				}
				return (millisecond / DAY);
			} // day
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 计算时间差
	 * @param time 时间戳
	 * @return 时间差的时间戳
	 */
	public static long countTimeDiff(long time) {
		
		return System.currentTimeMillis() - time;
	}


	/**
	 * 获取当前时间的年月日
	 * @param time 时间格式
	 * @return 年月日：yyyy-MM-dd
	 */
	public static String getDate(Long time){
		String s = millis2String(time);
		return getDate(s);
	}

	/**
	 * 获取当前时间的年月日
	 * @param date 时间格式 yyyy-MM-dd HH:mm:ss
	 * @return 年月日：yyyy-MM-dd
	 */
	public static String getDate(String date){
		return date.substring(0,10);
	}
	/**
	 * 获取当前时间的时分
	 * @param date 时间格式 yyyy-MM-dd HH:mm:ss
	 * @return 年月日：HH:mm
	 */
	public static String getHourMinute(String date) {
		return date.substring(11, 16);
	}

	/**
	 * 获取相差得天数
	 * @param  时间格式 yyyy-MM-dd HH:mm:ss
	 * @return int
	 */
	public static int getDayDifference(String startStr,String endStr){
		Date start = string2Date(startStr, DEFAULT_SIMPLE_TIME);
		Date end = string2Date(endStr,DEFAULT_SIMPLE_TIME);
		long difference = end.getTime() - start.getTime();
		int day = (int)(difference / (1000 * 60 * 60 * 24));
		return day;
	}

	/**
	 * 获取当前时间的年月日
	 * @param date 时间格式 yyyy-MM-dd HH:mm:ss
	 * @return 小时：HH
	 */
	public static String getHour(String date){
		return date.substring(11,13);
	}

	/**
	 * 获取当前时间的年月日
	 * @return 年月日：yyyy-MM-dd
	 */
	public static String getNowDate() {
		String nowTimeString = getNowTimeString();
		return getDate(nowTimeString);
	}

	/**
	 * 获取当前小时
	 * @return 小时：HH
	 */
	public static String getNowHour() {
		String nowTimeString = getNowTimeString();
		return getHour(nowTimeString);
	}
	/**
	 * 获取当前时间的前一天的年月日
	 * @return 返回年月日格式：yyyy-MM-dd
	 */
	public static String getLastDate() {
		String dateStr = date2String(dateReckon(getNowTimeString(), -1));
		return getDate(dateStr);
	}

	/**
	 * 获取指定时间的前一天的年月日
	 ** @param dateStr 时间 格式为yyyy-MM-dd
	 * @return 返回年月日格式：yyyy-MM-dd
	 */
	public static String getLastDate(String dateStr){
		Date date = string2Date(dateStr, DEFAULT_SIMPLE_TIME);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH,-1);
		Date time = cal.getTime();
		String date2String = date2String(time);
		return getDate(date2String);
	}
    /**
     * 根据long类型的date获取昨天日期
     ** @param date 时间 格式为yyyy-MM-dd
     * @return 返回年月日格式：yyyy-MM-dd
     */
    public static String getLastDate(long date){
        String dateStr = millis2String(date);
        return getLastDate(dateStr);
    }

    //根据时间戳获取上一个小时时间
    public static String getLastHourDate(long date){
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(date);
		instance.add(Calendar.HOUR_OF_DAY,-1);
		return millis2String(instance.getTimeInMillis());
	}

	/**
	 * 获取指定时间向前推一周内的年月日
	 ** @param dateStr 时间 格式为yyyy-MM-dd
	 * @return 返回年月日格式：yyyy-MM-dd
	 */
	public static List<String> getOneWeekDate(String dateStr){
		ArrayList<String> result = new ArrayList<>();
		Date date = string2Date(dateStr, DEFAULT_SIMPLE_TIME);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		result.add(dateStr);
		for(int i = 0 ;i<6 ; i++){
			cal.add(Calendar.DAY_OF_MONTH,-1);
			Date time = cal.getTime();
			String date2String = date2String(time);
			result.add(getDate(date2String));
		}
		return result;
	}
	/**
	 * 根据时间获取上一个小时的字符串格式
	 * @param dateStr 时间 格式为yyyy-MM-dd HH:mm:ss
	 * @return 返回小时字符串：HH
	 */
	public static String getLastHour(String dateStr) {
		Date date = string2Date(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY,-1);
		Date time = cal.getTime();
		String lastHour = getHour(date2String(time));
		return lastHour;
	}


	/**
	 * 时间获取上一个小时的字符串格式
	 * @param date 时间 格式为yyyy-MM-dd
	 * @param hour 时间 格式为HH
	 * @return 返回小时字符串：HH
	 */
	public static String getLastHour(String date,String hour){
		return getLastHour(date+" "+hour+":00:00");
	}

	/**
	 * 生成一个小时列表
	 * @return 返回小时列表，值为0
	 */
	public static JSONObject initHourList(){
		JSONObject hourVolumes = new JSONObject(true);
		for (int i = 0; i < 24; i++) hourVolumes.put(String.format("%02d",i), 0);
		return hourVolumes;
	}

	//根据传过来的日期计算本周日和上周日
	public static List<String> sundayAndLastSunDay(String date){
		ArrayList<String> dates = new ArrayList<>();
		Calendar cal = getNowSunday(date);
		dates.add(DEFAULT_SIMPLE_SDF.format(cal.getTime()));
		cal.add(Calendar.DATE, -7);
		dates.add(DEFAULT_SIMPLE_SDF.format(cal.getTime()));
		return dates;
	}

	/**
	 * 根据日期得到本周周日时间
	 * @return 值为yyyy-MM-dd
	 */
	public static String getNowSundayStr(String date){
		Calendar weekDay = getNowSunday(date);
		return (DEFAULT_SIMPLE_SDF.format(weekDay.getTime()));
	}

	/**
	 * 根据日期得到本周周日时间
	 * @return
	 */
	public static long getNowSundayLong(String date){
		Calendar weekDay = getNowSunday(date);
		return weekDay.getTime().getTime();
	}

	/**
	 * 根据日期得到本周周日时间的Calendar格式
	 * @return
	 */
	public static Calendar getNowSunday(String date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(TimeUtils.string2Date(date, TimeUtils.DEFAULT_SIMPLE_TIME));
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if(1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day+6);
		return cal;
	}
	
	public static Date UTCStringToDate(String UTCDatestr) {
		UTCDatestr = UTCDatestr.replace("Z", " UTC");//注意是空格+UTC
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
		Date d = null;
		try {
		   d = format.parse(UTCDatestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static String UTCStringToDateString(String UTCDatestr) {
		Date d = UTCStringToDate(UTCDatestr);
		String dateString = "";
		if(null!=d) {
			dateString =  TimeUtils.date2String(d);
		}
		return dateString;
	}
}
