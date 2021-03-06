package com.lx.ohmyjuus;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

public class CalendarActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TextView textView;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        datePicker = findViewById(R.id.datePicker);
        textView = findViewById(R.id.textView2);

        

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                 monthOfYear += 1;
                textView.setText(year + "년" + monthOfYear  + "월" + dayOfMonth + "일");



            }
        });




    }
}