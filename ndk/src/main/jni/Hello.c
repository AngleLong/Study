//
// Created by hejin on 2018/1/2.
//

#include "com_hejin_ndk_Hello.h"

JNIEXPORT jstring JNICALL Java_com_hejin_ndk_Hello_sayHello
        (JNIEnv *env, jclass jclass1) {
    return (*env)->NewStringUTF(env, "hello java");
}

JNIEXPORT jstring JNICALL
Java_com_hejin_ndk_NDKDemo_testString(JNIEnv *env, jclass type) {

    return (*env)->NewStringUTF(env, "这个是测试用的");
}