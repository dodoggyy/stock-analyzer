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

public class OTCFundParserHandler extends BaseParserHandler {
    private BufferedReader mBufferReader;
    private int mfileType;
    private String mDownloadName;

    public OTCFundParserHandler() {
        this.ImportDir = new File(Config.DataAnalyze.outputDataDir);
        mDownloadName = Config.DataAnalyze.downloadName[Config.DataAnalyze.OTC_FUND];

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
        OTCFundParserHandler techParser = new OTCFundParserHandler();
        techParser.parseAllFileData();
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
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
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
                        if (mStrArr.length < 5 || mStrArr[0].length() != 4 || mStrArr[0].equals("股票代號")) {
                         // filter 非股票部分(權證)
                        } else {
                         // 代號0,名稱1,本益比2 ,股利3,殖利率4 ,淨值比5
                             System.out.printf("代號:%s, 本益比:%s, 殖利率:%s,淨值比:%s\n",mStrArr[0], mStrArr[2], mStrArr[4], mStrArr[5]);
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
        return false;
    }

}
