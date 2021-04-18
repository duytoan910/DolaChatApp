package com.example.dolaapp.Others;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dolaapp.Entities.User;

import java.util.ArrayList;

public class Session {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "Dola_Login";
    String SESSION_KEY_NAME = "session_userName";
    String SESSION_KEY_PHONE = "session_userPhone";

    public Session(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        //save session of user whenever user is logged in
        String userName = user.getUserName();
        String userPhone = user.getUserPhone();

        editor.putString(SESSION_KEY_NAME,userName).commit();
        editor.putString(SESSION_KEY_PHONE,userPhone).commit();
    }

    public ArrayList<String> getSession(){
        ArrayList<String> list = new ArrayList<>();
        list.add(sharedPreferences.getString(SESSION_KEY_NAME, ""));
        list.add(sharedPreferences.getString(SESSION_KEY_PHONE, ""));
        return list;
    }

    public void removeSession(){
        editor.putString(SESSION_KEY_NAME,"").commit();
        editor.putString(SESSION_KEY_PHONE,"").commit();
    }
}
