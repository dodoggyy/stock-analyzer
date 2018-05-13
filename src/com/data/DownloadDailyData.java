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

/**
 * Taiwan stock data downloader<T>
 * 
 * @author Chris Lin
 * @version 1.0 1 May 2018
 *
 * @param aDownloadDir
 *            更改下載位置,預設讀config
 */
public class DownloadDailyData {
    private int mDataLength;
    private String mDownloadDir;

    final static String[] SEL_TYPE = { "ALLBUT0999", "ALL" };

    public static void main(String[] args) {
        // download data from TWSE and OTC
        String mDate = "2018/4/26";
        DownloadDailyData mDonloader = new DownloadDailyData();
        mDonloader.downloadData(mDate);
    }

    public DownloadDailyData() {
        mDataLength = Config.DataAnalyze.DATA_MAX;
        mDownloadDir = Config.DataAnalyze.outputDataDir;
    }

    public DownloadDailyData(String aDownloadDir) {
        mDataLength = Config.DataAnalyze.DATA_MAX;
        mDownloadDir = aDownloadDir;
    }

    public void downloadData(String aDownloadDate) {
        String mFilename[] = new String[mDataLength];
        String mUrl[] = new String[mDataLength];
        String mUrlParm[] = new String[mDataLength];
        String mPostParm[] = { "response", "date", "type" };
        String mPostData[] = { "csv", "", "" };
        String mNow, mYr, mYrOtc, mMth, mDay = "";
        String mDate = "";
        String mStr[];

        File mFile = null;
        HttpURLConnection mConnection = null;

        System.setErr(System.out);
        System.out.println("\r\n*****Download Daily data(csv file) *****");

        // 下載日期ex: "2018/4/29"
        mNow = aDownloadDate;
        System.out.println(mNow + " Dir:" + mDownloadDir);

        // 日期區間格式處理
        mStr = mNow.split("/");
        mYrOtc = String.valueOf(Integer.valueOf(mStr[0]) - 1911); // OTC年份格式處理
        mYr = mStr[0];
        mMth = String.valueOf(Integer.valueOf(mStr[1]) + 100).substring(1);
        mDay = String.valueOf(Integer.valueOf(mStr[2]) + 100).substring(1);
        mDate = mYr + "-" + mMth + "-" + mDay;

        for (int i = 0; i < mDataLength; i++) {
            mFilename[i] = Config.DataAnalyze.downloadName[i] + "_" + mDate + ".csv";
            if (i == Config.DataAnalyze.OTC_TECH || i == Config.DataAnalyze.OTC_FUND) {
                mUrl[i] = String.format(Config.DataAnalyze.downloadUrl[i] + "l=zh-tw&d=%s/%s/%s&s=0,asc,0", mYrOtc,
                        mMth, mDay);
            } else {
                mUrl[i] = Config.DataAnalyze.downloadUrl[i];
            }

        }

        for (int i = 0; i < mDataLength; i++) {
            try {
                if (i == Config.DataAnalyze.TWSE_TECH || i == Config.DataAnalyze.TWSE_FUND) {
                    mPostData[1] = mYr + mMth + mDay;
                    if (i == Config.DataAnalyze.TWSE_FUND) {
                        mPostData[2] = SEL_TYPE[1];
                    } else {
                        mPostData[2] = SEL_TYPE[0];
                    }
                    for (int j = 0; j < mPostParm.length; j++) {
                        mUrlParm[i] += "&" + mPostParm[j] + "=" + URLEncoder.encode(mPostData[j], "UTF-8");
                    }
                    mUrlParm[i] = mUrlParm[i].substring(1);
                    mConnection = excutePost(mUrl[i], mUrlParm[i]);
                    // System.out.println(mUrlParm[i]);
                    // System.out.println(mDir + mFilename[i]);
                    downloadFromUrl("", mDownloadDir + mFilename[i], mConnection);
                } else if (i == Config.DataAnalyze.OTC_TECH || i == Config.DataAnalyze.OTC_FUND) {
                    mConnection = null;
                    // System.out.println(mDir + mFilename[i]);
                    downloadFromUrl(mUrl[i], mDownloadDir + mFilename[i], mConnection);
                }

                mFile = new File(mDownloadDir, mFilename[i]);

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
