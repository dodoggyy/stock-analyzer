package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

import com.common.Config;
import com.common.Utility;
import com.database.TechDatabaseHandler;

public class OTCTechParserHandler extends BaseParserHandler {

    private TechDatabaseHandler mStockDB;
    private BufferedReader mBufferReader;
    private int mfileType;
    private String mDownloadName;

    public OTCTechParserHandler() throws SQLException {
        this.ImportDir = new File(Config.DataAnalyze.outputDataDir);
        mDownloadName = Config.DataAnalyze.downloadName[Config.DataAnalyze.OTC_TECH] + "_";
        mStockDB = new TechDatabaseHandler();

        if (!this.ImportDir.exists()) {
            System.err.println("沒有這個目錄 " + ImportDir);
            System.exit(Config.ErrorHandle.EXIT_ERROR);
        }
        this.aFiles = ImportDir.listFiles();
        if (this.aFiles.length == 0) {
            System.err.println("No Files Match!");
            System.exit(Config.ErrorHandle.EXIT_ERROR);
        }
        Arrays.sort(aFiles, new Comparator<Object>() {
            @Override
            public int compare(Object aFile1, Object aFile2) {
                return ((File) aFile1).getName().compareTo(((File) aFile2).getName());
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        OTCTechParserHandler techParser = new OTCTechParserHandler();
        techParser.parseAllFileData();
    }

    public boolean parseAllFileData() throws SQLException {
        String mFileName = "", mFileExt = "";
        int mSeparateIndex = 0;

        for (int i = 0; i < aFiles.length; i++) {
            if (!aFiles[i].isDirectory()) {
                mFileName = aFiles[i].getName();
                mSeparateIndex = mFileName.indexOf(".");
                mFileExt = mFileName.substring(mSeparateIndex);
                System.out.println("Deal SQL data with " + mFileName);
            }
//            System.out.println(" " + mFileName.substring(0, mDownloadName.length()));
//            System.out.printf("mDownloadName.length():%d\n",mDownloadName.length());
//            System.out.printf("%s\n",mFileName.substring(mDownloadName.length(), mDownloadName.length()
//                    + Config.DataAnalyze.DATE_LENGTH));
            if (mFileName.substring(0, mDownloadName.length()).equals(mDownloadName)
                    && Config.DataAnalyze.csvFilter.contains(mFileExt)) {
                parseFileData(aFiles[i], mFileName.substring(mDownloadName.length(), mDownloadName.length()
                        + Config.DataAnalyze.DATE_LENGTH));
            }
        }
        mStockDB.executeSqlPrepareCmd();
        return true;
    }

    @Override
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
        //System.out.println(aDate);
        int mLines = 0;
        String mTmpLine = "";
        String[] mStrArr;
        mfileType = Config.ErrorHandle.ERROR_MAX;
        try {
            // csv file need to set decode as MS950 to prevent garbled
            mfileType = (aFile.getName().contains("BUT")) ? Config.ErrorHandle.TRANSCATION_DATA_EXCEPTION
                    : Config.ErrorHandle.TRANSCATION_DATA_NORMAL;
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
                            // 代號0,名稱1,收盤2 ,漲跌3,開盤4 ,最高5 ,最低6,均價7 ,成交股數8
//                            System.out.printf("日期:%s,  代號:%s, 收盤:%s, 成交股數:%s, 開盤:%s, 最高:%s,最低:%s\n", aDate, mStrArr[0], mStrArr[2], mStrArr[8], mStrArr[4],
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
//              aStrArr[8], aStrArr[4], aStrArr[5], aStrArr[6]);
        mStockDB.generateSqlPrepareStrCmd(1, aStrArr[0]); // stock_id
        mStockDB.generateSqlPrepareStrCmd(2, aDate); // stock_date
        mStockDB.generateSqlPrepareIntCmd(3, Utility.float2Int(aStrArr[2], 2)); // stock_closing_price
        mStockDB.generateSqlPrepareIntCmd(4, Utility.float2Int(aStrArr[4], 2)); // stock_opening_price
        mStockDB.generateSqlPrepareIntCmd(5, Utility.float2Int(aStrArr[5], 2)); // stock_high_price
        mStockDB.generateSqlPrepareIntCmd(6, Utility.float2Int(aStrArr[6], 2)); // stock_low_price
        mStockDB.generateSqlPrepareIntCmd(7, Utility.float2Int(aStrArr[8], 0)); // stock_volume

        mStockDB.addSqlPrepareCmd2Batch();
        return true;
    }

}
