package com.tvisha.trooptime.activity.activity.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by koti on 23/5/17.
 */

public class TimeHelper {

    public static TimeHelper timeHelper = new TimeHelper();
    SimpleDateFormat dateFormat_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat timeFormat_HH_MM_SS = new SimpleDateFormat("hh:mm", Locale.getDefault());

    public static TimeHelper getInstance() {
        return timeHelper = new TimeHelper();
    }

    public int get_count_of_days(String start_date_String, String end_date_String) {
        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat_YYYY_MM_DD.parse(start_date_String);
            Expire_CovertedDate = dateFormat_YYYY_MM_DD.parse(end_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat_YYYY_MM_DD.parse(dateFormat_YYYY_MM_DD.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int c_year = 0, c_month = 0, c_day = 0;

        if (Created_convertedDate.after(todayWithZeroTime)) {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }
        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        //return ("" + (int) dayCount + " Days");

        /*
         * +1 is to show exact count otherwise it will take
         * */
        return ((int) dayCount) + 1;
    }

    public String timeDifference(String time1, String time2) {
        try {
            Date Date1 = timeFormat_HH_MM_SS.parse(time1);
            Date Date2 = timeFormat_HH_MM_SS.parse(time2);

            long mills = Date2.getTime() - Date1.getTime();
            int hours = (int) mills / (1000 * 60 * 60);
            long remainMil = mills % (1000 * 60 * 60);
            int Mins = (int) (remainMil / (1000 * 60));
            if (Mins < 0 || hours < 0) {
                return null;
            } else {
                String diff = hours + "H:" + Mins + "M";
                return diff;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
