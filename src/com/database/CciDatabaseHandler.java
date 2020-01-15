/**
 * 
 */
package com.database;

import java.sql.SQLException;

import com.common.DatabaseConfig;
import com.common.KeyDefine;
import com.common.KeyDefine.CalculateCycle;

/**
 * @author Chris Lin
 *
 */
public class CciDatabaseHandler extends DatabaseHandler{
    private CalculateCycle mCalculateCycle = KeyDefine.CalculateCycle.CYCLE_MAX;
    
    public CciDatabaseHandler(CalculateCycle enCycleType) throws SQLException {
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
                "CCI     INT, " +
                "stock_type     INT, " +
                "FOREIGN KEY(stock_type)REFERENCES listed_type(id)) ";
    }

    @Override
    protected void setupDatabase() {
        // TODO Auto-generated method stub
        switch(mCalculateCycle) {
        case CYCLE_DAY:
            setTableName(DatabaseConfig.DEFAULT_TECH_CCI_DAY);
            break;
        case CYCLE_WEEK:
            setTableName(DatabaseConfig.DEFAULT_TECH_CCI_WEEK);
            break;
        case CYCLE_MONTH:
            setTableName(DatabaseConfig.DEFAULT_TECH_CCI_MONTH);
            break;
        case CYCLE_SEASON:
            setTableName(DatabaseConfig.DEFAULT_TECH_CCI_SEASON);
            break;
        case CYCLE_YEAR:
            setTableName(DatabaseConfig.DEFAULT_TECH_CCI_YEAR);
            break;
        default:
            log.error("Unknown cycle or not define");
            break;
        }
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, CCI)"
                + "VALUES (?, ?, ?);";
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
        String mInsertSql =  "INSERT INTO " + mTableName + " (stock_id, stock_date, CCI, stock_type)"
              + "VALUES ('6116', '2099-06-03', -12972, 2);";

        executeSqlPrepareCmd(mInsertSql);
    }


    @Override
    void initQuerySql(String aStockID, String aDate) {
        // TODO Auto-generated method stub
    }

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
//        CciDatabaseHandler mStockDB = new CciDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_DAY);
//        CciDatabaseHandler mStockDB = new CciDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_WEEK);
//        CciDatabaseHandler mStockDB = new CciDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_MONTH);
//        CciDatabaseHandler mStockDB = new CciDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_SEASON);
        CciDatabaseHandler mStockDB = new CciDatabaseHandler(KeyDefine.CalculateCycle.CYCLE_YEAR);

        DBOperation(mStockDB);
    }
}
