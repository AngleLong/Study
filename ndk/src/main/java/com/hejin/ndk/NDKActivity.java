package com.hejin.ndk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hejin.study.R;

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

