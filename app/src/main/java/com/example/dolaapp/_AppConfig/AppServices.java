package com.example.dolaapp._AppConfig;

import android.content.Context;
import android.widget.ImageView;

import com.example.dolaapp.R;
import com.example.dolaapp.UserSettingActivity;
import com.squareup.picasso.Picasso;

public class AppServices {
    private String route = "http://192.168.1.10";
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
}
