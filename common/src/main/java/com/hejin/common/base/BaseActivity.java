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
public abstract class BaseActivity extends AppCompatActivity {

    private Context mContext;


    //------------------------生命周期方法------------------------//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取本页面的上下文
        mContext = this;

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
    }

    //------------------------对外暴漏的方法------------------------//


    //------------------------必须实现的方法------------------------//

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 设置布局的方法
     * 创建时间: 2018/4/ 13:33
     */
    public abstract int createContentView();


}
