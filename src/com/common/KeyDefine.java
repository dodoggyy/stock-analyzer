package com.common;

import org.apache.logging.log4j.Level;

public class KeyDefine {
    public final static Level LOG_LEVEL = Level.DEBUG;
    
    public final static int DATE_LENGTH = 10; // e.g.2018-06-11
    public final static int OLD_TWSE_FUND_LENGTH = 5;
    public final static int STOCK_INFO_LENGTH = 8;
    public final static String csvFilter = ".csv;.CSV";
    public final static int START_STOCK_ID = 1100;
    public final static int MAX_STOCK_ID = 9999;
    public final static int  THOUSAND_OF_SHARES = 1000;
    public final static int  DB_PRICE_SHIFT = 2; // 資料庫欄位數值轉換
    public final static float  PERCENT_UPPER_BOUND = 100f;
    public final static float  PERCENT_LOWER_BOUND = 0f;
    public final static int DAY_PER_YEAR = 365; // 1年 = 365天
    
    public final static float  SECURITIES_FEE_PERCETAGE = 1.425f/1000; // 券商手續費 0.1425%
    public final static float  SECURITIES_FEE_DISCOUNT = 0.5f; // 券商手續費折扣, 50%
    public final static float  TRADE_TAX = 3/1000; // 證交稅,賣出 0.30%

    public static final int OTC_TECH = 0, // 上櫃盤後
            TWSE_TECH = 2, // 上市盤後
            TWSE_FUND = 3, // 上市基本面
            OTC_FUND = 1, // 上櫃基本面
            DOWNLOAD_DATA_MAX = 4,
    
            ES_FUND = 4, // 興櫃基本面
            PO_FUND = 5, // 公開發行基本面
            CE_FUND = 6, // 創櫃基本面
            UKNOWN_FUND = 7; 
    
    public enum EnMarketStrategyType {
        EN_MARKET_STRATEGY_OTC_TECH,  // 上櫃盤後
        EN_MARKET_STRATEGY_TWSE_TECH, // 上市盤後
        EN_MARKET_STRATEGY_TWSE_FUND, // 上市基本面
        EN_MARKET_STRATEGY_OTC_FUND,  // 上櫃基本面
        EN_MARKET_STRATEGY_ES_FUND,   // 興櫃基本面
        EN_MARKET_STRATEGY_PO_FUND,   // 公開發行基本面
        EN_MARKET_STRATEGY_CE_FUND,   // 創櫃基本面
        EN_MARKET_STRATEGY_UNKNOWN,
        EN_MARKET_STRATEGY_MAX,
    }
    
    public enum EnAnalyzeStrategyType {
        EN_ANALYZE_STRATEGY_TECH,
        EN_ANALYZE_STRATEGY_TECH_CCI,
        EN_ANALYZE_STRATEGY_TECH_KDJ,
        EN_ANALYZE_STRATEGY_TECH_BIAS,
        EN_ANALYZE_STRATEGY_FUND,
        EN_ANALYZE_STRATEGY_FUND_PROFIT,
        EN_ANALYZE_STRATEGY_UNKNOWN,
        EN_ANALYZE_STRATEGY_MAX,
    }
    
    public enum EnQueryType {
        EN_QUERY_TECH,
        EN_QUERY_FUND,
        EN_QUERY_MAX,
    }
        
    public static final int STOCK_INFO = 0, // 股票基本資訊
            PARSER_HTML_MAX = 1;

    public static final int TECH_ANAL = 0, // 技術面分析
            FUND_ANAL = 1; // 基本面分析

    public static final int TWSE = 0, // 上市
            OTC = 1; // 上櫃

    public static final int DOWNLOAD_SPECIFIED_RANGE = 2, // 指定特定區間
            DOWNLOAD_DB_UPDATE = 1, // 資料庫更新
            DOWNLOAD_DB_NONE = 0; // skip DB update

    public static final int STORE_TO_DB = 1, // 指定特定區間
            STORE_NONE = 2; // 資料庫更新

    public static final String[] downloadUrl = {
            "https://www.tpex.org.tw/web/stock/aftertrading/otc_quotes_no1430/stk_wn1430_result.php?", // otc_tech
            "https://www.tpex.org.tw/web/stock/aftertrading/peratio_analysis/pera_download.php?", // otc_fund
            "http://www.twse.com.tw/exchangeReport/MI_INDEX", // twse_tech
            "http://www.twse.com.tw/exchangeReport/BWIBBU_d" }; // twse_fund

    public static final String[] htmlUrl = {
            "http://isin.twse.com.tw/isin/single_main.jsp?"  // stock_info
    };
    
    public static final String DEFAULT_TWSE_LISTED_FUND = "TWSEListedFund",
            DEFAULT_TWSE_LISTED_TECH = "TWSEListedTech",
            DEFAULT_OTC_LISTED_FUND = "OTCListedFund",
            DEFAULT_OTC_LISTED_TECH = "OTCListedTech",
            DEFAULT_ES_LISTED_TECH = "ESListedTech",
            DEFAULT_PO_LISTED_TECH = "POListedTech",
            DEFAULT_CE_LISTED_TECH = "CEListedTech",
            DEFAULT_UNKNOWN_LISTED_TECH = "UNKNOWNListedTech";    

    public static final String DEFAULT_LIST[] = {
        DEFAULT_OTC_LISTED_TECH,
        DEFAULT_OTC_LISTED_FUND,
        DEFAULT_TWSE_LISTED_TECH,
        DEFAULT_TWSE_LISTED_FUND,
        DEFAULT_ES_LISTED_TECH,
        DEFAULT_PO_LISTED_TECH,
        DEFAULT_CE_LISTED_TECH,
        DEFAULT_UNKNOWN_LISTED_TECH,
    };

    // 錯誤處理
    public class ErrorHandle {
        public static final int EXIT_ERROR = -1, // exit error
                TRANSCATION_DATA_NORMAL = 0, // parser data normal case
                TRANSCATION_DATA_EXCEPTION = 1, // special case for parser data
                EXIT_TIMEOUT = 2, ERROR_MAX = 3;
    }
    
    public enum DBOperationType {  
        DB_OPERATION_TYPE_CREATE_TABLE,
        DB_OPERATION_TYPE_TEST_INSERT_TABLE,
        DB_OPERATION_TYPE_DROP_TABLE,
        DB_OPERATION_TYPE_MAX,
    }
    
    public enum CalculateCycle {
        CYCLE_WEEK,
        CYCLE_MONTH,
        CYCLE_SEASON,
        CYCLE_YEAR,
        CYCLE_MAX,
    }
}
