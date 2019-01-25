/**
 * 
 */
package com.database;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.common.KeyDefine;
import com.common.Utility;

/**
 * @author Chris Lin
 *
 */
public class WeekAverageDatabaseHandler extends AverageDatabaseHandler {
    public class WeeklyDatabaseData {
        ArrayList<Double> mKs;
        ArrayList<Double> mDs;
        ArrayList<Double> mJs;
        ArrayList<Double> mRSVs;
        ArrayList<String> mDateStrTech;
        ArrayList<String> mDateStrKDJ;
    }
    
    
    public WeekAverageDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     * @throws SQLException
     * @throws ParseException
     */
    public static void main(String[] args) throws SQLException, ParseException {
        // TODO Auto-generated method stub
         WeekAverageDatabaseHandler mStockDB = new WeekAverageDatabaseHandler();
         DBOperation(mStockDB);
        // ArrayList<String> mStockIdList;
        // mStockIdList = mStockDB.queryAllStockId();
        // for(int i = 0 ;i < mStockIdList.size() ; i++){
        // System.out.println(mStockIdList.get(i));
        // }

//        String mDate = "2017-02-10";
//        SimpleDateFormat mSimpleDate = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = mSimpleDate.parse(mDate);
//
//        System.out.println(Utility.getDateOfWeek(date) + " " + Utility.getDateOfYear(date) + " "
//                + Utility.getDateOfCurrentYear(date));
    }

    protected void setupDatabase() {
        // TODO Auto-generated method stub
        this.mTableName = KeyDefine.DEFAULT_LISTED_TECH_WEEK;
    }

}
