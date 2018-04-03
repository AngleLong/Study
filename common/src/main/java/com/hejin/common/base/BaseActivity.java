package com.hejin.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hejin.common.utils.BindEventBus;
import com.hejin.common.utils.EventBusUtils;

/**
 * 作者: 贺金龙 QQ:753355530
 * 项目名称: Study-master
 * 类名称:com.hejin.common.base
 * 类描述: 所有Activity的基类
 * 创建时间: 2018/4/3 12:02
 * 修改内容:
 * 修改时间:
 * 修改描述:
 */
public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>> extends AppCompatActivity {

    private Context mContext;
    private P mPresenter;


    //------------------------生命周期方法------------------------//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取本页面的上下文
        mContext = this;

        //获取相应页面传递的参数
        getExtra();

        /*
         * 这里通过注解的形式将EventBus绑定到相应的内容上
         * 这里注意一个问题，在使用的Activity或者Fragment上面添加相应的BindEventBus的注解就可以使用了，
         * 并且加上下面这段就可以了
         * @Subscribe(threadMode = ThreadMode.MAIN)
         * public void onMessageEvent(MessageEvent event)
         */
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusUtils.register(this);
        }

        //设置相应的Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }

        //设置布局的方法
        setContentView(createContentView());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //如果注册EventBus就解绑
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusUtils.unregister(this);
        }

        //释放相应Presenter的资源
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    //------------------------对外暴漏的方法------------------------//

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 获取上个页面传递的相应参数，这个参数可以不去实现
     * 创建时间: 2018/43 14:38
     */
    public void getExtra() {

    }


    //------------------------必须实现的方法------------------------//

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 设置布局的方法
     * 创建时间: 2018/4/ 13:33
     */
    public abstract int createContentView();

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 设置相应的Presenter
     * 创建时间: 2018/4/ 14:35
     */
    public abstract P createPresenter();
}
