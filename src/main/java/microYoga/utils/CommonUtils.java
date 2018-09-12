package microYoga.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {
    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getCurrentMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(new Date());
    }

    public static String getCurrentYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(new Date());
    }

    public static String getMonthDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getDateStr(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String dateAddDay(String dateStr, int days){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateStr);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, days);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            return "";
        }

    }

    public static Date dateAddDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date getDateByString(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }

    public static String getWeekName(Date date){
        SimpleDateFormat dateFm = new SimpleDateFormat("EEE", Locale.ENGLISH);
        switch (dateFm.format(date).toLowerCase()){
            case "mon": return "周一";
            case "tue": return "周二";
            case "wed": return "周三";
            case "thu": return "周四";
            case "fri": return "周五";
            case "sat": return "周六";
            case "sun": return "周日";
            default: return "";
        }

    }
}
