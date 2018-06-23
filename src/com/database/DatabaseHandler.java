package com.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseHandler {
    private String createDbSQL;

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
    }

    //read database config
    abstract void readConfig();
    
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
    abstract void generateSqlCmd();
    
    // generate SQL prepare string command for batch
    abstract void generateSqlPrepareStrCmd(int aColumn, String aFieldValue)throws SQLException;
    
    // generate SQL prepare integer command for batch
    abstract void generateSqlPrepareIntCmd(int aColumn, int aFieldValue)throws SQLException;
    
    // add SQL command for batch
    abstract void addSqlPrepareCmd2Batch() throws SQLException;

    // execute SQL prepare command for batch
    abstract void executeSqlPrepareCmd() throws SQLException;
}
