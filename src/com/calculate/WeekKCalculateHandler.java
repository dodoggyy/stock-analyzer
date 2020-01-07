/**
 * 
 */
package com.calculate;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import com.common.DatabaseConfig;
import com.common.Utility;
import com.database.WeekAverageDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class WeekKCalculateHandler extends AverageKCalculateHandler {

    

    WeekKCalculateHandler() throws SQLException {
        super();
        setTable();
    }

    @Override
    public String getHashKey(Date aDate) {
        return Utility.getDateOfWeek(aDate) + "";
    }
    
    @Override
    public void setTable() throws SQLException {
        // TODO Auto-generated method stub
        this.mStockDB = new WeekAverageDatabaseHandler();
        this.mTableSrc = DatabaseConfig.TABLE_DAY_TECH;
        this.mTableDst = DatabaseConfig.DEFAULT_LISTED_TECH_WEEK;
    }
    
    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) {
        Utility.timerStart();
        try {
            WeekKCalculateHandler mCalculator = new WeekKCalculateHandler();
//            WeekAverageDatabaseHandler mStockDB = new WeekAverageDatabaseHandler();
//            ArrayList<String> mStockIdList = new ArrayList<>();
//            mStockIdList = mStockDB.queryAllStockId();
            
//            for(String mStockID: mStockIdList) {
//                mCalculator.calculateValue(mStockID, false);
//            }
            
//            mCalculator.calculateValue("8093", false);
            mCalculator.calculateValueWholeData();
            
        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e);
        }

        Utility.timerEnd();
    }
}
