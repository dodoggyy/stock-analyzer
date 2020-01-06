package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.common.Config;
import com.common.KeyDefine;
import com.common.KeyDefine.EnMarketStrategyType;
import com.database.DatabaseHandler;


public abstract class BaseParserHandler {
    protected static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    protected File ImportDir;
    protected File[] aFiles;
    protected BufferedReader mBufferReader;
    protected String mDownloadName;
    
    protected DatabaseHandler mStockDB;

    public BaseParserHandler() {
        // set log level
        Configurator.setRootLevel(KeyDefine.LOG_LEVEL);
        this.ImportDir = new File(Config.DataAnalyze.outputTmpDir);
        
        if (!this.ImportDir.exists()) {
            log.error("沒有這個目錄 " + ImportDir);
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
        }
        this.aFiles = ImportDir.listFiles();
        if (this.aFiles.length == 0) {
            log.error("No Files Match!");
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
        }
        Arrays.sort(aFiles, new Comparator<Object>() {
            @Override
            public int compare(Object aFile1, Object aFile2) {
                return ((File) aFile1).getName().compareTo(((File) aFile2).getName());
            }
        });
    }
    
    public boolean parseAllFileData() throws SQLException {
        String mFileName = "", mFileExt = "";
        int mSeparateIndex = 0;

        for (int i = 0; i < aFiles.length; i++) {
            if (!aFiles[i].isDirectory()) {
                mFileName = aFiles[i].getName();
                mSeparateIndex = mFileName.indexOf(".");
                mFileExt = mFileName.substring(mSeparateIndex);
                log.info("Deal SQL data with " + mFileName);
            }
//            System.out.println(" " + mFileName.substring(0, mDownloadName.length()));
//            System.out.printf("mDownloadName.length():%d\n",mDownloadName.length());
//            System.out.printf("%s\n",mFileName.substring(mDownloadName.length(), mDownloadName.length()
//                  + Config.DataAnalyze.DATE_LENGTH));
            if (mFileName.substring(0, mDownloadName.length()).equals(mDownloadName)
                    && KeyDefine.csvFilter.contains(mFileExt)) {
                System.out.println(mFileName.substring(mDownloadName.length(), mDownloadName.length()+ KeyDefine.DATE_LENGTH));
                parseFileData(aFiles[i], mFileName.substring(mDownloadName.length(), mDownloadName.length()
                        + KeyDefine.DATE_LENGTH));
            }
            // move finished file to specific dir
            //aFiles[i].renameTo(new File(Config.DataAnalyze.outputDataDir + mFileName));
        }

        //mStockDB.deleteSqlDuplicateData();
        mStockDB.executeSqlPrepareCmd();
        return true;
    }
    
    /**
     * 
     * @param aParserType
     * @return BaseParserHandler
     * @throws SQLException
     */
    public static BaseParserHandler parserGenerator(EnMarketStrategyType aParserType) throws SQLException {
        switch(aParserType) {
        case EN_MARKET_STRATEGY_OTC_TECH:
            return new OTCTechParserHandler();
        case EN_MARKET_STRATEGY_OTC_FUND:
            return new OTCFundParserHandler();
        case EN_MARKET_STRATEGY_TWSE_TECH:
            return new TWSETechParserHandler();
        case EN_MARKET_STRATEGY_TWSE_FUND:
            return new TWSEFundParserHandler();
        default:
            return null;
        }
    }

    // write data to DB
    abstract boolean writeData2DB(String aDate, String[] aStrArr) throws SQLException;

    // parse specific data
    abstract boolean parseFileData(File aFile, String aDate);
}
