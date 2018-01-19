package com.hejin.audio.video_utils;

import android.media.MediaPlayer;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/5 16:09
 * 类描述 : 处理音频相关的所有接口
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 : 这个相当于一个契约接口，所有的接口都要在这里实现
 */
public interface VideoStatus {

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:16
     * description : 关于暂停的回调
     * instructions :
     * version : 1.0
     */
    interface VideoPauseListener {
        /**
         * author :  贺金龙
         * create time : 2018/1/5 16:12
         * description : 暂停成功的回调
         * instructions :
         * version : 1.0
         */
        void pauseSuccess();

        /**
         * author :  贺金龙
         * create time : 2018/1/5 16:13
         * description : 暂停失败的回调
         * instructions :
         * version : 1.0
         *
         * @param e 相应的异常
         */
        void pauseFail(Exception e);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:17
     * description : 关于暂停之后恢复的操作
     * instructions :
     * version :
     */
    interface VideoRestoreListener {
        /**
         * author :  贺金龙
         * create time : 2018/1/5 16:14
         * description : 暂停之后的恢复失败
         * instructions :
         * version : 1.0
         *
         * @param e 相应的异常
         */
        void restoreFail(IllegalStateException e);

        /**
         * author :  贺金龙
         * create time : 2018/1/5 16:15
         * description : 暂停之后恢复成功
         * instructions :
         * version : 1.0
         */
        void restoreSuccess();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:20
     * description : 开始录音的回调
     * instructions :
     * 这里应该处理开始录音的一些监听:(剩下的想到了在添加吧)
     * 1.录音之前的监听,这里处理一些相应的UI操作
     * 2.开始录音的监听,这里处理一些相应的UI变化
     * 3.开始录音失败的监听,这里给出用户相应的提示
     * <p>
     * version :
     */
    interface StartVideoRecorderListener {
        /**
         * author :  贺金龙
         * create time : 2018/1/4 15:49
         * description : 录音前的回调
         * instructions :
         * version :1.0
         */
        void beforeVideo();

        /**
         * author :  贺金龙
         * create time : 2018/1/4 15:54
         * description : 开始录音的回调
         * instructions :
         * version :1.0
         */
        void startVideo();

        /**
         * author :  贺金龙
         * create time : 2018/1/4 15:54
         * description : 录音失败的回调
         * instructions :
         * version : 1.0
         *
         * @param e 相应的异常
         */
        void failVideo(Exception e);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:19
     * description : 播放自定资源的的监听
     * instructions : 相应的回调
     * 1.播放出错
     * 2.播放成功
     * version : 1.0
     */
    interface PlayVideoListener {

        /**
         * author :  贺金龙
         * create time : 2018/1/4 18:04
         * description : 播放失败
         * instructions :
         * version : 1.0
         *
         * @param e 播放的异常
         */
        void playFail(Exception e);

        /**
         * author :  贺金龙
         * create time : 2018/1/4 18:05
         * description : 播放成功
         * instructions :
         * version : 1.0
         */
        void playSuccess();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:21
     * description :
     * instructions :  相应的回调:(想到了在添加)
     * 1. 录制时间不足的回调lack
     * 2.录制成功的回调
     * <p>
     * version :
     */
    interface FinishRecorderListener {
        /**
         * author :  贺金龙
         * create time : 2018/1/4 17:22
         * description : 录制时间不足
         * instructions :
         * version : 1.0
         */
        void timeLack(long time);

        /**
         * author :  贺金龙
         * create time : 2018/1/4 17:22
         * description : 录制成功
         * instructions :
         * version : 1.0
         */
        void successRecorder(long time);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:22
     * description :释放资源的接口
     * instructions : 相应的错误回调:(剩下的想到了在写吧)
     * 1.释放资源错误的回调
     * 2.结束失败的监听
     * <p>
     * version : 1.0
     */
    interface FailRecorderListener {
        /**
         * author :  贺金龙
         * create time : 2018/1/4 16:31
         * description : 停止失败的监听
         * instructions :
         * version :1.0
         */
        void stopFailRecorder(Exception e);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:22
     * description : 播放完成的回调
     * instructions : 这里只是封装了一些,主要是为了释放相应的资源
     * version : 1.0
     */
    interface CompletionListener {

        /**
         * author :  贺金龙
         * create time : 2018/1/4 18:14
         * description : 完成的回调
         * instructions :
         * version : 1.0
         */
        void onCompletion(MediaPlayer mp);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/5 16:23
     * description : 相应的振幅的回调
     * instructions :
     * version : 1.0
     */
    interface AmplitudeListener {
        /**
         * author :  贺金龙
         * create time : 2018/1/4 19:04
         * description : 振幅的回调,这个是时时回调的
         * instructions :
         * version : 1.0
         */
        void upDateAmplitude(int db);
    }
}
