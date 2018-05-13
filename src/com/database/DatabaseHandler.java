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

    // generate SQL command
    abstract void generateSqlCmd();

}
