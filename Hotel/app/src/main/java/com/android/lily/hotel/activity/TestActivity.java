package com.android.lily.hotel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.lily.hotel.R;
import com.android.lily.hotel.view.XCalendarView;

/**
 * Created by Administrator on 2016/11/1.
 */
public class TestActivity extends Activity implements XCalendarView.DateClick{

    XCalendarView calendarView;
    Button btnCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        calendarView = (XCalendarView) findViewById(R.id.xcv_date);
        calendarView.setDateClick(this);

        btnCalendar = (Button) findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, CalendarActivity.class));
            }
        });
    }

    @Override
    public void onClickOnDate(int x,int y) {
        Toast.makeText(TestActivity.this, "x= "+ x +", y= "+ y, Toast.LENGTH_SHORT).show();
    }
}
