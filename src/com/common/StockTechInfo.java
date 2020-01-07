/**
 * 
 */
package com.common;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Chris Lin
 *
 */
public class StockTechInfo extends StockBaseInfo implements Comparable<StockTechInfo>{
    private String stockID;
    private String stockDate; 
    private float stockClose;
    private float stockOpen;
    private float stockHigh;
    private float stockLow;
    private int stockVolume;


    public StockTechInfo() {
        
    }
    
    public StockTechInfo(String stockID) {
        this.stockID = stockID;
        this.stockClose = 0;
        this.stockOpen = 0;
        this.stockHigh = Integer.MIN_VALUE;
        this.stockLow = Integer.MAX_VALUE;
        this.stockVolume = 0;
    }
    
    /**
     * @return the stockID
     */
    public String getStockID() {
        return stockID;
    }

    /**
     * @param stockID the stockID to set
     */
    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    /**
     * @return the stockDate
     */
    public String getStockDate() {
        return stockDate;
    }

    /**
     * @param stockDate the stockDate to set
     */
    public void setStockDate(String stockDate) {
        this.stockDate = stockDate;
    }

    /**
     * @return the stockClose
     */
    public float getStockClose() {
        return stockClose;
    }

    /**
     * @param stockClose the stockClose to set
     */
    public void setStockClose(float stockClose) {
        this.stockClose = stockClose;
    }

    /**
     * @return the stockOpen
     */
    public float getStockOpen() {
        return stockOpen;
    }

    /**
     * @param stockOpen the stockOpen to set
     */
    public void setStockOpen(float stockOpen) {
        this.stockOpen = stockOpen;
    }

    /**
     * @return the stockHigh
     */
    public float getStockHigh() {
        return stockHigh;
    }

    /**
     * @param stockHigh the stockHigh to set
     */
    public void setStockHigh(float stockHigh) {
        this.stockHigh = stockHigh;
    }

    /**
     * @return the stockLow
     */
    public float getStockLow() {
        return stockLow;
    }

    /**
     * @param stockLow the stockLow to set
     */
    public void setStockLow(float stockLow) {
        this.stockLow = stockLow;
    }

    /**
     * @return the stockVolume
     */
    public int getStockVolume() {
        return stockVolume;
    }

    /**
     * @param stockVolume the stockVolume to set
     */
    public void setStockVolume(int stockVolume) {
        this.stockVolume = stockVolume;
    }

    @Override
    public int compareTo(StockTechInfo other) {
        // TODO Auto-generated method stub
        try {
            Date date1 = Utility.string2Date(this.stockDate);
            Date date2 = Utility.string2Date(other.stockDate);
            return date1.compareTo(date2);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
            return 0;
        }
    }

    
}
