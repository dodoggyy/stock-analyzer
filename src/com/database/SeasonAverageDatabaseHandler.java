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
public class SeasonAverageDatabaseHandler extends AverageDatabaseHandler {

    public SeasonAverageDatabaseHandler() throws SQLException {
        // TODO Auto-generated constructor stub
        super();
    }

    protected void setupDatabase() {
        // TODO Auto-generated method stub
        super.setupDatabase();
        setTableName(DatabaseConfig.DEFAULT_LISTED_TECH_SEASON);
    }

    /**
     * @param args
     * @throws SQLException
     * @throws ParseException
     */
    public static void main(String[] args) throws SQLException, ParseException {
        // TODO Auto-generated method stub
        SeasonAverageDatabaseHandler mStockDB = new SeasonAverageDatabaseHandler();
        DBOperation(mStockDB);
    }
}
