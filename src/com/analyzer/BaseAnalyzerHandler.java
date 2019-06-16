/**
 * 
 */
package com.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.common.Utility;
import com.common.KeyDefine.EnAnalyzeStrategyType;
import com.parser.BaseParserHandler;

/**
 * @author Chris Lin
 *
 */
public abstract class BaseAnalyzerHandler {
    protected File ImportDir;
    protected File[] aFiles;
    protected BufferedReader mBufferReader;
    protected String mAnalyzeFileName;

    public BaseAnalyzerHandler() {

    }

    public static BaseParserHandler parserGenerator(EnAnalyzeStrategyType aParserType) throws SQLException {
        switch (aParserType) {
        case EN_ANALYZE_STRATEGY_TECH:
        case EN_ANALYZE_STRATEGY_FUND:
        default:
            return null;
        }
    }

}
