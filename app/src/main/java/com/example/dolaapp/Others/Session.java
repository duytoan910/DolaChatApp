package com.example.dolaapp.Others;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dolaapp.Entities.User;

import java.util.ArrayList;

public class Session {
    private static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "Dola_Login";
    String SESSION_KEY_NAME = "session_userName";
    static String SESSION_KEY_PHONE = "session_userPhone";
    String SESSION_KEY_PASSWORD = "session_userPassword";

    public Session(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        //save session of user whenever user is logged in
        String userName = user.getUserName();
        String userPhone = user.getUserPhone();
        String userPassword = user.getUserPassword();

        editor.putString(SESSION_KEY_NAME,userName).commit();
        editor.putString(SESSION_KEY_PHONE,userPhone).commit();
        editor.putString(SESSION_KEY_PASSWORD,userPassword).commit();
    }


    public  void savePassword ( String pass){
        editor.putString(SESSION_KEY_PASSWORD,pass).commit();
    }

    public ArrayList<String> getSession(){
        ArrayList<String> list = new ArrayList<>();
        list.add(sharedPreferences.getString(SESSION_KEY_NAME, ""));
        list.add(sharedPreferences.getString(SESSION_KEY_PHONE, ""));
        list.add(sharedPreferences.getString(SESSION_KEY_PASSWORD, ""));
        return list;
    }

    public void removeSession(){
        editor.putString(SESSION_KEY_NAME,"").commit();
        editor.putString(SESSION_KEY_PHONE,"").commit();
        editor.putString(SESSION_KEY_PASSWORD,"").commit();
    }
    public static String GetUserPhone(){
        return sharedPreferences.getString(SESSION_KEY_PHONE, "");
    }
}
