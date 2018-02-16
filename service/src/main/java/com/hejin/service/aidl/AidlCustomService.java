package com.hejin.service.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hejin.service.ICustomAidl;
import com.hejin.service.PersonBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author :  贺金龙
 * create time : 2018/2/15 7:09
 * description : 传递自定义类型的服务
 * instructions : 传递相应的Person类,由于这里返回的是一个相应的集合,所以这里创建了一个相应的集合
 * version :
 */
public class AidlCustomService extends Service {

    private String TAG = AidlCustomService.class.getSimpleName();
    private List<PersonBean> mPersonBeans;

    private IBinder mIBinder = new ICustomAidl.Stub() {
        @Override
        public List<PersonBean> add(PersonBean personBean) throws RemoteException {
            mPersonBeans.add(personBean);
            return mPersonBeans;
        }
    };

    public AidlCustomService() {
        Log.e(TAG, "AidlCustomService: 这个方法执行吗???");
        mPersonBeans = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: 绑定成功");
        mPersonBeans = new ArrayList<>();
        return mIBinder;
    }
}
