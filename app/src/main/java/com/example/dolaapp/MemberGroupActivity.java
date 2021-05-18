package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Conversaion_Group_BottomSheet;
import com.example.dolaapp.Others.MemberListSelect_BottomSheet;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.UserListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberGroupActivity extends AppCompatActivity {
    ListView listView_GroupMember;
    ArrayList<User> userList;
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
                    userList = (ArrayList<User>) response.body();
                    UserListAdapter adapter = new UserListAdapter(userList, MemberGroupActivity.this);
                    listView_GroupMember.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
        listView_GroupMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberListSelect_BottomSheet modal = new MemberListSelect_BottomSheet(userList.get(position));
                modal.show(getSupportFragmentManager(), "info_group_member_Modal");
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