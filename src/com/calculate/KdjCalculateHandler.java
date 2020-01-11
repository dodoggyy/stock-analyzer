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
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import com.common.DatabaseConfig;
import com.common.KeyDefine;
import com.common.KeyDefine.CalculateCycle;
import com.common.StockTechInfo;
import com.common.Utility;
import com.database.KdjDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class KdjCalculateHandler extends BaseCalculateHandler {
    public class KDJ {
        public String stockID;
        public String stockDate; 
        public float K;
        public float D;
        public float J;
        
        public KDJ() {
            this.K = this.D = this.J = 0;
        }
        
        public KDJ(String stockID) {
            this.stockID = stockID;
            this.K = this.D = this.J = 0;
        }
    }
    
    private int KDJ_DAY = KeyDefine.KDJ_DAY;
    
    protected KdjDatabaseHandler mStockDB;
    private ArrayList<StockTechInfo> mTechSrc;
    private ArrayList<KDJ> mTechDst;
    private HashMap<String, ArrayList<KDJ>> mMap;
    private CalculateCycle mCycleType = KeyDefine.CalculateCycle.CYCLE_MAX;

    protected String mTableSrc;
    protected String mTableDst;

    public KdjCalculateHandler(CalculateCycle mCycleType) throws SQLException {
        // TODO Auto-generated constructor stub
        this.mCycleType = mCycleType;
        this.setTable();
        this.mStockDB = new KdjDatabaseHandler(mCycleType);
        mTechSrc = new ArrayList<>();
        mTechDst = new ArrayList<>();
        mMap = new HashMap<>();
    }
    
    @Override
    public void calculateValue(String aStockId, boolean bIsUpdateAll) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        ResultSet mResultSet = null;

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
                StockTechInfo mStockInfo = new StockTechInfo();
                mStockInfo.setStockDate(mResultSet.getString("stock_date"));
                mStockInfo.setStockID(aStockId);
                mStockInfo.setStockLow(Utility.int2Float(mResultSet.getInt("stock_low_price"), KeyDefine.DB_PRICE_SHIFT));
                mStockInfo.setStockHigh(Utility.int2Float(mResultSet.getInt("stock_high_price"), KeyDefine.DB_PRICE_SHIFT));
                mStockInfo.setStockClose(Utility.int2Float(mResultSet.getInt("stock_closing_price"), KeyDefine.DB_PRICE_SHIFT));
                mStockInfo.setStockOpen(Utility.int2Float(mResultSet.getInt("stock_opening_price"), KeyDefine.DB_PRICE_SHIFT));
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
                log.trace("Open:\t" + mTechInfo.getStockOpen() + " ");
                log.trace("High:\t" + mTechInfo.getStockHigh() + "");
                log.trace("Low:\t" + mTechInfo.getStockLow() + " ");
                log.trace("Close:\t" + mTechInfo.getStockClose() + "\n");
            } 
        }
        
        /**
                         * 前N-2天 KDJ不列入計算 設為0 第N-1天 KDJ設為50, N天開始做KDJ計算
        */
        float mMinPrice = Float.MAX_VALUE;
        float mMaxPrice = Float.MIN_VALUE;
        Queue<Float> mListMin = new LinkedList<>();
        Queue<Float> mListMax = new LinkedList<>();
        for(int i = 0; i < mTechSrc.size(); i++) {
            StockTechInfo mDataSrc = mTechSrc.get(i);
            KDJ mCurKDJ = new KDJ();
            mCurKDJ.stockDate = mDataSrc.getStockDate();
            mCurKDJ.stockID = mDataSrc.getStockID();
            
            mListMin.offer(mDataSrc.getStockLow());
            mListMax.offer(mDataSrc.getStockHigh());
            
            //log.trace(System.out.printf("%s Low: %.2f High: %.2f",mDataSrc.getStockDate(), mDataSrc.getStockLow(), mDataSrc.getStockHigh()));
            
            if(i >= KDJ_DAY) {
                float mTmpMin = mListMin.remove();
                float mTmpMax = mListMax.remove();
                if(mMinPrice == mTmpMin || mDataSrc.getStockLow() < mMinPrice) {
                    mMinPrice = Collections.min(mListMin);
                }
                if(mMaxPrice == mTmpMax || mDataSrc.getStockHigh() > mMaxPrice) {
                    mMaxPrice = Collections.max(mListMax);
                }
            }
            //log.trace(System.out.printf("%s min:%.2f, max:%.2f",mCurKDJ.stockDate, mMinPrice, mMaxPrice));
            
            if(i > (KDJ_DAY - 2)) {
                float mClosePrice = mDataSrc.getStockClose();
                //log.trace(System.out.printf("Close: %.2f",mClosePrice));
                
                float mRsv = ((mClosePrice - mMinPrice) / (mMaxPrice - mMinPrice)) * 100;
                //  need refine due to KDJ formula parameter RSV Kt weight always set as 3
                // 當日K值 =  2/3 前一日 K值 + 1/3 RSV
                // 當日D值 =  2/3 前一日 D值＋ 1/3 當日K值
                // J = 3*D - 2*K
                mCurKDJ.K = mRsv / 3 + (2 * mTechDst.get(mTechDst.size() - 1).K) / 3;
                mCurKDJ.D = mCurKDJ.K / 3 + (2 * mTechDst.get(mTechDst.size() - 1).D) / 3;
                mCurKDJ.J = 3 * mCurKDJ.D - 2 * mCurKDJ.K;
                
                //log.trace(System.out.printf("%s RSV:%.2f ,K: %.2f, D: %.2f, J: %.2f",mCurKDJ.stockDate, mRsv, mCurKDJ.K, mCurKDJ.D,mCurKDJ.J));
                mCurKDJ = checkKDJ(mCurKDJ);
            } else if (i < (KDJ_DAY - 2)) {
                mCurKDJ.K = 0;
                mCurKDJ.D = 0;
                mCurKDJ.J = 0;
                
            } else {
                mCurKDJ.K = 50;
                mCurKDJ.D = 50;
                mCurKDJ.J = 50;
            }
            mTechDst.add(mCurKDJ);
        }

        writeData2DB();
        clearData();
    }
    
    private KDJ checkKDJ(KDJ mKDJ) {
        if (mKDJ.K < 0 || Double.isNaN(mKDJ.K)) {
            mKDJ.K = 0;
        }
        if (mKDJ.K > 100) {
            mKDJ.K = 100;
        }
        if (mKDJ.D < 0 || Double.isNaN(mKDJ.D)) {
            mKDJ.D = 0;
        }
        if (mKDJ.D > 100) {
            mKDJ.D = 100;
        }
        if (Double.isNaN(mKDJ.J)) {
            mKDJ.J = 0;
        }
        
        return mKDJ;
    }
    
    /*
    public void calculateValueWholeData() throws SQLException, ParseException {
        KDJ mStockInfo = new KDJ();
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
    */
    
    // set table source
    public void setTable() throws SQLException {
        switch(mCycleType) {
        case CYCLE_DAY:
            this.mTableSrc = DatabaseConfig.TABLE_DAY_TECH;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_KDJ_DAY;
            break;
        case CYCLE_WEEK:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_WEEK;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_KDJ_WEEK;
            break;
        case CYCLE_MONTH:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_MONTH;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_KDJ_MONTH;
            break;
        case CYCLE_SEASON:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_SEASON;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_KDJ_SEASON;
            break;
        case CYCLE_YEAR:
            this.mTableSrc = DatabaseConfig.DEFAULT_LISTED_TECH_YEAR;
            this.mTableDst = DatabaseConfig.DEFAULT_TECH_KDJ_YEAR;
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
        for (KDJ mStockInfo : mTechDst) {
            try {
                mStockDB.generateSqlPrepareStrCmd(1, mStockInfo.stockID); // stock_id
                mStockDB.generateSqlPrepareStrCmd(2, mStockInfo.stockDate); // stock_date
                mStockDB.generateSqlPrepareIntCmd(3, Utility.float2Int(mStockInfo.K, KeyDefine.DB_PRICE_SHIFT) ); // K
                mStockDB.generateSqlPrepareIntCmd(4, Utility.float2Int(mStockInfo.D, KeyDefine.DB_PRICE_SHIFT)); // D
                mStockDB.generateSqlPrepareIntCmd(5, Utility.float2Int(mStockInfo.J, KeyDefine.DB_PRICE_SHIFT)); // J
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
     * @return the kDJ_DAY
     */
    public int getKDJ_DAY() {
        return KDJ_DAY;
    }

    /**
     * @param kDJ_DAY the kDJ_DAY to set
     */
    public void setKDJ_DAY(int kDJ_DAY) {
        KDJ_DAY = kDJ_DAY;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Utility.timerStart();
        
        try {
            KdjCalculateHandler mCalculator = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_DAY);
            //mCalculator.calculateValue("6116", true);
            ArrayList<String> mStockIdList = new ArrayList<>();
            KdjDatabaseHandler mStockDB = new KdjDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
            mStockIdList = mStockDB.queryAllStockId();
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_MONTH);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_SEASON);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
            
            mCalculator = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_YEAR);
            for (String mStockID : mStockIdList) {
                mCalculator.calculateValue(mStockID, false);
            }
        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        /*
        try {
            KdjCalculateHandler mCalulate = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
            mCalulate.calculateValueWholeData();
            mCalulate = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_MONTH);
            mCalulate.calculateValueWholeData();
            mCalulate = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_SEASON);
            mCalulate.calculateValueWholeData();
            mCalulate = new KdjCalculateHandler(KeyDefine.CalculateCycle.CYCLE_YEAR);
            mCalulate.calculateValueWholeData();
        } catch (SQLException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e);
        }
        */
        
        Utility.timerEnd();
    }

}
