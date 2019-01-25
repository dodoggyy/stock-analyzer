/**
 * 
 */
package com.parser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.common.Config;
import com.common.KeyDefine;
import com.common.Utility;
import com.database.StockInfoDatabaseHandler;

/**
 * @author Chris.Lin
 *
 */
public class StockInfoParserHandler extends BaseParserHandler {

    private StockInfoDatabaseHandler mStockDB;
    /**
     * @param args
     * @throws IOException 
     * @throws SQLException 
     */
    public StockInfoParserHandler() throws SQLException {
        mStockDB = new StockInfoDatabaseHandler();
    }
    
    public static void main(String[] args) throws IOException, SQLException {
        // TODO Auto-generated method stub
        int mStartStockID, mEndStockID;
        Scanner mScanner = new Scanner(System.in);
        System.out.println("Start stock id:" );
        mStartStockID = mScanner.nextInt();
        System.out.println("End stock id:" );
        mEndStockID = mScanner.nextInt();
        
        Utility.timerStart();
        
        StockInfoParserHandler mParser = new StockInfoParserHandler();
        mParser.parseAllHTMLData(mStartStockID, mEndStockID);

        Utility.timerEnd();
    }

    @Override
    boolean writeData2DB(String aDate, String[] aStrArr) throws SQLException {
        // TODO Auto-generated method stub
//        aStrArr[6] = Utility.getDateTimeFormat(aStrArr[6],"yyyy/M/d","yyyy-MM-dd",java.util.Locale.TAIWAN);
//        System.out.printf("id:%s, 證券代號:%s, ISIN Code:%s,上市日:%s, 市場別:%s, 產業別:%s, CFICode:%s\n",aStrArr[1], aStrArr[2], aStrArr[0], aStrArr[6], aStrArr[3], aStrArr[5], aStrArr[7]);

        mStockDB.generateSqlPrepareStrCmd(1, aStrArr[1]); // stock_id
        mStockDB.generateSqlPrepareStrCmd(2, aStrArr[2]); // stock_name
        mStockDB.generateSqlPrepareStrCmd(3, aStrArr[0]); // stock_isin_code
        mStockDB.generateSqlPrepareStrCmd(4, Utility.getDateTimeFormat(aStrArr[6],"yyyy/M/d","yyyy-MM-dd",java.util.Locale.TAIWAN)); // stock_listing_date
        mStockDB.generateSqlPrepareStrCmd(5, aStrArr[5]); // stock_industry
        mStockDB.generateSqlPrepareStrCmd(6, aStrArr[7]); // stock_cfi_code
        mStockDB.generateSqlPrepareIntCmd(7, judgeStockType(aStrArr[3]) + 1); // stock_type

        mStockDB.addSqlPrepareCmd2Batch();
        return true;
    }

    @Override
    boolean parseFileData(File aFile, String aDate) {
        // TODO Auto-generated method stub
        return false;
    }
    //Config.DataAnalyze.HTML_INITIAL_STOCK_ID
    public boolean parseAllHTMLData(int aStartStockID, int aEndStockID) throws IOException, SQLException {
        String mStockID ="";

        if(aStartStockID < KeyDefine.START_STOCK_ID) {
            aStartStockID = KeyDefine.START_STOCK_ID;
        }
        if(aEndStockID > KeyDefine.MAX_STOCK_ID) {
            aEndStockID = KeyDefine.MAX_STOCK_ID;
        }
        
        for(int i = aStartStockID; i <= aEndStockID; i++) {
            if((i%Config.DataAnalyze.HTML_PARSER_DELAY_CYCLE) == 0) {
                mStockDB.executeSqlPrepareCmd();
                Utility.scheduleDelay(Config.DataAnalyze.HTML_PARSER_DELAY_CYCLE_TIME);
            }
            if(i == Config.DataAnalyze.HTML_MAX_ETF_ID) {
                i = Config.DataAnalyze.HTML_INITIAL_STOCK_ID;
            }
            mStockID = String.format("%04d", i);
            //System.out.println(mStockID);
            parseHTMLData(mStockID);
            Utility.scheduleDelay(Config.DataAnalyze.HTML_PARSER_DELAY_EACH_TIME);
        }

        //parseHTMLData("0050");

        //mStockDB.executeSqlPrepareCmd();

        return true;
    }

    boolean parseHTMLData(String aStockID) throws IOException, SQLException {
        Document mDoc = Jsoup.connect(String.format(KeyDefine.htmlUrl[KeyDefine.STOCK_INFO] + "owncode=%s&stockname=",aStockID )).timeout(5000).get();
        String[] mStrArr = new String[KeyDefine.STOCK_INFO_LENGTH];
        int mArrayCount = 0;
        Elements mNewsHeadlines = mDoc.select("td");
        boolean bIsStart = false;
        for (Element aHeadline : mNewsHeadlines) {
            if(bIsStart == false)
            {
                if(aHeadline.text().equals("1")) {
                    bIsStart = true;
                }
            } else {
                //System.out.printf("%s\n", aHeadline.text());
                if(mArrayCount <  KeyDefine.STOCK_INFO_LENGTH) {
                    mStrArr[mArrayCount++] = aHeadline.text();
                }
            }

        }
        if(bIsStart == false) {
            System.out.printf("No such stock id:%s\n", aStockID);
        } else {
//            for(int i = 0; i < mStrArr.length; i++) {
//                System.out.printf("%s\n", mStrArr[i]);
//            }
            writeData2DB(null, mStrArr);
        }
        
        return true;
    }
    
    int judgeStockType(String aStockType) {
        int mStockType = 0;
        if(aStockType.equals("上櫃")) {
            mStockType = KeyDefine.OTC_FUND;
        } else if(aStockType.equals("上市")) {
            mStockType = KeyDefine.TWSE_FUND;
        } else if(aStockType.equals("興櫃")) {
            System.out.println("興櫃");
            mStockType = KeyDefine.ES_FUND;
        } else if(aStockType.equals("公開發行")) {
            System.out.println("公開發行");
            mStockType = KeyDefine.PO_FUND;
        }else if(aStockType.equals("創櫃版")) {
            System.out.println("創櫃版");
            mStockType = KeyDefine.CE_FUND;
        }else {
            mStockType = KeyDefine.UKNOWN_FUND;
            System.out.println("Unknown sotck type");
        }
        
       return mStockType;
    }
}
