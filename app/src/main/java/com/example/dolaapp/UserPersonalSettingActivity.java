package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp._AppConfig.ExternalServices.FetchPath;
import com.example.dolaapp._AppConfig.AppServices;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPersonalSettingActivity extends AppCompatActivity {
    ArrayList<String> userInfos;

    private static final int IMG_REQUEST =  1;
    ImageView imageToUpload;
    ImageButton uploadBtn;
    ImageButton btnChooseImage;
    String profileImgPath;
    Bitmap bitmap;
    private AlertDialog.Builder builder;

    String UPLOAD_URL;

    @Override
    protected void onStart() {
        getPermissions();

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_setting);

        Session sessionManagement = new Session(UserPersonalSettingActivity.this);
        userInfos = sessionManagement.getSession();

        UPLOAD_URL = new AppServices().getRouteAPI() + userInfos.get(1) + "/ChangeAvatarImage";
        initVars();

        ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //((TextView) findViewById(R.id.userSettingName)).setText(response.body().getUserName());
                //((TextView) findViewById(R.id.userSettingPhone)).setText(response.body().getUserPhone());
                new AppServices().setImageToImageView(UserPersonalSettingActivity.this, response.body().getAvatar(), findViewById(R.id.imagetoupload));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void initVars() {
        builder = new AlertDialog.Builder(this);
        imageToUpload = findViewById(R.id.imagetoupload);
        uploadBtn = findViewById(R.id.btnNewMessage);
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            if( requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
                Uri path = data.getData();
                if(path != null) {
                    profileImgPath = FetchPath.getPath(this, path);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                        imageToUpload.setImageBitmap(getCroppedBitmap(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static boolean isEmpty(String field) {

        return field == null || field.isEmpty();
    }

    public void displayAlert(String message) {

        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void pressBack(View view) {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void btnSaveInfo(View view) {
        if(isEmpty(profileImgPath)) {

            displayAlert("Please select a profile picture");
            return;
        }

        Ion.with(this)
                .load(new AppServices().getRouteAPI() + userInfos.get(1) + "/ChangeAvatarImage")
                .setMultipartFile("image", "image/jpeg", new File(profileImgPath))
                .asJsonObject()
                .withResponse()
                .setCallback((e, result) -> {
                    if(e != null) {
                        Toast.makeText(this, "Error is: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        switch (result.getHeaders().code()) {
                            case 500:
                                Toast.makeText(this, "Image Uploading Failed. Unknown Server Error!", Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                                Toast.makeText(this, "Image Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                                        R.drawable.images);

                                ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        //((TextView) findViewById(R.id.userSettingName)).setText(response.body().getUserName());
                                        //((TextView) findViewById(R.id.userSettingPhone)).setText(response.body().getUserPhone());
                                        new AppServices().setImageToImageView(UserPersonalSettingActivity.this, response.body().getAvatar(), findViewById(R.id.imagetoupload));
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }
                                });

                                profileImgPath = null;
                                break;
                        }
                    }

                });

    }

    public void chooseAvatar(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }
}