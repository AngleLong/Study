package com.hejin.common;

import com.hejin.common.imageloader.GlideLoader;
import com.hejin.common.imageloader.ImageLoaderManager;

import org.litepal.LitePalApplication;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2017/12/30 22:21
 * 类描述 : 整个项目的Application
 * 修改人 : 贺金龙
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 * 当你引入数据库的时候会有一个很重要的问题要去分析
 * 1.如果你要是把所有的实体类都写在基础组件中的话,这样效果不是很好
 * 2.所以这里最好把数据库写在基础库中,但是这样我还没有尝试真的不知道行不行
 * 验证的结果是第二个方法是可行的,所以这里只要在相应的litePal中设置相应的实体类就可以了
 */
public class BaseApp extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/7 18:06
     * description : 初始化相应的图片加载框架
     */
    private void initImageLoader() {
        GlideLoader glideLoader = new GlideLoader(this);
        ImageLoaderManager.getInstance().init(glideLoader);
    }
}
