package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dolaapp.Others.Session;

import java.util.ArrayList;

public class UserSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        Session sessionManagement = new Session(UserSettingActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();
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
}