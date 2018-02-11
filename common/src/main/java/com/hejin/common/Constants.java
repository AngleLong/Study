package com.hejin.common;


/**
 * author :  贺金龙
 * create time : 2017/10/26 11:00
 * description : 静态井段存放地址
 * instructions :
 * version :
 */

public class Constants {
    /**
     * author :  贺金龙
     * create time : 2017/10/26 11:01
     * description : 所有Url字段保存位置
     * instructions : 存放所有静态字段保存的位置
     * version :1.0
     */
    public interface UrlPath {
        /**
         * 所有请求的根接口
         */
        String BaseUrl = "http://op.juhe.cn/onebox/basketball/";
        /**
         * NBA常规赛赛程赛果
         */
        String TIMING = "nba?key=09f926b1259390831a0dae5875ef4654";
    }
}