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
public class WeekAverageDatabaseHandler extends AverageDatabaseHandler {

    public WeekAverageDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        super();
    }

    protected void setupDatabase() {
        // TODO Auto-generated method stub
        super.setupDatabase();
        setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_WEEK);
    }

    /**
     * @param args
     * @throws SQLException
     * @throws ParseException
     */
    public static void main(String[] args) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        WeekAverageDatabaseHandler mStockDB = new WeekAverageDatabaseHandler();
        DBOperation(mStockDB);
    }
}
