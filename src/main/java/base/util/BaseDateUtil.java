package base.util;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: liuwentao@kongzhong.com
 * Date: 2008-3-29 18:28:12
 * <p/>
 * 说明 ：
 */
public class BaseDateUtil {
    public static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";//默认日期格式
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";


    //==============================最新整理============================

    /**
     * @param date
     * @return
     */
    public static String getFormatStringDay(Date date) {
        return getFormatString(date, DATE_FORMAT_DAY);
    }

    /**
     * @param date
     * @return
     */
    public static String getFormatStringSecond(Date date) {
        return getFormatString(date, DATE_FORMAT_SECOND);
    }

    /**
     * 日期 按格式 输出成字符串
     *
     * @param date
     * @param fmt
     * @return
     */
    public static String getFormatString(Date date, String fmt) {
        if (date == null) {
            return "- -";
        }
        if (fmt == null || fmt.trim().equals("")) {
            fmt = DATE_FORMAT_SECOND;
        }

        DateFormat dateFormat = new SimpleDateFormat(fmt);
        return dateFormat.format(date);
    }

    public static String getFormatSecond(Date date) {
        if (date == null) {
            return "- -";
        }

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SECOND);
        return dateFormat.format(date);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String getFormatDay(Date date) {
        if (date == null) {
            return "- -";
        }

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DAY);
        return dateFormat.format(date);
    }


    public static Date getDate(HttpServletRequest request, String paramName) {
        String dateStr = request.getParameter(paramName);
        Date date = getDateByFormatString(dateStr,DATE_FORMAT_DAY);
        return date;
    }


    /**
     * 将字符串转成时间
     *
     * @param dateStr
     * @param fmt
     * @return
     */
    public static Date getDateByFormatString(String dateStr, String fmt) {
        if (BaseStringUtil.isBlank(dateStr)) {
            return new Date();
        }
        if (BaseStringUtil.isBlank(fmt)) {
            fmt = DATE_FORMAT_SECOND;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 得到日期
     *
     * @param obj
     * @return
     */
    public static Date getDateByFormatString(Object obj) {
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof String) {
            return BaseDateUtil.getDateByFormatString((String) obj, "");
        }
        return new Date();
    }

    /**
     * 得到 date 之后 day天 时间
     *
     * @param date ：null表示当前时间
     * @param day  : 负数表示 之前
     * @return
     */
    public static Date getDateAfterDay(Date date, int day) {
        Calendar sysdate = Calendar.getInstance();
        if (date != null) {
            sysdate.setTime(date);
        }
        sysdate.set(Calendar.DATE, sysdate.get(Calendar.DATE) + day);
        return sysdate.getTime();
    }

    /**
     * 得到两个时间 之间 的 一个 随机 时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Date getDateRandom(Date beginDate, Date endDate) {
        if (beginDate == null) {
            beginDate = new Date();
        }
        if (endDate == null) {
            endDate = new Date();
        }

        long begin = beginDate.getTime();
        long end = endDate.getTime();

        long time = begin + (long) (Math.random() * (end - begin));

        return new Date(time);
    }

    /**
     * 得到两个日期相差 几天
     *
     * @param beginDate
     * @param endDate
     * @return int
     */
    public static int getDiffDay(Date beginDate, Date endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        if (beginDate != null) {
            beginCalendar.setTime(beginDate);
        }

        if (endDate != null) {
            endCalendar.setTime(endDate);
        }

        int beginYear = beginCalendar.get(Calendar.YEAR);
        int endYear = endCalendar.get(Calendar.YEAR);

        int beginDay = beginCalendar.get(Calendar.DAY_OF_YEAR);
        int endDay = endCalendar.get(Calendar.DAY_OF_YEAR);

        return (endDay - beginDay) + (endYear - beginYear) * 365;
    }

    /**
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffHour(Date beginDate, Date endDate) {
        long diff = getDiffMillisecond(beginDate,endDate);
        int hour = (int) (diff / (1000 * 60 * 60));
        return hour;
    }

    /**
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffMinute(Date beginDate, Date endDate) {
        long diff = getDiffMillisecond(beginDate,endDate);
        int minute = (int) (diff / (1000 * 60));
        return minute;
    }

    /**
     * 得到两个时间 相差 多少毫秒
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getDiffMillisecond(Date beginDate, Date endDate) {
        if (beginDate == null) {
            beginDate = new Date();
        }
        if (endDate == null) {
            endDate = new Date();
        }
        return endDate.getTime() - beginDate.getTime();
    }

    public static boolean isNew(Date createTime){
        if(createTime==null){
            return false;
        }
        String todayStr = getFormatDay(new Date());
        todayStr = todayStr + " 00:00:01";
        boolean before = validateDateBefore(createTime,todayStr);
        return !before;
    }

    /**
     * 判断 当前时间 是否在 beginTime与endTime之间 即当前时间是否失效
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean validDateBetween(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return true;
        }
        Calendar sysdate = Calendar.getInstance();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);

        if (sysdate.after(beginCalendar) && sysdate.before(endCalendar)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        boolean b = validateDateBefore(new Date(), "2013-10-12", "yyyy-MM-dd");
        System.out.println(b);
    }

    /**
     * 判断当前时间是否在 time 之前
     *
     * @param time
     * @return
     */
    public static boolean validateDateBefore(Date time) {
        return validateDateBefore(time, new Date());
    }

    /**
     * @param time1
     * @param timeStr2
     * @return
     */
    public static boolean validateDateBefore(Date time1, String timeStr2) {
        return validateDateBefore(time1, timeStr2, "yyyy-MM-dd");
    }

    /**
     * @param time1
     * @param timeStr2
     * @param format
     * @return
     */
    public static boolean validateDateBefore(Date time1, String timeStr2, String format) {
        Date time2 = BaseDateUtil.getDateByFormatString(timeStr2, format);
        return validateDateBefore(time1, time2);
    }

    /**
     * @param time1
     * @param time2
     * @return
     */
    public static boolean validateDateBefore(Date time1, Date time2) {
        if (time1 == null || time2 == null) {
            return false;
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);
        return calendar1.before(calendar2);
    }


    /**
     *
     * @return
     */
    public static String getWeek(){
        int week =  Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(week);
    }

}
