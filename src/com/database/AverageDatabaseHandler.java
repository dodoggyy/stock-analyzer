/**
 * 
 */
package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.common.DatabaseConfig;

/**
 * @author Chris
 *
 */
public class AverageDatabaseHandler extends DatabaseHandler{

    public AverageDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        init();
    }
    
    public void init() {
        super.init();
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

    @Override
    protected void setupDatabase() {
        // TODO Auto-generated method stub
        mTableName = DatabaseConfig.TABLE_WEEK_TECH;
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
//        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_closing_price, stock_opening_price, stock_high_price, stock_low_price, stock_volume, stock_type)"
//                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    void generateSqlCmd() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    void deleteSqlDuplicateData() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    void TestInsertTable() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    ArrayList<String> queryAllStockId() {
        ArrayList<String> mDbStockId = new ArrayList<String>();
        String mQueryStockIdSql = "SELECT DISTINCT stock_id FROM listed_tech ORDER BY stock_id ASC";
        try {
            mStatement = mConnection.createStatement();
            mResultSet = mStatement.executeQuery(mQueryStockIdSql);
            while(mResultSet.next()) {
                //System.out.println(mResultSet.getString("stock_id"));
                mDbStockId.add(mResultSet.getString("stock_id"));
            }
        } catch(SQLException e) {
            System.out.println("QueryDB Exception :" + e.toString()); 
        } finally {
            this.closeObject();
        }
        
        return mDbStockId;
    }

    @Override
    void initQuerySql(String aStockID, String aDate) {
        // TODO Auto-generated method stub
        
    }
}
