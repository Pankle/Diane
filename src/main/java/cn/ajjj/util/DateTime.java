package cn.ajjj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pankle on 2018/12/12.
 */
public class DateTime {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    //获得当天24点时间
    public static long getTimesnight(long timestampe){
        long night = timestampe + (24 * 60 * 60 * 1000)-1;
        return  (night);
    }
    //获得当天0点时间


    public static long getTimesmorning(String day) throws ParseException {

        Date parse = simpleDateFormat.parse(day);
        long time = parse.getTime();

        return time;
    }
}
