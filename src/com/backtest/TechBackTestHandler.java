/**
 * 
 */
package com.backtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.analyzer.TechAnalyzerHandler;
import com.analyzer.TechAnalyzerHandler.TechCsvStruct;
import com.common.Config;
import com.common.KeyDefine;
import com.common.StockInformation;
import com.common.StockTechInfo;
import com.common.TechAnalyzerInfo;
import com.common.Utility;
import com.common.KeyDefine.EnAnalyzeStrategyType;

/**
 * @author Chris Lin
 *
 */
public class TechBackTestHandler extends BaseBackTestHandler implements BaseBackTestInterface {
    private static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public TechBackTestHandler(String aStockID, String aDate) throws SQLException, ParseException {
        super(aStockID, aDate);
        // TODO Auto-generated constructor stub
        setParameter();
        listParameter();
    }

    @Override
    public void setParameter() {
        // TODO Auto-generated method stub
        // entry strategy setting
        TradeStrategyHandler mTradeEntry = new TradeStrategyHandler();
        TechAnalyzerInfo enEntryInfo = new TechAnalyzerInfo();
        enEntryInfo.setIsCCI(true);
        enEntryInfo.setIsKDJ(false);
        enEntryInfo.setIsBIAS(false);

        mTradeEntry.add(EnAnalyzeStrategyType.EN_ANALYZE_STRATEGY_TECH_CCI, enEntryInfo);
        mTradeEntry.add(EnAnalyzeStrategyType.EN_ANALYZE_STRATEGY_TECH_BIAS, enEntryInfo);
        mTradeEntry.add(EnAnalyzeStrategyType.EN_ANALYZE_STRATEGY_TECH_KDJ, enEntryInfo);
        this.setEntryStrategy(mTradeEntry);

        // exit strategy setting
        TradeStrategyHandler mTradeExit = new TradeStrategyHandler();
        TechAnalyzerInfo enExitInfo = new TechAnalyzerInfo();
        enExitInfo.setProfitPercentage(Config.DataAnalyze.BACK_TEST_PROFIT_PERCENT);

        mTradeExit.add(EnAnalyzeStrategyType.EN_ANALYZE_STRATEGY_FUND_PROFIT, enExitInfo);
        this.setExitStrategy(mTradeExit);
    }

    @Override
    public void listParameter() {
        // TODO Auto-generated method stub
        TradeStrategyHandler mEntryTrade = getEntryStrategy();

        int mIndex = 1;
        System.out.println("Entry Strategy:");
        for (EnAnalyzeStrategyType enType : mEntryTrade.getStrategy().keySet()) {
            System.out.println(mIndex + ":" + mEntryTrade.getStrategyName(enType));
            // System.out.println(mEntryTrade.getStrategy().get(enType).toString());
            mIndex++;
        }

        TradeStrategyHandler mExitTrade = getExitStrategy();

        mIndex = 1;
        System.out.println("Exit Strategy:");
        for (EnAnalyzeStrategyType enType : mExitTrade.getStrategy().keySet()) {
            System.out.println(mIndex + ":" + mExitTrade.getStrategyName(enType));
            // System.out.println(mEntryTrade.getStrategy().get(enType).toString());
            mIndex++;
        }
    }

