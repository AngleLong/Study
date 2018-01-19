package com.hejin.audio;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.hejin.study.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://www.cnblogs.com/whoislcj/p/5583833.html
 * https://www.cnblogs.com/zhujiabin/p/5666368.html
 * 录制视频的页面
 * 这里使用的是MediaRecorder进行相应的视频录制
 * 其实和录制音频差不多,也是保存相应的文件资源
 * 然后通过相应的MediaPlayer进行相应的视频播放
 * <p>
 * 这里标注一下逻辑:
 * 1.需要相应的SurfaceView和Camera
 * 2.初始化相应的内容
 * <p>
 * 这里应该注意的问题:
 * 1.在页面初始化的时候就应该展示相应的Camera和SurfaceView进行预览页面
 * 1.1在相应的onResume()的时候应该展示
 * 1.2在相应的onPasue()的时候应该停止
 * 2.生命周期的处理
 * <p>
 * 遇见的问题:
 * 1.首先你要释放相应的资源之后,视频才能进行播放
 */
public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private String TAG = VideoActivity.class.getSimpleName();
    private SurfaceView mMainSV;//SurfaceView
    private SurfaceHolder mMainSVHolder;//相应的CallBack
    private Camera mCamera;//摄像头相应的类
    private MediaRecorder mMediaRecorder;
    private boolean isPlay;//是否正在播放
    private boolean isStarted;//是否正在录制
    private File mOutFile;//文件保存的路径
    private MediaPlayer mMediaPlayer;//视频播放的页面
    private ExecutorService mExecutor;/*单线程对象*/
    private Handler mMainHanlder;//主线程的Handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);

        init();//初始化一些相应的参数
        initSurfaceView();//初始化surfaceView
    }

    private void init() {
        mExecutor = Executors.newSingleThreadExecutor();
        mMainHanlder = new Handler(getMainLooper());
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 11:51
     * description : 初始化一些参数
     */
    private void initSurfaceView() {
        mMainSV = findViewById(R.id.sv_main);
        mMainSVHolder = mMainSV.getHolder();

//        mMainSVHolder.setFormat(PixelFormat.TRANSLUCENT);//设置SurfaceView半透明
        mMainSVHolder.setFormat(PixelFormat.TRANSPARENT);//设置SurfaceView全透明
        mMainSVHolder.setKeepScreenOn(true);//保持屏幕位置
        mMainSVHolder.addCallback(this);//添加相应的回调
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPreview(mCamera, mMainSVHolder);//开始预览
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 13:31
     * description : 获取后置摄像头的方法
     */
    private Camera getCamera() throws RuntimeException {
        Camera camera;
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        /*获取相应Camera对象(后置摄像头)*/
        if (camera == null) {
            throw new RuntimeException("获取相应的摄像头信息失败");
        }
        return camera;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 14:03
     * description : 开始预览的方法
     */
    private void startPreview(Camera camera, SurfaceHolder surfaceHolder) {
        if (camera == null) {
            mCamera = camera = getCamera();//这里会抛出一个相应的获取摄像头异常
            if (surfaceHolder != null && camera != null) {
                try {
                    //关联相应的surfaceHolder
                    camera.setPreviewDisplay(surfaceHolder);
                    //设置相应的角度,如果不设置这个角度的话,预览的时候可能是横屏的
                    camera.setDisplayOrientation(90);
                    //开始预览
                    camera.startPreview();
                } catch (IOException | RuntimeException e) {
                    Log.e(TAG, "开启预览失败" + e.toString());
                }
            }
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 11:50
     * description : 开始录制的方法
     * 这里的逻辑:
     * 1.从预览到录制前给用户相应的提示
     * 2.开始预览
     * 3.处理相应的异常
     * <p>
     * 当录制失败的时候给用户相应的提示
     * 当从预览到录制视频的时候这一应该有一个相应的提示
     */
    public void btn_start(View view) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                //开始录制视频的方法
                if (!startVideo()) {
                    errorHint("开始录制视频失败");
                }
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 19:04
     * description : 失败提示
     *
     * @param hint 失败的提示
     */
    private void errorHint(final String hint) {
        mMainHanlder.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoActivity.this, hint, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 19:03
     * description : 开始录制的方法
     *
     * @return 返回开始录制视频是否成功
     */
    private boolean startVideo() {
        //当开始录制,或者是播放的情况下不能开始录制
        if (isPlay || isStarted) return false;
        //当摄像头未空的时候不能开始录制
        if (mCamera == null) {
            mCamera = getCamera();
        }
        //锁定摄像头的类,这里在使用MediaRecorder录制视频的时候必须的
        mCamera.unlock();

        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setCamera(mCamera);

        //设置输出的文件位置
        mOutFile = new File(Environment.getExternalStorageDirectory().getPath() + "/AudioDemo/" + System.currentTimeMillis() + ".mp4");

        try {
            mOutFile.getParentFile().mkdirs();
            mOutFile.createNewFile();

                /*这个要在相应的format之前调用*/

                /*
                * setAudioSource() 设置录制的音频来源
                * MediaRecorder.AudioSource参数说明
                * 1. 默认音频源 MediaRecorder.AudioSource.DEFAULT=0
                * 2. 麦克风音频源(对应手机底部的麦克风) MediaRecorder.AudioSource.MIC = 1
                * 3. 上行声音 MediaRecorder.AudioSource.VOICE_UPLINK = 2
                * 4. 下行声音 MediaRecorder.AudioSource.VOICE_DOWNLINK =3
                * 5. 上行和下行的声音 MediaRecorder.AudioSource.VOICE_CALL=4
                * 5.  摄像头旁边的麦克风 MediaRecorder.AudioSource.CAMCORDER=5
                * 6. 语音识别 MediaRecorder.AudioSource.VOICE_RECOGNITION = 6
                * 7. 消除噪音的(自己是这么理解的) MediaRecorder.AudioSource.VOICE_COMMUNICATION=7
                */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

                /*
                *  setVideoSource() 开始捕捉相应的数据到相应的setOutputFile（指定的文件）
                *  MediaRecorder.VideoSource相应参数说明
                *  MediaRecorder.VideoSource.DEFAULT 默认的
                *  MediaRecorder.VideoSource.CAMERA 从摄像头采集相应数据(Camera)
                *  MediaRecorder.VideoSource.SURFACE 从摄像头采集相应数据(Camera2)
                */
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                /*
                 * setOutputFormat() 输出格式
                 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

                /*
                 * setAudioEncoder()设置音频的编码格式
                 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                /*
                 * setVideoEncoder()设置视频的编码格式
                 */
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);


            mMediaRecorder.setVideoSize(640, 480);
            mMediaRecorder.setVideoFrameRate(30);//每秒钟的帧数
            mMediaRecorder.setVideoEncodingBitRate(6 * 1024 * 1024);//影响视频大小的
            mMediaRecorder.setOrientationHint(90);
            //设置记录会话的最大持续时间（毫秒）
            mMediaRecorder.setMaxDuration(30 * 1000);//录制的时间
            //设置录制过程中的预览
            mMediaRecorder.setPreviewDisplay(mMainSVHolder.getSurface());

            mMediaRecorder.setOutputFile(mOutFile.getPath());

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isStarted = true;

        } catch (RuntimeException | IOException e) {
            Log.e(TAG, "开始录制的时候失败");
            return false;
        }

        return true;
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 11:50
     * description : 停止录制的方法
     */
    public void btn_stop(View view) {
        isStarted = false;
        // 这里停止录制的时候,主要就是释放相应的资源,也就是相应的MediaRecorder的资源
        /*
        * 这里停止录制的时候,主要就是释放相应的资源,也就是相应的MediaRecorder的资源
        * 这里注意一个问题,当你上面设置相应的时长虽然是30秒,但是如果你小于这个时间就点击停止的话
        * 那么文件也会进行相应的保存,知识时间短了
        */
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.e(TAG, "btn_stop: 释放资源失败");
                mMediaRecorder = null;
            }
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 11:50
     * description : 开始播放的方法
     */
    public void btn_play(View view) {
        isPlay = true;

        mMediaPlayer = new MediaPlayer();
        try {
            /*创建相应的MediaRecorder对象*/
            mMediaPlayer = new MediaPlayer();
            /*设置音量 TODO 这里涉及到一个相应的播放声音问题,还有播放指定位置的方法*/
            mMediaPlayer.setVolume(1, 1);
            /*设置是否循环*/
            mMediaPlayer.setLooping(false);

            /*设置播放的录音*/

            mMediaPlayer.setDataSource(this, Uri.parse(mOutFile.getPath()));

            mMediaPlayer.setDisplay(mMainSVHolder);

            /*设置相应的监听*/
            /*播放完成的监听*/
            mMediaPlayer.setOnCompletionListener(this);
            /*设置错误的监听*/
            mMediaPlayer.setOnErrorListener(this);

            /*准备/播放*/
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------surfaceView的回调----------------------------------------//
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /*改变,重启整个预览功能*/
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/17 14:52
     * description : 释放相应的Camera
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();/*取消取景功能*/
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
