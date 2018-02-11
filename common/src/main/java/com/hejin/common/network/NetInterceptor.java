package com.hejin.common.network;


import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author :  贺金龙
 * create time : 2017/10/26 11:07
 * description : 这里是添加缓存内容的
 * instructions :
 * version :
 */
public class NetInterceptor implements Interceptor {
    private static final String TAG = NetInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.e(TAG, "intercept: " + request);
        Response response = chain.proceed(request);
        Log.e(TAG, "response=" + response);

        String cacheControl = request.cacheControl().toString();
        if (TextUtils.isEmpty(cacheControl)) {
            cacheControl = "public, max-age=60";
        }
        return response.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build();
    }
}