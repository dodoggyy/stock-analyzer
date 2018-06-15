package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;

import com.common.Config;
import com.common.Utility;

public class TWSETechParserHandler extends BaseParserHandler {

    private BufferedReader mBufferReader;

    public TWSETechParserHandler() {
        this.ImportDir = new File(Config.DataAnalyze.outputDataDir);

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

    public boolean parseAllFileData() {
        String mFileName = "", mFileExt = "";
        String mDownloadName = Config.DataAnalyze.downloadName[Config.DataAnalyze.TWSE_TECH];
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
        return false;
    }

    @Override
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
        String mTmpLine = "";
        String[] mStrArr;
        int mfileType = -1;
        int mLines = 0;
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
                        if (mfileType == Config.ErrorHandle.TRANSCATION_DATA_NORMAL && mTmpLine.contains("每日收盤行情")) {
                            mBufferReader.readLine();
                            bIsBegin = true;
                        } else if (mfileType == Config.ErrorHandle.TRANSCATION_DATA_EXCEPTION
                                && mTmpLine.contains("恢復交易者。")) {
                            mTmpLine = mBufferReader.readLine();
                            mBufferReader.readLine();
                            mBufferReader.readLine();
                            bIsBegin = true;
                        }

                    } else {
                        // 解決字串中的,號，如"200,450,000"==> 200450000
                        mLines++;
                        mStrArr = Utility.removeMessyChar(mTmpLine).split(",");
                        bIsValid = true;
                    }
                }
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
    boolean writeData2DB() {
        // TODO Auto-generated method stub

        return false;
    }

}
