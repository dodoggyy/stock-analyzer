/**
 * 
 */
package com.backtest;

import java.util.ArrayList;
import java.util.Iterator;

import com.analyzer.TechAnalyzerHandler;
import com.analyzer.TechAnalyzerHandler.TechCsvStruct;

/**
 * @author Chris Lin
 *
 */
public class BackTestMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        // 讀取資料
        TechAnalyzerHandler mAnalyzer = new TechAnalyzerHandler();
        mAnalyzer.parseCalculatorData();
        
        ArrayList<TechCsvStruct> mTmp = mAnalyzer.getAnalyzerData();
        
        Iterator mIter = mTmp.iterator();
        while (mIter.hasNext()) {
            TechCsvStruct mTechData = (TechCsvStruct) mIter.next();
            
            
        }
    }

}
