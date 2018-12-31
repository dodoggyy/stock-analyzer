package com.common;

public class KeyDefine {
    public final static int DATE_LENGTH = 10; // e.g.2018-06-11
    public final static int OLD_TWSE_FUND_LENGTH = 5;
    public final static String csvFilter = ".csv;.CSV";

    public static final int OTC_TECH = 0, // 上櫃盤後
            TWSE_TECH = 2, // 上市盤後
            TWSE_FUND = 3, // 上市基本面
            OTC_FUND = 1, // 上櫃基本面
            DATA_MAX = 4;

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
            "http://www.tpex.org.tw/web/stock/aftertrading/otc_quotes_no1430/stk_wn1430_result.php?", // otc_tech
            "http://www.tpex.org.tw/web/stock/aftertrading/peratio_analysis/pera_download.php?", // otc_fund
            "http://www.twse.com.tw/exchangeReport/MI_INDEX", // twse_tech
            "http://www.twse.com.tw/exchangeReport/BWIBBU_d" }; // twse_fund

    public static final String DEFAULT_TWSE_LISTED_FUND = "TWSEListedFund",
            DEFAULT_TWSE_LISTED_TECH = "TWSEListedTech",
            DEFAULT_OTC_LISTED_FUND = "OTCListedFund",
            DEFAULT_OTC_LISTED_TECH = "OTCListedTech";

    public static final String DEFAULT_LIST[] = {
        DEFAULT_OTC_LISTED_TECH,
        DEFAULT_OTC_LISTED_FUND,
        DEFAULT_TWSE_LISTED_TECH,
        DEFAULT_TWSE_LISTED_FUND, 
    };

    // 錯誤處理
    public class ErrorHandle {
        public static final int EXIT_ERROR = -1, // exit error
                TRANSCATION_DATA_NORMAL = 0, // parser data normal case
                TRANSCATION_DATA_EXCEPTION = 1, // special case for parser data
                EXIT_TIMEOUT = 2, ERROR_MAX = 3;
    }
}
