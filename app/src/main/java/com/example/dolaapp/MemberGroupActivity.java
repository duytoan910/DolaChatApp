package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.UserListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberGroupActivity extends AppCompatActivity {
    ListView listView_GroupMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_group);

        listView_GroupMember = findViewById(R.id.listView_GroupMember);

        Intent intent = getIntent();
        Session sessionManagement = new Session(MemberGroupActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();

        ApiService.api.getAllConversationMember(intent.getStringExtra("group_member")).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.body().size()>0){
                    UserListAdapter adapter = new UserListAdapter((ArrayList<User>) response.body(), MemberGroupActivity.this);
                    listView_GroupMember.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void pressBack(View view) {
        finish();
    }
}