package com.hejin.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/7 17:46
 * 类描述 : Glide实现类
 * 类说明 : 本类是实现策略模式的Glide的实现类
 */
public class GlideLoader implements IImageLoader {

    private Context mContext;

    public GlideLoader(Context context) {
        mContext = context;
    }

    @Override
    public void displayImage(String url, ImageView imageView) {
        Glide.with(mContext).load(url).into(imageView);
    }
}
