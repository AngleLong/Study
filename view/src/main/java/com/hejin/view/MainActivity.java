package com.hejin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hejin.study.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 17:57
     * description : 操作View中Canvas的一些方法
     */
    public void canvasOperate(View view) {
        startActivity(new Intent(this, View1Activity.class));
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 17:58
     * description : ProgressBar处理圆角的一些知识
     */
    public void progressBarOperate(View view) {
        startActivity(new Intent(this, ProgressBarActivity.class));
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 17:58
     * description : 自定义分段的View
     */
    public void customOperate(View view) {
        startActivity(new Intent(this, SegmentationActivity.class));
    }
}
