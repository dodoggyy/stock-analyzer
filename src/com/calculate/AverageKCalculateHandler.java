/**
 * 
 */
package com.calculate;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Chris
 *
 */
public class AverageKCalculateHandler extends BaseCalculateHandler{

    public class TechAverge{
        protected ArrayList<Double> mHighPrice;
        protected ArrayList<Double> mLowPrice;
        protected ArrayList<Double> mOpenPrice;
        protected ArrayList<Double> mClosePrice;
        protected ArrayList<Integer> mVolume;
        protected ArrayList<String> mDBDate;
        
        public void init() {
            mHighPrice = new ArrayList<>();
            mLowPrice = new ArrayList<>();
            mOpenPrice = new ArrayList<>();
            mClosePrice = new ArrayList<>();
            mVolume = new ArrayList<>();
            mDBDate = new ArrayList<>();
        }
        
        public void clear() {
            mHighPrice.clear();
            mLowPrice.clear();
            mOpenPrice.clear();
            mClosePrice.clear();
            mVolume.clear();
            mDBDate.clear();
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    @Override
    void calculateValue(){
        // TODO Auto-generated method stub
        
    }

    @Override
    void writeData2DB() {
        // TODO Auto-generated method stub
        
    }

}
