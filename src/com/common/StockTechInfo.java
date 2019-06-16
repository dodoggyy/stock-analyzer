/**
 * 
 */
package com.common;

/**
 * @author Chris Lin
 *
 */
public class StockTechInfo extends StockBaseInfo{
    private String stockID;
    private String stockDate;  
    private float stockClose;
    private float stockOpen;
    private float stockHigh;
    private float stockLow;
    private float stockVolume;


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
    public float getStockVolume() {
        return stockVolume;
    }

    /**
     * @param stockVolume the stockVolume to set
     */
    public void setStockVolume(float stockVolume) {
        this.stockVolume = stockVolume;
    }

    
}
