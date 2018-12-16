package com.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.common.DatabaseConfig;

public class ListTypeDatabaseHandler extends DatabaseHandler{
    private PreparedStatement mPreparedStatementList = null;
    private String mUrl;
    private String mTableName = "listed_type";
    private String mInsertListSql = "";
    
    /**
     * id, "交易類別"
     */
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "type_name     VARCHAR(30)) ";

    private String mFiledTypeName = "";
    
    public ListTypeDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        this.closeObject();
        this.initPrepareSql();
        this.readConfig();
        this.setupDatabase();
        this.connectDatabase();
    }

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        ListTypeDatabaseHandler mStockDB = new ListTypeDatabaseHandler();
        
        //Only need execute create table in first time
        mStockDB.createTable();
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

    @Override
    void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            this.mPreparedStatementList = mConnection.prepareStatement(mInsertListSql);
            // System.out.println(mInsertTechSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    void setupDatabase() {
        // TODO Auto-generated method stub
        mFiledTypeName = "type_name";
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
            if(mPreparedStatementList != null) { 
                mPreparedStatementList.close(); 
                mPreparedStatementList = null;
            }
        } catch(SQLException e) { 
            System.out.println("Close Exception :" + e.toString()); 
        }
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertListSql =  "INSERT INTO " + mTableName + " (type_name)"
                + "VALUES (?);";
    }

    @Override
    void generateSqlCmd() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void generateSqlPrepareStrCmd(int aColumn, String aFieldValue) throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatementList.setString(aColumn, aFieldValue);
    }

    @Override
    void generateSqlPrepareIntCmd(int aColumn, int aFieldValue) throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    void addSqlPrepareCmd2Batch() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    void executeSqlPrepareCmd() throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatementList.executeBatch();
        mConnection.commit(); 
        mPreparedStatementList.clearBatch();
    }

}
