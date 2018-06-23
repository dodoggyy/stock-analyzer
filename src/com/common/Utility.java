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

    public static void initStr(StringBuilder aStringBuilder) {
        if (aStringBuilder.length() != 0) {
            aStringBuilder.delete(0, aStringBuilder.length());
        }
    }
    
    public static int float2Int(String aFloatValue, int aDigit) {
        int mReturnInt = 0;
        mReturnInt = (int) Math.round(Float.parseFloat(aFloatValue)*Math.pow(10, aDigit));
        return mReturnInt;
    }
    
    public static int float2Int(float aFloatValue, int aDigit) {
        int mReturnInt = 0;
        mReturnInt = (int) Math.round(aFloatValue*Math.pow(10, aDigit));
        return mReturnInt;
    }
}
