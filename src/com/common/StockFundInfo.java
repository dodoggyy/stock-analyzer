/**
 * 
 */
package com.common;

/**
 * @author Chris Lin
 *
 */
public class StockFundInfo extends StockBaseInfo{
    private String stockID;
    private String stockDate; 
    private float stockYieldRate;
    private float stockPBR;
    private float stockPER;


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
     * @return the stockYieldRate
     */
    public float getStockYieldRate() {
        return stockYieldRate;
    }

    /**
     * @param stockYieldRate the stockYieldRate to set
     */
    public void setStockYieldRate(float stockYieldRate) {
        this.stockYieldRate = stockYieldRate;
    }

    /**
     * @return the stockPBR
     */
    public float getStockPBR() {
        return stockPBR;
    }

    /**
     * @param stockPBR the stockPBR to set
     */
    public void setStockPBR(float stockPBR) {
        this.stockPBR = stockPBR;
    }

    /**
     * @return the stockPER
     */
    public float getStockPER() {
        return stockPER;
    }

    /**
     * @param stockPER the stockPER to set
     */
    public void setStockPER(float stockPER) {
        this.stockPER = stockPER;
    }

}
