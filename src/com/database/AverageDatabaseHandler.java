/**
 * 
 */
package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Chris
 *
 */
public class AverageDatabaseHandler extends DatabaseHandler{

    protected String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "stock_id     VARCHAR(4), " +
            "stock_date     DATE, " +
            "stock_closing_price     INT, " +
            "stock_opening_price     INT, " +
            "stock_high_price     INT, " +
            "stock_low_price     INT, " +
            "stock_volume     INT, " +
            "stock_type     INT, " +
            "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";
    
    public AverageDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        this.closeObject();
        this.setupDatabase();
        this.readConfig();
        this.connectDatabase();
    }

    @Override
    protected void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            // System.out.println(mInsertSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void setupDatabase() {
        // TODO Auto-generated method stub
    }
    
    protected void setTableName(String aTableName) {
        this.mTableName = aTableName;
    }
    
    protected String getTableName() {
        return this.mTableName;
    }

    @Override
    protected void createTable() {
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
    protected void closeObject() {
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
}
