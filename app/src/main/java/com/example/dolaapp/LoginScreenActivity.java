package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dolaapp.Entities.User;

public class LoginScreenActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button loginButton,forgotPassword,signUp;
    ImageButton info;
    ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initControls();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
				
				User loginUser = new User();
				loginUser.setUserName(usernameEditText.getText().toString());
				loginUser.setUserPassword(passwordEditText.getText().toString());

            }
        });
    }


    protected void initControls(){
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUp);
        info = findViewById(R.id.info);
        loadingProgressBar = findViewById(R.id.loading);
    }
}