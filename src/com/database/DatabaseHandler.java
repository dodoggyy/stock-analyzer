package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.common.DatabaseConfig;

public abstract class DatabaseHandler {
    protected String createDbSQL = null;
    protected PreparedStatement mPreparedStatement = null;
    protected String mUrl;
    protected String mTableName;
    protected String mInsertSql = "";

    protected    String host,
            port,
            socket,
            username,
            password,
            database,
            chartset;


    public Connection mConnection = null;
    public ResultSet mResultSet = null;
    public Statement mStatement = null;

    public DatabaseHandler() throws SQLException {
        this.closeObject();
        this.setupDatabase();
        this.initPrepareSql();
        this.readConfig();
        this.connectDatabase();
    }

    //read database config
    void readConfig() {
        host = DatabaseConfig.DB_KEY_HOST;
        port = DatabaseConfig.DB_KEY_PORT;
        socket = DatabaseConfig.DB_KEY_UNIX_SOCKET;
        username = DatabaseConfig.DB_KEY_USERNAME;
        password = DatabaseConfig.DB_KEY_PASSWORD;
        database = DatabaseConfig.DB_KEY_DATABASE;
        mUrl = "jdbc:" + DatabaseConfig.DB_SECTION_MYSQL + "://localhost:" + port + "/" + database +"?serverTimezone=UTC&characterEncoding=utf-8";
//        System.out.println(mUrl);
    }
    
    // connect Database
    abstract void connectDatabase();

    // setup related database parameters
    abstract void setupDatabase();

    // initial SQL table
    abstract void createTable();

    // close SQL related object
    abstract void closeObject();
    
    // init prepare SQL cmd
    abstract void initPrepareSql();

    // generate SQL command
    abstract void generateSqlCmd() throws SQLException;
    
    // generate SQL prepare string command for batch
    abstract void generateSqlPrepareStrCmd(int aColumn, String aFieldValue)throws SQLException;
    
    // generate SQL prepare integer command for batch
    abstract void generateSqlPrepareIntCmd(int aColumn, int aFieldValue)throws SQLException;
    
    // add SQL command for batch
    abstract void addSqlPrepareCmd2Batch() throws SQLException;

    // execute SQL prepare command for batch
    abstract void executeSqlPrepareCmd() throws SQLException;
    
    // execute SQL command for delete dublicate data
    abstract void deleteSqlDuplicateData() throws SQLException;
}
