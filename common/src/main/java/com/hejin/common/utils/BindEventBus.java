package com.hejin.common.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者: 贺金龙 QQ:753355530
 * 项目名称: Study
 * 类名称:com.hejin.common.utils
 * 类描述:
 * 创建时间: 2018/4/3 14:16
 * 修改内容:
 * 修改时间:
 * 修改描述:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindEventBus {
}