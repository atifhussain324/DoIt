package com.example.atif.todolist;

import android.content.Intent;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Atif on 7/10/17.
 */

public class DatabaseWriter {
    public void saveTask(DatabaseReference mDatabase, String title, String description, String dueDate, String dueTime, String type, String checkbox){
        Task newTask = new Task();

        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setDueTime(dueTime);
        newTask.setType(type);
        newTask.setReminder(checkbox);

        String key = mDatabase.push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, newTask);
        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                }
            }
        });

    }
}
