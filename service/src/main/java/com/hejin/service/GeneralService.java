package com.hejin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * author :  贺金龙
 * create time : 2018/1/22 16:43
 * description : 普通的服务
 */
public class GeneralService extends Service {
    private String TAG = GeneralService.class.getSimpleName();

    public GeneralService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        /*这个是绑定Binder的相应的方法,
          *这个服务中先不去考虑
          */
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        * 创建服务的时候调的回调
        * 这个方法只有在创建Service的时候执行一次
        */
        Log.e(TAG, "服务创建了");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        * 具体执行逻辑的时候的方法
        * 1.这里有一点问题说明一下,有的时候你创建相应的服务了,但是由于后台资源不足,服务被销毁了
        * 这个时候,在执行的时候就会存在相应intent为空的情况,当你遇到这个情况的时候,只要返回相应的标识就可以了
        *  return Service.START_REDELIVER_INTENT;就能保证服务再次启动的时候intent不为空了
        * 2.这个方法每次调用相应的startService都会去执行
        */
        Log.e(TAG, "服务正在执行");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        * 销毁服务的回调
        * 这个方法只有在销毁Service的时候执行一次
        */
        Log.e(TAG, "服务销毁了");
    }
}

