package com.android.lily.hotel.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.lily.hotel.R;
import com.android.lily.hotel.model.XMonth;
import com.android.lily.hotel.view.MonthView;

import java.util.Calendar;
import java.util.List;

/***********
 *
 * @Author rape flower
 * @Date 2016-11-02 18:12
 * @Describe 日历适配器
 *
 */
public class CalendarAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflater;
    private List<XMonth> list;
    private SparseArray<MonthView> monthViews = new SparseArray<MonthView>();

    public CalendarAdapter (Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        monthViews.clear();
    }

    /**
     * 设置数据源
     * @param list
     */
    public void setList(List<XMonth> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarHolder holder = null;
        View view = null;
        if (convertView == null) {
            view =  inflater.inflate(R.layout.item_calendar, parent, false);
            holder = new CalendarHolder();
            holder.tvYearMonth = (TextView) view.findViewById(R.id.tv_year_month);
            holder.monthView = (MonthView) view.findViewById(R.id.monthDateView);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (CalendarHolder) view.getTag();
        }

        if (list == null || list.size() == 0) {
            return view;
        }

        XMonth xMonth = list.get(position);
        String strYearMonth = xMonth.getYear() + "年" + (xMonth.getMonth() + 1) + "月";
//        Log.w("CalendarAdapter", "Year = " + xMonth.getYear() + ", Month = " + xMonth.getMonth() + ", Day = " + xMonth.getDay());
        //设置当前的年月日
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH);
        int currDay = calendar.get(Calendar.DATE);

        //设置Tag
        Log.w("CalendarAdapter", "MonthView = " + position);
        holder.monthView.setTag(position);
        monthViews.put(position, holder.monthView);

        //设置当前年月日
        holder.monthView.setCurrYear(currYear);
        holder.monthView.setCurrMonth(currMonth);
        if (xMonth.getYear() == currYear && xMonth.getMonth() == currMonth) {
            holder.monthView.setCurrDay(currDay);
        }
        holder.tvYearMonth.setText(strYearMonth);
        holder.monthView.setSelectYearMonth(xMonth.getYear(), xMonth.getMonth(), xMonth.getDay());
        holder.monthView.invalidate();
        //设置选中的监听
        holder.monthView.setDateClick(new MonthView.DateClick() {
            @Override
            public void onClickOnDate(MonthView monthView, int selDay) {
                int pos = (int) monthView.getTag();
                Log.w("MonthView.DateClick", "-------------------> getTag() = " + pos);
                reDrawMonthView(pos, selDay);
            }
        });

        return view;
    }

    private class CalendarHolder {
        private TextView tvYearMonth;
        private MonthView monthView;
    }

    /**
     * 重新刷新界面
     */
    private void reDrawMonthView(int pos, int selDay) {
        if (list == null || list.size() == 0) {
            return;
        }
        int key = 0;
        for(int i = 0; i < monthViews.size(); i++) {
            key = monthViews.keyAt(i);
            // get the object by the key.
            XMonth xMonth = list.get(key);
            if (key != pos) {
                xMonth.setDay(0);
            } else {
                xMonth.setDay(selDay);
            }
        }
        notifyDataSetChanged();
    }
}
