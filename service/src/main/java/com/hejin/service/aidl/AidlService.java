package com.hejin.service.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hejin.service.IAddAidl;

/**
 * author :  贺金龙
 * create time : 2018/2/14 6:19
 * description : 由于AIDL是通过接口进行相应的传递的,接口是通过服务进行创建的
 * instructions : 但是这里注意一点,相应的service是通过绑定实现的
 * version :
 */
public class AidlService extends Service {
    private String TAG = AidlService.class.getSimpleName();

    private IBinder mAddBinder = new IAddAidl.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            Log.e(TAG, "传递过来相应的内容");
            return a + b;
        }
    };

    public AidlService() {
        Log.e(TAG, "远程服务被创建了");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "绑定成功");
        return mAddBinder;
    }
}
