package com.dev.mendes.android_mytasks.fragment;


import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.Icon;
        import android.os.Build;
        import android.support.v4.app.NotificationCompatSideChannelService;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Luke on 07/11/2017.
 */



/*
  IMPLEMENTATION EXEMPLE




NotificationInterface notif = new NotificationInterface(
        getApplicationContext(),
        MainActivity.class,
        R.mipmap.ic_launcher,
        "Mais um Titulo Teste",
        "Um texto qualquer, só clica aqui."
        );

        notif.setModel (
        getApplicationContext(),
        MainActivity.class,
        R.mipmap.ic_launcher,
        "Mais um Titulo Teste",
        "Um texto qualquer, só clica aqui."
        );


        notif.postNotification();
*/

public class NotificationInterface {

    // ID da notificação, para atualiza-la depois
    int notifID = 0;

    // Declaração do gerenciado de notificação
    NotificationManager manager;

    // Configuração do cannal de notificação
    String channelID = "4565";
    CharSequence channelName = "Memor";
    int importance = NotificationManager.IMPORTANCE_DEFAULT;

    // Activity para click da notificação
    Class clickResult;
    Intent resultIntent;
    PendingIntent resultPendingIntent;

    // Inicialização do modelo de notificação
    Notification.Builder model;

    public NotificationInterface(Context context, Class clickResult, int icon, String title, String text) {
        // atribuição ao serviço de notificação do sistema
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        //Aqui se define para qual Activity será aberta quando o usuário clicar na notificação
        this.clickResult = clickResult;
        resultIntent = new Intent(context,this.clickResult);
        resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Criação do modelo da notificação
        model = new Notification.Builder(context.getApplicationContext())
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(resultPendingIntent);

        // Configuraçõ do canal de notificação que será usado
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(channelID,channelName,importance);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            manager.createNotificationChannel(notificationChannel);

            model.setChannelId(channelID);
        }

    }



    public void setModel(Context context, Class clickResult, int icon, String title, String text) {

        this.clickResult = clickResult;
        resultIntent = new Intent(context,this.clickResult);
        resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        model   .setSmallIcon(icon)
                .setContentTitle(notifID + title)
                .setContentText(text)
                .setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= 26)
            model.setChannelId(channelID);
    }



    public void postNotification() {
        // Notificação é identificada e lançada ao sistema
        manager.notify(notifID, model.build());
        notifID += 1;
    }

}
