package com.hejin.study.audio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hejin.study.R;

/**
 * author :  贺金龙
 * create time : 2017/12/30 21:30
 * description : 音频处理的首页
 */
public class AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
    }

    /**
     * author :  贺金龙
     * create time : 2017/12/30 21:35
     * description : 文件模式录制视频
     */
    public void btn_file(View view) {

    }

    /**
     * author :  贺金龙
     * create time : 2017/12/30 21:35
     * description : 字节流模式录制视频
     */
    public  void btn_byte(View view){

    }
}
