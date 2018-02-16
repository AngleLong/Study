package com.hejin.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


/**
 * author :  贺金龙
 * create time : 2018/1/22 16:41
 * description : 理解服务相关的内容
 */
public class ServiceActivity extends AppCompatActivity {

    private static final String TAG = ServiceActivity.class.getSimpleName();
    /*绑定相应的连接*/
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /*这个方法会在相应的创建的时候进行调用*/
            Log.e(TAG, "onServiceConnected:  连接创建的时候调用");
            //向下转型从而调用相应的方法
            BindService.MyBinder binder = (BindService.MyBinder) service;
            binder.bindService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            /*这个方法是在Service丢失的情况下才被调用
            * 或者在Service被杀死的视乎调用*/
            Log.e(TAG, "onServiceDisconnected: 连接取消的时候调用");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/22 16:41
     * description : 开启普通服务
     */
    public void startGeneralService(View view) {
        Intent intent = new Intent(this, GeneralService.class);
        startService(intent);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/22 16:53
     * description : 停止普通服务
     */
    public void stopGeneralService(View view) {
        Intent intent = new Intent(this, GeneralService.class);
        stopService(intent);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/22 17:23
     * description : 开启自关闭服务
     * instructions : TODO 这里我想验证个问题,当服务没有结束的时候我点击结束会怎样
     * 这里面的这个问题的答案是你即便调用相应的Stop也没有办法停止相应的服务
     */
    public void startIntentService(View view) {
        Intent intent = new Intent(this, IntentService.class);
        startService(intent);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/22 17:51
     * description : 绑定服务
     */
    public void bindService(View view) {
        Intent intent = new Intent(this, BindService.class);
        /*参数三有必要说明下:
         *BIND_AUTO_CREATE 这个方法是创建关联后自动创建Service,会使得相应Service中的onCreate()方法的到执行
         * 但相应的onStartCommand()不会被执行
         */
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/22 17:51
     * description : 解绑服务
     */
    public void unBindService(View view) {
        unbindService(mConnection);
    }
}
