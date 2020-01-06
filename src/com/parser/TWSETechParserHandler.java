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

public class TWSETechParserHandler extends BaseParserHandler {

    public TWSETechParserHandler() throws SQLException {
        super();
        mDownloadName = Config.DataAnalyze.downloadName[KeyDefine.TWSE_TECH] + "_"; // fixed me
        mStockDB = new TechDatabaseHandler();
    }

    @Override
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
        //System.out.println(aDate);
        String mTmpLine = "";
        String[] mStrArr;
        int mfileType = KeyDefine.ErrorHandle.ERROR_MAX;
        boolean bIsBegin = false;
        boolean bIsValid = false;
        try {
            // csv file need to set decode as MS950 to prevent garbled
            mfileType = (aFile.getName().contains("BUT")) ? KeyDefine.ErrorHandle.TRANSCATION_DATA_EXCEPTION
                    : KeyDefine.ErrorHandle.TRANSCATION_DATA_NORMAL;
            InputStreamReader mStreamReader = new InputStreamReader(new FileInputStream(aFile), "MS950");
            mBufferReader = new BufferedReader(mStreamReader);

            try {
                while ((mTmpLine = mBufferReader.readLine()) != null) {
                    if (!bIsBegin) {
                        if (mfileType == KeyDefine.ErrorHandle.TRANSCATION_DATA_NORMAL && mTmpLine.contains("每日收盤行情")) {
                            mBufferReader.readLine();
                            bIsBegin = true;
                        } else if (mfileType == KeyDefine.ErrorHandle.TRANSCATION_DATA_EXCEPTION
                                && mTmpLine.contains("恢復交易者。")) {
                            mTmpLine = mBufferReader.readLine();
                            mBufferReader.readLine();
                            mBufferReader.readLine();
                            bIsBegin = true;
                        }

                    } else {
                        // 解決字串中的,號，如"200,450,000"==> 200450000
                        mStrArr = Utility.removeMessyChar(mTmpLine).split(",");
                        bIsValid = true;

                        // 略過非證券資料的列 e.g.欄位不符合TWSE資料, 指數資料, 證券標題
                        if ((mStrArr.length < 8) || (mStrArr[0].length() != 4) || (mStrArr[0].equals("證券代號"))
                                || (mStrArr[0].length() == 0)) {
                            bIsValid = false;
                        }

                        // update or insert to database table stock
                        if (bIsValid) {
                            for (int i = 0; i < mStrArr.length; i++) {
                                // 刪除多餘頭尾空白
                                mStrArr[i] = mStrArr[i].trim();
                            }

                            // 證券代號0,證券名稱1,成交股數2,成交筆數3,成交金額4,開盤價5,最高價6,最低價7,收盤價8
//                            System.out.printf("代號:%s, 收盤:%s, 成交股數:%s, 開盤:%s,最高:%s, 最低:%s\n", mStrArr[0], mStrArr[8],
//                                    mStrArr[2], mStrArr[5], mStrArr[6], mStrArr[7]);

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
        //System.out.printf("代號:%s, 收盤:%s, 成交股數:%s, 開盤:%s,最高:%s, 最低:%s\n", aStrArr[0], aStrArr[8],
              //aStrArr[2], aStrArr[5], aStrArr[6], aStrArr[7]);
        mStockDB.generateSqlPrepareStrCmd(1, aStrArr[0]); // stock_id
        mStockDB.generateSqlPrepareStrCmd(2, aDate); // stock_date
        mStockDB.generateSqlPrepareIntCmd(3, Utility.float2Int(aStrArr[8], 2)); // stock_closing_price
        mStockDB.generateSqlPrepareIntCmd(4, Utility.float2Int(aStrArr[5], 2)); // stock_opening_price
        mStockDB.generateSqlPrepareIntCmd(5, Utility.float2Int(aStrArr[6], 2)); // stock_high_price
        mStockDB.generateSqlPrepareIntCmd(6, Utility.float2Int(aStrArr[7], 2)); // stock_low_price
        mStockDB.generateSqlPrepareIntCmd(7, Utility.float2Int(aStrArr[2], 0) / KeyDefine.THOUSAND_OF_SHARES); // stock_volume
        mStockDB.generateSqlPrepareIntCmd(8, KeyDefine.TWSE_TECH + 1); // stock_type

        mStockDB.addSqlPrepareCmd2Batch();
        return true;
    }

    public static void main(String[] args) throws SQLException {
        TWSETechParserHandler techParser = new TWSETechParserHandler();
        techParser.parseAllFileData();
    }
}
