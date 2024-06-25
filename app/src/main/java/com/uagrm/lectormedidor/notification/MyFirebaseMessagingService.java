package com.uagrm.lectormedidor.notification;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.activity.PantallaInicioActivity;

import java.util.Map;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Map data;
    private NotificationCompat.Builder mBuilder;

    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(250);
        ShowNotification(remoteMessage);
    }


    private void ShowNotification(RemoteMessage remoteMessage) {
        String channelId = "miCanalNotificable";
        data = remoteMessage.getData();
        String titulo = data.get("title").toString();
        String cuerpo = data.get("alert").toString();
        NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, (String) null);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, VerificarIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, titulo, importancia);
            mChannel.setDescription(titulo);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mChannel.enableLights(true);
            mChannel.setVibrationPattern(new long[]{1000, 500, 1000});
            notificationManager.createNotificationChannel(mChannel);
            mBuilder = new NotificationCompat.Builder(this, channelId);
        }
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setLights(Color.BLUE, 1, 0)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentInfo("Notificación")
                .setSound(defaultSound)
                .setPriority(Notification.PRIORITY_LOW)
                .setVibrate(new long[]{1000, 500, 1000})
                .setContentText(cuerpo);
        notificationManager.notify(new Random().nextInt(1000), mBuilder.build());
    }

    private Intent VerificarIntent(){
        Intent i;
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        String cuerpo = data.get("alert").toString();
        String titulo = data.get("title").toString();
        int tipo = Integer.parseInt(String.valueOf(data.get("tipo")));
        int idContenido = Integer.parseInt(String.valueOf(data.get("idContenido")));
        i= new Intent(this, PantallaInicioActivity.class);
        i.putExtra("idNotificacion", true);
        i.putExtra("idContenido", idContenido);
        i.putExtra("tipo",tipo);
        i.putExtra("cuerpo",cuerpo);
        i.putExtra("titulo",titulo);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return i;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }


    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }


}

