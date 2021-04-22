package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.SignUp.SignUp_1_Activity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreenActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button loginButton,forgotPassword,signUp;
    ImageButton info;

    List<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initControls();

        getAllUser();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list == null) {
                    User user = new User(
                            "Test_chaunguyenduytoan@gmail.com",
                            "Test_toanheo",
                            "Test_0947047314",
                            "Test_Châu Nguyễn Duy Toàn",
                            "Test_09/10/1998");
                    Session sessionManagement = new Session(LoginScreenActivity.this);
                    sessionManagement.saveSession(user);

                    Intent intent = new Intent(LoginScreenActivity.this, ConversationScreenActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return;
                }else
                    if(list.size() > 0){
                    for(User user : list){
                        if(user.getUserPhone().equals(usernameEditText.getText().toString())){
                            if(user.getUserPassword().equals(passwordEditText.getText().toString())){

                                Session sessionManagement = new Session(LoginScreenActivity.this);
                                sessionManagement.saveSession(user);

                                Intent intent = new Intent(LoginScreenActivity.this, ConversationScreenActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                return;
                            }
                        }
                    }
                    Toast.makeText(LoginScreenActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
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

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, AppInfoActivity.class);
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

    private void getAllUser(){
        ApiService.api.getAllUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                list = (List<User>) response.body();
                Log.e("Length: ", response.body().size() + "");
                for(User user : list){
                    Log.e("Email: ",user.getUserEmail().toString());
                    Log.e("Phone: ",user.getUserPhone().toString());
                    Log.e("DoB: ",user.getUserDoB().toString());
                    Log.e("Name: ",user.getUserName().toString());
                    Log.e("Password: ",user.getUserPassword().toString());
                    Log.e("---------------------",user.getUserName().toString() + " [End]");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(LoginScreenActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
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