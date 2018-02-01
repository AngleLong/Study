package com.hejin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * author :  贺金龙
 * create time : 2018/1/22 17:51
 * description : 绑定服务的演示
 * instructions :
 * version :
 */
public class BindService extends Service {
    private String TAG = BindService.class.getSimpleName();

    private MyBinder mBinder = new MyBinder();

    public BindService() {
        Log.e(TAG, "BindService: 构造方法被调用了");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: 绑定方法被调用了");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 方法被调用了" );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: 方法被调用了" );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: 方法被调用了" );
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/22 17:55
     * description : 绑定相应的Binder
     * instructions :
     * version :
     */
    class MyBinder extends Binder {
        public void bindService() {
            Log.e(TAG, "Binder中的方法调用了");
        }
    }
}
