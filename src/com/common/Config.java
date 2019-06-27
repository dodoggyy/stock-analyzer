package com.common;

public class Config {

    // 資料分析參數
    public static class DataAnalyze {
        public final static String outputDataDir = "F:\\Stock\\tmp\\";
        public final static String outputAnalyzerDir = "F:\\Stock\\analyzer\\src";
        public final static String outputAnalyzerResultName = "F:\\Stock\\analyzer\\result\\result.csv";
        public final static String DATE_FORMAT = "yyyy-MM-dd";
        public final static int DOWNLOAD_FILE_SIZE = 10 * 1024; // 10Kb
        public final static int DOWNLOAD_DELAY_TIME = 5000;//10000; // 5 sec
        public final static int HTML_PARSER_DELAY_EACH_TIME = 100; // 0.1 sec
        public final static int HTML_PARSER_DELAY_CYCLE_TIME = 20000; // 20 sec
        public final static int HTML_PARSER_DELAY_CYCLE = 100; // 100 stocks
        public final static int HTML_INITIAL_STOCK_ID = 1100;
        public final static int HTML_MAX_ETF_ID = 100;
        
        
        public final static int DOWNLOAD_DELAY_TIME_TWSE = 5000; // 5 sec
        public final static int DOWNLOAD_DELAY_TIME_OTC = 5000; // 5 sec
        
        public final static int BACK_TEST_TRACKING_DAY = 240; // back test day
        public final static int BACK_TEST_INITIAL_MONEY = 1000000; // back test initial money

        // TWSE:上市, OTC:上櫃, TECH:技術面, FUND:基本面
        public static final String[] downloadName = { "otc_tech", "otc_fund", "twse_tech", "twse_fund" };
    }

    // 技術分析參數
    public class TechnicalAnalyze {
        public static final int TECH_ANAL = 0, FUND_ANAL = 1, TECH_KDJ = 3, TECH_CCI = 4;

        public class KDJ {
            public static final int KDJ_DAY = 9, KDJ_RSV_WEIGHT = 3, KDJ_KT_WEIGHT = 3;
        }

        public class CCI {
            public final static int CCI_DAY = 14;
            public final static double CCI_UPPER_BOUND = 999.99d, // 上限
                    CCI_LOWER_BOUND = -999.99d; // 下限
        }

    }
}
