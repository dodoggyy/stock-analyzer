package com.data;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.common.Config;
import com.common.KeyDefine;
import com.common.Utility;
import com.database.TechDatabaseHandler;
import com.parser.BaseParserHandler;
import com.parser.OTCFundParserHandler;
import com.parser.OTCTechParserHandler;
import com.parser.TWSEFundParserHandler;
import com.parser.TWSETechParserHandler;

/**
 * Taiwan stock data handler<T>
 * 
 * @author Chris Lin
 * @version 1.1 1 May 2019
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

    @SuppressWarnings("resource")
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        int mDowloadType, mStoreType;
        String mBeginDay = "", mEndDay = "";
        Scanner mScanner = new Scanner(System.in);
        System.out.println("(0)Skip (1)DB last date to Today (2)defined range by self");
        mDowloadType = mScanner.nextInt();

        System.out.println("(1)Store to DB (2)exit");
        mStoreType = mScanner.nextInt();

        if (mDowloadType == KeyDefine.DOWNLOAD_DB_UPDATE) {
            // not implement yet
            // System.out.println("Please select download type: (1)update to
            // newest date (2)insert old date data");
            String mDbDate = "";
            ArrayList<String> mDateStr = new ArrayList<String>();
            TechDatabaseHandler mHandler = new TechDatabaseHandler();

            try {
                mDbDate = mHandler.getDbLatestDate();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
//            System.out.printf("Today:%s , DB date: %s\n",Utility.getTodayDate(), mDbDate);
            
            if(mDbDate == null) {
                System.out.println("null date in Database");
            } else {
                try {
                    mBeginDay = Utility.getDateAfterDay(mDbDate, 1);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mEndDay = Utility.getTodayDate();
                DataHandlerMain mDataHandler = new DataHandlerMain(mBeginDay, mEndDay);
                mDataHandler.downloadData(mDowloadType);
            }
            
        } else if (mDowloadType == KeyDefine.DOWNLOAD_SPECIFIED_RANGE) {

            System.out.printf("Please input begin day: ex:2019-06-27\n");
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
            
//            OTCTechParserHandler test = (OTCTechParserHandler) BaseParserHandler.parserGenerator(KeyDefine.OTC_TECH);
//            test.parseAllFileData();
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
        boolean bRet = true;
        ArrayList<String> mDateArray = new ArrayList<String>();
        SimpleDateFormat mFormatter = new SimpleDateFormat(Config.DataAnalyze.DATE_FORMAT);

        if ((aDowloadType == KeyDefine.DOWNLOAD_SPECIFIED_RANGE) || 
                (aDowloadType == KeyDefine.DOWNLOAD_DB_UPDATE)) {
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
//            for (int i = 0; i < mTotalDay; i++) {
//                //System.out.println(mDateArray.get(i).toString());
//                DownloadDailyData mDonloader = DownloadDailyData.getInstance();
//                for (int j = 0; j < KeyDefine.DOWNLOAD_DATA_MAX; j++) {
//                    mDonloader.downloadData(mDateArray.get(i).toString(), j);
//                    Utility.scheduleDelay(Utility.getScheduleDelayTimeMs(aDowloadType));
//                }
////                mDonloader.downloadData(mDateArray.get(i).toString(), KeyDefine.TWSE_TECH);
////                Utility.scheduleDelay(Utility.getScheduleDelayTimeMs(KeyDefine.TWSE_TECH));
//            }
            
            MuiltiDownloader mDownloadTwseFund = new MuiltiDownloader(mDateArray, mTotalDay, KeyDefine.TWSE_FUND);
            MuiltiDownloader mDownloadOtcFund = new MuiltiDownloader(mDateArray, mTotalDay, KeyDefine.OTC_FUND);
            MuiltiDownloader mDownloadTwseTech = new MuiltiDownloader(mDateArray, mTotalDay, KeyDefine.TWSE_TECH);
            MuiltiDownloader mDownloadOtcTech = new MuiltiDownloader(mDateArray, mTotalDay, KeyDefine.OTC_TECH);

            Thread mThreadTwseFund = new Thread(mDownloadTwseFund);
            Thread mThreadOtcFund = new Thread(mDownloadOtcFund);
            Thread mThreadTwseTech = new Thread(mDownloadTwseTech);
            Thread mThreadOtcTech = new Thread(mDownloadOtcTech);

            mThreadTwseFund.start();
            mThreadOtcFund.start();
            try {
                mThreadTwseFund.join();
                mThreadOtcFund.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mThreadTwseTech.start();
            mThreadOtcTech.start();
        } else {
            System.out.println("Unknown type");
        }

        return bRet;
    }
    
    public class MuiltiDownloader implements Runnable {
        private int mDowloadType;
        private ArrayList<String> mDateArray;
        private int mTotalDay;
        
        public MuiltiDownloader(ArrayList<String> aDateArray, int aTotalDay, int aDowloadType) {
            this.mDowloadType = aDowloadType;
            this.mDateArray = aDateArray;
            this.mTotalDay = aTotalDay;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            for (int i = 0; i < mTotalDay; i++) {
                DownloadDailyData mDonloader = DownloadDailyData.getInstance();
                mDonloader.downloadData(mDateArray.get(i).toString(), mDowloadType);
                Utility.scheduleDelay(Utility.getScheduleDelayTimeMs(mDowloadType));
                //System.out.println("DowloadType:" + mDowloadType + " Date:" + mDateArray.get(i).toString());
            }
        }
       
    }
}
