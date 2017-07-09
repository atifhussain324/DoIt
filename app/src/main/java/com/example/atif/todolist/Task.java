package com.example.atif.todolist;

import java.util.HashMap;

/**
 * Created by Atif on 7/6/17.
 */

public class Task {
    private String title;
    private String description;
    private String dueDate;
    private String dueTime;
    private String reminder;
    private String type;
    private String taskID;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }
    public String getReminder() {
        return title;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }



  /*  public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
        todo.put("name", name);
        todo.put("message", message);
        todo.put("date", date);

        return todo;
    }*/

}
