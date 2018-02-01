package com.hejin.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hejin.ndk.R;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * author :  贺金龙
 * create time : 2018/1/30 16:10
 * description : 使用LitePal操作相应的数据库基本实现
 */
public class LitePalActivity extends AppCompatActivity {

    private static final String TAG = LitePalActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_pal);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/30 16:14
     * description : 增加的操作
     */
    public void add(View view) {
        LitePalBean zhangSanBean = new LitePalBean("张三", "20", "男");
        zhangSanBean.save();
        LitePalBean liSiBean = new LitePalBean("李四", "25", "男");
        liSiBean.save();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/30 16:14
     * description : 删除的操作
     */
    public void del(View view) {
        /*这里就是删除所有名字是张三的*/
        DataSupport.deleteAll(LitePalBean.class, "name =?", "张三");
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/30 16:15
     * description : 更改数据
     * instructions : 这里是直接更改后sava方法就可以了
     * version :
     */
    public void upData(View view) {
        LitePalBean zhaoWuBean = new LitePalBean("赵五", "25", "男");
        zhaoWuBean.save();
        zhaoWuBean.setAge("30");
        zhaoWuBean.save();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/30 16:15
     * description : 查询
     */
    public void query(View view) {
        List<LitePalBean> all = DataSupport.findAll(LitePalBean.class);
        for (int i = 0; i < all.size(); i++) {
            Log.e(TAG, "query: " + all.get(i).toString());
        }
    }
}
