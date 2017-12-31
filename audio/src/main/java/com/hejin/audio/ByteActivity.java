package com.hejin.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hejin.study.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 学习到的内容:
 * 1.volatile关键字的使用;
 * 2.AudioRecord的使用;
 * 3.缓冲区的一些问题;
 * 4.文件的读写;
 * <p>
 * 大体逻辑:
 * 根据按钮状态控制是否开始录音,因为这里是获取相应的字节流,所以必须一个状态去停止,
 * 所以这里要根据状态去实现停止的逻辑,如果失败了,应该把状态模式更改过来
 * <p>
 * 存在的问题
 * 1.当时间小于规定时间的时候,应该删除相应的文件
 * 2.按下的状态选择器不对
 * 3.看看抛出异常后还会执行finally的代码吗?
 * 4.一个相应的播放声音问题,还有播放指定位置的方法
 * 5.还有一个问题就是,当你播放声音的时候还能录制
 */
public class ByteActivity extends AppCompatActivity {

    private TextView mTvHint;
    private Button mBtnStart;

    private boolean mIsRecourding = false;/*是否正在录音的状态*/
    private volatile ExecutorService mExecutor;
    private Handler mMainHandler;
    private AudioRecord mAudioRecord;
    private File mOutFile;/*保存的文件*/

    private byte[] mBuffer;/*读取用到字节数组*/
    private int BUFFERSIZE = 2048;/*缓冲区大小*/
    private int RECORDERTIME = 3;/*确定保存录音的时间*/

