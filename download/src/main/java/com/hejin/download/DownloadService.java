package com.hejin.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author :  贺金龙
 * create time : 2018/1/26 14:47
 * description : Service实现断点续传
 * 主要内容:
 * 1.写入文件通过RandomAccessFile进行相应的制定位置写入
 * 2.
 */
public class DownloadService extends Service {

    public static final String TAG = DownloadService.class.getSimpleName();
    public static final String ACTION_START = "ACTION_START";//开始下载的标记
    public static final String ACTION_STOP = "ACTION_STOP";//结束下载的标记
    public static final int MSG_INIT = 0;//文件长度的消息
    private Handler mHandler = new Handler(getMainLooper());
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case MSG_INIT:
//                    FileInfo fileInfo = (FileInfo) msg.obj;
//                    Log.e(TAG, "handleMessage: " + fileInfo.toString());
//                    break;
//            }
//        }
//    };

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";

    public DownloadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*获得Activity传来的参数*/
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.e(TAG, "开始下载" + fileInfo.toString());

            InitThread initThread = new InitThread(fileInfo);
            initThread.start();
        } else if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.e(TAG, "结束下载 " + fileInfo.toString());
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * author :  贺金龙
     * create time : 2018/1/26 15:06
     * description : 初始化一个子线程
     */
    class InitThread extends Thread {
        private FileInfo mFileInfo;//文件
        private HttpURLConnection mCoon;
        private int mContentLength;//文件长度

        public InitThread(FileInfo fileInfo) {
            mFileInfo = fileInfo;
        }

        @Override
        public void run() {
            super.run();
            /*
            * 1.连接网络文件
            * 2.获得文件长度
            * 3.在本地创建文件
            * 4.设置文件长度
            */
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mFileInfo.getUrl());
                mCoon = (HttpURLConnection) url.openConnection();
                mCoon.setConnectTimeout(3000);
                mCoon.setRequestMethod("GET");
                int length = -1;
                if (mCoon.getResponseCode() == 200) {
                    /*文件长度*/
                    mContentLength = mCoon.getContentLength();
                }
                if (mContentLength <= 0) {
                    return;
                }

                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                /*本地文件*/
                File file = new File(dir, mFileInfo.getFileName());

                /*
                * 特殊的输出流,可以在文件的任何位置进行写入
                * r 指读取权限
                * w 指写入权限
                * d 指删除权限
                */
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);//设置长度

                /*向Service传递信息*/
//                mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "handleMessage: " + mFileInfo.toString());
                    }
                });


            } catch (java.io.IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    raf.close();
                    mCoon.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
