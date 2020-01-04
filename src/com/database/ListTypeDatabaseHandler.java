package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.common.DatabaseConfig;
import com.common.KeyDefine;

public class ListTypeDatabaseHandler extends DatabaseHandler{
    private String mInsertListSql = "";

    public ListTypeDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        init();
    }

    public void init() {
        super.init();
        /**
         * id, "交易類別"
         */
        createDbSQL = "CREATE TABLE " + mTableName + " (" + 
                "id     INT NOT NULL AUTO_INCREMENT," +
                "PRIMARY KEY (id)," +
                "type_name     VARCHAR(20)) ";
    }

    protected void TestInsertTable() throws SQLException {
        String mInsertSql="";
        for(int i = 0; i < KeyDefine.DEFAULT_LIST.length;i++) {
        mInsertSql =  "INSERT INTO " + mTableName + " (type_name)"
              + "VALUES ( \""+ KeyDefine.DEFAULT_LIST[i] +"\");";
        log.debug(mInsertSql);

        this.mPreparedStatement.addBatch(mInsertSql);
        }
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }

    @Override
    void setupDatabase() {
        // TODO Auto-generated method stub
        setTableName(DatabaseConfig.TABLE_DAY_TYPE);
    }

    @Override
    void initPrepareSql() {
        // TODO Auto-generated method stub
        mInsertListSql =  "INSERT INTO " + mTableName + " (type_name)"
                + "VALUES (?);";
    }

    @Override
    void generateSqlCmd() {
        // TODO Auto-generated method stub
        
    }

    @Override
    void deleteSqlDuplicateData() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    @Override
    void initQuerySql(String aStockID, String aDate) {
        // TODO Auto-generated method stub
        
    }

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        ListTypeDatabaseHandler mStockDB = new ListTypeDatabaseHandler();
        
        DBOperation(mStockDB);
    }
}
