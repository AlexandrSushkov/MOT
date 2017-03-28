package dev.nelson.mot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import dev.nelson.mot.R;

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
        put("01", MyApplication.getContext().getString(R.string.january));
        put("02", MyApplication.getContext().getString(R.string.february));
        put("03", MyApplication.getContext().getString(R.string.march));
        put("04", MyApplication.getContext().getString(R.string.april));
        put("05", MyApplication.getContext().getString(R.string.may));
        put("06", MyApplication.getContext().getString(R.string.june));
        put("07", MyApplication.getContext().getString(R.string.july));
        put("08", MyApplication.getContext().getString(R.string.august));
        put("09", MyApplication.getContext().getString(R.string.september));
        put("10", MyApplication.getContext().getString(R.string.october));
        put("11", MyApplication.getContext().getString(R.string.november));
        put("12", MyApplication.getContext().getString(R.string.december));
    }};
}
