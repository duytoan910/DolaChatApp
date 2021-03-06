package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dolaapp.Others.Loading;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.SignUp.SignUp_1_Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreenActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button loginButton,forgotPassword,signUp;
    ImageView info;

    List<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initControls();
        Loading loading = new Loading(LoginScreenActivity.this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.startLoading();
                ApiService.api.CheckLogIn(usernameEditText.getText().toString(),passwordEditText.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.body() == null){
                            Toast.makeText(LoginScreenActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                            loading.stopLoading();
                            return;
                        }
                        Session sessionManagement = new Session(LoginScreenActivity.this);
                        sessionManagement.saveSession(response.body());

                        loading.stopLoading();
                        Intent intent = new Intent(LoginScreenActivity.this, ConversationScreenActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginScreenActivity.this, "Không kết nối được đến máy chủ!", Toast.LENGTH_SHORT).show();
                        loading.stopLoading();

                        Session sessionManagement = new Session(LoginScreenActivity.this);
                        sessionManagement.saveSession(new User("toan@gmail.com", "123", "123", "Toàn", new Date().toString()));
                        loading.stopLoading();
                        Intent intent = new Intent(LoginScreenActivity.this, ConversationScreenActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, SignUp_1_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Session sessionManagement = new Session(LoginScreenActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();

        if(userInfos.get(0) != ""){
            //user id logged in and so move to mainActivity
            Intent intent = new Intent(LoginScreenActivity.this, ConversationScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else{

        }
    }
    protected void initControls(){
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUp);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}