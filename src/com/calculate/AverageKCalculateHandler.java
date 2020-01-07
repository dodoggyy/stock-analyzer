/**
 * 
 */
package com.calculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.common.DatabaseConfig;
import com.common.KeyDefine;
import com.common.KeyDefine.CalculateCycle;
import com.common.StockTechInfo;
import com.common.Utility;
import com.database.AverageDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class AverageKCalculateHandler extends BaseCalculateHandler {
    protected AverageDatabaseHandler mStockDB;
    private ArrayList<StockTechInfo> mTechAvg;
    private HashMap<String, ArrayList<StockTechInfo>> mMap;
    private CalculateCycle mCycleType = KeyDefine.CalculateCycle.CYCLE_MAX;
    protected String mTableSrc;
    protected String mTableDst;

    public AverageKCalculateHandler(CalculateCycle mCycleType) throws SQLException {
        // TODO Auto-generated constructor stub
        this.mCycleType = mCycleType;
        this.setTable();
        this.mStockDB = new AverageDatabaseHandler(mCycleType);
        mTechAvg = new ArrayList<>();
        mMap = new HashMap<>();
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
                if(mTmpLow != 0) { // prevent 0 compare
                    mStockInfo.setStockLow(Math.min(mStockInfo.getStockLow(), mTmpLow));
                }
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
    
    public void calculateValueWholeData() throws SQLException, ParseException {
        StockTechInfo mStockInfo = new StockTechInfo();
        ResultSet mResultSet = null;
        String mPreKey = null;

        mSqlWhere = " WHERE 1";
        mSqlQueryCmd = "SELECT * FROM "
                + mTableSrc + mSqlWhere + " ORDER BY stock_id ASC";

        log.debug(mSqlQueryCmd);

        try {
            mResultSet = mStockDB.mResultSet = mStockDB.mStatement.executeQuery(mSqlQueryCmd);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try {
            while (mResultSet.next()) {
                String mStockID = mResultSet.getString("stock_id");
                mStockInfo = new StockTechInfo(mStockID);
                mStockInfo.setStockDate(mResultSet.getString("stock_date"));
                mStockInfo.setStockClose(mResultSet.getInt("stock_closing_price"));
                mStockInfo.setStockOpen(mResultSet.getInt("stock_opening_price"));
                mStockInfo.setStockHigh(mResultSet.getInt("stock_high_price"));
                mStockInfo.setStockLow(mResultSet.getInt("stock_low_price"));
                mStockInfo.setStockVolume(mResultSet.getInt("stock_volume"));

                ArrayList<StockTechInfo> mListStockInfo;
                if(!mMap.containsKey(mStockID)) {
                    mListStockInfo = new ArrayList<StockTechInfo>();
                } else {
                    mListStockInfo = mMap.get(mStockID);
                }
                mListStockInfo.add(mStockInfo);
                mMap.put(mStockID, mListStockInfo);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mResultSet.close();
        
        Iterator<Entry<String, ArrayList<StockTechInfo>>> mIter = mMap.entrySet().iterator();
        while(mIter.hasNext()) {
            Map.Entry<String, ArrayList<StockTechInfo>> mPair = (Map.Entry<String, ArrayList<StockTechInfo>>) mIter.next();
            ArrayList<StockTechInfo> mList = mPair.getValue();
            mStockInfo = new StockTechInfo(mPair.getKey());
            Collections.sort(mList); // sort by date
            if(!mList.isEmpty()) {
                // prevent null date in first data
                mStockInfo.setStockDate(mList.get(0).getStockDate());
            }
            
            for(StockTechInfo mCurInfo : mList) {
                // ignore no trade case
                if (mCurInfo.getStockVolume() == 0) {
                    continue;
                }
                String mCurrentKey = getHashKey(Utility.string2Date(mCurInfo.getStockDate()));
                
                if (mCurrentKey.equals(mPreKey)) {
                    
                    if (mStockInfo.getStockOpen() == 0) {
                        mStockInfo.setStockOpen(mCurInfo.getStockOpen());
                    }
                } else {
                    if (mStockInfo.getStockVolume() != 0) {
                        mTechAvg.add(mStockInfo);
                    }
                    float mTmpOpen = mStockInfo.getStockOpen();

                    mPreKey = mCurrentKey;
                    mStockInfo = new StockTechInfo(mCurInfo.getStockID());
                    
                    mStockInfo.setStockDate(mCurInfo.getStockDate());
                    if (mCurInfo.getStockVolume() == 0) {
                        mStockInfo.setStockOpen(mTmpOpen);
                    } else {
                        mStockInfo.setStockOpen(mCurInfo.getStockOpen());
                    }
                }
                float mTmpLow = mCurInfo.getStockLow();
                float mTmpHigh = mCurInfo.getStockHigh();
                float mTmpClose = mCurInfo.getStockClose();
                
                if(mTmpLow == 0 || mTmpHigh == 0 || mTmpClose == 0) {
                    continue;
                }
                
                mStockInfo.setStockVolume(mStockInfo.getStockVolume() + mCurInfo.getStockVolume());
                mStockInfo.setStockLow(Math.min(mStockInfo.getStockLow(), mTmpLow));
                mStockInfo.setStockHigh(Math.max(mStockInfo.getStockHigh(), mTmpHigh));
                mStockInfo.setStockClose(mTmpClose);
            }
            mTechAvg.add(mStockInfo);
            writeData2DB();
            clearData();
            mIter.remove();
        }
    }
    
    // return specific hash key to judge different type
    public String getHashKey(Date aDate) {
        switch(mCycleType) {
        case CYCLE_WEEK:
            return Utility.getDateOfWeek(aDate) + "";
        case CYCLE_MONTH:
            return Utility.getDateOfMonth(aDate) + "";
        case CYCLE_SEASON:
            return Utility.getDateOfSeason(aDate) + "";
        case CYCLE_YEAR:
            return Utility.getDateOfYear(aDate) + "";
        default:
            log.error("Unknown calculate type or not define to set hash key");
            return "";
        }
    }
    
    // set table source
    public void setTable() throws SQLException {
        switch(mCycleType) {
        case CYCLE_WEEK:
            this.mTableSrc = DatabaseConfig.TABLE_DAY_TECH;
            this.mTableDst = DatabaseConfig.DEFAULT_LISTED_TECH_YEAR;
            break;
        case CYCLE_MONTH:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_WEEK;
            this.mTableDst = DatabaseConfig.DEFAULT_LISTED_TECH_MONTH;
            break;
        case CYCLE_SEASON:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_MONTH;
            this.mTableDst = DatabaseConfig.DEFAULT_LISTED_TECH_SEASON;
            break;
        case CYCLE_YEAR:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_SEASON;
            this.mTableDst = DatabaseConfig.DEFAULT_LISTED_TECH_YEAR;
            break;
        default:
            log.error("Unknown calculate type or not define to set table");
            break;
        }
    }
    
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
        Utility.timerStart();
        /*
        try {
            AverageDatabaseHandler mStockDB = new AverageDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
            ArrayList<String> mStockIdList = new ArrayList<>();
            mStockIdList = mStockDB.queryAllStockId();
            AverageKCalculateHandler mCalculator = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_MONTH);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_SEASON);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_YEAR);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        
        try {
            AverageKCalculateHandler mCalulate = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
            mCalulate.calculateValueWholeData();
            mCalulate = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_MONTH);
            mCalulate.calculateValueWholeData();
            mCalulate = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_SEASON);
            mCalulate.calculateValueWholeData();
            mCalulate = new AverageKCalculateHandler(KeyDefine.CalculateCycle.CYCLE_YEAR);
            mCalulate.calculateValueWholeData();
        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e);
        }
        
        Utility.timerEnd();
    }
}
