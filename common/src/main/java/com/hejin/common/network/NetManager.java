package com.hejin.common.network;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/11 8:00
 * 类描述 : 网络请求的策略模式实现
 * 类说明 : 这里主要问题是关于网络请求模式的隔离,因为当你要准备替换网络请求的时候,
 * 可能存在全部替换的问题,但是这里如果是要全部替换的话,工程量太大了,所以必须考虑隔离的问题
 */
public class NetManager {
    private NetManager() {
    }

    private static NetManager mNetManager;

    public static NetManager getInstence() {
        if (null == mNetManager) {
            synchronized (NetManager.class) {
                if (null == mNetManager) {
                    mNetManager = new NetManager();
                }
            }
        }
        return mNetManager;
    }
}
