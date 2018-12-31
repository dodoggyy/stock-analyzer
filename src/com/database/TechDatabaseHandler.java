package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.common.DatabaseConfig;
import com.common.Utility;

public class TechDatabaseHandler extends DatabaseHandler {
    /**
     * sync with TWSETechParserHandler, OTCTechParserHandler
     * id, "證券代號", "日期", "收盤價", "開盤價", "最高價", "最低價", "成交量", "交易類別"
     */
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "stock_id     VARCHAR(30), " +
            "stock_date     DATE, " +
            "stock_closing_price     INT, " +
            "stock_opening_price     INT, " +
            "stock_high_price     INT, " +
            "stock_low_price     INT, " +
            "stock_volume     INT, " +
            "stock_type     INT, " +
            "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";

    private String mFieldDate = "";
    private String mFiledStockId = "";
    private String mFieldClose = "";
    private String mFieldOpen = "";
    private String mFieldHigh = "";
    private String mFieldLow = "";
    private String mFieldVolume = "";

    public static void main(String[] args) throws SQLException {
        Utility.timerStart();
        TechDatabaseHandler mStockDB = new TechDatabaseHandler();
        
        //Only need execute create table in first time
        mStockDB.createTable();
        
        //for(int i = 0; i < 100;i++)
        //mStockDB.TestInsertTable();

        //mStockDB.deleteSqlDuplicateData();

        Utility.timerEnd();
    }
    
    void TestInsertTable() throws SQLException {
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_closing_price, stock_opening_price, stock_high_price, stock_low_price, stock_volume, stock_type)"
              + "VALUES ('6116', '2017-06-03', 802, 802, 804, 796, 21080000, 2);";

        this.mPreparedStatement.addBatch(mInsertSql);
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }
    
    public TechDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            this.mPreparedStatement = mConnection.prepareStatement(mInsertSql);
            // System.out.println(mInsertSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    void setupDatabase() {
        // TODO Auto-generated method stub
        mTableName = "listed_tech";

        mFiledStockId = "stock_id";
        mFieldDate = "stock_date";
        mFieldHigh = "stock_high_price";
        mFieldLow = "stock_low_price";
        mFieldOpen = "stock_opening_price";
        mFieldClose = "stock_closing_price";
        mFieldVolume = "stock_volume";
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
    void closeObject() {
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
    void generateSqlCmd(){
        // TODO Auto-generated method stub
    }

    @Override
    public void generateSqlPrepareStrCmd(int aColumn, String aFieldValue) throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.setString(aColumn, aFieldValue);
    }

    @Override
    public void generateSqlPrepareIntCmd(int aColumn, int aFieldValue) throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.setInt(aColumn, aFieldValue);
    }

    @Override
    public void executeSqlPrepareCmd() throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.executeBatch();
        mConnection.commit(); 
        mPreparedStatement.clearBatch();
    }

    @Override
    public void addSqlPrepareCmd2Batch() throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.addBatch();
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_closing_price, stock_opening_price, stock_high_price, stock_low_price, stock_volume, stock_type)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    public void deleteSqlDuplicateData() throws SQLException {
        // TODO Auto-generated method stub
        String mInsertSql = "DELETE FROM "+ mTableName + " WHERE `id` in (SELECT b.id FROM (SELECT * FROM "+ mTableName + " a WHERE id<>(select MAX(id) FROM "+ mTableName + 
                " WHERE stock_id=a.stock_id and stock_date=a.stock_date)"
                + ")b);";
        System.out.println(mInsertSql);
        this.mPreparedStatement.addBatch(mInsertSql);
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }
}
