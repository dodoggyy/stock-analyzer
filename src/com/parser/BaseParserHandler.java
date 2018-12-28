package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.sql.SQLException;

public abstract class BaseParserHandler {
    protected File ImportDir;
    protected File[] aFiles;
    protected BufferedReader mBufferReader;
    protected String mDownloadName;

    public BaseParserHandler() {
    }

    // write data to DB
    abstract boolean writeData2DB(String aDate, String[] aStrArr) throws SQLException;

    // parse specific data
    abstract boolean parseFileData(File aFile, String aDate);
}
