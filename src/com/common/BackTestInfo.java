/**
 * 
 */
package com.common;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Chris Lin
 * @version 1.0
 */
public class BackTestInfo {
    private float mInitialCurrency; // 初始資金
    private int mBackTestTime; // 回測時間(天)

    private HashMap<String, Integer> mInStock;// 庫存, K:stock id, V:quantity
    private HashMap<String, Float> mInStockCost;// 庫存成本, K:stock id, V:quantity
    private LinkedHashMap<String, Float> mBuyLog; // 買進數量紀錄 K:date, V:quantity
    private LinkedHashMap<String, Float> mSellLog; // 賣出數量紀錄 K:date, V:quantity
    private int mBuyTimes; // 買進次數
    private int mSellTimes; // 賣出次數
    private int mWinTimes; // 獲利次數

    private HashMap<String, Integer> mProfitAmount; // 已實現損益, K:stock id,
                                                    // V:profit
    private float mTradeFee; // 總交易手續費
    private float mTotalEntryAmount; // 總進場金額
    private float mTotalExitAmount; // 總出場金額

    public BackTestInfo() {
        this.mInStock = new HashMap<>();
        this.mInStockCost = new HashMap<>();
        this.mBuyLog = new LinkedHashMap<>();
        this.mSellLog = new LinkedHashMap<>();
        this.mProfitAmount = new HashMap<>();
        this.mBuyTimes = 0;
        this.mSellTimes = 0;
        this.mWinTimes = 0;
    }

    /**
     * @return the mInitialCurrency
     */
    public float getInitialCurrency() {
        return mInitialCurrency;
    }

    /**
     * @param mInitialCurrency
     *            the mInitialCurrency to set
     */
    public void setInitialCurrency(int mInitialCurrency) {
        this.mInitialCurrency = mInitialCurrency;
    }

    /**
     * @param mInitialCurrency
     *            the mInitialCurrency to update
     */
    public void updateInitialCurrency(int mUpdateCurrency, boolean bIsSell) {
        if (!bIsSell) {
            mUpdateCurrency = -mUpdateCurrency;
        }
        this.mInitialCurrency += mUpdateCurrency;
    }

    /**
     * @return the mBackTestTime
     */
    public int getBackTestTime() {
        return mBackTestTime;
    }

    /**
     * @param mBackTestTime
     *            the mBackTestTime to set
     */
    public void setBackTestTime(int mBackTestTime) {
        this.mBackTestTime = mBackTestTime;
    }

    /**
     * @return the mBuyTimes
     */
    public int getBuyTimes() {
        return mBuyTimes;
    }

    /**
     * @param mBuyTimes
     *            the mBuyTimes to set
     */
    public void addBuyTimes() {
        this.mBuyTimes = this.getBuyTimes() + 1;
    }

    /**
     * @return the mSellTimes
     */
    public int getSellTimes() {
        return mSellTimes;
    }

    /**
     * @param mSellTimes
     *            the mSellTimes to set
     */
    public void addSellTimes() {
        this.mSellTimes = this.getSellTimes() + 1;
    }

    /**
     * @return the mTransactionWin
     */
    public float getTransactionWinPercentage() {
        return (getSellTimes() == 0) ? 0 : (getWinTimes() / getSellTimes())*100;
    }

    /**
     * @return the mTradeFee
     */
    public float getTradeFee() {
        return mTradeFee;
    }

    /**
     * @param mTradeFee
     *            the mTradeFee to set
     */
    public void addTradeFee(float mTradeFee) {
        if (mTradeFee < 1) {
            mTradeFee = 1;
        }
        this.mTradeFee = this.getTradeFee() + mTradeFee;
    }

    /**
     * @return the mInStock
     */
    public HashMap<String, Integer> getInStock() {
        return mInStock;
    }

    /**
     * @return the mInStock
     */
    public int getInStock(String aStockID) {
        return (mInStock.containsKey(aStockID)) ? mInStock.get(aStockID) : 0;
    }

    /**
     * @param mInStock
     *            the mInStock to set
     */
    public void setInStock(HashMap<String, Integer> mInStock) {
        this.mInStock = mInStock;
    }

