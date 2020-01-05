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
import com.database.MonthAverageDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class MonthKCalculateHandler extends AverageKCalculateHandler {

    

    MonthKCalculateHandler() throws SQLException {
        super();
        setTable();
    }

    @Override
    public String getHashKey(Date aDate) {
        return Utility.getDateOfMonth(aDate) + "";
    }
    
    @Override
    public void setTable() throws SQLException {
        // TODO Auto-generated method stub
        this.mStockDB = new MonthAverageDatabaseHandler();
        this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_WEEK;
        this.mTableDst = DatabaseConfig.DEFAULT_LISTED_TECH_MONTH;
    }
    
    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) {
        Utility.timerStart();
        try {
            MonthKCalculateHandler mCalculator = new MonthKCalculateHandler();
            AverageDatabaseHandler mStockDB = new AverageDatabaseHandler();
            ArrayList<String> mStockIdList = new ArrayList<>();
            mStockIdList = mStockDB.queryAllStockId();
            
            for(String mStockID: mStockIdList) {
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
