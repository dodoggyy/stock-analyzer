/**
 * 
 */
package com.backtest;


import java.sql.SQLException;
import java.text.ParseException;

import com.common.BackTestInfo;
import com.common.KeyDefine;
import com.common.StockInformation;

/**
 * @author Chris Lin
 *
 */
public abstract class BaseBackTestHandler {
    protected StockInformation mStockInfo;
    protected TradeStrategyHandler mAppearance;
    protected TradeStrategyHandler mArrival;
    protected BackTestInfo mBackTestInfo;
    protected String mStockID;

    public BaseBackTestHandler(String aStockID) throws SQLException, ParseException {
        this.mStockInfo = new StockInformation(aStockID);
        this.mStockInfo.getDbData("6116", "2019-05-01", KeyDefine.EnQueryType.EN_QUERY_TECH);
        this.mBackTestInfo = new BackTestInfo();
        this.mAppearance = new TradeStrategyHandler();
        this.mArrival = new TradeStrategyHandler();
        this.mStockID = aStockID;
    }

    /**
     * @return the mStockInfo
     */
    public StockInformation getStockInfo() {
        return mStockInfo;
    }

    /**
     * @param mStockInfo the mStockInfo to set
     */
    public void setStockInfo(StockInformation mStockInfo) {
        this.mStockInfo = mStockInfo;
    }

    /**
     * @return the mAppearance
     */
    public TradeStrategyHandler getAppearance() {
        return mAppearance;
    }

    /**
     * @param mAppearance the mAppearance to set
     */
    public void setAppearance(TradeStrategyHandler mAppearance) {
        this.mAppearance = mAppearance;
    }

    /**
     * @return the mArrival
     */
    public TradeStrategyHandler getArrival() {
        return mArrival;
    }

    /**
     * @param mArrival the mArrival to set
     */
    public void setArrival(TradeStrategyHandler mArrival) {
        this.mArrival = mArrival;
    }

    /**
     * @return the mBackTestInfo
     */
    public BackTestInfo getBackTestInfo() {
        return mBackTestInfo;
    }

    /**
     * @param mBackTestInfo the mBackTestInfo to set
     */
    public void setBackTestInfo(BackTestInfo mBackTestInfo) {
        this.mBackTestInfo = mBackTestInfo;
    }

    /**
     * @return the mStockID
     */
    public String getStockID() {
        return mStockID;
    }

    /**
     * @param mStockID the mStockID to set
     */
    public void setStockID(String mStockID) {
        this.mStockID = mStockID;
    }

}
