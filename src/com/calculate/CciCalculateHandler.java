/**
 * 
 */
package com.calculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.common.DatabaseConfig;
import com.common.KeyDefine;
import com.common.KeyDefine.CalculateCycle;
import com.common.StockTechInfo;
import com.common.Utility;
import com.database.CciDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class CciCalculateHandler extends BaseCalculateHandler {
    public class CCI {
        public String stockID;
        public String stockDate;
        public float cci;
        public float mAPs;
        public float mMAPs;
        public float mMDs;
        
        public CCI() {
            this.cci = this.mAPs = this.mMAPs = this.mMDs = 0;
        }
    }
    
    private int CCI_DAY = KeyDefine.CCI_DAY;
    
    protected CciDatabaseHandler mStockDB;
    private ArrayList<StockTechInfo> mTechSrc;
    private ArrayList<CCI> mTechDst;
    private HashMap<String, ArrayList<StockTechInfo>> mMap;
    private CalculateCycle mCycleType = KeyDefine.CalculateCycle.CYCLE_MAX;

    protected String mTableSrc;
    protected String mTableDst;

    public CciCalculateHandler(CalculateCycle mCycleType) throws SQLException {
        // TODO Auto-generated constructor stub
        this.mCycleType = mCycleType;
        this.setTable();
        this.mStockDB = new CciDatabaseHandler(mCycleType);
        mTechSrc = new ArrayList<>();
        mTechDst = new ArrayList<>();
        mMap = new HashMap<>();
    }
    
    @Override
    public void calculateValue(String aStockId, boolean bIsUpdateAll) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        ResultSet mResultSet = null;

        mSqlWhere = " WHERE stock_id=" + "'" + aStockId + "'";
        // no need to query open for CCI
        mSqlQueryCmd = "SELECT stock_date,stock_high_price,stock_low_price,stock_closing_price, stock_volume FROM "
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
                StockTechInfo mStockInfo = new StockTechInfo();
                mStockInfo.setStockDate(mResultSet.getString("stock_date"));
                mStockInfo.setStockID(aStockId);
                mStockInfo.setStockLow(Utility.int2Float(mResultSet.getInt("stock_low_price"), KeyDefine.DB_PRICE_SHIFT));
                mStockInfo.setStockHigh(Utility.int2Float(mResultSet.getInt("stock_high_price"), KeyDefine.DB_PRICE_SHIFT));
                mStockInfo.setStockClose(Utility.int2Float(mResultSet.getInt("stock_closing_price"), KeyDefine.DB_PRICE_SHIFT));
                mTechSrc.add(mStockInfo);
            }
            mResultSet.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mResultSet.close();
        if(log.isTraceEnabled()) {
            for (StockTechInfo mTechInfo : mTechSrc) {
                log.trace(mTechInfo.getStockDate() + " ");
                log.trace("High:\t" + mTechInfo.getStockHigh() + "");
                log.trace("Low:\t" + mTechInfo.getStockLow() + " ");
                log.trace("Close:\t" + mTechInfo.getStockClose() + "\n");
            } 
        }
        
        /**
                       * 前N-1天只計算AP ,第N天開始做MAP計算  ,第(N-1)*2天開始做MD,CCI計算
         * AP: Average Price
         * MAP: Mean of Average Prices
         * MD: Mean Deviation
         */
        for(int i = 0; i < mTechSrc.size(); i++) {
            int mInitialDay = i - CCI_DAY + 1;
            StockTechInfo mDataSrc = mTechSrc.get(i);
            CCI mCurCCI = new CCI();
            
            mCurCCI.stockDate = mDataSrc.getStockDate();
            mCurCCI.stockID = mDataSrc.getStockID();
            //Average Price
            mCurCCI.mAPs = (mDataSrc.getStockHigh() + mDataSrc.getStockLow() + mDataSrc.getStockClose() )/3;
            
            if(i >= (CCI_DAY - 1)) {
                float mTotalMean = 0;
                float mTotalMeanDeviation = 0;

                //Mean of Average Prices
                for(int j = mInitialDay; j < i; j++) {
                    mTotalMean += (mTechDst.get(j).mAPs);
                }
                mTotalMean += mCurCCI.mAPs;
                mCurCCI.mMAPs = mTotalMean / CCI_DAY;
                log.trace("mMap: " + mCurCCI.mMAPs);
                
                //According to formula, it may valid start from 2*(CCI_DAY -1)
                if(i > (CCI_DAY - 1) * 2) {
                    // Mean Deviation
                    for(int k = mInitialDay; k < i; k++) {
                        mTotalMeanDeviation += Math.abs(mTechDst.get(k).mMAPs - mTechDst.get(k).mAPs);
                    }
                    mTotalMeanDeviation += Math.abs(mCurCCI.mMAPs - mCurCCI.mAPs);
                    mCurCCI.mMDs = mTotalMeanDeviation / CCI_DAY;
                    mCurCCI.cci = (float) ((mCurCCI.mAPs - mCurCCI.mMAPs) / (0.015 * mCurCCI.mMDs));
                    mCurCCI = checkCCI(mCurCCI);
                    log.trace(mCurCCI.stockDate + " CCI: " + mCurCCI.cci + "mMAPs: " + mCurCCI.mMAPs);
                    log.trace("mAPs: " + mCurCCI.mAPs + "mMDs: " + mCurCCI.mMDs);
                }
            }
            mTechDst.add(mCurCCI);
        }

        writeData2DB();
        clearData();
    }
    
    private CCI checkCCI(CCI mCCI) {
        if (Double.isNaN(mCCI.cci)) {
            mCCI.cci = KeyDefine.CCI_LOWER_BOUND;
        }
        if(mCCI.cci > KeyDefine.CCI_UPPER_BOUND) {
            mCCI.cci = KeyDefine.CCI_UPPER_BOUND;
        } else if(mCCI.cci < KeyDefine.CCI_LOWER_BOUND) {
            mCCI.cci = KeyDefine.CCI_LOWER_BOUND;
        }
        
        return mCCI;
    }

    public void calculateValueWholeData() throws SQLException, ParseException {
        StockTechInfo mStockInfo = new StockTechInfo();
        ResultSet mResultSet = null;

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
                mStockInfo.setStockLow(Utility.int2Float(mResultSet.getInt("stock_low_price"), KeyDefine.DB_PRICE_SHIFT));
                mStockInfo.setStockHigh(Utility.int2Float(mResultSet.getInt("stock_high_price"), KeyDefine.DB_PRICE_SHIFT));
                mStockInfo.setStockClose(Utility.int2Float(mResultSet.getInt("stock_closing_price"), KeyDefine.DB_PRICE_SHIFT));

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

            /**
                                 * 前N-1天只計算AP ,第N天開始做MAP計算  ,第(N-1)*2天開始做MD,CCI計算
            * AP: Average Price
            * MAP: Mean of Average Prices
            * MD: Mean Deviation
            */
            
            for(int i = 0; i < mList.size(); i++) {
                int mInitialDay = i - CCI_DAY + 1;
                StockTechInfo mDataSrc = mList.get(i);
                CCI mCurCCI = new CCI();
                
                mCurCCI.stockDate = mDataSrc.getStockDate();
                mCurCCI.stockID = mDataSrc.getStockID();
                //Average Price
                mCurCCI.mAPs = (mDataSrc.getStockHigh() + mDataSrc.getStockLow() + mDataSrc.getStockClose() )/3;
                
                if(i >= CCI_DAY - 1) {
                    float mTotalMean = 0;
                    float mTotalMeanDeviation = 0;

                    //Mean of Average Prices
                    for(int j = mInitialDay; j < i; j++) {
                        mTotalMean += (mTechDst.get(j).mAPs);
                    }
                    mTotalMean += mCurCCI.mAPs;
                    mCurCCI.mMAPs = mTotalMean / CCI_DAY;
                    log.trace("mMap: " + mCurCCI.mMAPs);
                    
                    //According to formula, it may valid start from 2*(CCI_DAY -1)
                    if(i > (CCI_DAY - 1) * 2) {
                        // Mean Deviation
                        for(int k = mInitialDay; k < i; k++) {
                            mTotalMeanDeviation += Math.abs(mTechDst.get(k).mMAPs - mTechDst.get(k).mAPs);
                        }
                        mTotalMeanDeviation += Math.abs(mCurCCI.mMAPs - mCurCCI.mAPs);
                        mCurCCI.mMDs = mTotalMeanDeviation / CCI_DAY;
                        mCurCCI.cci = (float) ((mCurCCI.mAPs - mCurCCI.mMAPs) / (0.015 * mCurCCI.mMDs));
                        mCurCCI = checkCCI(mCurCCI);
                    }
                }

                mTechDst.add(mCurCCI);
            }
            writeData2DB();
            clearData();
            mIter.remove();
        }
    }
    
    // set table source
    public void setTable() throws SQLException {
        switch(mCycleType) {
        case CYCLE_DAY:
            this.mTableSrc = DatabaseConfig.TABLE_DAY_TECH;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_CCI_DAY;
            break;
        case CYCLE_WEEK:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_WEEK;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_CCI_WEEK;
            break;
        case CYCLE_MONTH:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_MONTH;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_CCI_MONTH;
            break;
        case CYCLE_SEASON:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_SEASON;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_CCI_SEASON;
            break;
        case CYCLE_YEAR:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_YEAR;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_CCI_YEAR;
            break;
        default:
            log.error("Unknown calculate type or not define to set table");
            break;
        }
    }
    
    public void clearData() {
        mTechSrc.clear();
        mTechDst.clear();
    }

    @Override
    boolean writeData2DB() throws SQLException {
        // TODO Auto-generated method stub
        for (CCI mStockInfo : mTechDst) {
            try {
                mStockDB.generateSqlPrepareStrCmd(1, mStockInfo.stockID); // stock_id
                mStockDB.generateSqlPrepareStrCmd(2, mStockInfo.stockDate); // stock_date
                mStockDB.generateSqlPrepareIntCmd(3, Utility.float2Int(mStockInfo.cci, KeyDefine.DB_PRICE_SHIFT) ); // K
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
     * @return the CCI_DAY
     */
    public int getCCI_DAY() {
        return CCI_DAY;
    }

    /**
     * @param CCI_DAY the CCI_DAY to set
     */
    public void setCCI_DAY(int CCI_DAY) {
        this.CCI_DAY = CCI_DAY;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Utility.timerStart();
        /*
        try {
            CciCalculateHandler mCalculator = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_DAY);
//            mCalculator.calculateValue("6116", true);
            ArrayList<String> mStockIdList = new ArrayList<>();
            CciDatabaseHandler mStockDB = new CciDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
            mStockIdList = mStockDB.queryAllStockId();
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_MONTH);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_SEASON);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_YEAR);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        
        
        try {
            CciCalculateHandler mCalulate = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_DAY);
            mCalulate.calculateValueWholeData();
            mCalulate = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
            mCalulate.calculateValueWholeData();
            mCalulate = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_MONTH);
            mCalulate.calculateValueWholeData();
            mCalulate = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_SEASON);
            mCalulate.calculateValueWholeData();
            mCalulate = new CciCalculateHandler(KeyDefine.CalculateCycle.CYCLE_YEAR);
            mCalulate.calculateValueWholeData();
        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e);
        }
        
        
        Utility.timerEnd();
    }

}
