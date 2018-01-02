package com.hejin.ndk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hejin.study.R;

//http://blog.csdn.net/xiaozhu0922/article/details/78835144
//http://blog.csdn.net/sw5131899/article/details/77197838
//http://blog.csdn.net/sweettool/article/details/71436575   解决下面那个问题
/*
* Error:Execution failed for task ':ndk:compileDebugNdk'.
> Error: Your project contains C++ files but it is not using a supported native build system.
  Consider using CMake or ndk-build integration. For more information, go to:
   https://d.android.com/r/studio-ui/add-native-code.html
  Alternatively, you can use the experimental plugin:
   https://developer.android.com/r/tools/experimental-plugin.html
* */
/*
* Error:Execution failed for task ':ndk:compileDebugNdk'.
> Error: Flag android.useDeprecatedNdk is no longer supported and will be removed in the next version of Android Studio.  Please switch to a supported build system.
  Consider using CMake or ndk-build integration. For more information, go to:
   https://d.android.com/r/studio-ui/add-native-code.html#ndkCompile
   To get started, you can use the sample ndk-build script the Android
   plugin generated for you at:
   D:\Temp\Study\ndk\build\intermediates\ndk\debug\Android.mk
  Alternatively, you can use the experimental plugin:
   https://developer.android.com/r/tools/experimental-plugin.html
  To continue using the deprecated NDK compile for another 60 days, set
  android.deprecatedNdkCompileLease=1514829916803 in gradle.properties
* */

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/1 22:37
 * 类描述 : 封装C语言的代码的类,这里应该能直接调用相应的JNI的方法
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 *  
 */

/**
 * author :  贺金龙
 * create time : 2018/1/1 22:39
 * description : 这里调用的是C语言的代码
 * instructions : 这里的逻辑是这样的->
 * 1.生成相应的头文件(也就是.h文件)
 * 命令说明:ls 显示当前文件夹下的所有文件
 * cd xxx/ 切换到xxx文件下
 * clean 清除显示
 * ../上一层文件夹下
 * javah -d ../jni(文件夹名称) 包名.类名(这里创建的jni文件夹和java文件夹是同级的目录)
 * 2.clean project一下
 * 3.build.gradle中配置ndk的使用
 * 4.最后在本类中创建静态方法,使用相应的代码
 */

public class NDKActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk);
        tv = findViewById(R.id.tv);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/1 22:37
     * description : java调用C语言的代码
     */
    public void javaCallC(View view) {
        tv.setText(Hello.sayHello());
    }
}

