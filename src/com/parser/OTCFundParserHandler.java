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
import com.common.KeyDefine;
import com.common.Utility;
import com.database.FundDatabaseHandler;

public class OTCFundParserHandler extends BaseParserHandler {

    private FundDatabaseHandler mStockDB;
    private int mfileType;

    public OTCFundParserHandler() throws SQLException {
        this.ImportDir = new File(Config.DataAnalyze.outputDataDir);
        mDownloadName = Config.DataAnalyze.downloadName[KeyDefine.OTC_FUND] + "_";
        mStockDB = new FundDatabaseHandler();

        if (!this.ImportDir.exists()) {
            System.err.println("沒有這個目錄 " + ImportDir);
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
        }
        this.aFiles = ImportDir.listFiles();
        if (this.aFiles.length == 0) {
            System.err.println("No Files Match!");
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
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
        OTCFundParserHandler techParser = new OTCFundParserHandler();
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
//          System.out.println(" " + mFileName.substring(0, mDownloadName.length()));
//          System.out.printf("mDownloadName.length():%d\n",mDownloadName.length());
//          System.out.printf("%s\n",mFileName.substring(mDownloadName.length(), mDownloadName.length()
//                + Config.DataAnalyze.DATE_LENGTH));
            if (mFileName.substring(0, mDownloadName.length()).equals(mDownloadName)
                    && KeyDefine.csvFilter.contains(mFileExt)) {
                parseFileData(aFiles[i], mFileName.substring(mDownloadName.length(), mDownloadName.length()
                        + KeyDefine.DATE_LENGTH));
            }
        }

        //mStockDB.deleteSqlDuplicateData();
        mStockDB.executeSqlPrepareCmd();
        return true;
    }

    @Override
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
        int mLines = 0;
        String mTmpLine = "";
        String[] mStrArr;
        mfileType = KeyDefine.ErrorHandle.ERROR_MAX;
        try {
            // csv file need to set decode as MS950 to prevent garbled
            mfileType = (aFile.getName().contains("BUT")) ? KeyDefine.ErrorHandle.TRANSCATION_DATA_EXCEPTION
                    : KeyDefine.ErrorHandle.TRANSCATION_DATA_NORMAL;
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
                        if (mStrArr.length < 5 || mStrArr[0].length() != 4 || mStrArr[0].equals("股票代號")) {
                         // filter 非股票部分(權證)
                        } else {
                         // 代號0,名稱1,本益比2 ,股利3,殖利率4 ,淨值比5
                             //System.out.printf("代號:%s, 本益比:%s, 殖利率:%s,淨值比:%s\n",mStrArr[0], mStrArr[2], mStrArr[4], mStrArr[5]);
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
        //mStockDB.generateSqlPrepareIntCmd(8, Config.DataAnalyze.TWSE); // stock_type
        //System.out.printf("代號:%s, 本益比:%s, 殖利率:%s,淨值比:%s\n",aStrArr[0], aStrArr[2], aStrArr[4], aStrArr[5]);
        mStockDB.generateSqlPrepareStrCmd(1, aStrArr[0]); // stock_id
        mStockDB.generateSqlPrepareStrCmd(2, aDate); // stock_date
        mStockDB.generateSqlPrepareIntCmd(3, Utility.float2Int(aStrArr[4], 2)); // stock_yield_rate
        mStockDB.generateSqlPrepareIntCmd(4, Utility.float2Int(aStrArr[2], 2)); // stock_pbr
        mStockDB.generateSqlPrepareIntCmd(5, Utility.float2Int(aStrArr[5], 2)); // stock_per

        mStockDB.addSqlPrepareCmd2Batch();
        return true;
    }

}
