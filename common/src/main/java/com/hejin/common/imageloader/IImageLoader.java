package com.hejin.common.imageloader;

import android.widget.ImageView;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/7 17:40
 * 类描述 : 图片加载类的接口,这里主要是使用相应的接口
 * 类说明 : 图片类的抽象接口
 */
public interface IImageLoader {
    /**
     * author :  贺金龙
     * create time : 2018/2/7 18:14
     * description : 基本的加载方法
     */
    void displayImage(String url, ImageView imageView);
}
