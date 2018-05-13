package com.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.common.Config;
import com.common.Utility;

/**
 * Taiwan stock data handler<T>
 * 
 * @author Chris Lin
 * @version 1.0 1 May 2018
 *
 * @param aBeginDay
 *            開始日期
 * @param aEndDay 
 *            結束日期
 * 
 */
public class DataHandlerMain {
    private String mBeginDay;
    private String mEndDay;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int mDowloadType;
        Scanner mScanner = new Scanner(System.in);
        System.out.println("(1)DB last date to Today (2)defined range by self");
        mDowloadType = mScanner.nextInt();

        if (mDowloadType == Config.DataAnalyze.DOWNLOAD_DB_UPDATE) {
            // not implement yet
            // System.out.println("Please select download type: (1)update to
            // newest date (2)insert old date data");
            System.out.println("not implement yet");
        } else if (mDowloadType == Config.DataAnalyze.DOWNLOAD_SPECIFIED_RANGE) {
            String mBeginDay = "", mEndDay = "";

            System.out.println("Please input begin day: ex:2018/4/23");
            mScanner = new Scanner(System.in);
            mBeginDay = mScanner.next();
            System.out.println("Please input end day:");
            mEndDay = mScanner.next();
            DataHandlerMain mDataHandler = new DataHandlerMain(mBeginDay, mEndDay);
            mDataHandler.writeData(mDowloadType);
        }
    }

    public DataHandlerMain(String aBeginDay, String aEndDay) {
        mBeginDay = aBeginDay;
        mEndDay = aEndDay;
    }

    public boolean writeData(int aDowloadType) {
        int mTotalDay = 0;
        boolean bRet = false;
        ArrayList<String> mDateArray = new ArrayList<String>();
        SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy/M/d");

        if (aDowloadType == Config.DataAnalyze.DOWNLOAD_DB_UPDATE) {
            System.out.println("not implement yet");
        } else if (aDowloadType == Config.DataAnalyze.DOWNLOAD_SPECIFIED_RANGE) {
            try {
                Date mDateBegin = mFormatter.parse(mBeginDay);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDateBegin);
                Date mDateEnd = mFormatter.parse(mEndDay);
                while (calendar.getTime().compareTo(mDateEnd) <= 0) {
                    mTotalDay++;
                    mDateArray.add(mFormatter.format(calendar.getTime()));
                    System.out.println(mFormatter.format(calendar.getTime()) + " " + mTotalDay);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // download data from TWSE and OTC
            for (int i = 0; i < mTotalDay; i++) {
                System.out.println(mDateArray.get(i).toString());
                DownloadDailyData mDonloader = new DownloadDailyData();
                mDonloader.downloadData(mDateArray.get(i).toString());
                Utility.scheduleDelay(Config.DataAnalyze.DOWNLOAD_DELAY);
            }
        } else {
            System.out.println("Unknown type");
        }

        return bRet;
    }
    
    public boolean writeData2DB(int aDowloadType) {
        return true;
    }

}
