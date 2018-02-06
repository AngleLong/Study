package debug;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.hejin.common.BaseApp;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/5 9:57
 * 类描述 :
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 *  
 */
public class BaiduApp extends BaseApp {
    private String mBaiduToken;

    @Override
    public void onCreate() {
        super.onCreate();

        initBaiduApi();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/5 17:44
     * description : 初始化百度文字识别的API
     */
    private void initBaiduApi() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                mBaiduToken = accessToken.getAccessToken();
            }

            @Override
            public void onError(OCRError ocrError) {

            }
        }, this);
    }


    /**
     * author :  贺金龙
     * create time : 2018/2/5 17:45
     * description : 返回百度的token内容
     */
    public String getBaiduToken() {
        return mBaiduToken;
    }
}
