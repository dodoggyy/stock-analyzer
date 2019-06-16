/**
 * 
 */
package com.common;

import java.util.ArrayList;

/**
 * @author Chris Lin
 *
 */
public class BackTestInfo {
    private int mInitialCurrency; // 初始資金
    private int mBackTestTime; // 回測時間

    private int mInStock ;// 庫存
    private ArrayList<Integer> mBuyVolume; // 買進張數
    private ArrayList<Integer> mSellVolume; // 賣出張數
    private int mBuyTimes; // 買進次數
    private int mSellTimes; // 賣出次數
    private float mTransactionWin; // 交易勝率

    private int mProfitAmount; // 獲利金額
    private int mTradeFee; // 累積交易手續費
    private float mTotalROI; // 累積投資報酬率
    private float mIRR; // 年化報酬率
    /**
     * @return the mInitialCurrency
     */
    public int getmInitialCurrency() {
        return mInitialCurrency;
    }
    /**
     * @param mInitialCurrency the mInitialCurrency to set
     */
    public void setmInitialCurrency(int mInitialCurrency) {
        this.mInitialCurrency = mInitialCurrency;
    }
    /**
     * @return the mBackTestTime
     */
    public int getmBackTestTime() {
        return mBackTestTime;
    }
    /**
     * @param mBackTestTime the mBackTestTime to set
     */
    public void setmBackTestTime(int mBackTestTime) {
        this.mBackTestTime = mBackTestTime;
    }
    /**
     * @return the mBuyTimes
     */
    public int getmBuyTimes() {
        return mBuyTimes;
    }
    /**
     * @param mBuyTimes the mBuyTimes to set
     */
    public void setmBuyTimes(int mBuyTimes) {
        this.mBuyTimes = mBuyTimes;
    }
    /**
     * @return the mSellTimes
     */
    public int getmSellTimes() {
        return mSellTimes;
    }
    /**
     * @param mSellTimes the mSellTimes to set
     */
    public void setmSellTimes(int mSellTimes) {
        this.mSellTimes = mSellTimes;
    }
    /**
     * @return the mTransactionWin
     */
    public float getmTransactionWin() {
        return mTransactionWin;
    }
    /**
     * @param mTransactionWin the mTransactionWin to set
     */
    public void setmTransactionWin(float mTransactionWin) {
        this.mTransactionWin = mTransactionWin;
    }
    /**
     * @return the mProfitAmount
     */
    public int getmProfitAmount() {
        return mProfitAmount;
    }
    /**
     * @param mProfitAmount the mProfitAmount to set
     */
    public void setmProfitAmount(int mProfitAmount) {
        this.mProfitAmount = mProfitAmount;
    }
    /**
     * @return the mTotalROI
     */
    public float getmTotalROI() {
        return mTotalROI;
    }
    /**
     * @param mTotalROI the mTotalROI to set
     */
    public void setmTotalROI(float mTotalROI) {
        this.mTotalROI = mTotalROI;
    }
    /**
     * @return the mIRR
     */
    public float getmIRR() {
        return mIRR;
    }
    /**
     * @param mIRR the mIRR to set
     */
    public void setmIRR(float mIRR) {
        this.mIRR = mIRR;
    }
    /**
     * @return the mInStock
     */
    public int getmInStock() {
        return mInStock;
    }
    /**
     * @param mInStock the mInStock to set
     */
    public void setmInStock(int mInStock) {
        this.mInStock = mInStock;
    }
    /**
     * @return the mBuyVolume
     */
    public ArrayList<Integer> getmBuyVolume() {
        return mBuyVolume;
    }
    /**
     * @param mBuyVolume the mBuyVolume to set
     */
    public void setmBuyVolume(ArrayList<Integer> mBuyVolume) {
        this.mBuyVolume = mBuyVolume;
    }
    /**
     * @return the mSellVolume
     */
    public ArrayList<Integer> getmSellVolume() {
        return mSellVolume;
    }
    /**
     * @param mSellVolume the mSellVolume to set
     */
    public void setmSellVolume(ArrayList<Integer> mSellVolume) {
        this.mSellVolume = mSellVolume;
    }
    /**
     * @return the mTradeFee
     */
    public int getmTradeFee() {
        return mTradeFee;
    }
    /**
     * @param mTradeFee the mTradeFee to set
     */
    public void setmTradeFee(int mTradeFee) {
        this.mTradeFee = mTradeFee;
    }

}
