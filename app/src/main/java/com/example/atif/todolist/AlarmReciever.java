package com.example.atif.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Atif on 7/11/17.
 */

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Intent is activated when notification is clicked. MainActivity starts.
        Intent notificationIntent = new Intent(context, MainActivity.class);

        //Extras from NewTaskActivity are retrieved. Title and Description from user input is stored.
        String nTitle = intent.getStringExtra("title");
        String nDescription = intent.getStringExtra("description");

        //TaskStackBuilder is implemented to handle navigation across MainActivity once notification click triggers intent.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Notification is built with custom icons and title/description fields.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setContentTitle(nTitle)
                .setContentText(nDescription)
                .setTicker("Task reminder!")
                .setSmallIcon(R.mipmap.checklist)
                .setContentIntent(pendingIntent).build();

        //Notification is triggered.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
