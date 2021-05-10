package com.example.dolaapp.Others.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.LoginScreenActivity;
import com.example.dolaapp.R;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class SignUp_3_Activity extends AppCompatActivity {
    Button nextStep;
    EditText userName,dob,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_3);

        userName = findViewById(R.id.userName);
        dob = findViewById(R.id.dob);
        email = findViewById(R.id.email);

        nextStep = findViewById(R.id.nextStep);

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || dob.getText().toString().isEmpty()){
                    Toast.makeText(SignUp_3_Activity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = (User)getIntent().getSerializableExtra("userOBJ");

                user.setUserName(userName.getText().toString());
                user.setUserEmail(email.getText().toString());
                user.setUserDoB(dob.getText().toString());

                ApiService.api.createNewUser(user.getUserPhone(), user.getUserName(), user.getUserDoB(), user.getUserEmail(), user.getUserPassword())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });

                Intent intent = new Intent(SignUp_3_Activity.this, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                Toast.makeText(SignUp_3_Activity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {

            dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };
    public void calClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, new Date().getYear()+1900, new Date().getMonth(), new Date().getDate());
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}