package com.example.atif.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Calendar myCalendar = Calendar.getInstance();
    EditText editDueDate;
    EditText editDueTime;
    EditText editTitle;
    EditText editDescription;
    CheckBox notificationBox;
    String type;
    String title;
    String description;
    String dueDate;
    String dueTime;
    String checkbox;
    int calYear;
    int calMonth;
    int calDay;
    int calHour;
    int calMinute;
    private DatabaseReference mDatabase;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        //Toolbar header created. Activity title set.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Task");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EditText views connected by ID.
        editTitle = (EditText) findViewById(R.id.title);
        editDescription = (EditText) findViewById(R.id.description);

        //Database Reference is set.
        mDatabase = FirebaseDatabase.getInstance().getReference("Tasks");

        //Floating Action Button listener is set.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //User inputs of task titles,description,duedate,and duetimes are saved.
                title = editTitle.getText().toString();
                description = editDescription.getText().toString();
                dueDate = editDueDate.getText().toString();
                dueTime = editDueTime.getText().toString();

                //If condition used to enforce all text fields have been filed out.
                //If satisfied, saveTask method is run.
                if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() || dueTime.isEmpty()) {
                    Toast.makeText(NewTaskActivity.this, "Please complete all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    saveTask();
                }


            }
        });


        //Date dialog is created on click of the due date text field.
        editDueDate = (EditText) findViewById(R.id.duedate);
        editDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewTaskActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Values selected from date picker dialogs are set and saved.
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calYear = year;
                calMonth = monthOfYear;
                calDay = dayOfMonth;
                updateLabel();

            }

        };


        //On click listener is set on Due time text field .
        editDueTime = (EditText) findViewById(R.id.duetime);
        editDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Current device time is retrieved.
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                //TimePicker dialog is created and displayed.
                TimePickerDialog mTimePicker = new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editDueTime.setText(selectedHour + ":" + selectedMinute);
                        calHour = selectedHour;
                        calMinute = selectedMinute;
                    }
                }
                        , hour, minute, false);
                mTimePicker.show();
            }
        });

        //Spinner is created to differentiate what type of task is being created.
        //Array adapter is filled with values from string resource file.
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Notification Checkbox is created. It is set to default. If checked checkbox is stored as true.
        notificationBox = (CheckBox) findViewById(R.id.checkbox);
        checkbox = "false";
        notificationBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationBox.isChecked()) {
                    checkbox = "true";
                } else {
                    checkbox = "false";
                }
            }
        });

    }

    //Date is formatted to simple date time format.
    private void updateLabel() {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDueDate.setText(sdf.format(myCalendar.getTime()));
    }


    // An item was selected from Spinner. It is stored in a string variable.
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        type = parent.getSelectedItem().toString();
        Log.v("Type", type);

    }

    //Toast displayed if no item from spinner is selected. Not in use.
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(NewTaskActivity.this, "No category has been selected!", Toast.LENGTH_SHORT).show();
    }


    //New task is created.
    public void saveTask() {
        //Task model created
        Task newTask = new Task();

        //All inputs entered by users is set and saved into newTask object.
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setDueTime(dueTime);
        newTask.setType(type);
        newTask.setReminder(checkbox);

        //newTask Hashmap is written to database with unique key value.
        //Database reference is set to "Tasks" node. New Task is created.
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

        //If notification checkbox is set to true, AlarmManager is created which triggers a local notification
        if (checkbox.equals("true")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //New intent is created and title/description extras are sent to be displayed in notification.
            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.putExtra("title", title);
            notificationIntent.putExtra("description", description);
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            //Pending Intent is created, which triggers the notificationIntent
            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            //Alarm manager triggers the pending intent, which device reached time specified by user input while choosing due date and time.
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, calYear);
            cal.set(Calendar.MONTH, calMonth);
            cal.set(Calendar.DAY_OF_MONTH, calDay);
            cal.set(Calendar.HOUR_OF_DAY, calHour);
            cal.set(Calendar.MINUTE, calMinute);
            cal.set(Calendar.SECOND, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);


        }


    }


}




