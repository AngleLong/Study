package com.hejin.study;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/*
* Error:Execution failed for task ':app:compileDebugNdk'.
> Error: Flag android.useDeprecatedNdk is no longer supported and will be removed in the next version of Android Studio.  Please switch to a supported build system.
  Consider using CMake or ndk-build integration. For more information, go to:
   https://d.android.com/r/studio-ui/add-native-code.html#ndkCompile
   To get started, you can use the sample ndk-build script the Android
   plugin generated for you at:
   D:\Temp\Study\app\build\intermediates\ndk\debug\Android.mk
  Alternatively, you can use the experimental plugin:
   https://developer.android.com/r/tools/experimental-plugin.html
  To continue using the deprecated NDK compile for another 60 days, set
  android.deprecatedNdkCompileLease=1514865236145 in gradle.properties
* */

/*
* Error:Execution failed for task ':app:compileDebugNdk'.
> Error: Your project contains C++ files but it is not using a supported native build system.
  Consider using CMake or ndk-build integration. For more information, go to:
   https://d.android.com/r/studio-ui/add-native-code.html
  Alternatively, you can use the experimental plugin:
   https://developer.android.com/r/tools/experimental-plugin.html*/

/*Reports native method declarations in Java where no corresponding JNI function is found in the project.*/
public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/30 21:29
     * description : 关于录音问题的总结
     */
    public void video(View view) {
    }
}
