package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void sendPasswordClick(View view) {
        EditText userID = findViewById(R.id.userID);
        if(userID.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Vui lòng nhập số điện thoại muốn khôi phục!", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService.api.sendPassword(userID.getText().toString().trim()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        Toast.makeText(ForgotPasswordActivity.this, "Yêu cầu cấp lại mật khẩu thành công! Mật khẩu sẽ được gửi vào email nếu số điện thoại chính xác.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginScreenActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}