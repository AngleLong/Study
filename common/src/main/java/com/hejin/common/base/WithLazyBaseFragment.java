package com.hejin.common.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/12 10:28
 * 类描述 :  带有懒加载的BaseFragment
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 : 使Fragment实现懒加载
 */
public abstract class WithLazyBaseFragment<V extends BaseView, T extends BasePresenter<V>> extends Fragment implements BaseView {

    public Context mContext;
    protected T mPresenter;
    private Dialog mDialog;
    /**
     * 是否准备完成,这里主要准备完成是相应的挂载到Activity上了
     */
    private boolean mIsPrepared;
    /**
     * 是否可见
     */
    private boolean mIsVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(createViewLayoutId(), container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsPrepared = true;
        initDate();
        initListener();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInVisible();
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:47
     * description : 可见的时候回调的方法
     * instructions :
     * version : 2.0.1
     */
    private void onVisible() {
        if (!mIsPrepared || !mIsVisible) {
            return;
        }
        LazyDate();
    }

    //----------------------------------------------可去进行复写的方法----------------------------------------------//

    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:45
     * description : 准备好了但是不可见调用的方法
     * instructions : 这个方法你可以选择性的实现,如果你相应针对懒加载进行处理的话就相应的实现
     * version : 2.0.1
     */
    protected void onInVisible() {

    }

    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:51
     * description : 懒加载数据
     * instructions : 当你要实现懒加载数据的话就实现这个方法
     * 这个方法调用时机是相应的每次进到页面都刷新的时候加载的数据
     * version : 2.0.1
     */
    public void LazyDate() {

    }

    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:55
     * description : 初始化数据/请求数据的相应方法
     * instructions : 调用这个方法要和相应的懒加载的数据区分开
     * 这个方法的调用的时机是可能不会刷新的数据的获取
     * version : 2.0.1
     */
    public void initDate() {
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/12 14:23
     * description : 设置相应的监听
     * instructions :
     * version : 2.0.1
     */
    public void initListener() {
    }

    //----------------------------------------------可去进行复写的方法----------------------------------------------//

    //----------------------------------------------对外的抽象方法----------------------------------------------//


    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:40
     * description : 设置相应View的方法
     * instructions :
     * version : 2.0.1
     */
    protected abstract int createViewLayoutId();

    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:34
     * description : 创建相应的Presenter
     * instructions :
     * version : 2.0.1
     */
    public abstract T createPresenter();

    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:41
     * description : 初始化参数的相应方法
     * instructions :
     * version : 2.0.1
     */
    protected abstract void initView(View rootView);

    //----------------------------------------------对外的抽象方法----------------------------------------------//

    //----------------------------------------------相应继承的方法----------------------------------------------//

    /**
     * author :  贺金龙
     * create time : 2018/1/12 10:36
     * description : 判断Fragment的状态
     * instructions :
     * version :
     */
    public boolean getStatus() {
        return (isAdded() && !isRemoving());
    }
    //----------------------------------------------相应继承的方法----------------------------------------------//

    //----------------------------------------------相应生命周期的方法----------------------------------------------//
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }
    //----------------------------------------------相应生命周期的方法----------------------------------------------//

}
