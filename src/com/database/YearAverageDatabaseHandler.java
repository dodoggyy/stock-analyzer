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
public class YearAverageDatabaseHandler extends AverageDatabaseHandler {
    
    public YearAverageDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        super();
    }

    protected void setupDatabase() {
        // TODO Auto-generated method stub
        super.setupDatabase();
        setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_YEAR);
    }

    /**
     * @param args
     * @throws SQLException
     * @throws ParseException
     */
    public static void main(String[] args) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        YearAverageDatabaseHandler mStockDB = new YearAverageDatabaseHandler();
        DBOperation(mStockDB);
    }
}
