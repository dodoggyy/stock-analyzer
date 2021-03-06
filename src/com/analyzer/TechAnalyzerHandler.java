/**
 * 
 */
package com.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.common.Config;
import com.common.KeyDefine;
import com.common.Utility;

/**
 * @author Chris Lin
 *
 */
public class TechAnalyzerHandler extends BaseAnalyzerHandler {

//    private TechAnalyzerHandler mAnalyzer;
    private ArrayList<TechCsvStruct> mAnalyzerData;

    public class TechCsvStruct implements Comparable<TechCsvStruct> {
        private String mStockID;
        private Date mStockDate;
        private boolean bIsBIAS;
        private boolean bIsKDJ;

        public TechCsvStruct(String mStockID, String mStockDate, Boolean bIsBIAS, Boolean bIsKDJ) {
            // TODO Auto-generated constructor stub
            this.mStockID = mStockID;
            this.bIsBIAS = bIsBIAS;
            this.bIsKDJ = bIsKDJ;
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.mStockDate = mDateFormat.parse(mStockDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        public void setStockID(String aStockID) {
            this.mStockID = aStockID;
        }
        
        public String getStockID() {
            return this.mStockID;
        }
        
        public void setbIsBIAS(boolean bIsBIAS) {
            this.bIsBIAS = bIsBIAS;
        }
        
        public boolean getbIsBIAS() {
            return this.bIsBIAS;
        }
        
        public void setbIsKDJ(boolean bIsKDJ) {
            this.bIsKDJ = bIsKDJ;
        }
        
        public boolean getbIsKDJ() {
            return this.bIsKDJ;
        }
        
        public void setDate(Date aDate) {
            this.mStockDate = aDate;
        }
        
        public Date getDate() {
            return this.mStockDate;
        }

        @Override
        public int compareTo(TechCsvStruct other) {
            // TODO Auto-generated method stub
            return (this.mStockDate.compareTo(other.mStockDate));
        }
    }

    public TechAnalyzerHandler() {
        this.ImportDir = new File(Config.DataAnalyze.outputAnalyzerDir);

        if (!this.ImportDir.exists()) {
            System.err.println("?????????????????? " + ImportDir);
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
        }
        this.aFiles = ImportDir.listFiles();
        if (this.aFiles.length == 0) {
            System.err.println("No Files Match!");
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
        }
        this.mAnalyzerData = new ArrayList<>();
    }

    // parse specific data
    boolean parseFileData(File aFile) {
        // TODO Auto-generated method stub
        int mLines = 0;
        String mTmpLine = "";
        String[] mStrArr;

        try {
            // csv file need to set decode as MS950 to prevent garbled
            InputStreamReader mStreamReader = new InputStreamReader(new FileInputStream(aFile), "MS950");
            mBufferReader = new BufferedReader(mStreamReader);

            while ((mTmpLine = mBufferReader.readLine()) != null) {
                mLines++;
                if (mLines >= 2) { // skip csv title
                    mStrArr = Utility.removeCsvMessyStr(mTmpLine).split(",");
//                    System.out.printf("stock_id:%s, stock_date:%s, is_BISA:%s  is_JDK:%s\n", mStrArr[2], mStrArr[3], mStrArr[13], mStrArr[14]);
                    mAnalyzerData.add(new TechCsvStruct(String.format("%04d", Integer.parseInt(mStrArr[2])), mStrArr[3],"1".equals(mStrArr[13]), "1".equals(mStrArr[14])));
                }
            }
            mBufferReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true; // fixme
    }

    public boolean parseCalculatorData() {
        String mFileName = "", mFileExt = "";
        int mSeparateIndex = 0;

        for (int i = 0; i < aFiles.length; i++) {
            if (!aFiles[i].isDirectory()) {
                mFileName = aFiles[i].getName();
                mSeparateIndex = mFileName.indexOf(".");
                mFileExt = mFileName.substring(mSeparateIndex);
                System.out.println("Deal CSV data with " + mFileName);
            }

            parseFileData(aFiles[i]);
        }

        return true;
    }

    /**
     * @return the mAnalyzerData
     */
    public ArrayList<TechCsvStruct> getAnalyzerData() {
        mAnalyzerData.sort(null);
        return mAnalyzerData;
    }

    /**
     * @param mAnalyzerData
     *            the mAnalyzerData to set
     */
    public void setmAnalyzerData(ArrayList<TechCsvStruct> mAnalyzerData) {
        this.mAnalyzerData = mAnalyzerData;
    }

    /**
     * @param args
     * @throws ParseException 
     */
    public static void main(String[] args) throws ParseException {
        // TODO Auto-generated method stub
        TechAnalyzerHandler mAnalyzer = new TechAnalyzerHandler();
        mAnalyzer.parseCalculatorData();

        ArrayList<TechCsvStruct> mTmp = mAnalyzer.getAnalyzerData();
        System.out.println(mTmp.size());
        Iterator mIter = mTmp.iterator();
        while (mIter.hasNext()) {
            TechCsvStruct mTechData = (TechCsvStruct) mIter.next();
            System.out.println(
                    mTechData.mStockID + " " + Utility.date2String(mTechData.getDate()) + " " + mTechData.getbIsBIAS() + " " + mTechData.getbIsKDJ());
        }

    }
}
