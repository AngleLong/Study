package com.hejin.service;

// 传递自定义类型参数的AIDL内容
// 这里说明一下:
// 1.这里传递的自定义类型要进行相应的序列化
// 2.这里传进来的自定义类型要进行相应的倒包,并且要在AIDL文件夹中生成以个对应的aidl文件
import com.hejin.service.PersonBean;

interface ICustomAidl {
    //这里传递的自定义类型,但是这里倒包应该是自己手动去写的
    List<PersonBean> add (in PersonBean personBean);
}
