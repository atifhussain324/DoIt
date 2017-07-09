package com.example.atif.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Calendar myCalendar = Calendar.getInstance();
    EditText editDueDate;
    EditText editDueTime;
    EditText editTitle;
    EditText editDescription;
    String type;
    String title;
    String description;
    String dueDate;
    String dueTime;

    private DatabaseReference mDatabase;

    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Task");
        setSupportActionBar(toolbar);

        editTitle = (EditText) findViewById(R.id.title);
        editDescription = (EditText) findViewById(R.id.description);

        mDatabase = FirebaseDatabase.getInstance().getReference("Tasks");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = editTitle.getText().toString();
                description = editDescription.getText().toString();
                dueDate = editDueDate.getText().toString();
                dueTime = editDueTime.getText().toString();
                Log.v("Task",title + description + dueDate + dueTime);
                saveTask();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        //Date dialog
        editDueDate = (EditText) findViewById(R.id.duedate);
        editDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewTaskActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editDueTime = (EditText) findViewById(R.id.duetime);
        editDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editDueTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDueDate.setText(sdf.format(myCalendar.getTime()));
    }



    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        type = parent.getSelectedItem().toString();
        Log.v("Type",type);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void saveTask(){
        Task newTask = new Task();

        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setDueTime(dueTime);
        newTask.setType(type);

        String key = mDatabase.push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, newTask);
        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    finish();
                }
            }
        });

    }




}




