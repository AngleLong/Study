package com.hejin.common.bean;

/**
 * 作者: 贺金龙 QQ:753355530
 * 项目名称: Study-master
 * 类名称:com.hejin.common.bean
 * 类描述: 这里是设置了一个相应的泛型这里只是写了一个基类，到时候直接维护相应的实体就可以了
 * 创建时间: 2018/4/3 13:44
 * 修改内容:
 * 修改时间:
 * 修改描述:
 */
public class EventBean<T> {
    private int code;
    private T data;

    public EventBean(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
