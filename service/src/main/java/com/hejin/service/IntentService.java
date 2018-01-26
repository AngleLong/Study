package com.hejin.service;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * author :  贺金龙
 * create time : 2018/1/22 17:16
 * description : IntentService服务
 * instructions : 其实就是在Service中维护了一个线程
 */
public class IntentService extends android.app.IntentService {
    private String TAG = IntentService.class.getSimpleName();

    public IntentService() {
        super("IntentService");
        /*调用父类的构造方法,这里面传入的名字是工作线程的名称,所以这个名称你可以随便传*/
        Log.e(TAG, "服务有名字了");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "服务创建了");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        /*
         * 在我的理解,这个工作线程只会有一个,因为里面维护了一个相应的Handler
         * 每次的耗时操作都会以相应的队列的方式在IntentService中一次执行
         * 每次开启服务的时候都会走这个方法,但是我调用相应的Stop无法停止相应的服务
         * 这个还有待于研究,因为子停止,或许不需要停止的相应的方法吧(反正相应的停止的方法是不好使的)
         */
        for (int i = 0; i < 100; i++) {
            Log.e(TAG, "服务正在执行" + i);
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        /*这个方法实在o'nHandleIntent之前进行执行
          *所以这里可以初始化一些相应的内容
          */
        Log.e(TAG, "onStartCommand: 执行了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*这个方法会在相应的onHandleIntent执行完成的时候回调*/
        Log.e(TAG, "服务销毁了");
    }
}
