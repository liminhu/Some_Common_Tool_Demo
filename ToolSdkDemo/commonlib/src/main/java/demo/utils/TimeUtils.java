package demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hulimin on 2017/9/21.
 */

public class TimeUtils {
    public static String getFormatrerTime(long currenTime){
        Date date=new Date(currenTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月-dd日 HH:mm:ss:SSS");
        String dateString = formatter.format(date);
        return dateString;
    }
}
