package dev.nelson.mot.legacy.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DateUtils {

    public static String getFirstDayOfCurrentMonth(){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String yearAndMonth = new SimpleDateFormat("yyyy-MM-", Locale.getDefault()).format(new Date());
        int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        return yearAndMonth + "0" + minDay;
    }

    public static String getLastDayOfCurrentMonth(){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String yearAndMonth = new SimpleDateFormat("yyyy-MM-", Locale.getDefault()).format(new Date());
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return yearAndMonth + maxDay;
    }

    public static HashMap<String, String> months = new HashMap<String, String>(){{
        put("01", "Jan");
        put("02", "Feb");
        put("03", "Mar");
        put("04", "Apr");
        put("05", "May");
        put("06", "June");
        put("07", "July");
        put("08", "Aug");
        put("09", "Sept");
        put("10", "Oct");
        put("11", "Nov");
        put("12", "Dec");
    }};
}
