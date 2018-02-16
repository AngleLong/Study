package com.hejin.file;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hejin.ndk.R;

import java.io.File;
import java.io.IOException;

public class FileActivity extends AppCompatActivity {

    private static final String TAG = FileActivity.class.getSimpleName();
    //内存卡的根路径
    private String BasePath = Environment.getExternalStorageDirectory().getPath() + "/AStudy/";
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        mFile = new File(BasePath, "a.txt");
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/2 17:39
     * description : 创建文件
     */
    public void createFile(View view) {

        /*这里逻辑是这样的:
        * 1. 获取相应文件的父文件的路径,然后在其下面创建相应的文件
        * 这里要区分相应的API
        * createNewFile()->创建相应的文件,这个是具体文件不是文件夹
        * mkdirs()创建具体的文件夹,这个只是根据文件夹的路径创建相应的路径文件夹
        * mkdir()在指定目录下创建文件夹
        * isFile()是否是文件 true文件 false文件夹
        *
        * */
        if (!mFile.exists()) {/*文件是否存在*/
            try {
                File parentFile = mFile.getParentFile();
                if (!parentFile.exists()) {
                    boolean isExist = parentFile.mkdirs();
                    if (isExist) {
                        mFile.createNewFile();
                    }
                } else {
                    mFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "createFile: 创建文件失败" + e);
            }
        }
    }


    /**
     * author :  贺金龙
     * create time : 2018/1/2 17:51
     * description : 删除文件
     */
    public void delFile(View view) {
        /*这里应该区分两个API
        * delete();删除文件,不管文件是否存在,立刻执行
        * deleteOnExit();当jvm结束的时候会执行,这里相当与一个声明.这里通常是对临时文件的操作
        * */
        if (mFile.exists()) {
            mFile.delete();
            Log.e(TAG, "delFile: 删除文件成功");
        }
    }
}
