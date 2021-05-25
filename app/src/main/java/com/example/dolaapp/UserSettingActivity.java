package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.google.android.material.textfield.TextInputEditText;

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
    Session sessionManagement;
    TextInputEditText txtDialog_OldPass,txtDialog_NewPass,txtDialog_ReEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        sessionManagement = new Session(UserSettingActivity.this);
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
        String userId = sessionManagement.getSession().get(1);
        String userPassword = sessionManagement.getSession().get(2);

        LayoutInflater inflater = getLayoutInflater();
        View customDialog = inflater.inflate(R.layout.dialog_change_password, null);

        txtDialog_OldPass = customDialog.findViewById(R.id.txtOldPassword);
        txtDialog_NewPass = customDialog.findViewById(R.id.txtNewPassword);
        txtDialog_ReEnter = customDialog.findViewById(R.id.txtReNewPassWord);

        txtDialog_OldPass.setText(userPassword);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(customDialog)
                .setPositiveButton("Xác nhận", null)
                .setNegativeButton("Hủy", null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChangePassValidate()){
                    String userNewPassword = txtDialog_NewPass.getText().toString();
                    ApiService.api.ChangePassword(userId,userPassword,userNewPassword).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            sessionManagement.savePassword(response.body());
                            Toast.makeText(UserSettingActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(UserSettingActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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

    private boolean ChangePassValidate (){
        String old = txtDialog_OldPass.getText().toString();
        String newP = txtDialog_NewPass.getText().toString();
        String reNewP = txtDialog_ReEnter.getText().toString();
        if (old.length() == 0) {
            txtDialog_OldPass.setError("Nhập mật khẩu hiện tại");
            return false;
        }

        if (newP.length() == 0) {
            txtDialog_NewPass.setError("Hãy nhập mật khẩu mới");
            return false;
        }

        if (reNewP.length() == 0) {
            txtDialog_ReEnter.setError("Hãy nhập lại mật khẩu mới");
            return false;
        }

        if (!newP.equals(reNewP)){
            txtDialog_ReEnter.setError("Nhập lại không khớp");
            return false;
        }

        if (old.equals(reNewP) || old.equals(newP) )
        {
            txtDialog_ReEnter.setError("Mật khẩu mới không trùng với mk cũ");
            return false;
        }
        return  true;
    }
}