package com.duck.yellowduck.publics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil
{
    public static String[] assemblyDate(String startTime)
            throws Exception
    {
        String[] str = new String[2];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Date date = sdf.parse(startTime);

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        cal.set(5, 1);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        startTime = sdf1.format(cal.getTime());

        cal.set(5, cal.getActualMaximum(5));

        String endTime = sdf1.format(cal.getTime());

        str[0] = startTime;
        str[1] = endTime;

        return str;
    }
}
