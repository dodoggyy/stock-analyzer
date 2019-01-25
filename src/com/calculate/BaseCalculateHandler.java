/**
 * 
 */
package com.calculate;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Chris Lin
 *
 */
public abstract class BaseCalculateHandler {
    
    protected String mSqlQueryCmd = "";
    protected String mSqlWhere = "";
    
    abstract void calculateValue();
    
    // write data to DB
    abstract void writeData2DB();
    
    
}
