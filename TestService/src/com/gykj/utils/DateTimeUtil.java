package com.gykj.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类
 * @author xianyl
 *
 */
public class DateTimeUtil {

	// 几种日期样式
	public static final String formatter_no_separator = "yyyyMMddHHmmss";
	public static final String formatter_long = "yyyy-MM-dd HH:mm:ss";
	public static final String formatter_time = "HH:mm:ss";
	public static final String formatter_short = "yyyy-MM-dd";
	public static final String formatter_long_zh = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String formatter_short_zh = "yyyy年MM月dd日";

	public DateTimeUtil() {
	}

	// 得到现在时间后几天的时间.如days=20,结果20070725093258656
	public static String getRelativeDateToString(int days) {
		Calendar c = Calendar.getInstance();
		c.set(5, c.get(5) + days);
		StringBuffer sb = new StringBuffer(17);
		sb.append(c.get(1));
		int tmp[] = { c.get(2) + 1, c.get(5), c.get(11), c.get(12), c.get(13),
				c.get(14) };
		for (int i = 0; i < tmp.length - 1; i++)
			sb.append(tmp[i] >= 10 ? "" : "0").append(tmp[i]);

		if (tmp[tmp.length - 1] < 10)
			sb.append("0");
		if (tmp[tmp.length - 1] < 100)
			sb.append("0");
		sb.append(tmp[tmp.length - 1]);
		return sb.toString();
	}
	
	
	/** 
	   * 得到几天前的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */  
	public static Date getDateBefore(int day){  
	   Calendar now =Calendar.getInstance();  
	   now.setTimeInMillis(getCurrenteDate().getTime());
	   now.set(Calendar.DATE,now.get(Calendar.DATE)-day);  
	   return now.getTime();  
	}  
	
	// 得到现在时间后几天的时间.
	public static Date getRelativeDay(int days) {
		Calendar c = Calendar.getInstance();  
	    c.setTimeInMillis(getCurrenteDate().getTime());
		c.add(Calendar.DATE, days);//n天后的日期
		return new Date(c.getTimeInMillis()); //将c转换成Date
	}
	// 得到现在时间后几分钟的时间.
	public static Date getRelativeMinute(int minute) {
		Calendar c = Calendar.getInstance();  
		c.setTimeInMillis(getCurrenteDate().getTime());
		c.add(Calendar.MINUTE, minute);//n分钟后的时间
		return new Date(c.getTimeInMillis()); //将c转换成Date
	}

	// 得到当前的时间.
	public static Date getCurrenteDate() {
		return new Date(); 
	}
	
	// 得到当前的时间.
	public static String getDate() {
		return formatDate(getCurrenteDate(), formatter_long); 
	}
	
	// t1和t2的差
	public static long compare(String t1, String t2) {
		return Long.valueOf(t1).longValue() - Long.valueOf(t2).longValue();
	}

	// 得到Year，time是Date类型
	public static int getYear(String time) {
		return Integer.valueOf(time.substring(0, 4)).intValue();
	}

	// 得到Month，time是Date类型
	public static int getMonth(String time) {
		return Integer.valueOf(time.substring(4, 6)).intValue();
	}

	// 得到Date，time是Date类型
	public static int getDate(String time) {
		return Integer.valueOf(time.substring(6, 8)).intValue();
	}

	// 得到Hour，time是Date类型
	public static int getHour(String time) {
		return Integer.valueOf(time.substring(8, 10)).intValue();
	}

	// 得到Minute，time是Date类型
	public static int getMinute(String time) {
		return Integer.valueOf(time.substring(10, 12)).intValue();
	}

	// 得到Second，time是Date类型
	public static int getSecond(String time) {
		return Integer.valueOf(time.substring(12, 14)).intValue();
	}

	// 得到MilliSencond，time是Date类型
	public static int getMilliSencond(String time) {
		return Integer.valueOf(time.substring(14, 17)).intValue();
	}

	// 将time(13位)格式化成指定string样式;如：1183605002016l,"yyyy年MM月dd日 HH时mm分ss秒"
	public static String getLongToString(long time, String string) {
		return (new SimpleDateFormat(string)).format(new Date(time));
	}

	// 把myDate(Thu Jul 05 10:06:37 CST 2007格式)转化成指定string如(yyyy年MM月dd日
	// HH时mm分ss秒)
	public static String formatDate(Date myDate, String string) {
		SimpleDateFormat formatter = new SimpleDateFormat(string);
		String time = formatter.format(myDate);
		return time;
	}

	// 将time指定格式string转化成long类型
	public static long getStrToLong(String time, String string) {
		try {
			return (new SimpleDateFormat(string)).parse(time).getTime();
		} catch (ParseException ex) {
			ex.getStackTrace();
			return 0L;
		}
	}

	// 将time指定string样式输出,两参数要匹配.如"2007-07-25 09:26:57","yyyy-MM-dd HH:mm:ss"
	public static String getShowFormat(String time, String string) {
		SimpleDateFormat temp = new SimpleDateFormat(string);
		try {
			if (time == null || time.equals(""))
				time = temp.format(new Date());
			else {
				time = temp.format(temp.parse(time));
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	 /** 
     * Hibernate日期查询工具 
     * @param dateStr 日期字符串，YYYY-MM-DD格式 
     * @return  日期数组，Date[0]=YYYY-MM-DD 0:00:00,Date[1]=YYYY-MM-DD 23:59:59 
     */  
    public static Date[] hibernateDateHelper(String dateStr){  
        Date[] dateArray = new Date[2];
        Date date = parseDate(dateStr, formatter_short);
        //如果转化失败,返回null
        if(date == null) return null;
        dateArray[0] = getDateStart(dateArray[0]);  
        dateArray[1] = getDateEnd(dateArray[0]);  
        return dateArray;  
    }  
    
    /** 
     * 格式化日期 
     * @param dateStr  String 字符型日期 
     * @param format   String 格式 
     * @return Date    日期 
     */  
    public static Date parseDate(String dateStr, String format) {
    	Date date = null;
        try {
        	new SimpleDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return date;
    }  
      
    /** 
     * 获取日期最早时间，如传入2015-07-10，返回2015-07-10 0:00:00 
     * @param date 
     * @return 
     */  
    public static Date getDateStart(Date d){
    	Calendar cal = Calendar.getInstance(); 
        cal.setTime(d);  
        cal.set(Calendar.HOUR, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        return cal.getTime();  
    }  
      
    /** 
     * 获取日期最晚时间，如传入2015-07-10，返回2015-07-10 23:59:59 
     * @param date 
     * @return 
     */  
    public static Date getDateEnd(Date d){ 
    	Calendar cal = Calendar.getInstance(); 
        cal.setTime(d);  
        cal.set(Calendar.HOUR, 23);  
        cal.set(Calendar.MINUTE, 59);  
        cal.set(Calendar.SECOND, 59);  
        return cal.getTime();  
    }

    
    
	public static String getTime() {
		return formatDate(getCurrenteDate(), formatter_time);
	}  
	
    
    
	
}