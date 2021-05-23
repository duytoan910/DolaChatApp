package com.example.dolaapp.Others.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.AppInfoActivity;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.LoginScreenActivity;
import com.example.dolaapp.R;
import com.example.dolaapp.UserSettingActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp_1_Activity extends AppCompatActivity {
    Button nextStep;
    ArrayList<User> list;
    TextInputEditText txtRegisPassword,txtRegisPasswordRe,txtRegisPhone,txtRegisEmail,txtRegisName,txtRegisBoD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_1);

        nextStep = findViewById(R.id.nextStep);
        txtRegisPhone = (TextInputEditText) findViewById(R.id.txtRegisPhone);
        txtRegisEmail = (TextInputEditText) findViewById(R.id.txtRegisEmail);
        txtRegisName = (TextInputEditText) findViewById(R.id.txtRegisName);
        txtRegisBoD = (TextInputEditText) findViewById(R.id.txtRegisBoD);
        txtRegisPassword = (TextInputEditText) findViewById(R.id.txtRegisPassword);
        txtRegisPasswordRe = (TextInputEditText) findViewById(R.id.txtRegisPasswordRe);

        EditText username = findViewById(R.id.username);
        ApiService.api.getAllUser().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                list = (ArrayList<User>) response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp_1_Activity.this,
                                android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                                dateSetListener,
                                Integer.parseInt(txtRegisBoD.getText().toString().split("/")[2]),
                                Integer.parseInt(txtRegisBoD.getText().toString().split("/")[1])-1,
                                Integer.parseInt(txtRegisBoD.getText().toString().split("/")[0])
                        );
                        datePickerDialog.show();
                    }else{
                        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp_1_Activity.this,
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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp_1_Activity.this,
                            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                            dateSetListener,
                            Integer.parseInt(txtRegisBoD.getText().toString().split("/")[2]),
                            Integer.parseInt(txtRegisBoD.getText().toString().split("/")[1])-1,
                            Integer.parseInt(txtRegisBoD.getText().toString().split("/")[0])
                    );
                    datePickerDialog.show();
                }else{
                    DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp_1_Activity.this,
                            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                            dateSetListener, new Date().getYear()+1900, new Date().getMonth(), new Date().getDate());
                    datePickerDialog.show();
                }
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

    public void convInfo(View view) {
        Intent intent = new Intent(SignUp_1_Activity.this, AppInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void register(View view) {
        boolean isReturn = false;
        String Pass1 = txtRegisPassword.getText().toString();
        String Pass2 = txtRegisPasswordRe.getText().toString();
        String Phone = txtRegisPhone.getText().toString();
        String email = txtRegisEmail.getText().toString();
        String Name = txtRegisName.getText().toString();
        String Birth = txtRegisBoD.getText().toString();

        if(Pass1.isEmpty()){
            txtRegisPassword.setError("Vui lòng nhâp mật khẩu!"); isReturn = true;
        }
        if(Pass2.isEmpty()){
            txtRegisPasswordRe.setError("Vui lòng nhâp mật khẩu!"); isReturn = true;
        }
        if(email.isEmpty()){
            txtRegisEmail.setError("Vui lòng nhâp email!"); isReturn = true;
        }
        if(Phone.isEmpty()){
            txtRegisPhone.setError("Vui lòng nhâp số điện thoại!"); isReturn = true;
        }
        if(Name.isEmpty()){
            txtRegisName.setError("Vui lòng nhâp họ và tên!"); isReturn = true;
        }
        if(Birth.isEmpty()){
            txtRegisBoD.setError("Vui lòng chọn ngày sinh!"); isReturn = true;
        }

        for (User user : list) {
            if(user.getUserPhone().equals(Phone.trim()))
            {
                isReturn = true;
                break;
            }
        }
        if(isReturn && !Phone.isEmpty()) {
            Toast.makeText(SignUp_1_Activity.this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Pass1.equals(Pass2)){
            ApiService.api.createNewUser(Phone,
                    Name,
                    new Date(Birth).toString(),
                    email,
                    Pass1)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                        }
                    });
            finish();
        }else {
            txtRegisPasswordRe.setError("Xác nhận mật khẩu không chính xác!");
        }
    }
}