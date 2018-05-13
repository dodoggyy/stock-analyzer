package com.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.common.DatabaseConfig;

public class TechDatabaseHandler extends DatabaseHandler {
    private PreparedStatement mPreparedStatementTech = null;
    private String mUrl;
    private String mTableName = "listed_tech";
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "stock_id     VARCHAR(30), " +
            "stock_date     DATE, " +
            "stock_closing_price     INT, " +
            "stock_opening_price     INT, " +
            "stock_high_price     INT, " +
            "stock_low_price     INT, " +
            "stock_volume     INT) ";

    public static void main(String[] args) throws SQLException {
        TechDatabaseHandler mStockDB = new TechDatabaseHandler();
        
        //Only need execute create table in first time
        mStockDB.createTable();
    }
    
    public TechDatabaseHandler() throws SQLException {
        super();
        readConfig();
        // TODO Auto-generated constructor stub
        
    }

    @Override
    void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            //this.mPreparedStatementTech = mConnection.prepareStatement(mInsertTechSql);
            // System.out.println(mInsertTechSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    void setupDatabase() {
        // TODO Auto-generated method stub

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
        } catch(SQLException e) { 
            System.out.println("Close Exception :" + e.toString()); 
        } 
    }

    @Override
    void generateSqlCmd() {
        // TODO Auto-generated method stub

    }

    @Override
    void readConfig() {
        // TODO Auto-generated method stub
       host = DatabaseConfig.DB_KEY_HOST;
       port = DatabaseConfig.DB_KEY_PORT;
       socket = DatabaseConfig.DB_KEY_UNIX_SOCKET;
       username = DatabaseConfig.DB_KEY_USERNAME;
       password = DatabaseConfig.DB_KEY_PASSWORD;
       database = DatabaseConfig.DB_KEY_DATABASE;
       mUrl = "jdbc:" + DatabaseConfig.DB_SECTION_MYSQL + "://localhost:" + port + "/" + database +"?serverTimezone=UTC&characterEncoding=utf-8";
       // System.out.println(mUrl);
    }
}
