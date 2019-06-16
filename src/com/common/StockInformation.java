/**
 * 
 */
package com.common;

import java.util.HashMap;

/**
 * @author Chris Lin
 *
 */
public class StockInformation {
    private String stockID;
    private HashMap<String, StockTechInfo> stockTechInfo;
    private HashMap<String, StockFundInfo> stockFundInfo;

    public StockInformation(String stockID) {
        this.stockID = stockID;
        this.stockTechInfo = new HashMap<>();
        this.stockFundInfo = new HashMap<>();
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
     * @return the stockTechInfo
     */
    public StockTechInfo getStockTechInfo(String aKey) {
        return stockTechInfo.get(aKey);
    }

    /**
     * @param stockTechInfo the stockTechInfo to set
     */
    public void setStockTechInfo(String aKey, StockTechInfo aStockTechInfo) {
        this.stockTechInfo.put(aKey, aStockTechInfo);
    }

    /**
     * @return the stockFundInfo
     */
    public StockFundInfo getStockFundInfo(String aKey) {
        return stockFundInfo.get(aKey);
    }

    /**
     * @param stockFundInfo the stockFundInfo to set
     */
    public void setStockFundInfo(String aKey, StockFundInfo aStockFundInfo) {
        this.stockFundInfo.put(aKey, aStockFundInfo);
    }

}
