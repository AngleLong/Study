package com.hejin.common.network;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author :  贺金龙
 * create time : 2017/10/26 11:09
 * description : 添加对话框的回调请求
 * instructions : 这个里面判断的请求的状况，这个还需要完善的，这里面的具体逻辑就是
 * 当你请求成功的时候正常走一个回调，当你不成功的时候这里不成功最好是使用Toast
 * 这样就不需要上下文了但是这个还是要根据具体情况具体去分析这个问题
 *
 * 使用这个的时候有一个相应的问题没有解决,当进行隔离的时候怎么做???
 * version :
 */
public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {

    @Override
    public void onSubscribe(Disposable d) {
        //开始的时候进行调用,这里一般处理一些loading的一些处理
    }

    @Override
    public void onNext(BaseBean<T> baseBean) {
        //这个是真正请求完成的回调
    }

    @Override
    public void onError(Throwable e) {
        //这个是请求错误的回调
    }

    @Override
    public void onComplete() {
        //这个是完成时的回调,也就是最后调用的方法,这里主要是处理一些相应的loading关闭的一些内容
    }
}