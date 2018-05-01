package com.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {
    public static void scheduleDelay(int aDelayTime) {

        try {
            Thread.sleep(aDelayTime);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(Config.ErrorHandle.EXIT_TIMEOUT);
        }
    }
    
    public static String getTodayDate() {
        Date mDate = new Date();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/M/dd");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String mDateStr = mDateFormat.format(mDate);
        // System.out.println(mDateStr);

        return mDateStr;
    }
}
