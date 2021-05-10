package com.example.dolaapp.Others.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dolaapp.Entities.User;
import com.example.dolaapp.R;

public class SignUp_2_Activity extends AppCompatActivity {
    Button nextStep;
    EditText password, passwordCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_2);

        nextStep = findViewById(R.id.nextStep);
        password = findViewById(R.id.password);
        passwordCheck = findViewById(R.id.passwordCheck);

        nextStep.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(password.getText().toString().equals(passwordCheck.getText().toString())){
                    Intent intent = new Intent(SignUp_2_Activity.this, SignUp_3_Activity.class);
                    intent.putExtra("userOBJ",
                            new User(
                                    "",
                                    password.getText().toString().trim(),
                                    getIntent().getStringExtra("userName"),
                                    "",
                                    ""
                            )
                    );
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else{
                    Toast.makeText(SignUp_2_Activity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}