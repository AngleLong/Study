package com.hejin.common.network;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/11 7:53
 * 类描述 : 这个是所有网络请求的基类
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 : 一般在所有网络请求中都应该有相应的基类设置,这样便于数据的处理
 */
public class BaseBean<T> {
    private String message;
    private String code;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
