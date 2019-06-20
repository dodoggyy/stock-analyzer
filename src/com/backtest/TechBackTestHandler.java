/**
 * 
 */
package com.backtest;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import com.common.KeyDefine;
import com.common.StockTechInfo;
import com.common.Utility;

/**
 * @author Chris Lin
 *
 */
public class TechBackTestHandler extends BaseBackTestHandler implements BaseBackTestInterface {

    public TechBackTestHandler(String aStockID, String aDate) throws SQLException, ParseException {
        super(aStockID, aDate);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void listParameter() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getResult() {
        // TODO Auto-generated method stub
        System.out.println("股票代號: " + this.getStockID());
        System.out.println("當前資金: " + mBackTestInfo.getInitialCurrency());
        System.out.println("回測時間(天): " + mBackTestInfo.getBackTestTime());
        System.out.println("庫存: " + mBackTestInfo.getInStock(this.getStockID()));
        System.out.println("庫存成本: " + mBackTestInfo.getInStockCost(this.getStockID()));

        System.out.println("買進數量紀錄:");
        Iterator mIter = mBackTestInfo.getBuyLog().entrySet().iterator();
        while (mIter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) mIter.next();
            System.out.println(mEntry.getKey() + " " + mEntry.getValue());
        }

        System.out.println("賣出數量紀錄:");
        mIter = mBackTestInfo.getSellLog().entrySet().iterator();
        while (mIter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) mIter.next();
            System.out.println(mEntry.getKey() + " " + mEntry.getValue());
        }

        System.out.println("買進次數: " + mBackTestInfo.getBuyTimes());
        System.out.println("賣出次數: " + mBackTestInfo.getSellTimes());
        System.out.println("獲利次數: " + mBackTestInfo.getWinTimes());
        System.out.println("勝率: " + mBackTestInfo.getTransactionWinPercentage());

