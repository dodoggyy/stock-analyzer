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
import com.common.KeyDefine;

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
    private static int mDataLength;
    private String mDownloadDir;

    final static String[] SEL_TYPE = { "ALLBUT0999", "ALL" };

    public static void main(String[] args) {
        // download data from TWSE and OTC
        String mDate = "2018/12/27";
        DownloadDailyData mDonloader = new DownloadDailyData();
        // mDonloader.downloadData(mDate, KeyDefine.OTC_FUND);
        for (int i = 0; i < mDataLength; i++) {
            mDonloader.downloadData(mDate, i);
        }
    }

    public DownloadDailyData() {
        mDataLength = KeyDefine.DOWNLOAD_DATA_MAX;
        mDownloadDir = Config.DataAnalyze.outputDataDir;
    }

    public DownloadDailyData(String aDownloadDir) {
        mDataLength = KeyDefine.DOWNLOAD_DATA_MAX;
        mDownloadDir = aDownloadDir;
    }

    public void downloadData(String aDownloadDate, int aDownloadType) {
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

        mFilename[aDownloadType] = Config.DataAnalyze.downloadName[aDownloadType] + "_" + mDate + ".csv";
        if ((aDownloadType == KeyDefine.OTC_TECH) || (aDownloadType == KeyDefine.OTC_FUND)) {
            mUrl[aDownloadType] = String.format(
                    KeyDefine.downloadUrl[aDownloadType] + "l=zh-tw&o=csv&d=%s/%s/%s&se=EW&s=0,asc,0", mYrOtc, mMth,
                    mDay);
        } else {
            mUrl[aDownloadType] = KeyDefine.downloadUrl[aDownloadType];
        }

        try {
            if ((aDownloadType == KeyDefine.TWSE_TECH) || (aDownloadType == KeyDefine.TWSE_FUND)) {
                mPostData[1] = mYr + mMth + mDay;
                if (aDownloadType == KeyDefine.TWSE_FUND) {
                    mPostData[2] = SEL_TYPE[1];
                } else {
                    mPostData[2] = SEL_TYPE[0];
                }
                for (int j = 0; j < mPostParm.length; j++) {
                    mUrlParm[aDownloadType] += "&" + mPostParm[j] + "=" + URLEncoder.encode(mPostData[j], "UTF-8");
                }
                mUrlParm[aDownloadType] = mUrlParm[aDownloadType].substring(1);
                mConnection = excutePost(mUrl[aDownloadType], mUrlParm[aDownloadType], KeyDefine.TWSE);
                // System.out.println(mUrlParm[aDownloadType]);
                // System.out.println(mDownloadDir + mFilename[aDownloadType]);
                downloadFromUrl("", mDownloadDir + mFilename[aDownloadType], mConnection);
            } else if ((aDownloadType == KeyDefine.OTC_TECH) || (aDownloadType == KeyDefine.OTC_FUND)) {
                // System.out.println(mUrl[aDownloadType] +" "+ mDownloadDir +
                // mFilename[aDownloadType]);
                mConnection = excutePost(mUrl[aDownloadType], mUrlParm[aDownloadType], KeyDefine.OTC);
                // downloadFromUrl(mUrl[aDownloadType], mDownloadDir +
                // mFilename[aDownloadType], mConnection);
                downloadFromUrl("", mDownloadDir + mFilename[aDownloadType], mConnection);
            }

            mFile = new File(mDownloadDir, mFilename[aDownloadType]);

            // if file size less than FILE_SIZE, then delete it
            if (mFile.length() < Config.DataAnalyze.DOWNLOAD_FILE_SIZE) {
                System.err.printf("\n %s未開盤", aDownloadDate);
                try {
                    mFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.printf("\nOK! %s\n", mFilename[aDownloadType]);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
        }

    }

    public static void downloadFromUrl(String aSource, String aDestination, HttpURLConnection aConnection)
            throws IOException, ConnectException {

        InputStream mInputStream;
        HttpURLConnection mConnection = null;

        // System.out.printf("aSource:%s\n",aSource);
        // System.out.printf("aDestination:%s\n",aDestination);

        if (aConnection != null) {
            mConnection = aConnection;
        }
        mInputStream = mConnection.getInputStream();
        FileOutputStream mFileOutputStream = new FileOutputStream(aDestination);
        byte[] buffer = new byte[1024];
        for (int length; (length = mInputStream.read(buffer)) > 0; mFileOutputStream.write(buffer, 0, length))
            ;
        mFileOutputStream.close();
        mInputStream.close();
    }

    public static HttpURLConnection excutePost(String aTargetURL, String aUrlParameters, int aType) {
        URL mUrl;
        DataOutputStream mWr;
        HttpURLConnection mConnection = null;
        try {
            mUrl = new URL(aTargetURL);
            mConnection = (HttpURLConnection) mUrl.openConnection();
            if (aType == KeyDefine.TWSE) {
                mConnection.setRequestMethod("POST");
                mConnection.setRequestProperty("Content-Length",
                        "" + Integer.toString(aUrlParameters.getBytes().length));
            }

            mConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            mConnection.setRequestProperty("Content-Language", "UTF-8");
            mConnection.setUseCaches(false);
            mConnection.setDoInput(true);
            mConnection.setDoOutput(true);

            // Send request
            mWr = new DataOutputStream(mConnection.getOutputStream());
            if (aType == KeyDefine.TWSE) {
                mWr.writeBytes(aUrlParameters);
            }
            mWr.flush();
            mWr.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(KeyDefine.ErrorHandle.EXIT_ERROR);
        } finally {

            if (mConnection != null) {
                mConnection.disconnect();
            }
        }
        return mConnection;
    }
}
