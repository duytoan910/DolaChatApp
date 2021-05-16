package com.example.dolaapp.Others.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.LoginScreenActivity;
import com.example.dolaapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp_1_Activity extends AppCompatActivity {
    Button nextStep;
    ArrayList<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_1);

        nextStep = findViewById(R.id.nextStep);

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

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isReturn = false;
                if(list.size()<1) return;
                for (User user : list) {
                    if(user.getUserPhone().equals(username.getText().toString().trim()))
                    {
                        isReturn = true;
                        break;
                    }
                }
                if(isReturn) {
                    Toast.makeText(SignUp_1_Activity.this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(SignUp_1_Activity.this, SignUp_2_Activity.class);
                intent.putExtra("userName", username.getText().toString().trim());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}