        System.out.println("已實現損益: " + mBackTestInfo.getProfitAmount(this.getStockID()));
        System.out.println("未實現損益: " + (int) getUnrealizedGains(this.getStockID()));
        System.out.println("總交易手續費: " + (int) mBackTestInfo.getTradeFee());
        System.out.println("總進場金額: " + (int) mBackTestInfo.getTotalEntryAmount());
        System.out.println("總出場金額: " + (int) mBackTestInfo.getTotalExitAmount());
        System.out.println("累積投資報酬率: " + mBackTestInfo.getProfitAmount(this.getStockID())/mBackTestInfo.getTotalExitAmount() + "%");
        // 年化報酬率(%) = (總報酬率+1)^(1/年數) -1
        double mYears = ((double)mBackTestInfo.getBackTestTime()/(double)KeyDefine.DAY_PER_YEAR);
//        System.out.println(mYears);
        System.out.println("年化報酬率: " +  (Math.pow((double)mBackTestInfo.getProfitAmount(this.getStockID()) + 1, (double)1/mYears) - 1) + "%");
    }

    public float getUnrealizedGains(String aStockID) {
        boolean bIsSell = true;
        // 當前庫存
        int mInStock = mBackTestInfo.getInStock(aStockID);
        // 持有成本＝(成交價＊股數)＋買進手續費(已含在庫存成本)
        float mTotalInStock = mBackTestInfo.getInStockCost(aStockID) * mInStock;
        // 抓取資料最後交易日之收盤價
        float mLastDateClosePrice = Utility.getMapLastKey(this.getStockInfo().getStockTechInfo()).getValue()
                .getStockClose();
        // 預估收入＝(市價＊股數)－賣出手續費－交易稅
        float mExpectedIncome = mLastDateClosePrice * mBackTestInfo.getInStock(aStockID)
                - calculateFee(mInStock, bIsSell);

        // 預估損益＝預估收入－持有成本
        return (mExpectedIncome - mTotalInStock);
    }

    public void execute() {

    }

    /**
     * buy behavior
     * 
     * @param aStockID
     * @param aDate
     * @param aBuyVolume
     * @return
     */
    public boolean buy(String aStockID, String aDate, int aBuyVolume) {
        boolean bIsSell = false;
        StockTechInfo mInfo = mStockInfo.getStockTechInfo(mStockInfo.getStockInfoKey(aStockID, aDate));
        float mNumShare = mInfo.getStockClose() * aBuyVolume;

        if (mNumShare > mBackTestInfo.getInitialCurrency() || mNumShare <= 0) {
            System.out.println("not enough money to buy or illegal input");
            return false;
        } else {
            // 當次交易手續費
            float mTotalFee = calculateFee(mNumShare, bIsSell);
            // 該股總庫存 = 庫存數量 + 買進數量
            int mTotalInStock = mBackTestInfo.getInStock(aStockID) + aBuyVolume;
            // 總成本 = (庫存數量*庫存每張成本 + 買進數量*買進價格 + 手續費)/(庫存數量+買進數量)
            float mTradeCost = mInfo.getStockClose() * aBuyVolume + mTotalFee;
            float mTotalCost = (mBackTestInfo.getInStock(aStockID) * mBackTestInfo.getInStockCost(aStockID)
                    + mTradeCost) / (mTotalInStock);
            mBackTestInfo.setInStockCost(aStockID, mTotalCost);
            mBackTestInfo.setInStock(aStockID, aBuyVolume, bIsSell);
            // 買進紀錄
            mBackTestInfo.setBuyLog(aDate, aBuyVolume);
            // 更新資金
            mBackTestInfo.updateInitialCurrency((int) mTradeCost, bIsSell);
            // 累計手續費
            mBackTestInfo.addTradeFee(mTotalFee);
            // 累計進場金額
            mBackTestInfo.addTotalEntryAmount(mTradeCost);

            return true;
        }
    }

    /**
     * sell behavior
     * 
     * @param aStockID
     * @param aDate
     * @param aSellVolume
     * @return
     */
    public boolean sell(String aStockID, String aDate, int aSellVolume) {
        boolean bIsSell = true;
        StockTechInfo mInfo = mStockInfo.getStockTechInfo(mStockInfo.getStockInfoKey(aStockID, aDate));
        float mNumShare = mInfo.getStockClose() * aSellVolume;

        if ((aSellVolume > mBackTestInfo.getInStock(aStockID)) || (aSellVolume <= 0)) {
            System.out.println("not enough stock to sell or illegal input");
            return false;
        } else {
            // 當次交易手續費
            float mTotalFee = calculateFee(mNumShare, bIsSell);
            // 該股總庫存
            int mTotalInStock = mBackTestInfo.getInStock(aStockID) - aSellVolume;
            // 出場金額 = 最後交易日收盤價 * 賣出數量 - 手續費
            int mExitAmount = (int) (Utility.getMapLastKey(mStockInfo.getStockTechInfo()).getValue().getStockClose()
                    * aSellVolume - mTotalFee);
            // 損益 = (庫存每張成本 -賣出價格) * 賣出數量* - 手續費)
            int mPorfit = (int) (mExitAmount - mInfo.getStockClose() * aSellVolume);
            mBackTestInfo.setProfitAmount(aStockID, mPorfit);
            // 更新庫存
            mBackTestInfo.setInStock(aStockID, aSellVolume, bIsSell);
            // 賣出紀錄
            mBackTestInfo.setSellLog(aDate, aSellVolume);
            // 更新資金
            mBackTestInfo.updateInitialCurrency(mExitAmount, bIsSell);
            // 累計手續費
            mBackTestInfo.addTradeFee(mTotalFee);
            // 累計出場金額
            mBackTestInfo.addTotalExitAmount(mExitAmount);

        }

        return true;
    }

    private float calculateFee(float aCost, boolean bIsSell) {
        float mTotalFee = 0;

        mTotalFee = aCost * KeyDefine.SECURITIES_FEE_PERCETAGE * KeyDefine.SECURITIES_FEE_DISCOUNT;
        if (bIsSell) { // sell case
            mTotalFee += aCost * KeyDefine.TRADE_TAX; // 賣出證交稅
        }

        return mTotalFee;
    }

    /**
     * @param args
     * @throws ParseException
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        TechBackTestHandler mBackTest = new TechBackTestHandler("6116", "2019-05-01");
        mBackTest.mBackTestInfo.setInitialCurrency(100000);
        mBackTest.mBackTestInfo.setBackTestTime(240);
        mBackTest.mStockInfo.setStockID("6116");
        mBackTest.buy("6116", "2019-05-06", 5000);
        mBackTest.sell("6116", "2019-06-03", 2000);
        mBackTest.sell("6116", "2019-06-04", 0);

        mBackTest.getResult();

    }
}
