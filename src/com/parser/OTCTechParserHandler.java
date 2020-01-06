package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import com.common.Config;
import com.common.KeyDefine;
import com.common.Utility;
import com.database.TechDatabaseHandler;

public class OTCTechParserHandler extends BaseParserHandler {

    public OTCTechParserHandler() throws SQLException {
        super();
        mDownloadName = Config.DataAnalyze.downloadName[KeyDefine.OTC_TECH] + "_";
        mStockDB = new TechDatabaseHandler();
    }

    @Override
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
        //System.out.println(aDate);
        int mLines = 0;
        String mTmpLine = "";
        String[] mStrArr;
        try {
            // csv file need to set decode as MS950 to prevent garbled
            InputStreamReader mStreamReader = new InputStreamReader(new FileInputStream(aFile), "MS950");
            mBufferReader = new BufferedReader(mStreamReader);

            try {
                while ((mTmpLine = mBufferReader.readLine()) != null) {
                    if (mTmpLine.contains("管理股票")) {
                        break;
                    }
                    mLines++;
                    if (mLines >= 3) {
                        mStrArr = Utility.removeMessyChar(mTmpLine).split(",");
                        if (mStrArr.length < 9 || mStrArr[0].length() != 4) {
                            // filter 非股票部分(權證)
                        } else {
                            // 代號0,名稱1,收盤2 ,漲跌3,開盤4 ,最高5 ,最低6,成交股數7 ,成交金額8
//                            System.out.printf("日期:%s,  代號:%s, 收盤:%s, 成交股數:%s, 開盤:%s, 最高:%s,最低:%s\n", aDate, mStrArr[0], mStrArr[2], mStrArr[7], mStrArr[4],
//                                    mStrArr[5], mStrArr[6]);
                            try {
                                writeData2DB(aDate, mStrArr);
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
                mBufferReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    @Override
    boolean writeData2DB(String aDate, String[] aStrArr) throws SQLException {
        // TODO Auto-generated method stub
     // listed_tech
//        System.out.printf("代號:%s, 收盤:%s, 成交股數:%s, 開盤:%s,最高:%s, 最低:%s\n", aStrArr[0], aStrArr[2],
//              aStrArr[7], aStrArr[4], aStrArr[5], aStrArr[6]);
        mStockDB.generateSqlPrepareStrCmd(1, aStrArr[0]); // stock_id
        mStockDB.generateSqlPrepareStrCmd(2, aDate); // stock_date
        mStockDB.generateSqlPrepareIntCmd(3, Utility.float2Int(aStrArr[2], 2)); // stock_closing_price
        mStockDB.generateSqlPrepareIntCmd(4, Utility.float2Int(aStrArr[4], 2)); // stock_opening_price
        mStockDB.generateSqlPrepareIntCmd(5, Utility.float2Int(aStrArr[5], 2)); // stock_high_price
        mStockDB.generateSqlPrepareIntCmd(6, Utility.float2Int(aStrArr[6], 2)); // stock_low_price
        mStockDB.generateSqlPrepareIntCmd(7, Utility.float2Int(aStrArr[7], 0)/ KeyDefine.THOUSAND_OF_SHARES); // stock_volume
        mStockDB.generateSqlPrepareIntCmd(8, KeyDefine.OTC_TECH + 1); // stock_type

        mStockDB.addSqlPrepareCmd2Batch();

        return true;
    }

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        OTCTechParserHandler techParser = new OTCTechParserHandler();
        techParser.parseAllFileData();
    }
}
