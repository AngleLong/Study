package com.hejin.baidu;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.google.gson.Gson;
import com.hejin.baiduAi.R;

import java.io.File;

/**
 * author :  贺金龙
 * create time : 2018/2/6 9:38
 * description : 文字识别的内容
 * 基本上所有百度文字识别的照片都在这里了,囊括了所有的方法,如果有什么不懂了可以查看相应的文档
 * http://ai.baidu.com/docs#/OCR-Android-SDK/1fe5dc4e
 */

public class TextActivity extends AppCompatActivity {

    private static final String TAG = TextActivity.class.getSimpleName();
    private TextView mTv_content;
    private Gson mGson;
    private ImageView mIv_pic;
    // TODO: 2018/2/6 这里如果想测试的话,直接百度找相应的图片进行下载放在相应的根路径就可以了
    //通用文字识别路径
    private String mTextIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/通用文字.jpg";
    //图片识别路径
    private String mPicIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/网络图片.jpg";
    //银行卡识别路径
    private String mCardIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/银行卡.jpg";
    //身份证识别路径
    private String mIdIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/身份证.png";
    //行驶证识别路径
    private String mCarIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/行驶证.jpg";
    //行驶证识别路径
    private String mDriverIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/驾驶证.jpg";
    //车牌识别路径
    private String mCarNumIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/车牌.jpg";
    //营业执照识别路径
    private String mBusinessLicenceIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/营业执照.jpg";
    //通用票据识别路径
    private String mBillIdentificationPath = Environment.getExternalStorageDirectory().getPath() + "/Study/baiduai/票据.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mTv_content = findViewById(R.id.tv_content);
        mIv_pic = findViewById(R.id.iv_pic);
        initData();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 9:54
     * description : 初始化数据
     */
    private void initData() {
        mGson = new Gson();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 9:47
     * description : 通用文字识别
     */
    public void btn_currency(View view) {
        /*设置相应的参数内容,这里主要是设置路径和*/
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);//检测朝向的,这个在说明文档中都有
        File imageFile = new File(mTextIdentificationPath);

        Log.e(TAG, "btn_currency: " + imageFile.getPath());
        param.setImageFile(imageFile);
        setImagePic(imageFile);

        OCR.getInstance().recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                Log.e(TAG, "onResult: " + generalResult.getJsonRes());
                String result = resultData(generalResult);
                mTv_content.setText(result);
            }

