/**
 * 
 */
package com.calculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import com.common.DatabaseConfig;
import com.common.StockTechInfo;
import com.common.Utility;
import com.database.AverageDatabaseHandler;
import com.database.WeekAverageDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public abstract class AverageKCalculateHandler extends BaseCalculateHandler {
    protected AverageDatabaseHandler mStockDB;
    private ArrayList<StockTechInfo> mTechAvg;
    protected String mTableSrc;
    protected String mTableDst;
    
    public AverageKCalculateHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        mTechAvg = new ArrayList<>();
    }
    
    @Override
    public void calculateValue(String aStockId, boolean bIsUpdateAll) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        StockTechInfo mStockInfo = new StockTechInfo(aStockId);
        ResultSet mResultSet = null;
        String mPreKey = null;

        mSqlWhere = " WHERE stock_id=" + "'" + aStockId + "'";
        mSqlQueryCmd = "SELECT stock_date,stock_high_price,stock_low_price,stock_closing_price, stock_opening_price, stock_volume FROM "
                + mTableSrc + mSqlWhere + " ORDER BY stock_date ASC";

        log.trace(mSqlQueryCmd);

        try {
            mResultSet = mStockDB.mResultSet = mStockDB.mStatement.executeQuery(mSqlQueryCmd);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            while (mResultSet.next()) {
                String mCurrentKey = getHashKey(Utility.string2Date(mResultSet.getString("stock_date")));
                int mTmpLow = mResultSet.getInt("stock_low_price");
                int mTmpHigh = mResultSet.getInt("stock_high_price");
                if (mCurrentKey.equals(mPreKey)) {
                    // ignore no trade case
                    if (mResultSet.getInt("stock_volume") == 0) {
                        continue;
                    }
                    mStockInfo.setStockClose(mResultSet.getInt("stock_closing_price"));
                    if (mStockInfo.getStockOpen() == 0) {
                        mStockInfo.setStockOpen(mResultSet.getInt("stock_opening_price"));
                    }
                } else {
                    float mTmpOpen = mStockInfo.getStockOpen();
                    if (mPreKey != null || mStockInfo.getStockVolume() != 0) {
                        mTechAvg.add(mStockInfo);
                    }
                    mPreKey = mCurrentKey;
                    mStockInfo = new StockTechInfo(aStockId);
                    
                    mStockInfo.setStockDate(mResultSet.getString("stock_date"));
                    if (mResultSet.getInt("stock_volume") == 0) {
                        mStockInfo.setStockOpen(mTmpOpen);
                    } else {
                        mStockInfo.setStockOpen(mResultSet.getInt("stock_opening_price"));
                    }
                }
                mStockInfo.setStockVolume(mStockInfo.getStockVolume() + mResultSet.getInt("stock_volume"));
                mStockInfo.setStockLow(Math.min(mStockInfo.getStockLow(), mTmpLow));
                mStockInfo.setStockHigh(Math.max(mStockInfo.getStockHigh(), mTmpHigh));
            }
            mTechAvg.add(mStockInfo);
            mResultSet.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mResultSet.close();
        if(log.isTraceEnabled()) {
            for (StockTechInfo mTechInfo : mTechAvg) {
                log.trace(mTechInfo.getStockDate() + " ");
                log.trace(mTechInfo.getStockOpen() + " ");
                log.trace(mTechInfo.getStockClose() + " ");
                log.trace(mTechInfo.getStockLow() + " ");
                log.trace(mTechInfo.getStockHigh() + "\n");
            } 
        }

        writeData2DB();
        clearData();
    }
    
    // return specific hash key to judge different type
    public abstract String getHashKey(Date aDate);
    
    // set table source
    public abstract void setTable() throws SQLException;
    
    public void clearData() {
        mTechAvg.clear();
    }

    @Override
    boolean writeData2DB() throws SQLException {
        // TODO Auto-generated method stub
        for (StockTechInfo mStockInfo : mTechAvg) {
            try {
                mStockDB.generateSqlPrepareStrCmd(1, mStockInfo.getStockID()); // stock_id
                mStockDB.generateSqlPrepareStrCmd(2, mStockInfo.getStockDate()); // stock_date
                mStockDB.generateSqlPrepareIntCmd(3, (int) mStockInfo.getStockClose()); // stock_closing_price
                mStockDB.generateSqlPrepareIntCmd(4, (int) mStockInfo.getStockOpen()); // stock_opening_price
                mStockDB.generateSqlPrepareIntCmd(5, (int) mStockInfo.getStockHigh()); // stock_high_price
                mStockDB.generateSqlPrepareIntCmd(6, (int) mStockInfo.getStockLow()); // stock_low_price
                mStockDB.generateSqlPrepareIntCmd(7, (int) mStockInfo.getStockVolume()); // stock_volume
                // mStockDB.generateSqlPrepareIntCmd(8, KeyDefine.OTC_TECH + 1);
                // // stock_type

                mStockDB.addSqlPrepareCmd2Batch();
                // fixme: sotck type
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                log.error(e);
            }
        }
        try {
            mStockDB.executeSqlPrepareCmd();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e);
        }
        
        

        return true;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
