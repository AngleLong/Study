package com.hejin.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hejin.service.IAddAidl;
/**
 * author :  贺金龙
 * create time : 2018/2/15 7:16
 * description : 连接普通的AIDL
 * instructions :
 * version :
 */
public class GeneralActivity extends AppCompatActivity {

    private TextView mTv_content;
    private EditText mEt_num1;
    private EditText mEt_num2;
    private IAddAidl mIAddAidl;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //这里注意写法,平时的都是直接强转的
            //这样绑定之后,就可以在相应的地方调用mIAddAidl的方法了
            mIAddAidl = IAddAidl.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (mIAddAidl != null) {
                mIAddAidl = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        initView();
        bind();
    }

    private void initView() {
        mEt_num1 = findViewById(R.id.num1);
        mEt_num2 = findViewById(R.id.num2);
        mTv_content = findViewById(R.id.tv_content);
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/14 6:27
     * description : 绑定服务的操作
     */
    private void bind() {
        Intent intent = new Intent();
        //这里说明一下,前面的是项目的包名,后面的是相应AIDL绑定服务的名(全路径哦)
        intent.setComponent(new ComponentName("com.hejin.service", "com.hejin.service.aidl.AidlService"));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/14 6:23
     * description : 通过点击事件,响应相应的远程服务
     */
    public void getContent(View view) {
        try {
            int sum = mIAddAidl.add(Integer.parseInt(mEt_num1.getText().toString()),
                    Integer.parseInt(mEt_num2.getText().toString()));
            mTv_content.setText(String.valueOf(sum));
        } catch (RemoteException e) {
            mTv_content.setText("数据错误");
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
