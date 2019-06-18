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

/**
 * @author Chris Lin
 *
 */
public class TechBackTestHandler extends BaseBackTestHandler implements BaseBackTestInterface {

    public TechBackTestHandler(String aStockID) throws SQLException, ParseException {
        super(aStockID);
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
        // todo
        System.out.println("未實現損益: " + mBackTestInfo.getUnrealizedGains(this.getStockID()));
        System.out.println("總交易手續費: " + mBackTestInfo.getTradeFee());
        // todo
        System.out.println("累積投資報酬率: " + mBackTestInfo.getTotalROI(this.getStockID()));
        // todo
        System.out.println("年化報酬率: " + mBackTestInfo.getIRR());
    }

    public void execute() {

    }

    public int getUnrealizedProfit(String aStockID, String aDate) {
        StockTechInfo mInfo = mStockInfo.getStockTechInfo(mStockInfo.getStockInfoKey(aStockID, aDate));
        return 0;
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
        StockTechInfo mInfo = mStockInfo.getStockTechInfo(mStockInfo.getStockInfoKey(aStockID, aDate));
        float mNumShare = mInfo.getStockClose() * aBuyVolume;

        if (mNumShare > mBackTestInfo.getInitialCurrency() || mNumShare <= 0) {
            System.out.println("not enough money to buy or illegal input");
            return false;
        } else {
            // 當次交易手續費
            float mTotalFee = calculateFee(mNumShare, false);
            // 該股總庫存 = 庫存數量 + 買進數量
            int mTotalInStock = mBackTestInfo.getInStock(aStockID) + aBuyVolume;
            // 總成本 = (庫存數量*庫存每張成本 + 買進數量*買進價格 + 手續費)/(庫存數量+買進數量)
            float mTradeCost = mInfo.getStockClose() * aBuyVolume + mTotalFee;
            float mTotalCost = (mBackTestInfo.getInStock(aStockID) * mBackTestInfo.getInStockCost(aStockID)
                    + mTradeCost) / (mTotalInStock);
            mBackTestInfo.setInStockCost(aStockID, mTotalCost);
            mBackTestInfo.setInStock(aStockID, aBuyVolume, false);
            // 買進紀錄
            mBackTestInfo.setBuyLog(aDate, aBuyVolume);
            // 更新資金
            mBackTestInfo.updateInitialCurrency((int) mTradeCost, false);
            // 累計手續費
            mBackTestInfo.addTradeFee(mTotalFee);

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
        StockTechInfo mInfo = mStockInfo.getStockTechInfo(mStockInfo.getStockInfoKey(aStockID, aDate));
        float mNumShare = mInfo.getStockClose() * aSellVolume;

        if ((aSellVolume > mBackTestInfo.getInStock(aStockID)) || (aSellVolume <=0)) {
            System.out.println("not enough stock to sell or illegal input");
            return false;
        } else {
            // 當次交易手續費
            float mTotalFee = calculateFee(mNumShare, true);
            // 該股總庫存
            int mTotalInStock = mBackTestInfo.getInStock(aStockID) - aSellVolume;
            // 損益 = (庫存每張成本 -賣出價格) * 賣出數量* - 手續費)
            int mPorfit = (int) ((mBackTestInfo.getInStockCost(aStockID) - mInfo.getStockClose()) * aSellVolume
                    - mTotalFee);
            mBackTestInfo.setProfitAmount(aStockID, mPorfit);
            // 更新庫存
            mBackTestInfo.setInStock(aStockID, aSellVolume, true);
            // 賣出紀錄
            mBackTestInfo.setSellLog(aDate, aSellVolume);
            // 更新資金
            mBackTestInfo.updateInitialCurrency(mPorfit, true);
            // 累計手續費
            mBackTestInfo.addTradeFee(mTotalFee);

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
        TechBackTestHandler mBackTest = new TechBackTestHandler("6116");
        mBackTest.mBackTestInfo.setInitialCurrency(100000);
        mBackTest.mBackTestInfo.setBackTestTime(365);
        mBackTest.mStockInfo.setStockID("6116");
        mBackTest.buy("6116", "2019-05-20", 5000);
        mBackTest.sell("6116", "2019-06-03", 2000);
        mBackTest.sell("6116", "2019-06-04", 0);

        mBackTest.getResult();
    }
}
