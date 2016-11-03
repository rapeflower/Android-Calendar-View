package com.android.lily.hotel.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.lily.hotel.R;
import com.android.lily.hotel.adapter.CalendarAdapter;
import com.android.lily.hotel.model.XMonth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/***********
 *
 * @Author rape flower
 * @Date 2016-11-02 11:13
 * @Describe 日历
 *
 */
public class CalendarActivity extends Activity implements AdapterView.OnItemClickListener{

    private static final String TAG = CalendarActivity.class.getSimpleName();
    private Context context;
    private ListView listView;
    private CalendarAdapter calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        context = CalendarActivity.this;

        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH);
        int currDay = calendar.get(Calendar.DATE);

        List<XMonth> xMonths = new ArrayList<XMonth>();

        XMonth xMonth11 = new XMonth();
        xMonth11.setYear(currYear);
        xMonth11.setMonth(currMonth);
        xMonth11.setDay(currDay);
        xMonths.add(xMonth11);

        XMonth xMonth12 = new XMonth();
        xMonth12.setYear(currYear);
        xMonth12.setMonth(currMonth + 1);
        xMonth12.setDay(0);
        xMonths.add(xMonth12);

        XMonth xMonth1 = new XMonth();
        xMonth1.setYear(currYear + 1);
        xMonth1.setMonth(0);
        xMonth1.setDay(0);
        xMonths.add(xMonth1);

        XMonth xMonth2 = new XMonth();
        xMonth2.setYear(currYear + 1);
        xMonth2.setMonth(1);
        xMonth2.setDay(0);
        xMonths.add(xMonth2);

        XMonth xMonth3 = new XMonth();
        xMonth3.setYear(currYear + 1);
        xMonth3.setMonth(2);
        xMonth3.setDay(0);
        xMonths.add(xMonth3);

        XMonth xMonth4 = new XMonth();
        xMonth4.setYear(currYear + 1);
        xMonth4.setMonth(3);
        xMonth4.setDay(0);
        xMonths.add(xMonth4);

        XMonth xMonth5 = new XMonth();
        xMonth5.setYear(currYear + 1);
        xMonth5.setMonth(4);
        xMonth5.setDay(0);
        xMonths.add(xMonth5);

        calendarAdapter = new CalendarAdapter(context);
        calendarAdapter.setList(xMonths);
        listView = (ListView) findViewById(R.id.lv_months);
        listView.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.w(TAG, "----------------------> position = " + position);
    }
}
