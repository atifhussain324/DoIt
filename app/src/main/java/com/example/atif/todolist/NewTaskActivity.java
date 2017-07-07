package com.example.atif.todolist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create a New Task");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton dateDialog = (ImageButton) findViewById(R.id.calender);
        dateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Calender", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }




    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = "You picked the following time: " + hourOfDay + "h" + minute + "m" + second;
        //timeTextView.setText(time);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        //dateTextView.setText(date);
    }

}


