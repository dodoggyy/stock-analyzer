package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.common.DatabaseConfig;
import com.common.Utility;

public abstract class DatabaseHandler {
    protected String createDbSQL = null;
    protected PreparedStatement mPreparedStatement = null;
    protected String mUrl;
    protected String mTableName;
    protected String mInsertSql = "";
    protected String mQuerySql = "";

    protected String host, port, socket, username, password, database, chartset;

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

    // read database config
    void readConfig() {
        host = DatabaseConfig.DB_KEY_HOST;
        port = DatabaseConfig.DB_KEY_PORT;
        socket = DatabaseConfig.DB_KEY_UNIX_SOCKET;
        username = DatabaseConfig.DB_KEY_USERNAME;
        password = DatabaseConfig.DB_KEY_PASSWORD;
        database = DatabaseConfig.DB_KEY_DATABASE;
        mUrl = "jdbc:" + DatabaseConfig.DB_SECTION_MYSQL + "://localhost:" + port + "/" + database
                + "?serverTimezone=UTC&characterEncoding=utf-8";
        // System.out.println(mUrl);
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

    // init query SQL cmd
    abstract void initQuerySql(String aStockID, String aDate);

    // execute query SQL cmd
    public ResultSet executeQuerySql() throws SQLException {
        return mStatement.executeQuery(mQuerySql);
    }

    // generate SQL prepare string command for batch
    public void generateSqlPrepareStrCmd(int aColumn, String aFieldValue) throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.setString(aColumn, aFieldValue);
    }

    // generate SQL prepare integer command for batch
    public void generateSqlPrepareIntCmd(int aColumn, int aFieldValue) throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.setInt(aColumn, aFieldValue);
    }

    // add SQL command for batch
    public void addSqlPrepareCmd2Batch() throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.addBatch();
    }

    // execute SQL prepare command for batch
    public void executeSqlPrepareCmd() throws SQLException {
        // TODO Auto-generated method stub
        mPreparedStatement.executeBatch();
        mConnection.commit();
        mPreparedStatement.clearBatch();
    }

    // execute SQL command for delete dublicate data
    abstract void deleteSqlDuplicateData() throws SQLException;

    abstract void TestInsertTable() throws SQLException;

    protected static void DBOperation(DatabaseHandler mStockDB) throws SQLException {
        int mOperationType = 0;
        Scanner mScanner = new Scanner(System.in);
        System.out.println("(0)Create Table (1)Test insert (2)Drop Table");
        mOperationType = mScanner.nextInt();
        mScanner.close();

        Utility.timerStart();

        switch (mOperationType) {
        case 0:
            // Only need execute create table in first time
            System.out.println("create table");
            mStockDB.createTable();
            break;
        case 1:
            // for(int i = 0; i < 100;i++)
            System.out.println("test insert table");
            mStockDB.TestInsertTable();
            break;
        case 2:
            System.out.println("drop table");
            break;
        default:
            break;
        }

        Utility.timerEnd();
    }
}
