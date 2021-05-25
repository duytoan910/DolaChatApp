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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pickImage();
            return;
        }
        // Các bạn nhớ request permison cho các máy M trở lên nhé, k là crash ngay đấy.
        int result = ContextCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            requestPermissions(new String[]{
                    READ_EXTERNAL_STORAGE}, READ_EXTERNAL_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode != READ_EXTERNAL_REQUEST) return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            Toast.makeText(getApplicationContext(), "R.string.permission_denied",
                    Toast.LENGTH_LONG).show();
        }
    }
    public void pickImage() {
        // Gọi intent của hệ thống để chọn ảnh nhé.
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),
                PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            Uri uri = data.getData();
            uploadFiles(uri);
        }
    }
    public void uploadFiles(Uri uri) {
        if (uri == null) return;
        File file = new File(uri.getPath());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse(getContentResolver().getType(uri)),
                file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);
        ApiService.api.uploadFile(userInfos.get(1), filePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}