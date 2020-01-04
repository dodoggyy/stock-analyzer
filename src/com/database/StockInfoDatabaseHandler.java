/**
 * 
 */
package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.common.DatabaseConfig;

/**
 * @author Chris Lin
 *
 */
public class StockInfoDatabaseHandler extends DatabaseHandler{

    public StockInfoDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        init();
    }
    
    public void init() {
        super.init();
        /**
         * sync with TWSEFundParserHandler, OTCFundParserHandler
         * id, "證券代號", "國際證券辨識號碼(ISIN Code)", "上市日", "市場別", "產業別", "CFICode"
         */
        createDbSQL = "CREATE TABLE " + mTableName + " (" + 
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
    }
    
    protected void TestInsertTable() throws SQLException {
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_name, stock_isin_code, stock_listing_date, stock_industry, stock_cfi_code, stock_type)"
              + "VALUES ('6116', '彩晶', 'TW0006116007', '2004-09-06', '光電業', 'ESVUFR', 3);";

        this.mPreparedStatement.addBatch(mInsertSql);
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }

    @Override
    public void setupDatabase() {
        // TODO Auto-generated method stub
        setTableName(DatabaseConfig.TABLE_STOCK_INFO);
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
            log.error("Close Exception :" + e.toString()); 
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

    @Override
    void initQuerySql(String aStockID, String aDate) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param args
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException {
     // TODO Auto-generated method stub
        StockInfoDatabaseHandler mStockDB = new StockInfoDatabaseHandler();

        DBOperation(mStockDB);
    }
}
