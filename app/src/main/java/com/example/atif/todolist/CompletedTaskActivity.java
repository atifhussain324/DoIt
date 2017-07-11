package com.example.atif.todolist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompletedTaskActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReferenceFromUrl("https://todolist-ff521.firebaseio.com/CompletedTasks");
    ListView mListView;
    FirebaseListAdapter<Task> firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Completed Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.completedList);

        firebaseListAdapter = new FirebaseListAdapter<Task>(this, Task.class, R.layout.task, myRef) {
            @Override
            protected void populateView(View v, Task task, int position) {

                TextView title = (TextView) v.findViewById(R.id.title);
                TextView description = (TextView) v.findViewById(R.id.description);
                TextView date = (TextView) v.findViewById(R.id.date);
                TextView time = (TextView) v.findViewById(R.id.time);
                ImageView taskIcon = (ImageView) v.findViewById(R.id.taskIcon);

                title.setText(task.getTitle());
                description.setText(task.getDescription());
                date.setText(task.getDueDate());
                time.setText(task.getDueTime());


                switch (task.getType()) {
                    case "Personal":
                        taskIcon.setImageResource(R.mipmap.brainstorm);
                        break;
                    case "Shopping":
                        taskIcon.setImageResource(R.mipmap.groceries);
                        break;
                    case "Work":
                        taskIcon.setImageResource(R.mipmap.job);
                        break;
                    case "School":
                        taskIcon.setImageResource(R.mipmap.school);
                        break;
                }


            }
        };
        mListView.setAdapter(firebaseListAdapter);
    }



}
