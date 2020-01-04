package com.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.common.DatabaseConfig;
import com.common.StockFundInfo;

public class FundDatabaseHandler extends DatabaseHandler{
    


    public FundDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        init();
    }
    
    public void init() {
        super.init();
        /**
         * sync with TWSEFundParserHandler, OTCFundParserHandler
         * id, "證券代號", "日期", "殖利率", "本益比", "淨值比", "交易類別"
         */
        createDbSQL = "CREATE TABLE " + mTableName + " (" + 
                "id     INT NOT NULL AUTO_INCREMENT," +
                "PRIMARY KEY (id)," +
                "stock_id     VARCHAR(4), " +
                "stock_date     DATE, " +
                "stock_yield_rate     INT, " +
                "stock_pbr     INT, " +
                "stock_per     INT, " +
                "stock_type     INT, " +
                "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";
    }
    
    protected void TestInsertTable() throws SQLException {
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_yield_rate, stock_pbr, stock_per, stock_type)"
              + "VALUES ('6116', '2099-06-03', 450, 0, 120, 2);";

        this.mPreparedStatement.addBatch(mInsertSql);
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }

    @Override
    public void setupDatabase() {
        // TODO Auto-generated method stub
        setTableName(DatabaseConfig.TABLE_DAY_FUND);
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

    @Override
    void initQuerySql(String aStockID, String aDate) {
        // TODO Auto-generated method stub
        
    }
    
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        FundDatabaseHandler mStockDB = new FundDatabaseHandler();

        DBOperation(mStockDB);
    }
}
