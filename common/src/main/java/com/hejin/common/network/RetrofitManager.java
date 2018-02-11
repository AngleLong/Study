package com.hejin.common.network;

import com.hejin.common.Constants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/11 7:44
 * 类描述 : 这个是实现Retrofit的管理者
 * 类说明 : TODO 这里看看是否要使用单例
 */
public abstract class RetrofitManager<T> {
    private static int mReadTime = 10;/*读取超时*/
    private static int mWriteTime = 10;/*写入超时*/
    private static int mConnectTime = 10;/*连接超时*/

//    //-------------------关于增加网络请求内容的
//    private static final long cacheSize = 1024 * 1024 * 20;//缓存文件最大限制大小20M
//    private static String cachedirPath = Environment.getExternalStorageState() + "/caches";//设置缓存的路径
//    private static Cache cache = new Cache(new File(cachedirPath), cacheSize);

    public static Retrofit createRetrofit() {
        /*构建Retrofit对象*/
        /*支持以实体类返回*/
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.UrlPath.BaseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())/*支持以实体类返回*/
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return mRetrofit;
    }


    /**
     * author :  贺金龙
     * create time : 2017/10/26 10:58
     * description : 创建OkHttpClient的实例
     * instructions : 这里只是基础配置,如果想要添加公共的请求参数的话,重写这个方法自己添加就可以了
     * 其他的配置也是一样的(像缓存)
     */

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .readTimeout(mReadTime, TimeUnit.SECONDS)
                .writeTimeout(mWriteTime, TimeUnit.SECONDS)
                .connectTimeout(mConnectTime, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)/*失败重连*/
                .connectionPool(new ConnectionPool())
                .build();
    }


    /**
     * author :  贺金龙
     * create time : 2017/10/26 13:37
     * description :  提供一个返回ApiServer的方法
     * instructions : 各个module通过继承这个方法根据各自的需要返回相应的内容
     * version :1.0
     */

    public  abstract T getApiSever();
}
