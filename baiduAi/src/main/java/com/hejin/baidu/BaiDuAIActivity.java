package com.hejin.baidu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hejin.baiduAi.R;

/**
 * author :  贺金龙
 * create time : 2018/2/6 9:50
 * description : 百度API的一些功能
 */
public class BaiDuAIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baiduai);
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/5 17:47
     * description : 文字识别的点击事件
     */
    public void btnClick(View view) {
        startActivity(new Intent(this, TextActivity.class));
    }
}
