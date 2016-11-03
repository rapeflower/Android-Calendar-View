package com.android.lily.hotel.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rape flower on 2016/5/3.
 */
public class XCalendarView extends View{

    private static final String TAG = "XCalendarView";

    private static final int TOTAL_COL = 7; // 7列
    private static final int TOTAL_ROW = 5; // 5行

    private Context mContext;
    private Paint paint1;
    private Paint paint2;
    private float mViewWidth;// 视图的宽度
    private float mViewHeight;// 视图的高度
    private float mCellWidth;// 单元格宽度
    private int downX = 0;
    private int downY = 0;

    private DateClick dateClick;

    /**
     * 星期字符串数组
     */
    private static String [] weekNames = {"日", "一", "二", "三", "四", "五", "六"};


    public XCalendarView(Context context) {
        super(context);
        init(context);
    }
    public XCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public XCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(Color.parseColor("#999999"));
        paint1.setTextSize(35);

        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setStrokeWidth(2);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(Color.parseColor("#4BD963"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = (float) w;
        mViewHeight = (float) h;
        mCellWidth = mViewWidth / TOTAL_COL;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.w(TAG, "-------------------------> Width = " + getWidth());
        Log.w(TAG, "-------------------------> Height = " + getHeight());
        drawWeekText(canvas);
        drawDateText(canvas);

        canvas.drawText("画文字", 400, 800, paint1);
        measure(canvas);
    }

    /**
     * 绘制头部的星期
     * @param canvas
     */
    private void drawWeekText(Canvas canvas) {
        float x = 0;
        float y = 35;
        float textWidth = paint1.measureText(weekNames[0]);
        for (int i = 0; i < weekNames.length; i++) {
            x = (mCellWidth - textWidth) / 2  + mCellWidth * i;
            canvas.drawText(weekNames[i], x, y, paint1);
        }
    }

    /**
     * 绘制具体的日期
     * @param canvas
     */
    private void drawDateText(Canvas canvas) {
        float constant = 120;
        float x = 0;
        float y = constant;
        int para = 0;
        for (int i = 1; i <= 31; i++) {
            float textWidth = paint1.measureText(String.valueOf(i));
            if (i % 7 >= 0) {
                if (i % 7 == 0) {
                    para = 6;
                } else {
                    para = (i % 7) - 1;
                }
            } else {
                para = i - 1;
            }

            x = (mCellWidth - textWidth) / 2  + mCellWidth * para;
            ///测量字体的高度
            FontMetrics fontMetrics = paint1.getFontMetrics();
            float textHeight = fontMetrics.ascent + fontMetrics.descent;
            if (i > 7 && i % 7 == 1) {
                y = ((i - 1) / 7) * textHeight + ((i - 1) / 7 + 1) * constant;
            }

            canvas.drawText(String.valueOf(i), x, y, paint1);
//            canvas.drawCircle(textWidth / 2 + x, textHeight / 2 + y, 35, paint2);
        }
    }

    /**
     * 测量字体
     */
    private void measure(Canvas canvas) {
        float textWidth = paint1.measureText("画文字");
        FontMetrics fontMetrics = paint1.getFontMetrics();
        float textHeight = fontMetrics.ascent + fontMetrics.descent;
//        float textHeight = fontMetrics.descent - fontMetrics.ascent;
//        Log.i(TAG, "------------------>>>> " + ((textHeight / 2) + 800));
        canvas.drawCircle(textWidth / 2 + 400, textHeight / 2 + 800, (textWidth / 2 + 20), paint2);
//        canvas.drawCircle(200, 200, (textWidth / 2 + 20), paint2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode=  event.getAction();
        switch(eventCode){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }
                break;
        }
        return true;
    }

    private void doClickAction(int x,int y){
//        invalidate();
        //执行activity发送过来的点击处理事件
        if(dateClick != null){
            dateClick.onClickOnDate(x, y);
        }
    }

    /**
     * 设置日期的点击回调事件
     * @author shiwei.deng
     *
     */
    public interface DateClick{
        public void onClickOnDate(int x,int y);
    }

    /**
     * 设置日期点击事件
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }
}
