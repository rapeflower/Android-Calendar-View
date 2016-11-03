package com.android.lily.hotel.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.lily.hotel.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

/***********
 *
 * @Author rape flower
 * @Date 2016-11-02 10:14
 * @Describe 月视图
 *
 */
public class MonthView extends View{

    private static final String TAG = "MonthView";
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mPaint;
    private int mDayColor = Color.parseColor("#000000");
    private int mSelectDayColor = Color.parseColor("#ffffff");
    private int mSelectBGColor = Color.parseColor("#1FC2F3");
    private int mSlideColor = Color.parseColor("#ff0000");
    private int mCurrentColor = Color.parseColor("#03b621");
    private int mCurrYear;
    private int mCurrMonth;
    private int mCurrDay;
    private int mSelYear;
    private int mSelMonth;
    private int mSelDay;
    private int mColumnSize,mRowSize;
    private DisplayMetrics mDisplayMetrics;
    private int mDaySize = 12;
    private int weekRow;
    private int [][] daysString;
    private int mCircleRadius = 6;
    private DateClick dateClick;
    private int mCircleColor = Color.parseColor("#ff0000");
    private List<Integer> daysHasThingList;
    private String [] states = new String[] {"入店", "离店"};

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        //日期数组
        daysString = new int[6][7];
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initSize();
        String dayString;
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
//        Log.d("DateView", "mMonthDays = " + mMonthDays +"，weekNumber = " + weekNumber);
        for(int day = 0; day < mMonthDays; day++){
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column]= day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString))/2);
            int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);

            //选中的日期
            if(dayString.equals(mSelDay+"")){
                //绘制背景色圆形
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                mPaint.setColor(mSelectBGColor);

                /*画圆*/
                float cx = (startRecX + endRecX) / 2;
                float cy = (startRecY + endRecY) / 2;
                float radius = mRowSize / 3;
                canvas.drawCircle(cx, cy, radius, mPaint);
                //入住
                mPaint.setColor(Color.parseColor("#303F9F"));
                canvas.drawText(states[1], cx - radius, endRecY + (radius / 3), mPaint);

                //记录第几行，即第几周
                weekRow = row + 1;
            }
            //绘制事务圆形标志
            drawCircle(row, column, day + 1,canvas);
            if(dayString.equals(mSelDay+"")){
                mPaint.setColor(mSelectDayColor);
            }else if(dayString.equals(mCurrDay+"") && mCurrDay != mSelDay && mCurrMonth == mSelMonth){
                //正常月，选中其他日期，则今日为红色
                mPaint.setColor(mCurrentColor);
            } else if (column == 0 || column == 6) {
                mPaint.setColor(mSlideColor);
            } else if ((day + 1) < mCurrDay) {//今天之前的日期
                mPaint.setColor(Color.parseColor("#33000000"));
            } else{
                mPaint.setColor(mDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint);
        }
    }

    private void drawCircle(int row,int column,int day,Canvas canvas){
        if(daysHasThingList != null && daysHasThingList.size() >0){
            if(!daysHasThingList.contains(day))return;
            mPaint.setColor(mCircleColor);
            float circleX = (float) (mColumnSize * column + mColumnSize*0.8);
            float circley = (float) (mRowSize * row + mRowSize*0.2);
            canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0,downY = 0;
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
                    int x = (upX + downX) / 2;
                    int y = (upY + downY) / 2;
                    if (isEffectiveDay(x, y)) {
                        performClick();
                        doClickAction(x, y);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize(){
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     * @param year
     * @param month
     */
    public void setSelectYearMonth(int year,int month,int day){
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 重新设置当前选中的日期
     * @param day
     */
    public void setSelDay(int day) {
        this.mSelDay = day;
    }

    /**
     * 执行点击事件
     * @param x
     * @param y
     */
    private void doClickAction(int x,int y){
        int row = y / mRowSize;
        int column = x / mColumnSize;
        int day = daysString[row][column];
        setSelectYearMonth(mSelYear,mSelMonth, day);
        Log.w(TAG, "-----------------> daysString = " + daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if(dateClick != null){
            dateClick.onClickOnDate(this, day);
        }
    }

    /**
     * 判断某个日期是不是今天之前的日期
     * @return true 不是 false 是
     */
    private boolean isEffectiveDay(int x,int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        int day = daysString[row][column];
        if (day == 0 && mCurrDay == 0) {
            return false;
        }
        if (day < mCurrDay) {
            return false;
        }
        return true;
    }

    /**
     * 设置当前年份
     * @param currYear
     */
    public void setCurrYear(int currYear) {
        this.mCurrYear = currYear;
    }

    /**
     * 设置当前月份
     * @param currMonth
     */
    public void setCurrMonth(int currMonth) {
        this.mCurrMonth = currMonth;
    }

    /**
     * 设置当前日
     * @param currDay
     */
    public void setCurrDay(int currDay) {
        this.mCurrDay = currDay;
    }

    /**
     * 获取选择的日期
     * @param
     */
    public int getSelDay() {
        return this.mSelDay;
    }
    /**
     * 普通日期的字体颜色，默认黑色
     * @param mDayColor
     */
    public void setDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     * @param mSelectDayColor
     */
    public void setSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     * @param mSelectBGColor
     */
    public void setSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }
    /**
     * 当前日期不是选中的颜色，默认红色
     * @param mCurrentColor
     */
    public void setCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     * @param mDaySize
     */
    public void setDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    /**
     * 设置事务天数
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * 设置圆圈的半径，默认为6
     * @param mCircleRadius
     */
    public void setCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置圆圈的半径
     * @param mCircleColor
     */
    public void setCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * 设置日期的点击回调事件
     * @author shiwei.deng
     *
     */
    public interface DateClick{
        public void onClickOnDate(MonthView monthView, int selDay);
    }

    /**
     * 设置日期点击事件
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }
}
