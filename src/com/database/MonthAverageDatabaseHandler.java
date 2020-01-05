/**
 * 
 */
package com.database;

import java.sql.SQLException;
import java.text.ParseException;

import com.common.DatabaseConfig;

/**
 * @author Chris Lin
 *
 */
public class MonthAverageDatabaseHandler extends AverageDatabaseHandler {
    
    public MonthAverageDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        super();
    }

    protected void setupDatabase() {
        // TODO Auto-generated method stub
        super.setupDatabase();
        setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_MONTH);
    }

    /**
     * @param args
     * @throws SQLException
     * @throws ParseException
     */
    public static void main(String[] args) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        MonthAverageDatabaseHandler mStockDB = new MonthAverageDatabaseHandler();
        DBOperation(mStockDB);
    }
}
