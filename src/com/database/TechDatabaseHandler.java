package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

import com.common.Config;
import com.common.StockTechInfo;
import com.common.Utility;

public class TechDatabaseHandler extends DatabaseHandler {
    /**
     * sync with TWSETechParserHandler, OTCTechParserHandler
     * id, "證券代號", "日期", "收盤價", "開盤價", "最高價", "最低價", "成交量", "交易類別"
     */
    private String dropDbSQL = "DROP TABLE IF EXISTS " +  mTableName;
    
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "stock_id     VARCHAR(4), " +
            "stock_date     DATE, " +
            "stock_closing_price     INT, " +
            "stock_opening_price     INT, " +
            "stock_high_price     INT, " +
            "stock_low_price     INT, " +
            "stock_volume     INT, " +
            "stock_type     INT, " +
            "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";

//    private StockTechInfo mTechInfo;

    public static void main(String[] args) throws SQLException {
        TechDatabaseHandler mStockDB = new TechDatabaseHandler();
        
//        DBOperation(mStockDB);
        try {
            System.out.println(mStockDB.getDbLatestDate());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //mStockDB.deleteSqlDuplicateData();


    }
    
    protected void TestInsertTable() throws SQLException {
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_closing_price, stock_opening_price, stock_high_price, stock_low_price, stock_volume, stock_type)"
              + "VALUES ('6116', '2017-06-03', 802, 802, 804, 796, 21080000, 2);";

        executeSqlPrepareCmd(mInsertSql);
    }
    
    public TechDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            this.mPreparedStatement = mConnection.prepareStatement(mInsertSql);
            // System.out.println(mInsertSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    void setupDatabase() {
        // TODO Auto-generated method stub
        mTableName = "listed_tech";
    }

    @Override
    void createTable() {
        // TODO Auto-generated method stub
        try {
            this.mConnection = DriverManager.getConnection(mUrl, username, password);
            this.mStatement = mConnection.createStatement();
            this.mStatement.executeUpdate(dropDbSQL);
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
            if(mPreparedStatement != null) { 
                mPreparedStatement.close(); 
                mPreparedStatement = null;
            }
        } catch(SQLException e) { 
            System.out.println("Close Exception :" + e.toString()); 
        }
    }

    @Override
    void generateSqlCmd(){
        // TODO Auto-generated method stub
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_closing_price, stock_opening_price, stock_high_price, stock_low_price, stock_volume, stock_type)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    public void deleteSqlDuplicateData() throws SQLException {
        // TODO Auto-generated method stub
        String mInsertSql = "DELETE FROM "+ mTableName + " WHERE `id` in (SELECT b.id FROM (SELECT * FROM "+ mTableName + " a WHERE id<>(select MAX(id) FROM "+ mTableName + 
                " WHERE stock_id=a.stock_id and stock_date=a.stock_date)"
                + ")b);";
        System.out.println(mInsertSql);
        executeSqlPrepareCmd(mInsertSql);
    }

    @Override
    public void initQuerySql(String aStockID, String aDate) {
        // TODO Auto-generated method stub
        try {
            mQuerySql = "SELECT stock_date,stock_high_price,stock_low_price,stock_opening_price,stock_closing_price,stock_volume FROM " + mTableName
                    + " WHERE stock_id=" + "'" + aStockID 
                    +  "' AND stock_date >" + "'" + aDate 
                    + "'  AND stock_date < '" + Utility.getDateAfterDay(aDate, Config.DataAnalyze.BACK_TEST_TRACKING_DAY)
                    +  "'" + " ORDER BY stock_date ASC";
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(mQuerySql);
    }
    
    public String getDbLatestDate() throws SQLException, ParseException{
        String mDate = "";
        String mLatestDate = "latest_date";
        String mQueryDateSql = "SELECT MAX(stock_date) AS " + mLatestDate + " FROM " + mTableName;
        
        mResultSet = mStatement.executeQuery(mQueryDateSql);
        while(mResultSet.next()) {
            mDate = Utility.date2String(mResultSet.getDate(mLatestDate));
        }
        
        return mDate;
    }
}
