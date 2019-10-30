package com.fenixu.logica_negocio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fenixu.R;
import com.fenixu.gui.CronogramaActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int alarmId = intent.getIntExtra("idAlarma", 0);
        String tituloAlarma = intent.getStringExtra("tituloAlarma");

        Intent mainIntent = new Intent(context, CronogramaActivity.class);
        mainIntent.putExtra("valor", "receiver");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager myNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Tienes un compromiso pendiente")
                .setContentText(tituloAlarma)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        myNotificationManager.notify(alarmId, builder.build());
    }
}
