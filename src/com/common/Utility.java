package com.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;

public class Utility {
    private static Long mBeginTime;
    private static Long mEndTime;

    public static void scheduleDelay(int aDelayTime) {

        try {
            Thread.sleep(aDelayTime);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(KeyDefine.ErrorHandle.EXIT_TIMEOUT);
        }
    }

    public static int getScheduleDelayTimeMs(int aDowloadType) {
        int mResult = 0;
        switch (aDowloadType) {
        case KeyDefine.TWSE_TECH:
        case KeyDefine.TWSE_FUND:
            mResult = Config.DataAnalyze.DOWNLOAD_DELAY_TIME_TWSE;
            break;
        case KeyDefine.OTC_TECH:
        case KeyDefine.OTC_FUND:
            mResult = Config.DataAnalyze.DOWNLOAD_DELAY_TIME_OTC;
            break;
        default:
            System.out.println("Not recognized download type");
        }

        return mResult;
    }

    public static String getTodayDate() {
        Date mDate = new Date();
        SimpleDateFormat mDateFormat = new SimpleDateFormat(Config.DataAnalyze.DATE_FORMAT);
        mDateFormat.setTimeZone(TimeZone.getTimeZone(Config.DataAnalyze.TIME_ZONE));
        String mDateStr = mDateFormat.format(mDate);
        // System.out.println(mDateStr);

        return mDateStr;
    }

    /**
     * 移除雜亂的字元，如"\"",",","=","--"等並取代
     *
     * @param aLine
     *            從csv讀進來的一行字
     * @return 移除雜亂字元後的字串
     */
    public static String removeMessyChar(String aLine) {
        char mCharArr[];
        StringBuilder mTmpStr = new StringBuilder(200);
        String mStrReturn = "";
        boolean bIsDelimeter = false;

        mCharArr = aLine.toCharArray();
        initStr(mTmpStr);
        for (int i = 0; i < mCharArr.length; i++) {
            if (mCharArr[i] == '\"') {
                bIsDelimeter = !bIsDelimeter;
            } else {
                if (bIsDelimeter) {
                    if (mCharArr[i] != ',') {
                        mTmpStr.append(mCharArr[i]);
                    }
                } else {
                    mTmpStr.append(mCharArr[i]);
                }
            }
        }
        mStrReturn = mTmpStr.toString().replace("---", "0.00");
        // mStrReturn = mTmpStr.toString().replace("--", "00");
        mStrReturn = mStrReturn.replace("--", "00");
        mStrReturn = mStrReturn.replace("0-", "00");
        mStrReturn = mStrReturn.replace("=", "");
        mStrReturn = mStrReturn.replace("-", "0");
        mStrReturn = mStrReturn.replace("N/A", "0.00");

        return mStrReturn;
    }

    /**
     * 移除csv雜亂的字元，如""等並取代
     *
     * @param aLine
     *            從csv讀進來的一行字
     * @return 移除雜亂字元後的字串
     */
    public static String removeCsvMessyStr(String aLine) {
        char mCharArr[];
        StringBuilder mTmpStr = new StringBuilder(200);
        String mStrReturn = "";
        boolean bIsDelimeter = false;

        mCharArr = aLine.toCharArray();
        initStr(mTmpStr);
        for (int i = 0; i < mCharArr.length; i++) {
            if (mCharArr[i] == '\"') {
                bIsDelimeter = !bIsDelimeter;
            } else {
                if (bIsDelimeter) {
                    if (mCharArr[i] != ',') {
                        mTmpStr.append(mCharArr[i]);
                    }
                } else {
                    mTmpStr.append(mCharArr[i]);
                }
            }
        }
        mStrReturn = mTmpStr.toString().replace("\"", "");
        // // mStrReturn = mTmpStr.toString().replace("--", "00");
        // mStrReturn = mStrReturn.replace("--", "00");
        // mStrReturn = mStrReturn.replace("0-", "00");
        // mStrReturn = mStrReturn.replace("=", "");
        // mStrReturn = mStrReturn.replace("-", "0");
        // mStrReturn = mStrReturn.replace("N/A", "0.00");

        return mStrReturn;
    }

    public static void initStr(StringBuilder aStringBuilder) {
        if (aStringBuilder.length() != 0) {
            aStringBuilder.delete(0, aStringBuilder.length());
        }
    }

    public static int float2Int(String aFloatValue, int aDigit) {
        if (aFloatValue.isEmpty()) {
            return 0;
        }
        
        return float2Int(Float.parseFloat(aFloatValue), aDigit);
    }

