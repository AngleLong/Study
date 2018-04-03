package com.hejin.common.base;

import java.lang.ref.WeakReference;

/**
 * 作者 贺金龙
 * <p>
 * 方法描述:MVP架构中的Presenter基类
 * 创建时间: 2018/4/3 14:28
 */
public class BasePresenter<V> {

    //使用弱引用,避免内存泄漏
    protected WeakReference<V> mReference;

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 挂在相应的Presenter层，这里主要是在相应的Activity中进行的
     * 创建时间: 2018/4/3 1431
     */
    public void attachView(V view) {
        mReference = new WeakReference<>(view);
    }

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 释放相应的Presenter，防止内存泄漏
     * 创建时间: 218/4/3 14:30
     */
    public void detachView() {
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 判断是否挂在的方法，但是这里因为基类中直接就创建了相应的挂栽应该是使用不上
     * 创建时间: 2018/4/ 14:29
     */
    public boolean isViewAttached() {
        return mReference != null && mReference.get() != null;
    }

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 获取相应的View层的引用
     * 创建时间: 2018/4/ 14:30
     */
    protected V getView() {
        return (mReference != null && mReference.get() != null) ? mReference.get() : null;
    }
}
