package com.backtest;

import java.util.LinkedHashMap;

import com.common.KeyDefine.EnAnalyzeStrategyType;
import com.common.TechAnalyzerInfo;

/**
 * @author Chris Lin
 *
 */
public class TradeStrategyHandler {
    private LinkedHashMap<EnAnalyzeStrategyType, TechAnalyzerInfo> mStrategy;

    public TradeStrategyHandler() {
        this.mStrategy = new LinkedHashMap<>();
    }

    /**
     * list all of strategy type
     */
    public void listType() {
        for (EnAnalyzeStrategyType enType : EnAnalyzeStrategyType.values()) {
            System.out.println(enType);
        }
    }

    public void listCurrent() {
        for (EnAnalyzeStrategyType enType : mStrategy.keySet()) {
            System.out.println(enType);
        }
    }

    /**
     * add strategy to list
     * 
     * @param enType
     * @return
     */
    public void add(EnAnalyzeStrategyType enType, TechAnalyzerInfo enInfo) {
        TechAnalyzerInfo enTmp = new TechAnalyzerInfo();
        switch (enType) {
        case EN_ANALYZE_STRATEGY_FUND_PROFIT:
            enTmp.setProfitPercentage(enInfo.getProfitPercentage());
            mStrategy.put(enType, enTmp);
            break;
        case EN_ANALYZE_STRATEGY_TECH_BIAS:
            enTmp.setIsBIAS(enInfo.getIsBIAS());
            mStrategy.put(enType, enTmp);
            break;
        case EN_ANALYZE_STRATEGY_TECH_CCI:
            enTmp.setIsCCI(enInfo.getIsCCI());
            mStrategy.put(enType, enTmp);
            break;
        case EN_ANALYZE_STRATEGY_TECH_KDJ:
            enTmp.setIsKDJ(enInfo.getIsKDJ());
            mStrategy.put(enType, enTmp);
            break;
        default:
            System.out.println("Unknown or unsupported type:"  + enType);
            break;
        }
    }

    /**
     * remove strategy to list
     * 
     * @param enType
     * @return
     */
    public void remove(EnAnalyzeStrategyType enType) {
        mStrategy.remove(enType);

    }

    /**
     * @return the mStrategy
     */
    public LinkedHashMap<EnAnalyzeStrategyType, TechAnalyzerInfo> getStrategy() {
        return mStrategy;
    }

    /**
     * @param mStrategy
     *            the mStrategy to set
     */
    public void setStrategy(LinkedHashMap<EnAnalyzeStrategyType, TechAnalyzerInfo> mStrategy) {
        this.mStrategy = mStrategy;
    }

    public static void main(String[] args) {
        TradeStrategyHandler mTrade = new TradeStrategyHandler();
        mTrade.listType();
        System.out.println();

        mTrade.listCurrent();

        TechAnalyzerInfo enInfo = new TechAnalyzerInfo();
        enInfo.setIsKDJ(true);
        enInfo.setIsBIAS(true);
        enInfo.setProfitPercentage(100);
        mTrade.add(EnAnalyzeStrategyType.EN_ANALYZE_STRATEGY_TECH_BIAS, enInfo);
        mTrade.add(EnAnalyzeStrategyType.EN_ANALYZE_STRATEGY_FUND_PROFIT, enInfo);
        mTrade.add(EnAnalyzeStrategyType.EN_ANALYZE_STRATEGY_TECH_KDJ, enInfo);

        for (EnAnalyzeStrategyType enType : mTrade.getStrategy().keySet()) {
            System.out.println("Strategy: " + enType);
            System.out.println(mTrade.getStrategy().get(enType).toString());
        }
        
//        Iterator<EnAnalyzeStrategyType> mIter = mTrade.getStrategy().keySet().iterator();
//        while(mIter.hasNext()) {
//            System.out.println("Strategy: " + mIter.next());
//        }

    }
}
