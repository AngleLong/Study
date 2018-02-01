package com.hejin.audio.video_utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/4 14:44
 * 类描述 : 录音和朗读的播放类
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 : 采用MediaRecorder录制相应的m4a文件,采用MediaPlayer进行播放音频
 * 提供内容:
 * 1.录制音频;
 * 录制相应的回调
 * 2.播放音频
 * 3.获取振幅的监听
 */
public class VideoManager implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private String TAG = VideoManager.class.getSimpleName();

    private File mRecorderFile;//录制保存的文件
    private ScheduledExecutorService mExecutor;//单线程的线程池
    private Handler mMainHandler;//主线程的Handler
    private MediaRecorder mMediaRecorder;//录音用到的MediaRecorder
    private VideoStatus.FailRecorderListener mFailRecorderListener;//错误的相应回调
    private VideoStatus.StartVideoRecorderListener mStartVideoRecorderListener;//录音的相关监听
    private int SAMPLINGRATE = 44100;//采样率,话说这个是所有Android都通用的一个
    private int BITRATE = 96000;//码率,话说这个也是所有Android都通用的一个
    private long mStartTime, mEndTime;//记录开始时间和结束时间
    private long MINRECORDERTIME = 3 * 1000;//规定录制的最短时间
    private VideoStatus.FinishRecorderListener mFinishRecorderListener;//录制完成的监听
    private MediaPlayer mMediaPlayer;//播放使用的MediaPlayer
    private boolean mIsPlay;//是否正在播放
    private boolean mIsLoop;//设置是否循环,默认是不循环
    private VideoStatus.PlayVideoListener mPlayVideoListener;//播放的相应监听
    private VideoStatus.CompletionListener mCompletionListener;//结束的监听;
    private VideoStatus.AmplitudeListener mAmplitudeListener;//振幅的回调
    private static final int MAX_AMPLITUDE = 32767;//振幅返回的最大值
    private int MAX_LEVEL = 150;//设置声音的等级
    private Random mRandom;//随机数，这里主要事用在获取振幅为空的时候
    private VideoStatus.VideoPauseListener mPauseListener;//暂停的回调
    private VideoStatus.VideoRestoreListener mRestoreListener;//暂停后播放的回调

    /*单例的构造模式*/
    private VideoManager() {
        /*单线程的线程池*/
        mExecutor = Executors.newSingleThreadScheduledExecutor();
        /*主线程的Handler,用于处理主线程的回调*/
        mMainHandler = new Handler(Looper.getMainLooper());
        /*设置随机数*/
        mRandom = new Random(System.currentTimeMillis());
    }

    private static VideoManager mManager;

    public static VideoManager getInstance() {
        if (mManager == null) {
            synchronized (VideoManager.class) {
                if (mManager == null) {
                    mManager = new VideoManager();
                }
            }
        }
        return mManager;
    }

    //*******************************开始录音的内容*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/4 15:12
     * description : 开始录音的方法
     * instructions : 这里应该有相应的回调;
     * 1.开始录制前的回调;
     * 2.录制开始的回调;
     * 3.录制失败的回调;
     * version : 1.0
     * <p>
     * 说明:这里有个相应的重载方法
     *
     * @param filePath 录制保存的文件路径
     */
    public void doStartRecorder(String filePath) {
        /*这里的处理逻辑*/
        this.doStartRecorder(new File(filePath));
    }

    public void doStartRecorder(File file) {
        if (file == null) {
            throw new RuntimeException("请正确传入相应的文件");
        }
        this.mRecorderFile = file;

        /*这里的处理:(创建相应的线程去处理内容)
        * 1.先释放相应的录音
        * 2.初始化MediaRecorder
        * 3.准备资源开始录音
        * 4.设置相应的回调
        * 5.捕获异常处理
        * */
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                /*释放资源*/
                releaseMediaRecorder();
                /*开始录音*/
                startRecorder();
            }
        });

        /*获取相应的振幅*/
        updateMicStatus();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 15:29
     * description : 释放相应的录音资源
     * instructions :
     * version :1.0
     */
    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 15:39
     * description : 开始录音
     * instructions : 注意这里是在子线程执行的
     * version :1.0
     */
    private void startRecorder() {
        mMediaRecorder = new MediaRecorder();

        try {
            mRecorderFile.getParentFile().mkdirs();
            mRecorderFile.createNewFile();

            /*配置相应的MediaRecorder*/
            /*使用麦克风,一般都是使用相应的麦克风,所以这里就不抽取了*/
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            /*设置输出类型*/
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /*设置采样率*/
            mMediaRecorder.setAudioSamplingRate(SAMPLINGRATE);
            /*设置编码格式*/
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            /*设置码率*/
            mMediaRecorder.setAudioEncodingBitRate(BITRATE);
            /*设置输出文件*/
            mMediaRecorder.setOutputFile(mRecorderFile.getAbsolutePath());

            /*设置开始录音之前的监听*/
            beforeVideoListener();

            /*开始录音*/
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            /*记录开始的时间*/
            mStartTime = System.currentTimeMillis();

            /*设置开始录制的监听*/
            startVideoListener();

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            Log.e(TAG, "开始录音失败" + e.toString());
            /*清除相应的文件*/
            delFile();
            /*开始录音错误的回调,回调到相应的主线程*/
            failVideoListener(e);
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:06
     * description : 录音开始前的监听
     * version : 1.0
     */
    private void beforeVideoListener() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mStartVideoRecorderListener != null)
                    mStartVideoRecorderListener.beforeVideo();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:55
     * description :  获取相应的振幅
     * instructions :
     * version : 1.0
     */
    private void updateMicStatus() {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                updateAmplitude();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:56
     * description : 更新相应的振幅的方法
     * instructions :
     * version : 1.0
     */
    private void updateAmplitude() {
        if (mMediaRecorder != null) {
            int ratio;
            try {
                ratio = mMediaRecorder.getMaxAmplitude();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.e(TAG, "获取相应的振幅失败,但是这里设置了一个随机数");
                ratio = mRandom.nextInt(MAX_AMPLITUDE);
            }

            /*获取相应的等级*/
            int level = ratio / (MAX_AMPLITUDE / MAX_LEVEL);
            /*UI更新相应的等级*/
            updateAmplitudeListener(level);
            /*再次调用，从而做到相应的实时更新*/
            /*50毫秒刷新一次*/
            mExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    updateAmplitude();
                }
            }, 50, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 19:06
     * description : 振幅的相应回调
     * instructions :
     * version :
     */
    private void updateAmplitudeListener(final int db) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mAmplitudeListener != null) {
                    mAmplitudeListener.upDateAmplitude(db);
                }
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:12
     * description : 开始录音的监听
     * version : 1.0
     */
    private void startVideoListener() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mStartVideoRecorderListener != null)
                    mStartVideoRecorderListener.startVideo();
            }
        });
    }


    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:21
     * description : 相应的清除文件的方法
     * instructions :
     * version : 1.0
     */
    private void delFile() {
        if (mRecorderFile != null && mRecorderFile.exists()) {
            mRecorderFile.delete();
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:15
     * description : 录音失败的监听
     * instructions :
     * version : 1.0
     *
     * @param e 相应的异常
     */
    private void failVideoListener(final Exception e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mStartVideoRecorderListener != null)
                    mStartVideoRecorderListener.failVideo(e);
            }
        });
    }

    //*******************************结束录音的内容*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:18
     * description : 相应结束录音的方法
     * instructions :
     * version :
     */
    public void doStopRecorder() {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                /*停止录音的方法*/
                stopRecorder();

                /*释放相应的资源*/
                releaseMediaRecorder();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:23
     * description : 停止录音的方法
     * instructions :
     * version : 1.0
     */
    private void stopRecorder() {
        try {
            mMediaRecorder.stop();

            /*结束时间*/
            mEndTime = System.currentTimeMillis();

            long recorderTime = mEndTime - mStartTime;
            /*这里处理相应的时间问题,默认的时间是3秒钟*/
            if (recorderTime > MINRECORDERTIME) {
                /*录制成功*/
                successRecorderListener(recorderTime);
            } else {
                /*录制时间不足*/
                timeLackListener(recorderTime);
                /*当录制时间不足的情况应该删除相应的文件*/
                delFile();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, "停止录音异常了" + e.toString());
            /*停止监听相应的异常处理和回调*/
            stopRecorderListener(e);
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 17:26
     * description : 录制成功的回调
     * instructions :
     * version : 1.0
     *
     * @param recorderTime 录制时间
     */
    private void successRecorderListener(final long recorderTime) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFinishRecorderListener != null)
                    mFinishRecorderListener.successRecorder(recorderTime);
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 17:28
     * description : 录制时间不足
     * instructions :
     * version : 1.0
     *
     * @param recorderTime 录制时间
     */
    private void timeLackListener(final long recorderTime) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFinishRecorderListener != null)
                    mFinishRecorderListener.timeLack(recorderTime);
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:37
     * description : 停止错误的监听
     * instructions :
     * version : 1.-
     */
    private void stopRecorderListener(final IllegalStateException e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFailRecorderListener != null)
                    mFailRecorderListener.stopFailRecorder(e);
            }
        });
    }

    //*******************************播放录音的内容*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/4 17:46
     * description : 播放录音的方法
     * instructions : 这里播放录音,应该有两个内容,播放网络url录音和播放本地路径的录音
     * version : 1.0
     *
     * @param path 相应的录音路径
     */
    public void doPlayVideo(final String path) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                startPlay(path);
            }
        });
    }

    public void doPlayVideo(File file) {
        this.doPlayVideo(file.getAbsoluteFile());
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 17:51
     * description : 播放相应的Url
     * instructions :
     * version : 1.0
     */
    private void startPlay(String path) {
        try {
            mMediaPlayer = new MediaPlayer();
            /*设置音量*/
            mMediaPlayer.setVolume(1, 1);
            /*设置是否循环*/
            mMediaPlayer.setLooping(mIsLoop);
            /*设置播放的录音*/
            mMediaPlayer.setDataSource(path);

            /*设置相应的监听*/
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);

            /*开始播放*/
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            /*播放成功的回调*/
            playSuccess();
        } catch (IOException | RuntimeException e) {
            Log.e(TAG, "播放制定资源出错了");
            e.printStackTrace();
            /*相应的错误的监听*/
            playFailListener(e);
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:27
     * description : 播放成功的回调,也就是说开始播放了
     * instructions : 也就是说开始播放了
     * version : 1.0
     */
    private void playSuccess() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPlayVideoListener != null)
                    mPlayVideoListener.playSuccess();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:09
     * description : 播放失败的监听
     * instructions :
     * version : 1.0
     *
     * @param e 相应的错误信息
     */
    private void playFailListener(final Exception e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPlayVideoListener != null)
                    mPlayVideoListener.playFail(e);
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:17
     * description : 处理相应的结束回调
     * instructions : 主线程回调
     * version : 1.0
     */
    @Override
    public void onCompletion(final MediaPlayer mp) {
        /*这里应该释放相应的资源*/
        releaseMediaPlayer();
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCompletionListener != null)
                    mCompletionListener.onCompletion(mp);
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:19
     * description : 释放相应的MediaPlayer
     * instructions :
     * version : 1.0
     */
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer = null;
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:21
     * description : 错误的监听
     * instructions : 当你处理了错误的时候一定要返回true
     * version : 1.0
     */
    @Override
    public boolean onError(MediaPlayer mp, final int what, int extra) {
        /*释放资源,返回一个监听*/
        releaseMediaPlayer();
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPlayVideoListener != null)
                    mPlayVideoListener.playFail(new Exception("发生错误的类型" + String.valueOf(what)));
            }
        });
        return true;
    }

    //*******************************录音的暂停功能*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/5 15:54
     * description : 暂停的功能
     * instructions :
     * version : 1.0
     */
    public void doPauseVideo() {
        /*暂停操作*/
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                pauseVideo();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 15:56
     * description : 暂停的具体操作
     * instructions :
     * version : 1.0
     */
    private void pauseVideo() {
        /*判空操作,这里要是不为空，或者正在播放*/
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            try {
                mMediaPlayer.pause();
                pauseSuccessListener();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.e(TAG, "暂停失败了" + e.toString());
                /*这里直接释放资源，因为已经异常了*/
                releaseMediaPlayer();
                pauseFailListener(e);
            }
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:27
     * description : 暂停成功的回调
     * instructions :
     * version : 1.0
     */
    private void pauseSuccessListener() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPauseListener != null) {
                    mPauseListener.pauseSuccess();
                }
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:28
     * description : 暂停失败的监听
     * instructions : 这个方法之前释放了所有的资源
     * version : 1.0
     *
     * @param e 相应的异常
     */
    private void pauseFailListener(final Exception e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPauseListener != null) {
                    mPauseListener.pauseFail(e);
                }
            }
        });
    }

    //*******************************暂停之后的恢复播放*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:04
     * description : 暂停之后的恢复
     * instructions :
     * version : 1.0
     */
    public void doRestoreVideo() {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                restoreVideo();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:05
     * description : 具体的恢复播放的操作
     * instructions :
     * version : 1.0
     */
    private void restoreVideo() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            try {
                mMediaPlayer.start();
                restoreSuccessListener();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.e(TAG, "恢复播放资源的时候失败");
                releaseMediaPlayer();
                restoreFailListener(e);
            }
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:35
     * description : 暂停之后的恢复成功
     * instructions :
     * version : 1.0
     */
    public void restoreSuccessListener() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mRestoreListener != null)
                    mRestoreListener.restoreSuccess();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:36
     * description : 暂停之后恢复失败
     * instructions :
     * version : 1.0
     * @param e 相应的异常
     */
    public void restoreFailListener(final IllegalStateException e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mRestoreListener != null)
                    mRestoreListener.restoreFail(e);
            }
        });
    }
    //*******************************对外暴露的方法*******************************//

    //*******************************监听的方法*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/4 15:35
     * description : 释放资源失败的接口
     * instructions :
     * version :
     */
    public void setReleaseRecorder(VideoStatus.FailRecorderListener failRecorderListener) {
        mFailRecorderListener = failRecorderListener;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 15:51
     * description : 录音开始的相关监听
     * instructions :
     * version : 1.0
     */
    public void setStartVideoRecorderListener(VideoStatus.StartVideoRecorderListener startVideoRecorderListener) {
        mStartVideoRecorderListener = startVideoRecorderListener;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 17:23
     * description : 录制完成的监听
     * instructions :
     * version : 1.0
     */
    public void setFinishRecorderListener(VideoStatus.FinishRecorderListener finishRecorderListener) {
        mFinishRecorderListener = finishRecorderListener;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:07
     * description : 播放的监听
     * instructions :
     * version : 1.0
     */
    public void setPlayVideoListener(VideoStatus.PlayVideoListener playVideoListener) {
        mPlayVideoListener = playVideoListener;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 19:08
     * description : 结束的监听
     * instructions :
     * version : 1.0
     */
    public void setCompletionListener(VideoStatus.CompletionListener completionListener) {
        mCompletionListener = completionListener;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 19:08
     * description : 振幅的监听
     * instructions :
     * version : 1.0
     */
    public void setAmplitudeListener(VideoStatus.AmplitudeListener amplitudeListener) {
        mAmplitudeListener = amplitudeListener;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:31
     * description : 设置暂停的监听
     * instructions :
     * version : 1.0
     */
    public void setPauseListener(VideoStatus.VideoPauseListener pauseListener) {
        mPauseListener = pauseListener;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:32
     * description : 暂停之后播放的监听
     * instructions :
     * version :
     */
    public void setRestoreListener(VideoStatus.VideoRestoreListener restoreListener) {
        mRestoreListener = restoreListener;
    }

//*******************************一些设置的方法*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/4 16:10
     * description : 设置相应最短的录制时间,
     * instructions :规定是3秒,这里使用的long类型的值
     * version : 1.0
     */
    public void setMinRecorderTime(long minRecorderTime) {
        this.MINRECORDERTIME = minRecorderTime;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 18:00
     * description : 设置播放的时候是否循环
     * instructions :
     * version : 1.0
     */
    public void setLoop(boolean loop) {
        mIsLoop = loop;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/4 17:41
     * description : 获取相应的录音文件
     * instructions : 因为这里可能录音失败, TODO 感觉这里应该抛出一个异常才对
     * version : 1.0
     */
    public File getRecorderFile() {
        return mRecorderFile;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 15:14
     * description : 设置相应的等级
     * instructions :
     * version :
     */
    public void setSmplitudeLevel(int level) {
        this.MAX_LEVEL = level;
    }
    //*******************************声明周期的方法*******************************//

    /**
     * author :  贺金龙
     * create time : 2018/1/4 17:37
     * description : 相应的释放资源的操作
     * instructions : 这里应该在相应的页面释放相应的资源
     * version : 1.0
     */
    public void onDestroy() {
        /*结束掉线程池*/
        mExecutor.shutdownNow();
        /*释放录制资源*/
        releaseMediaRecorder();
        /*释放相应的播放资源*/
        releaseMediaPlayer();
    }
}
