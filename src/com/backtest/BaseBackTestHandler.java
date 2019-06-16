/**
 * 
 */
package com.backtest;

import java.util.ArrayList;

import com.common.BackTestInfo;
import com.common.KeyDefine.EnAnalyzeStrategyType;
import com.common.StockInformation;

/**
 * @author Chris Lin
 *
 */
public abstract class BaseBackTestHandler {
    protected StockInformation mStockInfo;
    protected AppearanceCondition mAppearance;
    protected ArrivalCondition mArrival;
    protected BackTestInfo mBackTestInfo;

    public BaseBackTestHandler(StockInformation mStockInfo) {
        this.mStockInfo = mStockInfo;
        this.mBackTestInfo = new BackTestInfo();
    }

    public class AppearanceCondition {
        private ArrayList<EnAnalyzeStrategyType> enType;

        public AppearanceCondition() {
        }

        public void addStrategy(EnAnalyzeStrategyType enType) {
            this.enType.add(enType);
        }

        public void removeStrategy(EnAnalyzeStrategyType enType) {
            this.enType.remove(enType);
        }
    }

    public class ArrivalCondition {
        private ArrayList<EnAnalyzeStrategyType> enType;

        public ArrivalCondition(EnAnalyzeStrategyType enType) {
        }

        public void addStrategy(EnAnalyzeStrategyType enType) {
            this.enType.add(enType);
        }

        public void removeStrategy(EnAnalyzeStrategyType enType) {
            this.enType.remove(enType);
        }
    }
}