    private long mStartTime, mEndTime;/*开始和结束时间*/
    private FileOutputStream mFileOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_byte);

        mTvHint = findViewById(R.id.tvHint);
        mBtnStart = findViewById(R.id.startRecord);

        initRecorder();
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 2:30
     * description : 初始化录音的资源
     */
    private void initRecorder() {
        mExecutor = Executors.newSingleThreadExecutor();
        mBuffer = new byte[BUFFERSIZE];
        mMainHandler = new Handler(getMainLooper());
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 2:21
     * description : 开始/停止录音的方法
     */
    public void record(View view) {
        if (mIsRecourding) {/*正在录音*/
            //更改UI的状态
            mBtnStart.setText("开始录音");
            mIsRecourding = false;
        } else {/*停止录音*/
            //更新UI状态
            mBtnStart.setText("停止录音");
            mIsRecourding = true;
            //创建线程开始录音
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    if (!startRecord()) {
                        hintFail();
                    }
                }
            });
        }
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 0:17
     * description : 失败的提示
     */
    private void hintFail() {
        /*这里注意啊!这里是子线程在调用的,这里注意那个主线程的handler的写法*/
        /*这里还要变更状态*/
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ByteActivity.this, "录音失败了", Toast.LENGTH_SHORT).show();
                mIsRecourding = false;
                mBtnStart.setText("开始录音");
            }
        });

    }


    private boolean startRecord() {

        /*
        * 此处的逻辑:
        * 1.创建相应保存的文件
        * 2.配置AudioRecord需要的参数;
        *       1.配置音频文件的来源,麦克风
        *       2.配置采样率,这里的44100是所有Android都支持的采样率
        *       3.配置声道模式,这里配置的是单声道
        *       4.设置文件的格式,这里设置的16Bit也是所有Android都支持的配置
        *       5.配置相应的缓冲区
        *           1.首先缓冲区是有相应的规定的
        *           2.缓冲大小不能低于相应的要求,否则会报错;
        *           3.我们每次读取的大小不能小于相应的缓冲区大小;
        * 3.创建AudioRecord对象(这里获取的资源为pcm)
        * 4.开始录音
        * 5.记录开始时间
        * 6.读写文件
        * 7.释放资源
        */


        /*文件位置的设置*/
        mOutFile = new File(Environment.getExternalStorageDirectory().getPath() + "/AudioDemo/" + System.currentTimeMillis() + ".pcm");
        try {
            mOutFile.getParentFile().mkdirs();
            mOutFile.createNewFile();

            /*创建文件输出流*/
            mFileOutputStream = new FileOutputStream(mOutFile);

            /*配置AudioRecord需要的参数*/
            /*1.配置音频文件的来源,麦克风*/
            int audioSource = MediaRecorder.AudioSource.MIC;
            /*2.配置采样率,这里的44100是所有Android都支持的采样率*/
            int sampleRateInHz = 44100;
            /*3.配置声道模式,这里配置的是单声道*/
            int channelConfig = AudioFormat.CHANNEL_IN_MONO;
            /*4.设置文件的格式,这里设置的16Bit也是所有Android都支持的配置*/
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            /*5.配置相应的缓冲区,这里注意一下几点:
             *       1.首先缓冲区是有相应的规定的
             *       2.缓冲大小不能低于相应的要求,否则会报错;
             *       3.缓冲区大小也不能低于我们的要求;
             */
            int minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            int bufferSizeInBytes = Math.max(minBufferSize, BUFFERSIZE);

            /*创建AudioRecord对象*/
            mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);

            /*开始录音*/
            mAudioRecord.startRecording();

            /*记录开始时间*/
            mStartTime = System.currentTimeMillis();

            /*循环读取数据,如果在录音状态就一直读取*/
            while (mIsRecourding) {
                int read = mAudioRecord.read(mBuffer, 0, BUFFERSIZE);
                if (read > 0) {/*读取成功*/
                    /*读写文件*/
                    mFileOutputStream.write(mBuffer, 0, read);
                } else {/*读取失败*/
                    return false;
                }
            }
            /*这里会一直循环的,所以写在后面的就是循环完的逻辑了*/
            return stopRecord();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return false;
        } finally {
            /*这里如果抛出异常的话,应该把AudioRecord及时释放*/
            releaseAudioRecord();
        }
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 3:10
     * description : 停止录音的逻辑,因为这里也有可能失败
     * instructions : 这里注意这种写法,上面的方法有返回值,所以这里也有相应的返回值,这种写法很好
     */
    private boolean stopRecord() {
        try {
            releaseAudioRecord();
            mFileOutputStream.close();

            /*记录时常,是否保存录音*/
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
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 3:18
     * description : AudioRecord的释放资源
     */
    private void releaseAudioRecord() {
        if (mAudioRecord != null) {
            try {
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*这里应该释放资源*/
        releaseAudioRecord();
        audioTrackQuietly(mAudioTrack);
        closeQuietly(mFileInputStream);
        mIsPlay = false;
        mIsRecourding = false;
        // TODO: 2018/1/1 这里应该删除指定的文件
    }

    //-------------华丽的分割线(上面是录制相应录音的逻辑,下面是播放相应录音的逻辑)-------------//

    private volatile boolean mIsPlay;/*是否在播放状态*/
    AudioTrack mAudioTrack;/*相应播放的类*/
    FileInputStream mFileInputStream = null;

    /**
     * author :  贺金龙
     * create time : 2017/12/31 23:31
     * description : 播放录音的按钮
     */
    public void playVideo(View view) {
        /*
        * 此处的逻辑是这样的:
        * 1.根据状态值判断是否在录音
        * 2.在子线程开始播放逻辑
        * 3.配置相应的AudioTrack对象(用于播放字节流声音的对象)
        *       1.配置文件的输出类型(这里设置的是音乐模式,也就是使用扬声器)
        *       2.设置采样率(这里要保证和录制的时候一样)
        *       3.设置声道(这个是输出的单声道)
        *       4.设置码率(这里要保证和录制的时候一样)
        *       5.设置缓冲区(这里和上面是一样的)
        *       6.设置流模式播放
        * 4.创建文件输入流的文件
        * 5.开始播放
        * 6.处理异常(这里好好看看)
        *       1.关闭输入流
        *       2.释放AudioTrack对象
        *       3.提示用户
        * */
        if (mOutFile != null && !mIsPlay) {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    startPlay(mOutFile);
                }
            });
        }
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/31 23:40
     * description : 开始播放录音的逻辑
     */
    private void startPlay(File outFile) {
        mIsPlay = true;

        /*1.配置文件的输出类型(这里设置的是音乐模式,也就是使用扬声器)*/
        int streamType = AudioManager.STREAM_MUSIC;
        /*2.设置采样率(这里要保证和录制的时候一样)*/
        int sampleRateInHz = 44100;
        /*3.设置声道(这个是输出的单声道)*/
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        /*4.设置码率(这里要保证和录制的时候一样)*/
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        /* 5.设置缓冲区(这里和上面是一样的)*/
        /*最小的缓冲区,怎么设置都要比他大*/
        int minBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        int bufferSizeInBytes = Math.max(minBufferSize, BUFFERSIZE);
        /*6.设置流模式播放*/
        int mode = AudioTrack.MODE_STREAM;
        mAudioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);

        /*创建文件输入流*/
        try {
            /*开始播放*/
            mAudioTrack.play();

            mFileInputStream = new FileInputStream(outFile);
            /*文件的读写*/
            int read;
            while ((read = mFileInputStream.read(mBuffer)) > 0) {
                /*将读到的文件写入到相应的AudioTrack中*/
                int write = mAudioTrack.write(mBuffer, 0, read);
                /*这里返回的这个int类型,是用来判断是否成功的标识*/
                switch (write) {
                    case AudioTrack.ERROR_BAD_VALUE:
                    case AudioTrack.ERROR_INVALID_OPERATION:
                    case AudioTrack.ERROR_DEAD_OBJECT:
                        playFail();
                        return;/*跳出循环,也就是读取失败了*/
                    default:
                        break;
                }
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            playFail();// TODO: 2018/1/1 这里看看抛出异常后还会执行finally的代码吗?
            // TODO: 2018/1/1 这里存在一个问题,如果在这里提示的话,关闭页面释放的时候会弹出Toast ,看看解决办法
        } finally {
            /*这里明白一个问题,当你释放资源的时候就证明播放结束了*/
            mIsPlay = false;
            /*处理相应的异常,因为这里是一定执行的,所以把一些逻辑写在这里就可以了*/
            /*这里关闭相应的流的话会抛出异常,要是在这里写的话比较Low,所以定义一个方法去写把*/
            closeQuietly(mFileInputStream);/*其实这里有一个方法静默处理的,但是会有红线,所以就没用*/

            /*释放播放器资源*/
            audioTrackQuietly(mAudioTrack);
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/1 0:11
     * description : 静默关闭输出流
     */
    public void closeQuietly(FileInputStream fileInputStream) {
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * author :  贺金龙
     * create time : 2018/1/1 0:15
     * description : 释放播放器的资源
     */
    private void audioTrackQuietly(AudioTrack audioTrack) {
        /*这里在释放的时候会抛出运行是异常*/
        try {
            audioTrack.stop();
            audioTrack.release();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/1 0:06
     * description : 失败的提示方法
     */
    private void playFail() {
        mOutFile = null;/*把文件清空*/
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ByteActivity.this, "播放录音文件失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
