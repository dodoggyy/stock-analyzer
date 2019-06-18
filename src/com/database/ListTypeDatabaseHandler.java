package com.database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.common.DatabaseConfig;
import com.common.KeyDefine;

public class ListTypeDatabaseHandler extends DatabaseHandler{
    private String mInsertListSql = "";

    /**
     * id, "交易類別"
     */
    private String createDbSQL = "CREATE TABLE " + mTableName + " (" + 
            "id     INT NOT NULL AUTO_INCREMENT," +
            "PRIMARY KEY (id)," +
            "type_name     VARCHAR(20)) ";

    private String mFiledTypeName = "";
    
    public ListTypeDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        ListTypeDatabaseHandler mStockDB = new ListTypeDatabaseHandler();
        
        DBOperation(mStockDB);
    }
    
    protected void TestInsertTable() throws SQLException {
        String mInsertSql="";
        for(int i = 0; i < KeyDefine.DEFAULT_LIST.length;i++) {
        mInsertSql =  "INSERT INTO " + mTableName + " (type_name)"
              + "VALUES ( \""+ KeyDefine.DEFAULT_LIST[i] +"\");";
        System.out.println(mInsertSql);

        this.mPreparedStatement.addBatch(mInsertSql);
        }
        this.mPreparedStatement.executeBatch();
        this.mConnection.commit();
        mPreparedStatement.clearBatch();
    }

    @Override
    void connectDatabase() {
        // TODO Auto-generated method stub
        try {
            
            mConnection = DriverManager.getConnection(mUrl, username, password);
            mConnection.setAutoCommit(false);
            this.mStatement = mConnection.createStatement();
            this.mPreparedStatement = mConnection.prepareStatement(mInsertListSql);
            // System.out.println(mInsertSql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    void setupDatabase() {
        // TODO Auto-generated method stub
        mFiledTypeName = "type_name";
        mTableName = "listed_type";
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

}
