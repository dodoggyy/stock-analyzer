package com.common;

public class Config {
    public final static String outputDataDir = "F:\\Stock\\data\\";
    public final static String OtcTechDownloadUrl = "http://www.gretai.org.tw/ch/stock/aftertrading/DAILY_CLOSE_quotes/stk_quote_download.php?";
    public final static String OtcFundDownloadUrl = "http://www.tpex.org.tw/web/stock/aftertrading/peratio_analysis/pera_download.php?";
    public final static String TwseTechDownloadUrl = "http://www.twse.com.tw/exchangeReport/MI_INDEX";
    public final static String TwseFundDownloadUrl = "http://www.twse.com.tw/exchangeReport/BWIBBU_d";

    // TWSE:上市, OTC:上櫃, TECH:技術面, FUND:基本面
    public final static String OTC_TECH = "otc_tech_";
    public final static String OTC_FUND = "otc_fund_";
    public final static String TWSE_TECH = "twse_tech_";
    public final static String TWSE_FUND = "twse_fund_";

    public final static int TECH_ANAL = 0;
    public final static int FUND_ANAL = 1;
    public final static int TECH_KDJ = 3;
    public final static int TECH_CCI = 4;

    public final static int TWSE = 0;
    public final static int OTC = 1;

    public final static int DOWNLOAD_FILE_SIZE = 10 * 1024; // 10Kb
    public final static int DOWNLOAD_DELAY = 3000; // 3 sec

    public final static int EXIT_ERROR = -1;
    public final static int TRANSCATION_DATA_NORMAL = 1;
    public final static int TRANSCATION_DATA_EXCEPTION = 2;

    // 技術分析參數
    public final static int KDJ_DAY = 9;
    public final static int KDJ_RSV_WEIGHT = 3;
    public final static int KDJ_KT_WEIGHT = 3;
    public final static int CCI_DAY = 14;

    public final static double CCI_UPPER_BOUND = 999.99d;
    public final static double CCI_LOWER_BOUND = -999.99d;

}
