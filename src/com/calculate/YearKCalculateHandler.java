/**
 * 
 */
package com.calculate;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import com.common.DatabaseConfig;
import com.common.Utility;
import com.database.AverageDatabaseHandler;
import com.database.YearAverageDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class YearKCalculateHandler extends AverageKCalculateHandler {

    YearKCalculateHandler() throws SQLException {
        super();
        setTable();
    }

    @Override
    public String getHashKey(Date aDate) {
        return Utility.getDateOfYear(aDate) + "";
    }

    @Override
    public void setTable() throws SQLException {
        // TODO Auto-generated method stub
        this.mStockDB = new YearAverageDatabaseHandler();
        this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_SEASON;
        this.mTableDst = DatabaseConfig.DEFAULT_LISTED_TECH_YEAR;
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) {
        Utility.timerStart();
        try {
            YearKCalculateHandler mCalculator = new YearKCalculateHandler();
            AverageDatabaseHandler mStockDB = new AverageDatabaseHandler();
            ArrayList<String> mStockIdList = new ArrayList<>();
            mStockIdList = mStockDB.queryAllStockId();

            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }

        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e);
        }

        Utility.timerEnd();
    }
}