    /**
     * @param mInStock
     *            the mInStock to set
     */
    public void setInStock(String aStockID, int mNumsOfShare, boolean bIsSell) {
        if (bIsSell) {
            mNumsOfShare = -mNumsOfShare;
        }
        mInStock.put(aStockID, mInStock.getOrDefault(aStockID, 0) + mNumsOfShare);
    }

    /**
     * @return the mBuyLog
     */
    public LinkedHashMap<String, Float> getBuyLog() {
        return mBuyLog;
    }

    /**
     * @param mBuyLog
     *            the mBuyLog to set
     */
    public void setBuyLog(LinkedHashMap<String, Float> mBuyLog) {
        this.mBuyLog = mBuyLog;
    }

    /**
     * @param mBuyLog
     *            the mBuyLog to set
     */
    public void setBuyLog(String aDate, float aBuyVolume) {
        this.mBuyLog.put(aDate, mBuyLog.getOrDefault(aDate, 0f) + aBuyVolume);
        // 累加買進次數
        this.addBuyTimes();
    }

    /**
     * @return the mSellLog
     */
    public LinkedHashMap<String, Float> getSellLog() {
        return mSellLog;
    }

    /**
     * @param mSellLog
     *            the mSellLog to set
     */
    public void setSellLog(LinkedHashMap<String, Float> mSellLog) {
        this.mSellLog = mSellLog;
    }

    /**
     * @param mSellLog
     *            the mSellLog to set
     */
    public void setSellLog(String aDate, float aSellVolume) {
        this.mSellLog.put(aDate, mSellLog.getOrDefault(aDate, 0f) + aSellVolume);
        // 累加賣出次數
        this.addSellTimes();
    }

    /**
     * @return the mInStockCost
     */
    public HashMap<String, Float> getInStockCost() {
        return mInStockCost;
    }

    /**
     * @return the mInStockCost
     */
    public float getInStockCost(String aStockID) {
        return (mInStockCost.containsKey(aStockID)) ? mInStockCost.get(aStockID) : 0;
    }

    /**
     * @param mInStockCost
     *            the mInStockCost to set
     */
    public void setInStockCost(HashMap<String, Float> mInStockCost) {
        this.mInStockCost = mInStockCost;
    }

    /**
     * @param mInStockCost
     *            the mInStockCost to set
     */
    public void setInStockCost(String aStockID, float aCost) {
        this.mInStockCost.put(aStockID, aCost);
    }

    /**
     * @return the mProfitAmount
     */
    public HashMap<String, Integer> getProfitAmount() {
        return mProfitAmount;
    }

    /**
     * @return the mProfitAmount
     */
    public int getProfitAmount(String aStockID) {
        return (mProfitAmount.containsKey(aStockID)) ? mProfitAmount.get(aStockID) : 0;
    }

    /**
     * @param mProfitAmount
     *            the mProfitAmount to set
     */
    public void setProfitAmount(HashMap<String, Integer> mProfitAmount) {
        this.mProfitAmount = mProfitAmount;
    }

    /**
     * @param mProfitAmount
     *            the mProfitAmount to set
     */
    public void addProfitAmount(String aStockID, int mProfitAmount) {
        this.mProfitAmount.put(aStockID, this.mProfitAmount.getOrDefault(aStockID, 0) + mProfitAmount);
        if (mProfitAmount > 0)
            this.mWinTimes++;
    }

    /**
     * @return the mWinTimes
     */
    public int getWinTimes() {
        return mWinTimes;
    }

    /**
     * @param mWinTimes
     *            the mWinTimes to set
     */
    public void setWinTimes(int mWinTimes) {
        this.mWinTimes = mWinTimes;
    }

    public float getTotalEntryAmount() {
        return mTotalEntryAmount;
    }

    public void addTotalEntryAmount(float mTotalEntryAmount) {
        this.mTotalEntryAmount = this.getTotalEntryAmount() + mTotalEntryAmount;
    }

    public float getTotalExitAmount() {
        return mTotalExitAmount;
    }

    public void addTotalExitAmount(float mTotalExitAmount) {
        this.mTotalExitAmount = this.getTotalExitAmount() + mTotalExitAmount;
    }
}
