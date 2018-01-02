package com.hejin.study;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
