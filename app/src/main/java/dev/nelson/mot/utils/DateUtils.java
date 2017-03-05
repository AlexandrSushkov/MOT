package dev.nelson.mot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
}
