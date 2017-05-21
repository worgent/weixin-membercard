package weixin.assistant.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by worgen on 2015/9/1.
 */
public class TimeUtil {
    public static long now(){
        return System.currentTimeMillis()/1000;
    }

    public static Date getDate(long timestamp){
        Date date = new Date();
        date.setTime(timestamp*1000);
        return date;
    }

    //根據timemark返回開始，結束時間
    //2010-10;
    //開始為2010-10-01 0；0:0
    //結束為2010-11-01 0:0:0
    //左閉右開
    public Date getStartDate(String timeMark){
        Date date = new Date();
        timeMark += "01 0:0:0";
        DateTime dateTime = new DateTime();
        dateTime.plusMonths(1);
       // dateTime.toDate()
      //  date.
        return date;
    }
    public Date getEndDate(String timeMark){
        Date date = new Date();
        return date;
    }
}