    @Override
    public void getResult() throws IOException {
        // TODO Auto-generated method stub
        String mLineBreak = ",";
        String mLineTab = "  ";
        BufferedWriter mFileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Config.DataAnalyze.outputAnalyzerResultName, true), "UTF-8"));

        log.info("????????????: " + this.getStockID());
        log.info("????????????: " + mBackTestInfo.getInitialCurrency());
        log.info("????????????(???): " + mBackTestInfo.getBackTestTime());
        log.info("??????: " + mBackTestInfo.getInStock(this.getStockID()));
        log.info("????????????: " + mBackTestInfo.getInStockCost(this.getStockID()));

        mFileWriter.write(this.getStockID().toString() + mLineBreak); // ????????????
        mFileWriter.write(mBackTestInfo.getInitialCurrency() + mLineBreak); // ????????????
        mFileWriter.write(mBackTestInfo.getBackTestTime() + mLineBreak); // ????????????(???)
        mFileWriter.write(mBackTestInfo.getInStock(this.getStockID()) + mLineBreak); // ??????
        mFileWriter.write(mBackTestInfo.getInStockCost(this.getStockID()) + mLineBreak); // ????????????

        log.trace("??????????????????:");
        Iterator mIter = mBackTestInfo.getBuyLog().entrySet().iterator();
        while (mIter.hasNext()) { // ??????????????????
            Map.Entry mEntry = (Map.Entry) mIter.next();
            log.info(mEntry.getKey() + " " + mEntry.getValue());
            mFileWriter.write(mEntry.getKey() + " " + mEntry.getValue() + mLineTab);
        }
        mFileWriter.write(mLineBreak);

        log.info("??????????????????:");
        mIter = mBackTestInfo.getSellLog().entrySet().iterator();
        while (mIter.hasNext()) { // ??????????????????
            Map.Entry mEntry = (Map.Entry) mIter.next();
            log.info(mEntry.getKey() + " " + mEntry.getValue());
            mFileWriter.write(mEntry.getKey() + " " + mEntry.getValue() + mLineTab);
        }
        mFileWriter.write(mLineBreak);

        float mTotalROI = (mBackTestInfo.getProfitAmount(this.getStockID()) / mBackTestInfo.getTotalExitAmount()) * 100;

        // ???????????????(%) = (????????????+1)^(1/??????) -1
        float mYears = ((float) mBackTestInfo.getBackTestTime() / (float) KeyDefine.DAY_PER_YEAR);
        // System.out.println(mYears);
        float mIRR = (float) ((Math.pow((float) mTotalROI + 1, (float) 1 / mYears) - 1) * 100);

        log.info("????????????: " + mBackTestInfo.getBuyTimes());
        log.info("????????????: " + mBackTestInfo.getSellTimes());
        log.info("????????????: " + mBackTestInfo.getWinTimes());
        log.info("??????: " + mBackTestInfo.getTransactionWinPercentage() * 100);

        log.info("???????????????: " + mBackTestInfo.getProfitAmount(this.getStockID()));
        log.info("???????????????: " + (int) getUnrealizedGains(this.getStockID()));
        log.info("??????????????????: " + (int) mBackTestInfo.getTradeFee());
        log.info("???????????????: " + (int) mBackTestInfo.getTotalEntryAmount());
        log.info("???????????????: " + (int) mBackTestInfo.getTotalExitAmount());
        log.info("?????????????????????: " + mTotalROI + "%");
        log.info("???????????????: " + mIRR + "%");
        log.info("");

        mFileWriter.write(mBackTestInfo.getBuyTimes() + mLineBreak); // ????????????
        mFileWriter.write(mBackTestInfo.getSellTimes() + mLineBreak); // ????????????
        mFileWriter.write(mBackTestInfo.getWinTimes() + mLineBreak); // ????????????
        mFileWriter.write(mBackTestInfo.getTransactionWinPercentage() + mLineBreak); // ??????

        mFileWriter.write(mBackTestInfo.getProfitAmount(this.getStockID()) + mLineBreak); // ???????????????
        mFileWriter.write((int) getUnrealizedGains(this.getStockID()) + mLineBreak); // ???????????????
        mFileWriter.write((int) mBackTestInfo.getTradeFee() + mLineBreak); // ??????????????????
        mFileWriter.write((int) mBackTestInfo.getTotalEntryAmount() + mLineBreak); // ???????????????
        mFileWriter.write((int) mBackTestInfo.getTotalExitAmount() + mLineBreak); // ???????????????
        mFileWriter.write(mTotalROI + "%" + mLineBreak); // ?????????????????????
        mFileWriter.write(mIRR + "%" + mLineBreak); // ???????????????

        mFileWriter.write("\n");

        mFileWriter.flush();
        mFileWriter.close();
    }

    public float getUnrealizedGains(String aStockID) {
        boolean bIsSell = true;
        // ????????????
        int mInStock = mBackTestInfo.getInStock(aStockID);
        // ???????????????(??????????????????)??????????????????(?????????????????????)
        float mTotalInStock = mBackTestInfo.getInStockCost(aStockID) * mInStock;
        // ???????????????????????????????????????
        float mLastDateClosePrice = 0;
        Map.Entry<String, StockTechInfo> mEntry = Utility.getMapLastElement(this.getStockInfo().getStockTechInfo());
        if (mEntry != null) {
            mLastDateClosePrice = mEntry.getValue().getStockClose();
        }

        // ???????????????(???????????????)??????????????????????????????
        float mExpectedIncome = mLastDateClosePrice * mBackTestInfo.getInStock(aStockID)
                - calculateFee(mInStock, bIsSell);

        // ??????????????????????????????????????????
        return mLastDateClosePrice == 0 ? KeyDefine.ErrorHandle.TRANSCATION_DATA_EXCEPTION
                : (mExpectedIncome - mTotalInStock);
    }

    public boolean execute(Date aDate) {
        boolean bRet = false;

        String mStockID = this.getStockID();

        /*
         * TradeStrategyHandler mEntryTrade = getEntryStrategy();
         * 
         * for (EnAnalyzeStrategyType enType :
         * mEntryTrade.getStrategy().keySet()) {
         * mEntryTrade.getStrategyName(enType);
         * 
         * switch (enType) { case EN_ANALYZE_STRATEGY_TECH_BIAS:
         * mEntryTrade.getStrategy().get(enType).getIsBIAS(); break; case
         * EN_ANALYZE_STRATEGY_TECH_CCI:
         * mEntryTrade.getStrategy().get(enType).getIsCCI(); break; case
         * EN_ANALYZE_STRATEGY_TECH_KDJ:
         * mEntryTrade.getStrategy().get(enType).getIsKDJ(); break; default:
         * break; } }
         */

        // mBackTest.mStockInfo.setStockID("6116");
        // mBackTest.buy("6116", "2019-05-20", 5000);
        // mBackTest.sell("6116", "2019-06-03", 2000);

        /*
         * TradeStrategyHandler mExitTrade = getExitStrategy();
         * 
         * for (EnAnalyzeStrategyType enType :
         * mEntryTrade.getStrategy().keySet()) {
         * mEntryTrade.getStrategyName(enType);
         * 
         * switch (enType) { case EN_ANALYZE_STRATEGY_FUND_PROFIT:
         * mEntryTrade.getStrategy().get(enType).getProfitPercentage(); break;
         * default: break; } }
         */
        StockInformation mInfo = this.getStockInfo();

        // mInfo.getDbData(this.getStockID(), "2019-05-01",
        // KeyDefine.EnQueryType.EN_QUERY_TECH);

        boolean bBuyFirstTime = true; // buy next day if correspond to strategy

        float mExitProfitPercent = Utility.int2Float(Config.DataAnalyze.BACK_TEST_PROFIT_PERCENT, 2);
        float mExitStopLostPercent = Utility.int2Float(Config.DataAnalyze.BACK_TEST_STOP_LOST_PERCENT, 2);
        for (Map.Entry<String, StockTechInfo> mEntry : mInfo.getStockTechInfo().entrySet()) {
            if (bBuyFirstTime) {
                // buy
                this.buy(mStockID, mEntry.getValue().getStockDate(),
                        (int) ((this.getBackTestInfo().getInitialCurrency() / mEntry.getValue().getStockClose())
                                / KeyDefine.THOUSAND_OF_SHARES) * KeyDefine.THOUSAND_OF_SHARES);
                bBuyFirstTime = false;
            } else {
                // System.out.println("mExitProfitPercent:" +
                // mExitProfitPercent);
                int mStockVolume = mEntry.getValue().getStockVolume();
                if (mStockVolume <= 0) { // prevent 0 trade volume
                    continue;
                }
                float mStockClosePrice = mEntry.getValue().getStockClose();
                float mProfitPecentage = getProfitPercentage(this.mBackTestInfo.getInStockCost(mStockID),
                        mStockClosePrice);
                if (mProfitPecentage < mExitStopLostPercent || mProfitPecentage > mExitProfitPercent) {
                    this.sell(this.getStockID(), mEntry.getValue().getStockDate(),
                            this.mBackTestInfo.getInStock(mStockID));
                    return true;
                }
            }

            // System.out.print("Key:" + mEntry.getKey());
            // System.out.print(" Stock:" + mEntry.getValue().getStockID());
            // System.out.print(" Date:" + mEntry.getValue().getStockDate());
            // System.out.print(" High:" + mEntry.getValue().getStockHigh());
            // System.out.print(" Low:" + mEntry.getValue().getStockLow());
            // System.out.print(" Open:" + mEntry.getValue().getStockOpen());
            // System.out.print(" Close:" + mEntry.getValue().getStockClose());
            // System.out.print(" Vol:" + mEntry.getValue().getStockVolume());
            // System.out.println();
        }

        // for(int i = 1; i < Config.DataAnalyze.BACK_TEST_TRACKING_DAY; i++) {
        // String mTrackDate = Utility.getDateAfterDay(aDate, i);
        // if(getProfitPercentage(this.mBackTestInfo.getInStockCost(this.getStockID()),mInfo.getStockTechInfo(aKey))
        // > 15) { // fixme
        // this.sell(this.getStockID(), Utility.date2String(aDate),
        // this.mBackTestInfo.getInStock(this.getStockID()));
        // return true;
        // }
        // }

        return bRet;
    }

    /**
     * calculate profit percentage
     * 
     * @param mOrigin
     * @param mAfter
     * @return
     */
    public float getProfitPercentage(float mOrigin, float mAfter) {
        return (mAfter - mOrigin) / mOrigin;
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
            // ?????????????????????
            float mTotalFee = calculateFee(mNumShare, bIsSell);
            // ??????????????? = ???????????? + ????????????
            int mTotalInStock = mBackTestInfo.getInStock(aStockID) + aBuyVolume;
            // ????????? = (????????????*?????????????????? + ????????????*???????????? + ?????????)/(????????????+????????????)
            float mTradeCost = mInfo.getStockClose() * aBuyVolume + mTotalFee;
            float mTotalCost = (mBackTestInfo.getInStock(aStockID) * mBackTestInfo.getInStockCost(aStockID)
                    + mTradeCost) / (mTotalInStock);
            mBackTestInfo.setInStockCost(aStockID, mTotalCost);
            mBackTestInfo.setInStock(aStockID, aBuyVolume, bIsSell);
            // ????????????
            mBackTestInfo.setBuyLog(aDate, aBuyVolume);
            // ????????????
            mBackTestInfo.updateInitialCurrency((int) mTradeCost, bIsSell);
            // ???????????????
            mBackTestInfo.addTradeFee(mTotalFee);
            // ??????????????????
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
            // ?????????????????????
            float mTotalFee = calculateFee(mNumShare, bIsSell);
            // ???????????? = ?????????????????? * ???????????? - ?????????
            int mExitAmount = (int) (mInfo.getStockClose() * aSellVolume - mTotalFee);
            // ?????? = ???????????? - ???????????? * ????????????
            int mPorfit = (int) (mExitAmount - mBackTestInfo.getInStockCost(aStockID) * aSellVolume);
            mBackTestInfo.addProfitAmount(aStockID, mPorfit);
            // ????????????
            mBackTestInfo.setInStock(aStockID, aSellVolume, bIsSell);
            // ????????????
            mBackTestInfo.setSellLog(aDate, aSellVolume);
            // ????????????
            mBackTestInfo.updateInitialCurrency(mExitAmount, bIsSell);
            // ???????????????
            mBackTestInfo.addTradeFee(mTotalFee);
            // ??????????????????
            mBackTestInfo.addTotalExitAmount(mExitAmount);

        }

        return true;
    }

    private float calculateFee(float aCost, boolean bIsSell) {
        float mTotalFee = 0;

        mTotalFee = aCost * KeyDefine.SECURITIES_FEE_PERCETAGE * KeyDefine.SECURITIES_FEE_DISCOUNT;
        if (bIsSell) { // sell case
            mTotalFee += aCost * KeyDefine.TRADE_TAX; // ???????????????
        }

        return mTotalFee;
    }

    public static void writeCsvBom() {
        FileOutputStream mFileOutputstream;
        byte[] bs = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }; // UTF-8 encoding
        try {
            mFileOutputstream = new FileOutputStream(new File(Config.DataAnalyze.outputAnalyzerResultName));
            mFileOutputstream.write(bs);
            mFileOutputstream.flush();
            mFileOutputstream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeCsvTitle() {
        String[] mCsvTitle = { "????????????", "????????????", "????????????(???)", "??????", "????????????", "??????????????????", "??????????????????", "????????????", "????????????", "????????????",
                "??????(%)", "???????????????", "???????????????", "??????????????????", "???????????????", "???????????????", "?????????????????????", "???????????????" };
        BufferedWriter mFileWriter;
        try {
            mFileWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Config.DataAnalyze.outputAnalyzerResultName, true), "UTF-8"));
            for (String mStr : mCsvTitle) {
                mFileWriter.write(mStr + ",");
            }
            mFileWriter.write("\n");
            mFileWriter.flush();
            mFileWriter.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void write2file() {
        write2file(null, null);
    }

    public static void write2file(String aDateStart) {
        write2file(aDateStart, null);
    }

    public static void write2file(String aDateStart, String aDateEnd) {
        boolean bIsSatisfiedSell = false;

        Date mDateStart = null, mDateEnd = null;
        try {
            mDateStart = (aDateStart == null) ? new Date(Long.MIN_VALUE) : Utility.string2Date(aDateStart);
            mDateEnd = (aDateEnd == null) ? Utility.string2Date(Utility.getTodayDate()) : Utility.string2Date(aDateEnd);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        writeCsvBom();
        writeCsvTitle();

        Utility.timerStart();

        TechAnalyzerHandler mAnalyzer = new TechAnalyzerHandler();
        mAnalyzer.parseCalculatorData();
        ArrayList<TechCsvStruct> mTmp = mAnalyzer.getAnalyzerData();
        Iterator<TechCsvStruct> mIter = mTmp.iterator();
        while (mIter.hasNext()) {
            TechCsvStruct mTechData = mIter.next();
            Date mDateData = mTechData.getDate();
            // condition filter specific date
            if (mDateData.compareTo(mDateStart) < 0 || mDateData.compareTo(mDateEnd) > 0) {
                continue;
            }
            try {
                System.out.println(mTechData.getStockID() + " " + Utility.date2String(mDateData) + " " + "BIAS: "
                        + mTechData.getbIsBIAS() + " KDJ: " + mTechData.getbIsKDJ());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // condition filter for BIAS and KDJ
            if (mTechData.getbIsBIAS() && mTechData.getbIsKDJ()) {
                TechBackTestHandler mBackTest;
                try {

                    mBackTest = new TechBackTestHandler(mTechData.getStockID(),
                            Utility.date2String(mTechData.getDate()));
                    mBackTest.mBackTestInfo.setInitialCurrency(Config.DataAnalyze.BACK_TEST_INITIAL_MONEY);
                    mBackTest.mBackTestInfo.setBackTestTime(Config.DataAnalyze.BACK_TEST_TRACKING_DAY); // fixme
                    mBackTest.mStockInfo.setStockID(mTechData.getStockID());
                    bIsSatisfiedSell = mBackTest.execute(mTechData.getDate());
                    mBackTest.getResult();
                } catch (SQLException | ParseException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        Utility.timerEnd();
    }

    /**
     * @param args
     * @throws ParseException
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // Configurator.setRootLevel(Level.TRACE);
        // write2file();
        // write2file("2019-01-01", "2019-01-03");

        write2file("2019-01-01");

        /*
         * TechBackTestHandler mBackTest = new TechBackTestHandler("6116",
         * "2019-05-01"); mBackTest.mBackTestInfo.setInitialCurrency(100000);
         * mBackTest.mBackTestInfo.setBackTestTime(240);
         * mBackTest.mStockInfo.setStockID("6116"); mBackTest.buy("6116",
         * "2019-05-20", 5000); mBackTest.sell("6116", "2019-06-03", 2000);
         * mBackTest.sell("6116", "2019-06-04", 0);
         * 
         * 
         * mBackTest.getResult();
         */

    }
}
