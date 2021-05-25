package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class UserSettingActivity extends AppCompatActivity {
    public final static int PICK_IMAGE_REQUEST = 1;
    public final static int READ_EXTERNAL_REQUEST = 2;
    ArrayList<String> userInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        Session sessionManagement = new Session(UserSettingActivity.this);
        userInfos = sessionManagement.getSession();
        if(userInfos.get(0) != ""){
            ((TextView) findViewById(R.id.userSettingName)).setText(userInfos.get(0));
            ((TextView) findViewById(R.id.userSettingPhone)).setText(userInfos.get(1));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Session sessionManagement = new Session(UserSettingActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();
        if(userInfos.get(0) == ""){
            Intent intent = new Intent(UserSettingActivity.this, LoginScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void userChangePassword(View view) {

    }

    public void userLogout(View view) {
        Session sessionManagement = new Session(UserSettingActivity.this);
        sessionManagement.removeSession();

        finish();
    }

    public void userTermsAndPolicies(View view) {
        Intent intent = new Intent(UserSettingActivity.this, TermsAndPoliciesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void userReport(View view) {
        Intent intent = new Intent(UserSettingActivity.this, ReportErrorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void userAboutUs(View view) {
        Intent intent = new Intent(UserSettingActivity.this, AppInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void pressBack(View view) {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void userPersonalSetting(View view) {
        Intent intent = new Intent(UserSettingActivity.this, UserPersonalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}