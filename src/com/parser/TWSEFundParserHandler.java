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

public class TWSEFundParserHandler extends BaseParserHandler {

    private BufferedReader mBufferReader;
    private int mfileType;
    private String mDownloadName;

    public TWSEFundParserHandler() {
        this.ImportDir = new File(Config.DataAnalyze.outputDataDir);
        mDownloadName = Config.DataAnalyze.downloadName[Config.DataAnalyze.TWSE_FUND];

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

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TWSEFundParserHandler fundParser = new TWSEFundParserHandler();
        fundParser.parseAllFileData();
    }

    public boolean parseAllFileData() {
        String mFileName = "", mFileExt = "";
        int mSeparateIndex = 0;

        for (int i = 0; i < aFiles.length; i++) {
            if (!aFiles[i].isDirectory()) {
                mFileName = aFiles[i].getName();
                mSeparateIndex = mFileName.indexOf(".");
                mFileExt = mFileName.substring(mSeparateIndex);
                System.out.println("Deal SQL data with " + mFileName);
            }
            if (mFileName.substring(0, mDownloadName.length()).equals(mDownloadName)
                    && Config.DataAnalyze.csvFilter.contains(mFileExt)) {
                parseFileData(aFiles[i], mFileName.substring(mDownloadName.length(), mDownloadName.length())
                        + Config.DataAnalyze.DATE_LENGTH);
            }
        }
        return true;
    }

    @Override
    boolean writeData2DB(String aDate, String[] aStrArr) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
        String mTmpLine = "";
        String[] mStrArr;
        mfileType = Config.ErrorHandle.ERROR_MAX;
        boolean bIsBegin = false;
        boolean bIsValid = false;
        try {
            // csv file need to set decode as MS950 to prevent garbled
            mfileType = (aFile.getName().contains("BUT")) ? Config.ErrorHandle.TRANSCATION_DATA_EXCEPTION
                    : Config.ErrorHandle.TRANSCATION_DATA_NORMAL;
            InputStreamReader mStreamReader = new InputStreamReader(new FileInputStream(aFile), "MS950");
            mBufferReader = new BufferedReader(mStreamReader);

            try {
                while ((mTmpLine = mBufferReader.readLine()) != null) {
                    if (!bIsBegin) {
                        if (mTmpLine.contains("證券代號")) {
                            mBufferReader.readLine();
                            bIsBegin = true;
                        }

                    } else {
                        // 解決字串中的,號，如"200,450,000"==> 200450000
                        mStrArr = Utility.removeMessyChar(mTmpLine).split(",");
                        bIsValid = true;

                        // 略過非證券資料的列 e.g.欄位不符合TWSE資料
                        if ((mStrArr.length < 5) || (mStrArr[0].length() == 0)) {
                            bIsValid = false;
                        }

                        // update or insert to database table stock
                        if (bIsValid) {
                            for (int i = 0; i < mStrArr.length; i++) {
                                // 刪除多餘頭尾空白
                                mStrArr[i] = mStrArr[i].trim();
                            }

                            // 證券代號0,證券名稱1,殖利率2,股利年度3,本益比4,股價淨值比5,財報年季6
                            System.out.printf("代號:%s, 殖利率:%s,本益比:%s,股價淨值比:%s\n", mStrArr[0], mStrArr[2], mStrArr[4],
                                    mStrArr[5]);

                            // TWSE舊資料欄位不一致 20170413以前
                            if (mStrArr.length == Config.DataAnalyze.OLD_TWSE_FUND_LENGTH) {
                                // fixed me
                            } else {
                                // fixed me
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

}
