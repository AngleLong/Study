package com.hejin.audio;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hejin.study.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 学习到的内容:
 * 1.开始实现某些逻辑的时候,要先把架子搭好,整体把控之后在去实现相应的逻辑
 * 2.ExecutorService 这个东西的用法(这里现在还是不知道是什么)
 * 3.MediaRecord 的使用
 * 4.下次使用api的时候先点进去看看是否会抛出异常
 * <p>
 * 存在的问题
 * 1.当时间小于规定时间的时候,应该删除相应的文件
 * 2.按下的状态选择器不对
 */
public class FileActivity extends AppCompatActivity implements View.OnTouchListener {

    private ExecutorService mExecutor;/*录音使用的对象*/
    private MediaRecorder mMediaRecorder;
    private long mStartTime, mEndTime;
    private Handler mMainHandler;
    private File mOutFile;
    private int RECORDERTIME = 3;/*确定保存录音的时间*/
    private TextView mTvHint;
    private Button mBtnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        mTvHint = findViewById(R.id.tv_hint);
        mBtnRecord = findViewById(R.id.btn_video);
        mBtnRecord.setOnTouchListener(this);

        /*初始化录音使用的内容*/
        initVideo();
    }

    private void initVideo() {
        /*录音的JNI 不具备线程安全性 所以这里创建了一个单线程*/
        mExecutor = Executors.newSingleThreadExecutor();
        /*创建主线程的Handler*/
        mMainHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                stopRecord();
                break;
            case MotionEvent.ACTION_DOWN:
                startRecord();
                break;
        }
        return true;
    }


    /**
     * author :  贺金龙
     * create time : 2017/12/30 23:37
     * description : 开始录音
     */
    public void startRecord() {
        mBtnRecord.setText("正在录音");
        /*
          * 这里的逻辑:(在子线程执行的)
          * 1.先释放之前的资源
          * 2.开始进行录音(这里有可能失败,失败的话提示用户)
          */
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                releaseRecorder();
                if (!doStartRecorder()) {
                    hintFail();
                }
            }
        });
    }


    /**
     * author :  贺金龙
     * create time : 2017/12/30 23:44
     * description : 停止录音
     */
    private void stopRecord() {
        mBtnRecord.setText("开始录音");

        /*
        * 这里的逻辑:(也是在子线程执行的)
        * 1.停止录音(这里也是可能会失败的)
        * 2.释放资源
        */
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                if (!doStopRecorder()) {
                    hintFail();
                }
                releaseRecorder();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 0:16
     * description : 开始录音,这里的返回值证明是否成功
     */
    private boolean doStartRecorder() {
        /*
        * 这里的逻辑:
        * 1.创建MediaRecorder对象(因为停止录音的时候会把对象清空)
        * 2.文件位置的设置
        * 3.MediaRecorder的配置
        *   1.配置音频文件的来源
        *   2.配置输出类型
        *   3.配置采样率
        *   4.配置编码格式
        *   5.配置编码率
        *   6.配置保存文件的位置
        * 4.开始录音
        * 5.记录开始录音的时间
        */

        /*创建MediaRecorder对象(因为停止录音的时候会把对象清空)*/
        mMediaRecorder = new MediaRecorder();
        /*文件位置的设置*/
        mOutFile = new File(Environment.getExternalStorageDirectory().getPath() + "/AudioDemo/" + System.currentTimeMillis() + ".m4a");
        try {
            mOutFile.getParentFile().mkdirs();
            mOutFile.createNewFile();

            /*MediaRecorder的配置*/
            /*1.配置音频文件的来源,麦克风*/
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            /*2.配置输出类型*/
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /*3.配置采样率,这里的44100是所有Android都支持的采样率*/
            mMediaRecorder.setAudioSamplingRate(44100);
            /*4.配置编码格式*/
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            /*5.配置编码率,这个也是通用的码率*/
            mMediaRecorder.setAudioEncodingBitRate(96000);
            /*6.配置保存文件的位置*/
            mMediaRecorder.setOutputFile(mOutFile.getAbsolutePath());

            /*开始录音*/
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            /*记录开始录音的时间*/
            mStartTime = System.currentTimeMillis();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 0:20
     * description : 停止录音,这里的返回值证明是否成功
     */
    private boolean doStopRecorder() {
        /*这里的逻辑:
        *  1.获取停止时间
        *  2.判断相应的时间,如果大于你预定的时间就算成功
        */
        try {
            mMediaRecorder.stop();
            mEndTime = System.currentTimeMillis();

            /*获取录音的时间*/
            final int recorderTime = (int) ((mEndTime - mStartTime) / 1000);
            if (recorderTime > RECORDERTIME) {/*保存成功了*/
                /*这里应该修改UI*/
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTvHint.setText("录音完成:录音时间" + recorderTime + "秒钟");
                    }
                });
            }
            // TODO: 2017/12/31 这里少了相应的文件删除的操作
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 0:17
     * description : 释放资源
     */
    private void releaseRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 0:17
     * description : 失败的提示
     */
    private void hintFail() {
        /*这里要把相应的文件置换成null,因为没有录音成功*/
        mOutFile = null;
        /*这里注意啊!这里是子线程在调用的,这里注意那个主线程的handler的写法*/
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FileActivity.this, "录音失败了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*立即结束线程*/
        mExecutor.shutdownNow();
        /*这里要释放资源*/
        releaseRecorder();
    }
}