            @Override
            public void onError(OCRError ocrError) {
                Log.e(TAG, "onError: " + ocrError.toString());
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 11:02
     * description : 图片识别
     */
    public void btn_pic(View view) {
        // 网络图片文字识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        File picFile = new File(mPicIdentificationPath);
        param.setImageFile(picFile);

        setImagePic(picFile);

        // 调用通用文字识别服务
        OCR.getInstance().recognizeWebimage(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                Log.e(TAG, "onResult: " + result.getJsonRes());
                String resultData = resultData(result);
                mTv_content.setText(resultData);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 11:18
     * description : 银行卡识别
     * instructions :
     * version :
     */
    public void btn_card(View view) {
        // 银行卡识别参数设置
        BankCardParams param = new BankCardParams();
        File cardFile = new File(mCardIdentificationPath);
        param.setImageFile(cardFile);

        setImagePic(cardFile);

        // 调用银行卡识别服务
        OCR.getInstance().recognizeBankCard(param, new OnResultListener<BankCardResult>() {
            @Override
            public void onResult(BankCardResult result) {
                Log.e(TAG, "onResult: " + result.getBankCardNumber() + result.getBankName());
                mTv_content.setText("银行名称:" + result.getBankName() + "\n银行卡号:" + result.getBankCardNumber());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 11:33
     * description : 身份证识别
     */
    public void btn_ID(View view) {
        // 身份证识别参数设置
        IDCardParams param = new IDCardParams();
        File idFile = new File(mIdIdentificationPath);
        param.setDetectDirection(true);
        //以下内容是必须添加的内容,从而确保能正确打印相应的结果
        param.setIdCardSide(IDCardParams.ID_CARD_SIDE_FRONT);
        param.setImageQuality(20);
        param.setImageFile(idFile);

        setImagePic(idFile);

        // 调用身份证识别服务
        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                Log.e(TAG, "onResult: " + result.getJsonRes());
                mTv_content.setText("姓名:" + result.getName()
                        + "\n性别:" + result.getGender()
                        + "\n出生年月:" + result.getBirthday()
                        + "\n住址:" + result.getAddress()
                        + "\n身份证号码:" + result.getIdNumber());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                Log.e(TAG, "onError: " + error.toString());
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 15:55
     * description :  行驶证识别
     */
    public void btn_car(View view) {
        OcrRequestParams param = new OcrRequestParams();
        File carFile = new File(mCarIdentificationPath);
        param.setImageFile(carFile);
        //这个参数是必需传的,指的是图像是正常方向
        param.putParam("detect_direction", true);

        setImagePic(carFile);

        // 调用行驶证识别服务
        OCR.getInstance().recognizeVehicleLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                Log.e(TAG, "onResult: " + result.getJsonRes());
                mTv_content.setText(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 16:09
     * description : 驾驶证识别
     */
    public void btn_driver(View view) {
        // 驾驶证识别参数设置
        OcrRequestParams param = new OcrRequestParams();

        File driver = new File(mDriverIdentificationPath);
        param.setImageFile(driver);
        param.putParam("detect_direction", true);

        setImagePic(driver);

        // 调用驾驶证识别服务
        OCR.getInstance().recognizeDrivingLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                Log.e(TAG, "onResult: " + result.getJsonRes());
                mTv_content.setText(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 16:15
     * description : 车牌识别
     */
    public void btn_carNum(View view) {
        // 车牌识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        File carNumFile = new File(mCarNumIdentificationPath);
        // 设置image参数
        param.setImageFile(carNumFile);

        setImagePic(carNumFile);

        // 调用车牌识别服务
        OCR.getInstance().recognizeLicensePlate(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                Log.e(TAG, "onResult: " + result.getJsonRes());
                mTv_content.setText(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 16:21
     * description : 营业执照识别
     */
    public void btn_businessLicence(View view) {
        // 营业执照识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        File businessLicenceFile = new File(mBusinessLicenceIdentificationPath);

        // 设置image参数
        param.setImageFile(businessLicenceFile);

        setImagePic(businessLicenceFile);

        // 调用营业执照识别服务
        OCR.getInstance().recognizeBusinessLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                Log.e(TAG, "onResult: " + result.getJsonRes());
                mTv_content.setText(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {

            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 16:25
     * description : 通用票据
     */
    public void btn_bill(View view) {
        // 通用票据识别参数设置
        OcrRequestParams param = new OcrRequestParams();

        File billFile = new File(mBillIdentificationPath);

        // 设置image参数
        param.setImageFile(billFile);

        setImagePic(billFile);

        // 设置额外参数
        param.putParam("detect_direction", true);

        // 调用通用票据识别服务
        OCR.getInstance().recognizeReceipt(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                Log.e(TAG, "onResult: " + result.getJsonRes());
                mTv_content.setText(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 10:04
     * description : 设置图片资源到相应的ImageView中去
     */
    public void setImagePic(File imagePic) {
        if (null != imagePic && imagePic.exists())
            mIv_pic.setImageBitmap(BitmapFactory.decodeFile(imagePic.getPath()));
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/6 11:09
     * description : 处理相应的返回结果
     */
    public String resultData(GeneralResult generalResult) {
        StringBuffer stringBuffer = new StringBuffer();
        WordsBean wordsBean = mGson.fromJson(generalResult.getJsonRes(), WordsBean.class);
        for (int i = 0; i < wordsBean.getWords_result().size(); i++) {
            String words = wordsBean.getWords_result().get(i).getWords();
            stringBuffer.append(words + "\n");
        }

        return stringBuffer.toString();
    }
}
