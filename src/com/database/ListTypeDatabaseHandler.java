package com.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.common.DatabaseConfig;

public class ListTypeDatabaseHandler extends DatabaseHandler{
    private PreparedStatement mPreparedStatementList = null;
    private String mUrl;
    private String mTableName = "listed_type";
    private String mInsertTechSql = "";
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "type_name     VARCHAR(30)) ";

    public ListTypeDatabaseHandler() throws SQLException {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
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

    @Override
    void connectDatabase() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void setupDatabase() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void createTable() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void closeObject() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        
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
