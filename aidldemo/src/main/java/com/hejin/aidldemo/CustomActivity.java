package com.hejin.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hejin.service.ICustomAidl;
import com.hejin.service.PersonBean;

import java.util.List;

/**
 * author :  贺金龙
 * create time : 2018/2/15 7:17
 * description : 连接自定义类型的AIDL
 * version :
 */
public class CustomActivity extends AppCompatActivity {

    private static final String TAG = CustomActivity.class.getSimpleName();
    private ICustomAidl mICustomAidl;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: 这里执行吗?" );
            mICustomAidl = ICustomAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mICustomAidl = null;
        }
    };
    private TextView mTv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        mTv_content = findViewById(R.id.tv_content);

        bind();
    }

    private void bind() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.hejin.service", "com.hejin.service.aidl.AidlCustomService"));
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    public void add1(View view) {
        try {
            List<PersonBean> personBeans = mICustomAidl.add(new PersonBean("张三", "25"));
            mTv_content.setText("添加的结果是:" + personBeans.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void add2(View view) {
        try {
            List<PersonBean> personBeans = mICustomAidl.add(new PersonBean("李四", "30"));
            mTv_content.setText("添加的结果是:" + personBeans.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }
}
