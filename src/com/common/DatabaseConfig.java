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
    
    // K線週期
    public static final String DEFAULT_AVERAGE_TECH = "listed_tech_",
            DEFAULT_LISTED_TECH_WEEK = DEFAULT_AVERAGE_TECH + "week",
            DEFAULT_LISTED_TECH_MONTH = DEFAULT_AVERAGE_TECH + "month",
            DEFAULT_LISTED_TECH_SEASON = DEFAULT_AVERAGE_TECH + "season",
            DEFAULT_LISTED_TECH_YEAR = DEFAULT_AVERAGE_TECH + "year";
    
    public static final String DEFAULT_LISTED_TECH_LIST[] = {
            DEFAULT_LISTED_TECH_WEEK,
            DEFAULT_LISTED_TECH_MONTH,
            DEFAULT_LISTED_TECH_SEASON,
            DEFAULT_LISTED_TECH_YEAR,
    };
}