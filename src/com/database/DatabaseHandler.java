package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.common.DatabaseConfig;
import com.common.KeyDefine;
import com.common.Utility;

public abstract class DatabaseHandler {
    protected static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    
    protected String createDbSQL;
    protected PreparedStatement mPreparedStatement = null;
    protected String mUrl;
    protected String mTableName;
    protected String mInsertSql;
    protected String mQuerySql;
    protected String dropDbSQL;

    protected String host, port, socket, username, password, database, chartset;

    public Connection mConnection = null;
    public ResultSet mResultSet = null;
    public Statement mStatement = null;

    public DatabaseHandler() throws SQLException {
    }
    
    public void init() {
        // set log level
        Configurator.setRootLevel(KeyDefine.LOG_LEVEL);
        closeObject();
        setupDatabase();
        initPrepareSql();
        readConfig();
        connectDatabase();
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
        dropDbSQL = "DROP TABLE IF EXISTS " +  getTableName();
        log.debug(mUrl);
    }

    // connect Database
    public void connectDatabase() {
        try {
            log.debug(mInsertSql);
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            this.mPreparedStatement = mConnection.prepareStatement(mInsertSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // setup related database parameters
    abstract void setupDatabase();
    
    // initial SQL table
    public void createTable() {
        try {
            log.debug("Drop SQL:" + dropDbSQL);
            log.debug("Create SQL:" + createDbSQL);
            this.mConnection = DriverManager.getConnection(mUrl, username, password);
            this.mStatement = mConnection.createStatement();
            this.mStatement.executeUpdate(dropDbSQL);
            this.mStatement.executeUpdate(createDbSQL);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("DriverClassNotFound :"+e.toString());
        } finally {
            this.closeObject(); 
        }
    }
    
    // initial SQL table
    public void dropTable() {
        try {
            log.debug("Drop SQL:" + dropDbSQL);
            this.mConnection = DriverManager.getConnection(mUrl, username, password);
            this.mStatement = mConnection.createStatement();
            this.mStatement.executeUpdate(dropDbSQL);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("DriverClassNotFound :"+e.toString());
        } finally { 
            this.closeObject(); 
        }
    }

    // close SQL related object
    public void closeObject() {
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
            log.error("Close Exception :" + e.toString()); 
        }
    }

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
    
    public void executeSqlPrepareCmd(String aSqlCmd) throws SQLException {
        this.mPreparedStatement.addBatch(aSqlCmd);
        executeSqlPrepareCmd();
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
            log.info("create table");
            mStockDB.createTable();
            break;
        case 1:
            // for(int i = 0; i < 100;i++)
            log.info("test insert table");
            mStockDB.TestInsertTable();
            break;
        case 2:
            log.info("drop table");
            mStockDB.dropTable();
            break;
        default:
            break;
        }

        Utility.timerEnd();
    }

    /**
     * @return the mTableName
     */
    public String getTableName() {
        return mTableName;
    }

    /**
     * @param mTableName the mTableName to set
     */
    public void setTableName(String mTableName) {
        this.mTableName = mTableName;
    }
}
