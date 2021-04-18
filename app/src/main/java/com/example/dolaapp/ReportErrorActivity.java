package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ReportErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_error);
    }

    public void btnReportClick(View view) {
        Toast.makeText(this, "Ok!", Toast.LENGTH_SHORT).show();
    }

    public void pressBack(View view) {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}