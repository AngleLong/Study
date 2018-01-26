package com.hejin.audio.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/19 15:01
 * 类描述 : Camera相应的工具类
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 */
public class CameraPrivider {
    /**
     * author :  贺金龙
     * create time : 2018/1/18 16:01
     * description : 提供相应的方法
     */
    private static String TAG = CameraPrivider.class.getSimpleName();

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
    public static Camera.Parameters getBaseParameters(Camera.Parameters parameters, int width, int height) {
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

