package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class UserPersonalSettingActivity extends AppCompatActivity {

    private static final int INTENT_REQUEST_CODE = 100;
    private Button mBtImageSelect;
    private Button mBtImageShow;
    ArrayList<String> userInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_setting);

        Session sessionManagement = new Session(UserPersonalSettingActivity.this);
        userInfos = sessionManagement.getSession();

        initViews();

    }

    private void initViews() {

        mBtImageSelect = findViewById(R.id.fab);
        mBtImageShow = findViewById(R.id.fabUpload);

        mBtImageSelect.setOnClickListener((View view) -> {

            mBtImageShow.setVisibility(View.GONE);

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");

            try {
                startActivityForResult(intent, INTENT_REQUEST_CODE);

            } catch (ActivityNotFoundException e) {

                e.printStackTrace();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                try {

                    InputStream is = getContentResolver().openInputStream(data.getData());

                    uploadImage(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        ApiService.api.uploadFile(userInfos.get(1),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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