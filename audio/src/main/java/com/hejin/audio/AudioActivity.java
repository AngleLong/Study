package com.hejin.audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hejin.study.R;
/**
 * 欠缺的内容:
 * 1.文件的读写操作
 * 2.关于文件的操作
 * 3.相应的博客
 * 4.异常的处理
 * */
public class AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/30 23:21
     * description : 文件模式
     */
    public void btnFile(View view) {
        startActivity(new Intent(this, FileActivity.class));
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/30 23:21
     * description : 字节流模式
     */
    public void btnByte(View view) {
        startActivity(new Intent(this, ByteActivity.class));
    }
}
