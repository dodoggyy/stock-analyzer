package com.common;

public class DatabaseConfig {
    public static final String DB_SECTION_MYSQL="mysql",
            DB_KEY_HOST = "",
            DB_KEY_PORT="",
            DB_KEY_UNIX_SOCKET="",
            DB_KEY_USERNAME="",
            DB_KEY_PASSWORD="",
            DB_KEY_DATABASE="",
            DB_KEY_CHARSET="";
    
    public final static String TABLE_DAY_FUND = "listed_fund",
            TABLE_DAY_TECH = "listed_tech",
            TABLE_DAY_TYPE = "listed_type",
            TABLE_STOCK_INFO = "stock_info",
            TABLE_WEEK_TECH = "listed_tech_week";

    public static final String CYCLE_DAY = "day",
            CYCLE_WEEK = "week",
            CYCLE_MONTH = "month",
            CYCLE_SEASON = "season",
            CYCLE_YEAR = "year";
    
    // K線週期
    public static final String DEFAULT_AVERAGE_TECH = "listed_tech_",
            DEFAULT_LISTED_TECH_WEEK = DEFAULT_AVERAGE_TECH + CYCLE_WEEK,
            DEFAULT_LISTED_TECH_MONTH = DEFAULT_AVERAGE_TECH + CYCLE_MONTH,
            DEFAULT_LISTED_TECH_SEASON = DEFAULT_AVERAGE_TECH + CYCLE_SEASON,
            DEFAULT_LISTED_TECH_YEAR = DEFAULT_AVERAGE_TECH + CYCLE_YEAR;
    
    // KDJ線週期
    public static final String DEFAULT_KDJ_TECH = "tech_kdj_",
            DEFAULT_TECH_KDJ_DAY = DEFAULT_KDJ_TECH + CYCLE_DAY,
            DEFAULT_TECH_KDJ_WEEK = DEFAULT_KDJ_TECH + CYCLE_WEEK,
            DEFAULT_TECH_KDJ_MONTH = DEFAULT_KDJ_TECH + CYCLE_MONTH,
            DEFAULT_TECH_KDJ_SEASON = DEFAULT_KDJ_TECH + CYCLE_SEASON,
            DEFAULT_TECH_KDJ_YEAR = DEFAULT_KDJ_TECH + CYCLE_YEAR;
    
    // CCI線週期
    public static final String DEFAULT_CCI_TECH = "tech_cci_",
            DEFAULT_TECH_CCI_DAY = DEFAULT_CCI_TECH + CYCLE_DAY,
            DEFAULT_TECH_CCI_WEEK = DEFAULT_CCI_TECH + CYCLE_WEEK,
            DEFAULT_TECH_CCI_MONTH = DEFAULT_CCI_TECH + CYCLE_MONTH,
            DEFAULT_TECH_CCI_SEASON = DEFAULT_CCI_TECH + CYCLE_SEASON,
            DEFAULT_TECH_CCI_YEAR = DEFAULT_CCI_TECH + CYCLE_YEAR;
    
    public static final String DEFAULT_LISTED_TECH_LIST[] = {
            DEFAULT_LISTED_TECH_WEEK,
            DEFAULT_LISTED_TECH_MONTH,
            DEFAULT_LISTED_TECH_SEASON,
            DEFAULT_LISTED_TECH_YEAR,
    };
}