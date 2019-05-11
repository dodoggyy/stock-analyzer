package com.data;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.common.Config;
import com.common.KeyDefine;
import com.common.Utility;
import com.parser.OTCFundParserHandler;
import com.parser.OTCTechParserHandler;
import com.parser.TWSEFundParserHandler;
import com.parser.TWSETechParserHandler;

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

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        int mDowloadType, mStoreType;
        Scanner mScanner = new Scanner(System.in);
        System.out.println("(0)Skip (1)DB last date to Today (2)defined range by self");
        mDowloadType = mScanner.nextInt();

        System.out.println("(1)Store to DB (2)exit");
        mStoreType = mScanner.nextInt();

        if (mDowloadType == KeyDefine.DOWNLOAD_DB_UPDATE) {
            // not implement yet
            // System.out.println("Please select download type: (1)update to
            // newest date (2)insert old date data");
            System.out.println("not implement yet");
        } else if (mDowloadType == KeyDefine.DOWNLOAD_SPECIFIED_RANGE) {
            String mBeginDay = "", mEndDay = "";

            System.out.printf("Please input begin day: ex:2018/4/23\n");
            mScanner = new Scanner(System.in);
            mBeginDay = mScanner.next();
            System.out.printf("Please input end day:\n");
            mEndDay = mScanner.next();
            DataHandlerMain mDataHandler = new DataHandlerMain(mBeginDay, mEndDay);
            mDataHandler.downloadData(mDowloadType);
        } else if (mDowloadType == KeyDefine.DOWNLOAD_DB_NONE) {
            ;
        }

        Utility.timerStart();

        if (mStoreType == KeyDefine.STORE_TO_DB) {
            TWSETechParserHandler TWSETechParser = new TWSETechParserHandler();
            TWSETechParser.parseAllFileData();

            OTCTechParserHandler OTCTechParser = new OTCTechParserHandler();
            OTCTechParser.parseAllFileData();

            TWSEFundParserHandler TWSEFundParser = new TWSEFundParserHandler();
            TWSEFundParser.parseAllFileData();

            OTCFundParserHandler OTCFundParser = new OTCFundParserHandler();
            OTCFundParser.parseAllFileData();
        } else if (mStoreType == KeyDefine.STORE_NONE) {

            ;
        }

        Utility.timerEnd();
        
        mScanner.close();
    }

    public DataHandlerMain(String aBeginDay, String aEndDay) {
        mBeginDay = aBeginDay;
        mEndDay = aEndDay;
    }

    public boolean downloadData(int aDowloadType) {
        int mTotalDay = 0;
        boolean bRet = false;
        ArrayList<String> mDateArray = new ArrayList<String>();
        SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy/M/d");

        if (aDowloadType == KeyDefine.DOWNLOAD_DB_UPDATE) {
            System.out.println("not implement yet");
        } else if (aDowloadType == KeyDefine.DOWNLOAD_SPECIFIED_RANGE) {
            try {
                Date mDateBegin = mFormatter.parse(mBeginDay);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDateBegin);
                Date mDateEnd = mFormatter.parse(mEndDay);
                while (calendar.getTime().compareTo(mDateEnd) <= 0) {
                    mTotalDay++;
                    mDateArray.add(mFormatter.format(calendar.getTime()));
                    // System.out.println(mFormatter.format(calendar.getTime())
                    // + " " + mTotalDay);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // download data from TWSE and OTC
            for (int i = 0; i < mTotalDay; i++) {
                //System.out.println(mDateArray.get(i).toString());
                DownloadDailyData mDonloader = DownloadDailyData.getInstance();
                for (int j = 0; j < KeyDefine.DOWNLOAD_DATA_MAX; j++) {
                    mDonloader.downloadData(mDateArray.get(i).toString(), j);
                    Utility.scheduleDelay(Utility.getScheduleDelayTimeMs(i));
                }
//                mDonloader.downloadData(mDateArray.get(i).toString(), KeyDefine.TWSE_TECH);
//                Utility.scheduleDelay(Utility.getScheduleDelayTimeMs(KeyDefine.TWSE_TECH));
            }
        } else {
            System.out.println("Unknown type");
        }

        return bRet;
    }
}
