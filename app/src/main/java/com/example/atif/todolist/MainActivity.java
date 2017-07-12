package com.example.atif.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReferenceFromUrl("https://todolist-ff521.firebaseio.com/Tasks");
    ListView mListView;
    FirebaseListAdapter<Task> firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar header created. Application name and logo set.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.checklist);
        toolbar.setTitle("Do it!");

        //FloatingAction Button which is used to open NewTaskActivity.
        // Users will click the button and start the intent to create a new task.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, NewTaskActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        //ListView created to display all tasks users have already created.
        mListView = (ListView) findViewById(R.id.tasklist);

        //FirebaseListAdapter is created from the Firebase UI library.
        //PopulateView method auto generates a listview of objects stored in firebase database based on reference.
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
        //Adapter is set for ListView.
        mListView.setAdapter(firebaseListAdapter);


        //ItemLongClick listener is set in order to detect user long clicks.
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;

                //OnItemLongClick a custom dialog is created asking if user wants to delete a task.
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.header)
                        .content(R.string.content)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                //If positive option is chosen, Task is deleted from database and the ListView.
                                //Position of an item is determined from where user long clicks (i) and is stored into "position"
                                DatabaseReference itemRef = firebaseListAdapter.getRef(position);
                                itemRef.removeValue();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                //If negative option is chosen, the dialog is dismissed.
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });

        //ItemClick listener is set in order to detect user clicks.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;

                //Checkbox object is created. Checkbox is set invisible by default.
                // When an item is clicked, checkbox made visible.
                final CheckBox box = (CheckBox) view.findViewById(R.id.completedBox);
                box.setVisibility(View.VISIBLE);

                //Checkbox listener is created to detect if a checkbox has been checked.
                //If checked, the item is removed from the "Task" node and written into the "CompletedTask" node on FB database
                box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        //When a checkbox is checked, a Toast is displayed.
                        Toast.makeText(MainActivity.this, "Task has been completed!", Toast.LENGTH_SHORT).show();

                        //Database reference is changed from "tasks" to "completedTasks"
                        DatabaseReference completedTaskRef = database.getReferenceFromUrl("https://todolist-ff521.firebaseio.com/CompletedTasks");

                        //The Task object that is being deleted is retrieved in order to push to the "CompletedTasks" node.
                        Task selectedTask = firebaseListAdapter.getItem(position);

                        //Task HashMap object is written into database along with a unique key and database is updated.
                        //Task becomes a Completed task.
                        String key = completedTaskRef.push().getKey();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(key, selectedTask);
                        completedTaskRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    //Do nothing
                                }
                            }
                        });

                        //Task is deleted from original reference of "Tasks"
                        DatabaseReference itemRef = firebaseListAdapter.getRef(position);
                        itemRef.removeValue();


                    }
                });


            }


        });

    }

    //Menu is created and displayed on toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Completed Tasks activity is started on click.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_completed:
                Intent myIntent = new Intent(MainActivity.this, CompletedTaskActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


}
