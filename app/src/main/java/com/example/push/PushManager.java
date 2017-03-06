package com.example.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.signincouse.R;
import com.example.signincouse.ui.MessageListActivity;

/**
 * 推送管理器
 * <p>
 * Created by shi hao on 2017/2/27.
 */

public class PushManager {
    private Context context;
    private NotificationManager manager;
    private int id = 0;

    public PushManager(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void buildAndNotifyMsg(String title, String content) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title);
        Intent resultIntent = new Intent(context, MessageListActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        manager.notify(id++, notification);
    }
}
