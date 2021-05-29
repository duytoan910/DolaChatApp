package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.SignUp.SignUp_1_Activity;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp._AppConfig.ExternalServices.FetchPath;
import com.example.dolaapp._AppConfig.AppServices;
import com.google.android.material.textfield.TextInputEditText;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    Loading load;
    TextInputEditText txtRegisPhone,txtRegisEmail,txtRegisName,txtRegisBoD;
    Button btnSave;
    String UPLOAD_URL;
    Boolean isUpdateAvatar = false;

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

        txtRegisPhone = (TextInputEditText) findViewById(R.id.txtRegisPhone);
        txtRegisEmail = (TextInputEditText) findViewById(R.id.txtRegisEmail);
        txtRegisName = (TextInputEditText) findViewById(R.id.txtRegisName);
        txtRegisBoD = (TextInputEditText) findViewById(R.id.txtRegisBoD);
        btnSave = (Button) findViewById(R.id.btnSave);

        UPLOAD_URL = new AppServices().getRouteAPI() + userInfos.get(1) + "/ChangeAvatarImage";
        initVars();
        load = new Loading(UserPersonalSettingActivity.this);
        load.startLoading();

        ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ((TextView) findViewById(R.id.txtRegisPhone)).setText(response.body().getUserPhone());
                ((TextView) findViewById(R.id.txtRegisEmail)).setText(response.body().getUserEmail());
                ((TextView) findViewById(R.id.txtRegisName)).setText(response.body().getUserName());
                ((TextView) findViewById(R.id.txtRegisName)).setText(response.body().getUserName());
                Date bod = new Date(response.body().getUserDoB());
                txtRegisBoD.setText(bod.getDate() + "/" + (bod.getMonth()+ 1) + "/" + (bod.getYear() + 1900));
                new AppServices().setImageToImageView(UserPersonalSettingActivity.this, response.body().getAvatar(), findViewById(R.id.imagetoupload));

                load.stopLoading();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                txtRegisBoD.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };
        txtRegisBoD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!txtRegisBoD.getText().toString().equals("")){
                        DatePickerDialog datePickerDialog = new DatePickerDialog(UserPersonalSettingActivity.this,
                                android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                                dateSetListener,
                                Integer.parseInt(txtRegisBoD.getText().toString().split("/")[2]),
                                Integer.parseInt(txtRegisBoD.getText().toString().split("/")[1])-1,
                                Integer.parseInt(txtRegisBoD.getText().toString().split("/")[0])
                        );
                        datePickerDialog.show();
                    }else{
                        DatePickerDialog datePickerDialog = new DatePickerDialog(UserPersonalSettingActivity.this,
                                android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                                dateSetListener, new Date().getYear()+1900, new Date().getMonth(), new Date().getDate());
                        datePickerDialog.show();
                    }
                }
            }
        });
        txtRegisBoD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtRegisBoD.getText().toString().equals("")){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(UserPersonalSettingActivity.this,
                            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                            dateSetListener,
                            Integer.parseInt(txtRegisBoD.getText().toString().split("/")[2]),
                            Integer.parseInt(txtRegisBoD.getText().toString().split("/")[1])-1,
                            Integer.parseInt(txtRegisBoD.getText().toString().split("/")[0])
                    );
                    datePickerDialog.show();
                }else{
                    DatePickerDialog datePickerDialog = new DatePickerDialog(UserPersonalSettingActivity.this,
                            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                            dateSetListener, new Date().getYear()+1900, new Date().getMonth(), new Date().getDate());
                    datePickerDialog.show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load.startLoading();
                boolean isReturn = false;
                String email = txtRegisEmail.getText().toString();
                String Name = txtRegisName.getText().toString();
                String Birth = txtRegisBoD.getText().toString();

                if(email.isEmpty()){
                    txtRegisEmail.setError("Vui lòng nhâp email!"); isReturn = true;
                }
                if(Name.isEmpty()){
                    txtRegisName.setError("Vui lòng nhâp họ và tên!"); isReturn = true;
                }
                if(Birth.isEmpty()){
                    txtRegisBoD.setError("Vui lòng chọn ngày sinh!"); isReturn = true;
                }
                if(isReturn) {

                    load.stopLoading();
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    ApiService.api.updatePersonalInfo(
                            userInfos.get(1),
                            Name,
                            String.valueOf(format.parse(Birth.toString())),
                            email
                    ).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(UserPersonalSettingActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                            load.stopLoading();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(isUpdateAvatar){
                    Ion.with(UserPersonalSettingActivity.this)
                            .load(new AppServices().getRouteAPI() + userInfos.get(1) + "/ChangeAvatarImage")
                            .setMultipartFile("image", "image/jpeg", new File(profileImgPath))
                            .asJsonObject()
                            .withResponse()
                            .setCallback((e, result) -> {
                                load.stopLoading();
                                Toast.makeText(UserPersonalSettingActivity.this, "Cập nhập thông tin thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                }else{
                    load.stopLoading();
                    Toast.makeText(UserPersonalSettingActivity.this, "Cập nhập thông tin thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
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

    public void pressBack(View view) {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void chooseAvatar(View view) {
        isUpdateAvatar = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }
}