package com.hejin.file.net;

import com.hejin.common.network.BaseBean;
import com.hejin.common.network.BaseObserver;
import com.hejin.common.network.RetrofitManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/11 8:17
 * 类描述 :
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 *  
 */
public class FileRetrofitManager extends RetrofitManager<FileService> {
    private static FileRetrofitManager sManager;

    private FileRetrofitManager() {

    }

    public static FileRetrofitManager getInstance() {
        if (null == sManager) {
            synchronized (FileRetrofitManager.class) {
                if (null == sManager) {
                    sManager = new FileRetrofitManager();
                }
            }
        }
        return sManager;
    }

    @Override
    public FileService getApiSever() {
        return createRetrofit().create(FileService.class);
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/11 16:18
     * description : 这个方法是关联相应的上下游的内容的
     * instructions : 这里如果有其他使用的方法可以自行去创建
     */
    private <T> void setBaseSubscriber(Observable<BaseBean<T>> observable, BaseObserver<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())/*这个就是请求完成的时候会自动对call进行终止，也就是http的close*/
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    // TODO: 2018/2/11 其实把所有的请求都写在这里也是一个相应的问题,这里主要是有一个隔离的问题
    public void requestContent(BaseObserver<String> observer) {
        //请求就变成这个样子了
        setBaseSubscriber(getApiSever().getTiming(), observer);
    }
}
