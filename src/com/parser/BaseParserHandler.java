package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.sql.SQLException;

import com.common.KeyDefine.EnMarketStrategyType;


public abstract class BaseParserHandler {
    protected File ImportDir;
    protected File[] aFiles;
    protected BufferedReader mBufferReader;
    protected String mDownloadName;

    public BaseParserHandler() {
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
