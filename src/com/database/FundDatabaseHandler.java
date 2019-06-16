package com.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.common.DatabaseConfig;
import com.common.StockFundInfo;

public class FundDatabaseHandler extends DatabaseHandler{
    
    /**
     * sync with TWSEFundParserHandler, OTCFundParserHandler
     * id, "證券代號", "日期", "殖利率", "本益比", "淨值比", "交易類別"
     */
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "stock_id     VARCHAR(4), " +
            "stock_date     DATE, " +
            "stock_yield_rate     INT, " +
            "stock_pbr     INT, " +
            "stock_per     INT, " +
            "stock_type     INT, " +
            "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";

    private StockFundInfo fundInfo;

    public FundDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        FundDatabaseHandler mStockDB = new FundDatabaseHandler();

        DBOperation(mStockDB);
    }
    
    protected void TestInsertTable() throws SQLException {
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_yield_rate, stock_pbr, stock_per, stock_type)"
              + "VALUES ('6116', '2017-06-03', 450, 0, 120, 2);";

        this.mPreparedStatement.addBatch(mInsertSql);
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }

    @Override
    public void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            this.mPreparedStatement = mConnection.prepareStatement(mInsertSql);
            // System.out.println(mInsertTechSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void setupDatabase() {
        // TODO Auto-generated method stub
        mTableName = "listed_fund";
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
            System.out.println("Close Exception :" + e.toString()); 
        }
    }

    @Override
    public void generateSqlCmd() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_yield_rate, stock_pbr, stock_per, stock_type)"
                + "VALUES (?, ?, ?, ?, ?, ?);";
    }

    @Override
    public void deleteSqlDuplicateData() throws SQLException {
        // TODO Auto-generated method stub
        String mInsertSql = "DELETE FROM "+ mTableName + " WHERE `id` in (SELECT b.id FROM (SELECT * FROM "+ mTableName + " a WHERE id<>(select MAX(id) FROM "+ mTableName + 
                " WHERE stock_id=a.stock_id and stock_date=a.stock_date)"
                + ")b);";
        //System.out.println(mInsertSql);
        this.mPreparedStatement.addBatch(mInsertSql);
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }
}
