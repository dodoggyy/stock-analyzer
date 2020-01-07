/**
 * 
 */
package com.database;

import java.sql.SQLException;
import java.util.ArrayList;

import com.common.DatabaseConfig;
import com.common.KeyDefine;
import com.common.KeyDefine.CalculateCycle;

/**
 * @author Chris
 *
 */
public class AverageDatabaseHandler extends DatabaseHandler{
    private CalculateCycle mCalculateCycle = KeyDefine.CalculateCycle.CYCLE_MAX;
    
    public AverageDatabaseHandler(CalculateCycle enCycleType) throws SQLException {
        // TODO Auto-generated constructor stub
        this.mCalculateCycle = enCycleType;
        this.init();
    }

    public void init() {
        this.setupDatabase();
        super.init();
        createDbSQL = "CREATE TABLE " + mTableName + " (" + 
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
    }

    @Override
    protected void setupDatabase() {
        // TODO Auto-generated method stub
        switch(mCalculateCycle) {
        case CYCLE_WEEK:
            setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_WEEK);
            break;
        case CYCLE_MONTH:
            setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_MONTH);
            break;
        case CYCLE_SEASON:
            setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_SEASON);
            break;
        case CYCLE_YEAR:
            setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_YEAR);
            break;
        default:
            log.error("Unknown cycle or not define");
            break;
        }
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_closing_price, stock_opening_price, stock_high_price, stock_low_price, stock_volume)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    void generateSqlCmd() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    void deleteSqlDuplicateData() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void TestInsertTable() throws SQLException {
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, stock_closing_price, stock_opening_price, stock_high_price, stock_low_price, stock_volume, stock_type)"
              + "VALUES ('6116', '2099-06-03', 802, 802, 804, 796, 21080000, 2);";

        executeSqlPrepareCmd(mInsertSql);
    }


    @Override
    void initQuerySql(String aStockID, String aDate) {
        // TODO Auto-generated method stub
    }
    
    public ArrayList<String> queryAllStockId() {
        ArrayList<String> mDbStockId = new ArrayList<String>();
        String mQueryStockIdSql = "SELECT DISTINCT stock_id FROM " + DatabaseConfig.TABLE_DAY_TECH + " ORDER BY stock_id ASC";
        try {
            mStatement = mConnection.createStatement();
            mResultSet = mStatement.executeQuery(mQueryStockIdSql);
            while(mResultSet.next()) {
                log.trace(mResultSet.getString("stock_id"));
                mDbStockId.add(mResultSet.getString("stock_id"));
            }
        } catch(SQLException e) {
            log.error("QueryDB Exception :" + e.toString()); 
        } finally {
            this.closeObject();
        }
        
        return mDbStockId;
    }
}
