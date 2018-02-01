package com.hejin.audio.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.List;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/18 15:43
 * 类描述 : 自定义相机View
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private String TAG = CameraView.class.getSimpleName();
    private Camera mCamera;
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private PictureListener mPictureListener;//拍照相应的回调
    /**
     * author :  贺金龙
     * create time : 2018/1/19 14:12
     * description : 相应拍照的回调
     */
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            /*data是相应压缩后的图片*/
             /*这里就是通过相应的流进行图片处理了*/
             /*http://blog.csdn.net/harryweasley/article/details/51955467*/
            Bitmap pictureBitmap = byteToBitmap(data);
            if (mPictureListener != null) {
                mPictureListener.takePictureSuccess(pictureBitmap);
            }
        }
    };

    /**
     * author :  贺金龙
     * create time : 2018/1/19 13:27
     * description : 根据相应的数组进行压缩
     */
    private Bitmap byteToBitmap(byte[] data) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            /*处理图片旋转的问题*/
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Bitmap转换异常");
        }
        return bitmap;
    }

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        init();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/18 15:44
     * description : 初始化一些参数
     */
    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setOnTouchListener(this);//设置Touch的回调
    }


    //------------------相应的回调-----------------//
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        /*
          * 先释放相应的资源
          * 1.打开摄像头
          *     1.判断是否有相机功能
          *     2.判断使用相应的摄像头
          * 2.设置相应的参数(不应该在这里,这个最好在拍照的时候设置)
          * 3.关联相应的Holder
          * 4.预览
          */
        initPreviewCamera(holder);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/18 16:57
     * description : 释放相应的Camera的资源
     * 因为SurfaceView可能存在相应的重建
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();/*取消取景功能*/
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/18 15:54
     * description : 初始化相应的摄像头
     *
     * @param holder 相应的SurfaceHolder
     */
    private void initPreviewCamera(SurfaceHolder holder) {
        /*1.判断是否有相机功能*/
        if (!CameraViewProvider.isCameraHardware(getContext())) {
            Log.e(TAG, "没有可用的摄像头");
            return;
        }

        /*2.判断相应的摄像头,开始预览*/
        mCamera = CameraViewProvider.getUsingCamera(mContext);
        if (!startCameraPreview(holder)) {
            Log.e(TAG, "预览失败");
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/18 16:48
     * description : 设置预览的方法
     *
     * @param holder 相应的SurfaceHolder
     */
    private boolean startCameraPreview(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = CameraViewProvider.getUsingCamera(mContext);
        }
        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = CameraViewProvider.getBaseParameters(mCamera.getParameters(),
                    CameraViewProvider.getScreenMetrics(mContext).x,
                    CameraViewProvider.getScreenMetrics(mContext).y);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "开启预览的时候失败");
            return false;
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //这句话很重要之前总是在这里出问题
        mCamera.stopPreview();
        startCameraPreview(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void onPause() {
        releaseCamera();
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/18 16:01
     * description : 提供相应的方法
     */
    private static class CameraViewProvider {
        private static String TAG = CameraViewProvider.class.getSimpleName();

        /**
         * author :  贺金龙
         * create time : 2018/1/18 16:00
         * description : 是否能选择相应的摄像头
         *
         * @param facing 相应的摄像头的正反面
         *               Camera.CameraInfo.CAMERA_FACING_BACK 后面的摄像头
         *               Camera.CameraInfo.CAMERA_FACING_FRONT 前面的摄像头
         */
        public static boolean checkCameraFacing(int facing) {
            //获取相应的摄像头的数量
            int numberOfCameras = Camera.getNumberOfCameras();
            //获取相应照相机的信息
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            for (int i = 0; i < numberOfCameras; i++) {
                //返回摄像头相关的信息,保存在CameraInfo中
                Camera.getCameraInfo(i, cameraInfo);
                if (facing == cameraInfo.facing) {
                    return true;
                }
            }
            return false;
        }

        /**
         * author :  贺金龙
         * create time : 2018/1/18 16:20
         * description : 是否可以使用相应的摄像头
         */
        public static boolean isCameraHardware(Context context) {
            if (context != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                return true;
            }
            return false;
        }

        /**
         * author :  贺金龙
         * create time : 2018/1/18 16:25
         * description : 获取可以使用的摄像头(如果有)
         */
        public static Camera getUsingCamera(Context context) {
            Camera camera = null;
            try {
                if (isCameraHardware(context)) {
                    if (checkCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)) {
                        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    } else {
                        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "打开相应的摄像头失败");
            }
            return camera;
        }

        /**
         * author :  贺金龙
         * create time : 2018/1/18 17:59
         * description : 获取最佳的屏幕预览尺寸
         */
        private static Camera.Parameters getBaseParameters(Camera.Parameters parameters, int width, int height) {
            //http://www.xuebuyuan.com/1052006.html
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            //设置相应的数组宽度和高度的数组
            int[] a = new int[previewSizes.size()];
            int[] b = new int[previewSizes.size()];

            //遍历出所有预设的尺寸数据,与支持的宽高进行比较
            for (int i = 0; i < previewSizes.size(); i++) {
                int supportH = previewSizes.get(i).height;
                int supportW = previewSizes.get(i).width;
                //获取和支持的宽高相差的距离
                a[i] = Math.abs(supportW - height);
                b[i] = Math.abs(supportH - width);
                Log.d(TAG, "supportW:" + supportW + "supportH:" + supportH);
            }

            int minW = 0, minA = a[0];//相应的角标和数值
            /*通过遍历获取相应最小的宽度角标和最小的宽度数值*/
            for (int i = 0; i < a.length; i++) {
                if (a[i] <= minA) {
                    minW = i;
                    minA = a[i];
                }
            }
            /*通过遍历获取相应最小的高度角标和最小的高度数值*/
            int minH = 0, minB = b[0];
            for (int i = 0; i < b.length; i++) {
                if (b[i] < minB) {
                    minH = i;
                    minB = b[i];
                }
            }

            List<int[]> list = parameters.getSupportedPreviewFpsRange();
            // 设置预览图像大小
            parameters.setPreviewSize(previewSizes.get(minW).width, previewSizes.get(minH).height);
            parameters.setPreviewFpsRange(list.get(list.size() - 1)[0], list.get(list.size() - 1)[1]);
            return parameters;
        }

        /**
         * author :  贺金龙
         * create time : 2018/1/19 14:19
         * description : 获取相应的屏幕宽高的值
         */
        public static Point getScreenMetrics(Context context) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int w_screen = dm.widthPixels;
            int h_screen = dm.heightPixels;
            return new Point(w_screen, h_screen);
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/19 12:52
     * description : 拍照相应的设置
     * instructions :
     * version :
     */
    public interface PictureListener {
        /**
         * author :  贺金龙
         * create time : 2018/1/19 12:53
         * description : 拍照成功的回调
         */
        void takePictureSuccess(Bitmap bitmap);
    }

    //-----------------------------------------------对外提供的方法-----------------------------------------------//

    /**
     * author :  贺金龙
     * create time : 2018/1/19 9:33
     * description : 拍照的方法,通过相应的回调获取相应的Bitmap
     */
    public void takePicture() {
        /*参数的一些设置*/
        Camera.Parameters parameters = mCamera.getParameters();
        /*格式*/
        parameters.setPictureFormat(ImageFormat.JPEG);
        /*设置相应的尺寸*/
        parameters.setPreviewSize(CameraViewProvider.getScreenMetrics(mContext).x, CameraViewProvider.getScreenMetrics(mContext).y);
        /*对焦,这里设置的是自动对焦*/
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        /*对焦最准确的时候的回调*/
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    /*这里参数的说明
                    * 1.捕获的瞬间的回调,适合做声音处理
                    * 2.原始未被压缩的图片回调
                    * 3.压缩后的图片回调
                    */
                    mCamera.takePicture(null, null, mPictureCallback);
                }
            }
        });
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/19 13:45
     * description : 设置相应的拍照回调
     */
    public void setPictureListener(PictureListener pictureListener) {
        mPictureListener = pictureListener;
    }
}
