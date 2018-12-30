package com.common;

public class Config {

    // 資料分析參數
    public static class DataAnalyze {
        public final static String outputDataDir = "F:\\Stock\\data\\";
        public final static int DOWNLOAD_FILE_SIZE = 10 * 1024; // 10Kb
        public final static int DOWNLOAD_DELAY = 10000; // 10 sec

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
