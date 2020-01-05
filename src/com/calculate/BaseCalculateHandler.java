/**
 * 
 */
package com.calculate;


import java.sql.SQLException;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Chris Lin
 *
 */
public abstract class BaseCalculateHandler {
    protected static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    protected String mSqlQueryCmd = "";
    protected String mSqlWhere = "";

    abstract void calculateValue(String aStockId, boolean bIsUpdateAll) throws SQLException, ParseException;

    // write data to DB
    abstract boolean writeData2DB() throws SQLException;
}
