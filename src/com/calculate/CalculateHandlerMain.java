/**
 * 
 */
package com.calculate;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import com.common.Utility;
import com.database.AverageDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class CalculateHandlerMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Utility.timerStart();
        try {
            AverageDatabaseHandler mStockDB = new AverageDatabaseHandler();
            ArrayList<String> mStockIdList = new ArrayList<>();
            mStockIdList = mStockDB.queryAllStockId();
            WeekKCalculateHandler mCalculatorWeek = new WeekKCalculateHandler();
            MonthKCalculateHandler mCalculatorMonth = new MonthKCalculateHandler();
            SeasonKCalculateHandler mCalculatorSeason = new SeasonKCalculateHandler();
            YearKCalculateHandler mCalculatorYear = new YearKCalculateHandler();

            for (String mStockID : mStockIdList) {
                mCalculatorWeek.calculateValue(mStockID, false);
            }
            for (String mStockID : mStockIdList) {
                mCalculatorMonth.calculateValue(mStockID, false);
            }
            for (String mStockID : mStockIdList) {
                mCalculatorSeason.calculateValue(mStockID, false);
            }
            for (String mStockID : mStockIdList) {
                mCalculatorYear.calculateValue(mStockID, false);
            }

        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Utility.timerEnd();
    }

}
