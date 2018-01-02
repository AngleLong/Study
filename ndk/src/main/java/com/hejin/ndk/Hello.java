package com.hejin.ndk;


public class Hello {
    static {
        System.loadLibrary("hello");
    }

    public static native String sayHello();
}
