package com.hejin.ndk;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/2 14:36
 * 类描述 :
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 *  
 */
public class NDKDemo {
    static {
        System.loadLibrary("hello");
    }

    public static  native String testString();
}
