package com.example.dolaapp._AppConfig;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.example.dolaapp.ConversationScreenActivity;
import com.example.dolaapp.R;
import com.example.dolaapp.UserSettingActivity;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AppServices {
    private String route = "http://192.168.1.9";
    private String port = "3000";

    public String getRouteAPI(){
        return route + ":" + port + "/api/";
    }

    public String getRouteSocket(){
        return route + ":" + port + "/";
    }

    public String getRouteImage(String path){ return route + ":" + port + "/" + path; }

    public void setImageToImageView(Context context, String url, ImageView imgView){
        Picasso
                .with(context)
                .load(this.getRouteImage(url))
                .into(imgView);
    }

    public void createMessageNotification(Context context, String Message,String NameSender){
        String title = "Bạn có một tin nhắn mới";

        Intent it = new Intent(context, ConversationScreenActivity.class);
        PendingIntent pdIntent = PendingIntent.getActivity(context,1,it,PendingIntent.FLAG_UPDATE_CURRENT);

        Message = NameSender + " : " + Message;
        Notification notification =  new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(Message)
                .setSmallIcon(R.drawable.logo_pic)
                .setAutoCancel(true)
                .setContentIntent(pdIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) new Date().getTime(),notification);
    }

    public void createNewFriendNotification(Context context, String SenderName){
        String title = "Yêu cầu kết bạn";
        String message = "Bạn nhận được lời mời kết bạn từ : " + SenderName;

        Intent it = new Intent(context, ConversationScreenActivity.class);
        PendingIntent pdIntent = PendingIntent.getActivity(context,1,it,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification =  new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo_pic)
                .setAutoCancel(true)
                .setContentIntent(pdIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) new Date().getTime(),notification);
    }

    public void createAcceptFriendNotification(Context context, String AcceptorName){
        String title = "Chấp nhận yêu cầu kết bạn";
        String message = AcceptorName+ " đã chấp nhận lời mời kết bạn của bạn";

        Intent it = new Intent(context, ConversationScreenActivity.class);
        PendingIntent pdIntent = PendingIntent.getActivity(context,1,it,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification =  new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo_pic)
                .setAutoCancel(true)
                .setContentIntent(pdIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) new Date().getTime(),notification);
    }
}
