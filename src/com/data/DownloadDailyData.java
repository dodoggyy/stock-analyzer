package com.data;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.common.Config;

public class DownloadDailyData {
    static final String SEL_TYPE = "ALLBUT0999";

    public static void main(String[] args) {
        // download data from TWSE and OTC
        String mDownloadDir = Config.DataAnalyze.outputDataDir;
        String mDate = "2018/4/27";

        DownloadDailyData.downloadData(mDate, mDownloadDir);
    }

    public static void downloadData(String aDownloadDate, String aDownloadDirectory) {
        Config mCon = new Config();
        int mDataLength = Config.DataAnalyze.DATA_MAX;

        String mDir = "";
        String mFilename[] = new String[mDataLength];
        String mUrl[] = new String[mDataLength];
        String mUrlParm[] = new String[mDataLength];
        String mPostParm[] = { "response", "date", "type" };
        String mPostData[] = { "csv", "", SEL_TYPE };
        String mNow, mYr, mYr2, mMth, mDay = "";
        String mDate = "";
        String aStr[];

        File mFile = null;
        HttpURLConnection mConnection = null;

        System.setErr(System.out);
        System.out.println("\r\n*****Download Daily data(csv file) *****");

        // 下載日期, 存放位置 ex: "2018/4/29", "F:\\Stock\\data\\";
        mNow = aDownloadDate;
        mDir = aDownloadDirectory;
        System.out.println(mNow + " Dir:" + mDir);

        // 日期區間格式處理
        aStr = mNow.split("/");
        mYr2 = String.valueOf(Integer.valueOf(aStr[0]) - 1911);
        mYr = aStr[0];
        mMth = String.valueOf(Integer.valueOf(aStr[1]) + 100).substring(1);
        mDay = String.valueOf(Integer.valueOf(aStr[2]) + 100).substring(1);
        mDate = mYr + "-" + mMth + "-" + mDay;

        for (int i = 0; i < mDataLength; i++) {
            mFilename[i] = Config.DataAnalyze.downloadName[i] + "_" + mDate + ".csv";
            mUrl[i] = Config.DataAnalyze.downloadUrl[i];
        }

        for (int i = 0; i < mDataLength; i++) {
            try {
                if (i == Config.DataAnalyze.TWSE_TECH || i == Config.DataAnalyze.TWSE_FUND) {
                    mPostData[1] = mYr + mMth + mDay;
                    if (i == Config.DataAnalyze.TWSE_FUND) {
                        mPostData[2] = "ALL";
                    }
                    for (int j = 0; j < mPostParm.length; j++) {
                        mUrlParm[i] += "&" + mPostParm[j] + "=" + URLEncoder.encode(mPostData[j], "UTF-8");
                    }
                    mUrlParm[i] = mUrlParm[i].substring(1);
                    mConnection = excutePost(mUrl[i], mUrlParm[i]);
                    // System.out.println(mUrlParm[i]);
                    // System.out.println(mDir + mFilename[i]);
                    downloadFromUrl("", mDir + mFilename[i], mConnection);
                } else if (i == Config.DataAnalyze.OTC_TECH || i == Config.DataAnalyze.OTC_FUND) {
                    mConnection = null;
                    // System.out.println(mDir + mFilename[i]);
                    downloadFromUrl(mUrl[i], mDir + mFilename[i], mConnection);
                }

                mFile = new File(mDir, mFilename[i]);

                // if file size less than FILE_SIZE, then delete it
                if (mFile.length() < Config.DataAnalyze.DOWNLOAD_FILE_SIZE) {
                    System.err.printf("\n %s未開盤", aDownloadDate);
                    try {
                        mFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.printf("\nOK! %s\n", mFilename[i]);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                System.exit(Config.ErrorHandle.EXIT_ERROR);
            }
        }

    }

    public static void downloadFromUrl(String aSource, String aDestination, HttpURLConnection aConnection)
            throws IOException, ConnectException {

        InputStream mInputStream;
        HttpURLConnection mConnection;

        if (aConnection != null) {
            mConnection = aConnection;
        } else {
            mConnection = (HttpURLConnection) new URL(aSource).openConnection();
        }
        mInputStream = mConnection.getInputStream();
        FileOutputStream mFileOutputStream = new FileOutputStream(aDestination);
        byte[] buffer = new byte[1024];
        for (int length; (length = mInputStream.read(buffer)) > 0; mFileOutputStream.write(buffer, 0, length))
            ;
        mFileOutputStream.close();
        mInputStream.close();
    }

    public static HttpURLConnection excutePost(String aTargetURL, String aUrlParameters) {
        URL mUrl;
        DataOutputStream mWr;
        HttpURLConnection mConnection = null;
        try {
            mUrl = new URL(aTargetURL);
            mConnection = (HttpURLConnection) mUrl.openConnection();
            mConnection.setRequestMethod("POST");
            mConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            mConnection.setRequestProperty("Content-Length", "" + Integer.toString(aUrlParameters.getBytes().length));
            mConnection.setRequestProperty("Content-Language", "UTF-8");
            mConnection.setUseCaches(false);
            mConnection.setDoInput(true);
            mConnection.setDoOutput(true);

            // Send request
            mWr = new DataOutputStream(mConnection.getOutputStream());
            mWr.writeBytes(aUrlParameters);
            mWr.flush();
            mWr.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(Config.ErrorHandle.EXIT_ERROR);
        } finally {

            if (mConnection != null) {
                mConnection.disconnect();
            }
        }
        return mConnection;
    }
}
