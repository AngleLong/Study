package com.hejin.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hejin.ndk.R;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * author :  贺金龙
 * create time : 2018/1/26 14:34
 * description : 实现断点续传的内容
 * instructions : 这里会用到的知识点
 * 1.activity像Service传参数
 * <p>
 * 这里面断点续传的逻辑:
 * 1.通过Service进行参数的传递
 * 2.LitePal 操作数据库
 */
public class MainActivity extends AppCompatActivity {

    private TextView mTvDLProgress;//显示下载进度的textView
    private ProgressBar mPb;//显示进度的Prigress
    private Button mBtnDL;//下载
    private Button mBtnPause;//暂停
    private FileInfo mFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initInfo();//初始化相应下载的文件
    }

    private void initView() {
        mTvDLProgress = findViewById(R.id.tv_progress);
        mPb = findViewById(R.id.pb);
        mBtnDL = findViewById(R.id.btn_download);
        mBtnPause = findViewById(R.id.btn_pause);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/26 14:55
     * description : 初始化相应的下载的文件
     * instructions :
     * version :
     */
    private void initInfo() {
//        /*创建文件的信息对象*/
//        mFileInfo = new FileInfo(
//                0,
//                "https://www.baidu.com/link?url=-3zekbEWCOeN_M8Q8UWnn0VQ_vdXbQcx625V2yOp8FpSI8KdpN-bwxUu-P0W671c2b3i1nl9nBzBKFUsE9OmKhT4CNhPTzx9gcoKFz4KXHi&wd=&eqid=b28ed00f000084ec000000065a6ad135",
//                "kugou_8.2.4.20449_setup.exe",
//                0, 0
//        );
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/26 14:54
     * description :  下载按钮的点击事件
     */
    public void downLoad(View view) {
//        Intent intent = new Intent(this, DownloadService.class);
//        intent.setAction(DownloadService.ACTION_START);
//        intent.putExtra("fileInfo", mFileInfo);
//        startService(intent);
        /*将数据内容保存到相应的数据库中*/
        mFileInfo = new FileInfo();
        mFileInfo.setFileName("文件名称");
        mFileInfo.setFinished(20);
        mFileInfo.setLength(200);
        mFileInfo.setUrl("这是一个URL");
        mFileInfo.save();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/26 14:54
     * description : 暂停下载,也就是相应的结束下载
     */
    public void pause(View view) {
//        Intent intent = new Intent(this, DownloadService.class);
//        intent.setAction(DownloadService.ACTION_STOP);
//        intent.putExtra("fileInfo", mFileInfo);
//        startService(intent);

        List<FileInfo> fileInfos = DataSupport.select("fileName").find(FileInfo.class);
        mTvDLProgress.setText(fileInfos.get(0).getFileName());
    }
}
