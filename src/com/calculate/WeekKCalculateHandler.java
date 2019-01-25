/**
 * 
 */
package com.calculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.common.Config;
import com.common.KeyDefine;
import com.database.WeekAverageDatabaseHandler;

/**
 * @author Chris Lin
 *
 */
public class WeekKCalculateHandler extends AverageKCalculateHandler {

    private WeekAverageDatabaseHandler mStockDB;

    public TechAverge mTechAvgSrc;
    public TechAverge mTechAvgDst;

    WeekKCalculateHandler() throws SQLException {
        mStockDB = new WeekAverageDatabaseHandler();

        mTechAvgSrc = new TechAverge();
        mTechAvgDst = new TechAverge();

        mTechAvgSrc.init();
        mTechAvgDst.init();

        mTechAvgSrc.clear();
        mTechAvgDst.clear();
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) {
        try {
            WeekKCalculateHandler mCalculator = new WeekKCalculateHandler();
            mCalculator.calculateValue("1102", false);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void calculateValue(String aStockId, boolean bIsUpdateAll) throws SQLException {
        // TODO Auto-generated method stub
        ResultSet mResultSet = null;

        mTechAvgSrc.clear();
        mTechAvgDst.clear();

        mSqlWhere = " WHERE stock_id=" + "'" + aStockId + "'";
        mSqlQueryCmd = "SELECT stock_date,stock_high_price,stock_low_price,stock_closing_price, stock_opening_price FROM "
                + "listed_tech" + mSqlWhere + " ORDER BY stock_date ASC";

        System.out.println(mSqlQueryCmd);

        try {
            mResultSet = mStockDB.mResultSet = mStockDB.mStatement.executeQuery(mSqlQueryCmd);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            while (mResultSet.next()) {
                System.out.printf("%s ", mResultSet.getString("stock_date"));
                System.out.println("Close:" + mResultSet.getDouble("stock_closing_price") / 100 + ",Open:"
                        + mResultSet.getDouble("stock_opening_price") / 100 + ", High:"
                        + mResultSet.getDouble("stock_high_price") / 100 + ", Low:"
                        + mResultSet.getDouble("stock_low_price") / 100);
                try {
                    mTechAvgSrc.mHighPrice.add(mResultSet.getDouble("stock_high_price"));
                    mTechAvgSrc.mLowPrice.add(mResultSet.getDouble("stock_low_price"));
                    mTechAvgSrc.mOpenPrice.add(mResultSet.getDouble("stock_opening_price"));
                    mTechAvgSrc.mClosePrice.add(mResultSet.getDouble("stock_closing_price"));
                    mTechAvgSrc.mDBDate.add(mResultSet.getDate("stock_date").toString());
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mResultSet.close();
    }

    @Override
    void writeData2DB() {
        // TODO Auto-generated method stub

    }

    void init() {

    }
}
