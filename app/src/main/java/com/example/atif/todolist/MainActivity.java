package com.example.atif.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReferenceFromUrl("https://todolist-ff521.firebaseio.com/Tasks");
    ListView mListView;
    FirebaseListAdapter<Task> firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.checklist);
        toolbar.setTitle("Do it!");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, NewTaskActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        mListView = (ListView) findViewById(R.id.tasklist);
        mListView.isLongClickable();
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.header)
                        .content(R.string.content)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DatabaseReference itemRef = firebaseListAdapter.getRef(position);
                                itemRef.removeValue();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });
        mListView.isClickable();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cbox = (CheckBox)view.findViewById(R.id.completedBox);
                    Log.v("box","checked");


            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent myIntent = new Intent(MainActivity.this, CompletedTaskActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;

            case R.id.action_refresh:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }


}
