package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    public static final String FORMAT_TEXT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 比较时间date1是否在时间date2之前
     * @param date1
     * @param date2
     * @return
     */
    public static boolean before(Date date1, Date date2){
        return date1.compareTo(date2) < 0;
    }

    /**
     * 比较时间date1是否和时间date2相等
     * @param date1
     * @param date2
     * @return
     */
    public static boolean equals(Date date1, Date date2){
        return date1.compareTo(date2) == 0;
    }

    /**
     * 比较时间date1是否和时间date2相等
     * @param date1
     * @param date2
     * @return
     */
    public static boolean afier(Date date1, Date date2){
        return date1.compareTo(date2) > 0;
    }

    /**
     * 使用默认格式,格式化时间
     * @param date
     * @return
     */
    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TEXT_DEFAULT);
        return sdf.format(date);
    }

    /**
     * 使用指定格式,格式化时间
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 使用默认格式,将字符串日期解析为时间类型
     * @param strDate
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate,FORMAT_TEXT_DEFAULT);
    }

    /**
     * 使用指定格式,将字符串日期解析为时间类型
     * @param strDate
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parse(String strDate,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("日期解析出错");
        }
        return date;
    }

    /**
     * Date类型到日历格式的转换
     * @param date  date时间
     * @param day   Calendar.HOUR_OF_DAY
     * @param minute    Calendar.MINUTE
     * @param second    Calendar.SECOND
     * @param ms    Calendar.MILLISECOND
     * @return
     */
    public static Calendar date2Calender(Date date,int day,int minute,int second,int ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,day);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, ms);
        return calendar;
    }
}
