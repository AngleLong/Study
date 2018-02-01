#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_hejin_ndk_Hello_sayHello(JNIEnv *env, jclass type) {
    return (*env)->NewStringUTF(env, "你好C语言");
}