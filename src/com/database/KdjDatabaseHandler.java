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
 * @author Chris Lin
 *
 */
public class KdjDatabaseHandler extends DatabaseHandler{
    private CalculateCycle mCalculateCycle = KeyDefine.CalculateCycle.CYCLE_MAX;
    
    public KdjDatabaseHandler(CalculateCycle enCycleType) throws SQLException {
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
                "K     INT, " +
                "D     INT, " +
                "J     INT, " +
                "stock_type     INT, " +
                "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";
    }

    @Override
    protected void setupDatabase() {
        // TODO Auto-generated method stub
        switch(mCalculateCycle) {
        case CYCLE_DAY:
            setTableName(DatabaseConfig.DEFAULT_TECH_KDJ_DAY);
            break;
        case CYCLE_WEEK:
            setTableName(DatabaseConfig.DEFAULT_TECH_KDJ_WEEK);
            break;
        case CYCLE_MONTH:
            setTableName(DatabaseConfig.DEFAULT_TECH_KDJ_MONTH);
            break;
        case CYCLE_SEASON:
            setTableName(DatabaseConfig.DEFAULT_TECH_KDJ_SEASON);
            break;
        case CYCLE_YEAR:
            setTableName(DatabaseConfig.DEFAULT_TECH_KDJ_YEAR);
            break;
        default:
            log.error("Unknown cycle or not define");
            break;
        }
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, K, D, J)"
                + "VALUES (?, ?, ?, ?, ?);";
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
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, K, D, J, stock_type)"
              + "VALUES ('6116', '2099-06-03', 3819, 3200, 1962, 2);";

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

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        KdjDatabaseHandler mStockDB = new KdjDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_DAY);

        DBOperation(mStockDB);
    }
}
