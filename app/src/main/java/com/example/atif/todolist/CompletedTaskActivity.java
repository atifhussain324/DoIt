package com.example.atif.todolist;

import android.os.Bundle;
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

        //Toolbar header created. Activity title set.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Completed Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //New ListView is created for Completed Tasks.
        mListView = (ListView) findViewById(R.id.completedList);

        //FirebaseListAdapter is created from the Firebase UI library.
        //PopulateView method auto generates a listview of objects stored in firebase database based on reference.
        //This ListView reads the Completed Task Node.
        firebaseListAdapter = new FirebaseListAdapter<Task>(this, Task.class, R.layout.task, myRef) {
            @Override
            protected void populateView(View v, Task task, int position) {

                //TextView and ImageViews are created and connected to xml views.
                TextView title = (TextView) v.findViewById(R.id.title);
                TextView description = (TextView) v.findViewById(R.id.description);
                TextView date = (TextView) v.findViewById(R.id.date);
                TextView time = (TextView) v.findViewById(R.id.time);
                ImageView taskIcon = (ImageView) v.findViewById(R.id.taskIcon);

                //Task information are read by get methods defined in Task class and set to the textviews.
                title.setText(task.getTitle());
                description.setText(task.getDescription());
                date.setText(task.getDueDate());
                time.setText(task.getDueTime());

                //Switch cases created to designate unique logo based on task type that is selected from Spinner.
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
        //Adapter is set to ListView.
        mListView.setAdapter(firebaseListAdapter);
    }


}
