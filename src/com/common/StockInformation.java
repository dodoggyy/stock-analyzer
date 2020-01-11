package com.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.common.KeyDefine.EnQueryType;
import com.database.FundDatabaseHandler;
import com.database.TechDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class StockInformation {
    //private TechDatabaseHandler mTechDb;
    //private FundDatabaseHandler mFundDb;
    
    protected static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private String stockID;
    private LinkedHashMap<String, StockTechInfo> stockTechInfo;
    private LinkedHashMap<String, StockFundInfo> stockFundInfo;

    public StockInformation(String stockID) {
        this.stockID = stockID;
        this.stockTechInfo = new LinkedHashMap<>();
        this.stockFundInfo = new LinkedHashMap<>();
    }

    /**
     * @return the stockID
     */
    public String getStockID() {
        return stockID;
    }

    /**
     * @param stockID
     *            the stockID to set
     */
    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    /**
     * @return the stockTechInfo
     */
    public LinkedHashMap<String, StockTechInfo> getStockTechInfo() {
        return stockTechInfo;
    }

    /**
     * @return the stockTechInfo
     */
    public StockTechInfo getStockTechInfo(String aKey) {
        if (stockTechInfo.containsKey(aKey)) {
            return stockTechInfo.get(aKey);
        } else {
            System.out.println("Not has stock data for " + aKey);
            return new StockTechInfo();
        }

    }

    /**
     * @param stockTechInfo
     *            the stockTechInfo to set
     */
    public void setStockTechInfo(String aKey, StockTechInfo aStockTechInfo) {
        this.stockTechInfo.put(aKey, aStockTechInfo);
    }

    /**
     * @return the stockFundInfo
     */
    public LinkedHashMap<String, StockFundInfo> getStockFundInfo() {
        return stockFundInfo;
    }

    /**
     * @return the stockFundInfo
     */
    public StockFundInfo getStockFundInfo(String aKey) {
        return stockFundInfo.get(aKey);
    }

    /**
     * @param stockFundInfo
     *            the stockFundInfo to set
     */
    public void setStockFundInfo(String aKey, StockFundInfo aStockFundInfo) {
        this.stockFundInfo.put(aKey, aStockFundInfo);
    }

    public void getDbData(String aStockID, String aDate, EnQueryType enType) throws SQLException, ParseException {
        ResultSet mResultSet = null;

        switch (enType) {
        case EN_QUERY_TECH:
            TechDatabaseHandler mTechStockDB = new TechDatabaseHandler();
            mTechStockDB.initQuerySql(aStockID, aDate);
            mResultSet = mTechStockDB.executeQuerySql();

            while (mResultSet.next()) {
                // System.out.print("Date:" + mResultSet.getDate("stock_date"));
                // System.out.print(" High:" +
                // Utility.int2Float(mResultSet.getInt("stock_high_price"),
                // KeyDefine.DB_PRICE_SHIFT));
                // System.out.print(" Low:" +
                // Utility.int2Float(mResultSet.getInt("stock_low_price"),
                // KeyDefine.DB_PRICE_SHIFT));
                // System.out.print(" Open:" +
                // Utility.int2Float(mResultSet.getInt("stock_opening_price"),
                // KeyDefine.DB_PRICE_SHIFT));
                // System.out.print(" Close:" +
                // Utility.int2Float(mResultSet.getInt("stock_closing_price"),
                // KeyDefine.DB_PRICE_SHIFT));
                // System.out.print(" Volume:" +
                // mResultSet.getInt("stock_volume"));
                // System.out.println();

                String mQueryDate = Utility.date2String(mResultSet.getDate("stock_date"));
                StockTechInfo mTechInfo = new StockTechInfo();
                mTechInfo.setStockID(aStockID);
                mTechInfo.setStockDate(mQueryDate);
                mTechInfo.setStockHigh(
                        Utility.int2Float(mResultSet.getInt("stock_high_price"), KeyDefine.DB_PRICE_SHIFT));
                mTechInfo
                        .setStockLow(Utility.int2Float(mResultSet.getInt("stock_low_price"), KeyDefine.DB_PRICE_SHIFT));
                mTechInfo.setStockOpen(
                        Utility.int2Float(mResultSet.getInt("stock_opening_price"), KeyDefine.DB_PRICE_SHIFT));
                mTechInfo.setStockClose(
                        Utility.int2Float(mResultSet.getInt("stock_closing_price"), KeyDefine.DB_PRICE_SHIFT));
                mTechInfo.setStockVolume(mResultSet.getInt("stock_volume"));

                stockTechInfo.put(aStockID + "_" + mQueryDate, mTechInfo);
            }
            break;
        case EN_QUERY_FUND:
            FundDatabaseHandler mFundStockDB = new FundDatabaseHandler();
            // todo
            break;
        default:
            log.error("Unknown query type or not defined");
            break;
        }
    }

    public String getStockInfoKey(String aStockID, String aDate) {
        return aStockID + "_" + aDate;
    }

    public static void main(String[] args) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        StockInformation mInfo = new StockInformation("6116");
        mInfo.getDbData("6116", "2019-05-01", KeyDefine.EnQueryType.EN_QUERY_TECH);

        mInfo.getDbData("2027", "2019-04-20", KeyDefine.EnQueryType.EN_QUERY_TECH);

        // for(String mStr:mInfo.getStockTechInfo().keySet()) {
        // System.out.print("Key:" + mStr);
        // System.out.print(" Stock:" +
        // mInfo.getStockTechInfo(mStr).getStockID());
        // System.out.print(" Date:" +
        // mInfo.getStockTechInfo(mStr).getStockDate());
        // System.out.print(" High:" +
        // mInfo.getStockTechInfo(mStr).getStockHigh());
        // System.out.print(" Low:" +
        // mInfo.getStockTechInfo(mStr).getStockLow());
        // System.out.print(" Open:" +
        // mInfo.getStockTechInfo(mStr).getStockOpen());
        // System.out.print(" Close:" +
        // mInfo.getStockTechInfo(mStr).getStockClose());
        // System.out.print(" Vol:" +
        // mInfo.getStockTechInfo(mStr).getStockVolume());
        // System.out.println();
        // }

        for (Map.Entry<String, StockTechInfo> mEntry : mInfo.getStockTechInfo().entrySet()) {
            System.out.print("Key:" + mEntry.getKey());
            System.out.print(" Stock:" + mEntry.getValue().getStockID());
            System.out.print(" Date:" + mEntry.getValue().getStockDate());
            System.out.print(" High:" + mEntry.getValue().getStockHigh());
            System.out.print(" Low:" + mEntry.getValue().getStockLow());
            System.out.print(" Open:" + mEntry.getValue().getStockOpen());
            System.out.print(" Close:" + mEntry.getValue().getStockClose());
            System.out.print(" Vol:" + mEntry.getValue().getStockVolume());
            System.out.println();
        }

        System.out.println(mInfo.getStockID());
    }
}
