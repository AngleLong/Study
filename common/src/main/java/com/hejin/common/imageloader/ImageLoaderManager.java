package com.hejin.common.imageloader;

import android.widget.ImageView;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/7 17:38
 * 类描述 : 图片资源管理类
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 */
public class ImageLoaderManager implements IImageLoader {

    private static ImageLoaderManager sManager;
    private IImageLoader mImageLoader;

    private ImageLoaderManager() {
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/7 17:44
     * description :  单例模式
     */
    public static ImageLoaderManager getInstance() {
        if (sManager == null) {
            synchronized (ImageLoaderManager.class) {
                if (sManager == null) {
                    sManager = new ImageLoaderManager();
                }
            }
        }
        return sManager;
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/7 17:56
     * description : 初始化方法
     * instructions : 设置相应的上下文和初始化类型,这个在application中进行相应的初始化操作
     */
    public void init(IImageLoader iImageLoader) {
        //设置相应的策略模式,这里是传入相应的策略
        mImageLoader = iImageLoader;
    }

    @Override
    public void displayImage(String url, ImageView imageView) {
        if (null == mImageLoader)
            throw new IllegalStateException("图片加载框架没有初始化");
        mImageLoader.displayImage(url, imageView);
    }
}