    public static int float2Int(float aFloatValue, int aDigit) {
        int mReturnInt = 0;
        mReturnInt = (int) Math.round(aFloatValue * Math.pow(10, aDigit));
        return mReturnInt;
    }

    public static float int2Float(String aIntValue, int aDigit) {
        if (aIntValue.isEmpty()) {
            return 0;
        }
        
        return int2Float(Float.parseFloat(aIntValue), aDigit);
    }

    public static float int2Float(float aIntValue, int aDigit) {
        float mReturnFloat = 0;
        mReturnFloat = (float) (aIntValue / Math.pow(10, aDigit));
        return mReturnFloat;
    }

    public static void timerStart() {
        System.out.println("timer start");
        mBeginTime = System.currentTimeMillis();
    }

    public static void timerEnd() {
        if (mBeginTime == null) {
            System.out.println("Please set timerStart first");
        } else {
            mEndTime = System.currentTimeMillis();
            System.out.println("Total time:：" + (mEndTime - mBeginTime) / 1000 + "sec");
        }
    }

    /**
     * 轉換日期時間格式
     * 
     * @param dateTime
     *            要進行轉換的日期時間
     * @param oldFmt
     *            舊的日期時間格式
     * @param newFmt
     *            新的日期時間格式
     * @param area
     *            日期時間地區(例如裡面有要串am/pm，就需要指定為java.util.Locale.US)
     * @return
     */
    public static String getDateTimeFormat(String dateTime, String oldFmt, String newFmt, Locale area) {
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat(oldFmt, area);
        java.util.Date date;
        try {
            date = fmt.parse(dateTime);
            fmt.applyPattern(newFmt);

            return fmt.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;// 轉換產生錯誤時，回傳原始的日期時間
    }

    public static int getDateOfYear(Date aDate) {
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTime(aDate);

        return mCalendar.get(Calendar.YEAR);
    }

    public static int getDateOfWeek(Date aDate) {
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTime(aDate);

        return mCalendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static int getDateOfMonth(Date aDate) {
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTime(aDate);

        return mCalendar.get(Calendar.MONTH);
    }
    
    public static int getDateOfSeason(Date aDate) {
        return getDateOfMonth(aDate) / 4;
    }

    public static int getDateOfCurrentYear(Date aDate) {
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTime(aDate);

        return mCalendar.get(Calendar.YEAR);
    }

    public static Date string2Date(String aDate) throws ParseException {
        return string2Date(aDate, Config.DataAnalyze.DATE_FORMAT);
    }

    public static Date string2Date(String aDate, String aDateFormat) throws ParseException {
        SimpleDateFormat mSimpleDate = new SimpleDateFormat(aDateFormat);
        return mSimpleDate.parse(aDate);
    }

    public static String date2String(Date aDate) throws ParseException {
        return date2String(aDate, Config.DataAnalyze.DATE_FORMAT);
    }

    public static String date2String(Date aDate, String aDateFormat) throws ParseException {
        SimpleDateFormat mSimpleDate = new SimpleDateFormat(aDateFormat);
        return mSimpleDate.format(aDate);
    }

    public static String getDateAfterDay(Date aDate, int aAfterDay) throws ParseException {
        return getDateAfterDay(date2String(aDate), aAfterDay, Config.DataAnalyze.DATE_FORMAT);
    }

    public static String getDateAfterDay(String aDate, int aAfterDay) throws ParseException {
        return getDateAfterDay(aDate, aAfterDay, Config.DataAnalyze.DATE_FORMAT);
    }
    
    public static String getDateAfterDay(String aDate, int aAfterDay, String aDateFormat) throws ParseException {
        SimpleDateFormat mSimpleDate = new SimpleDateFormat(aDateFormat);
        Date mDate = Utility.string2Date(aDate);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mDate);
        mCalendar.add(Calendar.DAY_OF_MONTH, aAfterDay);

        return mSimpleDate.format(mCalendar.getTime());
    }

    /**
     * Get LinkedHashMap last key Time complexity:O(n)
     * 
     * @param aMap
     * @return
     */
    public static <K, V> Entry<K, V> getMapLastElement(LinkedHashMap<K, V> aMap) {
        // return aMap.entrySet().toArray()[aMap.size() - 1];
        Iterator<Entry<K, V>> iterator = aMap.entrySet().iterator();
        Entry<K, V> mTail = null;
        while (iterator.hasNext()) {
            mTail = iterator.next();
        }
        return mTail;

    }
}
