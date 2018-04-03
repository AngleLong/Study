package com.hejin.common.utils;

import com.hejin.common.bean.EventBean;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者: 贺金龙 QQ:753355530
 * 项目名称: Study-master
 * 类名称:com.hejin.common.utils
 * 类描述:
 * 创建时间: 2018/4/3 13:38
 * 修改内容:
 * 修改时间:
 * 修改描述:
 */
public class EventBusUtils {
    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 绑定（注册的方法）
     * 创建时间: 2018/4/ 13:44
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 解绑的方法
     * 创建时间: 2018/4/ 13:45
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 作者 贺金龙
     * <p>
     * 方法描述: 发送事件的方法
     * 创建时间: 2018/4/ 13:45
     */
    public static void sendEvent(EventBean event) {
        EventBus.getDefault().post(event);
    }


    /**
     * 作者 贺金龙
     * <p>
     * 方法描述:粘性事件，在注册之前便把事件发生出去，等到注册之后便会收到最近发送的粘性事件（必须匹配）
     * 注意：只会接收到最近发送的一次粘性事件，之前的会接受不到。
     * 创建时间: 2018/4/ 13:45
     */
    public static void sendStickyEvent(EventBean event) {
        EventBus.getDefault().postSticky(event);
    }
}
