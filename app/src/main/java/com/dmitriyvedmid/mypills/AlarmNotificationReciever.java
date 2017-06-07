package com.dmitriyvedmid.mypills;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.dmitriyvedmid.mypills.DrugsIntent.DrugsListActivity;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by dmitr on 6/5/2017.
 */

public class AlarmNotificationReciever extends BroadcastReceiver {

    public static DrugItem tDrug;

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNM = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, DrugsListActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Notification mNotify = new Notification.Builder(context)
                .setContentTitle("Час прийняти ліки")
                .setSmallIcon(R.drawable.ic_notification_active)
                .setContentIntent(pendingIntent1)
                .setSound(sound)
                .setAutoCancel(true)
                .build();

        mNM.notify(1,mNotify);

    }
}
