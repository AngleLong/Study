package com.hejin.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hejin.study.R;


/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/1/31 18:58
 * 类描述 : 分段任务的自定义View
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 : 这里需要那些自定义的属性
 * 1.圆环的半径,这里取的是宽度的最大值作为圆环的直径
 * 2.圆环的颜色
 * 1.完成的颜色
 * 2.未完成的颜色
 * 3.中间设置的图片
 * 4.线段中间间隔的度数
 * 5.线段的总数
 */
public class SegmentationTaskView extends View {
    private static final String TAG = SegmentationTaskView.class.getSimpleName();
    private static final boolean isDebug = true;
    private static final float mTotalAngle = 360f;//一共的角度

    /*一些默认的字段*/
    private String mDefaultHighColor = "#334455";//线段的高亮颜色
    private String mDefaultDarkColor = "#00ff00";//默认的线段的颜色
    private float mCircleRadius = 200;//圆弧的半径
    private float INTERVALDEGREE = 10.0f;/*线段中间间隔的度数*/

    private float startAngle = 0f;//开始角度
    private int mHighColor;//高亮的线段颜色
    private int mDarkColor;//暗色的线段颜色(线段的背景颜色)
    private int mTotalNum;//线段的总数
    private int mHighRes;//高亮显示的图片
    private int mDarkRes;//未高亮的图片
    private int mCurrentSegmentNum;//当前高亮的线段数量,默认是0个
    private float mRadiusWidth;//圆环的宽度,这里是设置笔触宽度的

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mBackGroundRectF = new RectF();/*绘制的矩形大小,这个矩形的大小和测量有关*/

    public SegmentationTaskView(Context context) {
        this(context, null);
    }

    public SegmentationTaskView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentationTaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 10:50
     * description : 初始化画笔
     */
    private void initPaint() {
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mRadiusWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * author :  贺金龙
     * create time : 2018/1/31 19:00
     * description : 初始化传入的参数
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SegmentationTaskView);
        mHighColor = typedArray.getColor(R.styleable.SegmentationTaskView_highColor, Color.parseColor(mDefaultHighColor));
        mDarkColor = typedArray.getColor(R.styleable.SegmentationTaskView_darkColor, Color.parseColor(mDefaultDarkColor));
        mTotalNum = typedArray.getInteger(R.styleable.SegmentationTaskView_totalSegment, 0);
        mHighRes = typedArray.getResourceId(R.styleable.SegmentationTaskView_highRes, R.mipmap.weekly_read_select);
        mDarkRes = typedArray.getResourceId(R.styleable.SegmentationTaskView_darkRes, R.mipmap.weekly_read_unselect);
        mCurrentSegmentNum = typedArray.getInteger(R.styleable.SegmentationTaskView_currentSegmentNum, 0);
        mRadiusWidth = typedArray.getDimension(R.styleable.SegmentationTaskView_radiusWidth, 20);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int currentWidth;
        int currentHeight;

        if (MeasureSpec.EXACTLY == widthMode) {//真实高度
            currentWidth = width;
        } else {
            currentWidth = (int) (mCircleRadius * 2 + getPaddingLeft() + getPaddingRight() + mRadiusWidth);
        }

        if (MeasureSpec.EXACTLY == heightMode) {
            currentHeight = height;
            if (isDebug) {
                Log.e(TAG, "onMeasure: " + mCircleRadius);
            }
        } else {
            currentHeight = (int) (mCircleRadius * 2 + getPaddingTop() + getPaddingBottom() + mRadiusWidth);
        }

        /*绘制圆弧的半径*/
        mCircleRadius = Math.min(currentHeight, currentWidth) / 2 - mRadiusWidth / 2;
        setMeasuredDimension(currentWidth, currentHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRes(canvas);
        drawDarkCircle(canvas);
        drawSegment(canvas);
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 10:45
     * description : 绘制暗色的圆弧
     */
    private void drawDarkCircle(Canvas canvas) {
        mPaint.setColor(mDarkColor);

        /*设置矩形位置*/
        float left = getPaddingLeft() + mRadiusWidth / 2;
        float top = getPaddingTop() + mRadiusWidth / 2;
        float right = left + mCircleRadius * 2 - mRadiusWidth / 2;
        float bottom = top + mCircleRadius * 2 - mRadiusWidth / 2;
        mBackGroundRectF.set(left, top, right, bottom);

        /*绘制默认的圆弧*/
        canvas.drawArc(mBackGroundRectF, startAngle, mTotalAngle, false, mPaint);
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 11:02
     * description : 绘制亮色线段
     */
    private void drawSegment(Canvas canvas) {
        mPaint.setColor(mHighColor);

        if (mCurrentSegmentNum > 0) {
            startAngle = 0;
            /*设置每一个角度的度数*/
            float angle = (mTotalAngle - mTotalNum * INTERVALDEGREE) / mTotalNum;

            for (int i = 0; i < mCurrentSegmentNum; i++) {
                canvas.drawArc(mBackGroundRectF, startAngle, angle, false, mPaint);
                startAngle += angle + INTERVALDEGREE;
                if (isDebug)
                    Log.e(TAG, "onDraw: " + startAngle);
            }
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 11:09
     * description : 绘制相应的资源
     * 这里应该规定一个矩形进行像是相应的图片
     */
    private void drawRes(Canvas canvas) {
        if (mCurrentSegmentNum >= mTotalNum) {//高亮的图片
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mHighRes);
            /*获取中心位置*/
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            Log.e(TAG, "drawRes: " + centerX + "-------" + centerY);
            centerX = centerX - bitmap.getWidth() / 2;
            centerY = centerY - bitmap.getHeight() / 2;
            canvas.drawBitmap(bitmap, centerX, centerY, mPaint);
        } else {//非高亮的图片
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mDarkRes);
            /*获取中心位置*/
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            centerX = centerX - bitmap.getWidth() / 2;
            centerY = centerY - bitmap.getHeight() / 2;
            canvas.drawBitmap(bitmap, centerX, centerY, mPaint);
        }
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 14:27
     * description : 设置相应的高亮段数
     */
    public void setCurrentSegmentNum(int currentSegmentNum) {
        if (currentSegmentNum >= mTotalNum) {
            currentSegmentNum = mTotalNum;
        }
        mCurrentSegmentNum = currentSegmentNum;
        invalidate();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 14:28
     * description : 设置相应的总段数
     */
    public void setTotalNum(int totalNum) {
        mTotalNum = totalNum;
        invalidate();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 14:32
     * description : 设置相应的高亮的资源图片
     */
    public void setHighRes(int highRes) {
        mHighRes = highRes;
        invalidate();
    }

    /**
     * author :  贺金龙
     * create time : 2018/2/1 14:33
     * description : 设置相应的不高亮图片
     */
    public void setDarkRes(int darkRes) {
        mDarkRes = darkRes;
        invalidate();
    }
}
