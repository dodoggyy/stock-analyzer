/**
 * 
 */
package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Chris Lin
 *
 */
public class StockInfoDatabaseHandler extends DatabaseHandler{

    /**
     * sync with TWSEFundParserHandler, OTCFundParserHandler
     * id, "證券代號", "國際證券辨識號碼(ISIN Code)", "上市日", "市場別", "產業別", "CFICode"
     */
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "stock_id     VARCHAR(4), " +
            "stock_name   VARCHAR(20), " +
            "stock_isin_code  VARCHAR(12), " +
            "stock_listing_date  DATE, " +
            "stock_industry    VARCHAR(20), " +
            "stock_cfi_code    VARCHAR(6), " +
            "stock_type     INT, " +
            "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";

    private String mFiledStockId = "";
    private String mFieldStockName = "";
    private String mFieldISIN = "";
    private String mFieldListingDate = "";
    private String mFieldIndustry = "";
    private String mFieldCFI = "";
    
    public StockInfoDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException {
     // TODO Auto-generated method stub
        StockInfoDatabaseHandler mStockDB = new StockInfoDatabaseHandler();

        //Only need execute create table in first time
        mStockDB.createTable();

        //mStockDB.TestInsertTable();
    }
    
    void TestInsertTable() throws SQLException {
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_name, stock_isin_code, stock_listing_date, stock_industry, stock_cfi_code, stock_type)"
              + "VALUES ('6116', '彩晶', 'TW0006116007', '2004-09-06', '光電業', 'ESVUFR', 3);";

        this.mPreparedStatement.addBatch(mInsertSql);
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }

    @Override
    public void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            this.mPreparedStatement = mConnection.prepareStatement(mInsertSql);
            // System.out.println(mInsertTechSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void setupDatabase() {
        // TODO Auto-generated method stub
        mTableName = "stock_info";
        
        mFiledStockId = "stock_id";
        mFieldStockName = "stock_name";
        mFieldISIN = "stock_isin_code";
        mFieldListingDate = "stock_listing_date";
        mFieldIndustry = "stock_industry";
        mFieldCFI = "stock_cfi_code";
    }

    @Override
    void createTable() {
        // TODO Auto-generated method stub
        try {
            this.mConnection = DriverManager.getConnection(mUrl, username, password);
            this.mStatement = mConnection.createStatement();
            this.mStatement.executeUpdate(createDbSQL);
            // System.out.println(createDbSQL);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("DriverClassNotFound :"+e.toString());
        } finally { 
            this.closeObject(); 
        }
    }

    @Override
    public void closeObject() {
        // TODO Auto-generated method stub
        try {
            if(mResultSet != null) { 
                mResultSet.close(); 
                mResultSet = null; 
            } 
            if(mStatement != null) { 
                mStatement.close(); 
                mStatement = null; 
            }
            if(mPreparedStatement != null) { 
                mPreparedStatement.close(); 
                mPreparedStatement = null;
            }
        } catch(SQLException e) { 
            System.out.println("Close Exception :" + e.toString()); 
        }
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_name, stock_isin_code, stock_listing_date, stock_industry, stock_cfi_code, stock_type)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    void generateSqlCmd() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteSqlDuplicateData() throws SQLException {
        // TODO Auto-generated method stub
        
    }

}
