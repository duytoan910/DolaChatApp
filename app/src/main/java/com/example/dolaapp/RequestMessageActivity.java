package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.FindFriendListAdapter;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp.Others.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestMessageActivity extends AppCompatActivity {
    private ArrayList<Conversation> conversations;
    ListView listView_RequestMessage;
    SwipeRefreshLayout swiperefresh;
    ArrayList<String> listRequestCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_message);

        listView_RequestMessage = findViewById(R.id.listView_RequestMessage);
        swiperefresh = findViewById(R.id.swiperefresh);

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        Session sessionManagement = new Session(RequestMessageActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();

        listRequestCurrentUser = new ArrayList<String>();
        ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getListRequest().size() > 0) {
                    listRequestCurrentUser = (ArrayList<String>) response.body().getListRequest();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        ApiService.api.getAllUser().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                Session sessionManagement = new Session(RequestMessageActivity.this);
                ArrayList<String> userInfos = sessionManagement.getSession();
                ArrayList<User> _tmpList = new ArrayList<User>();
                for (int i=0; i< ((ArrayList<User>) response.body()).size() ; i++){
                    for (String s : listRequestCurrentUser) {
                        if(response.body().get(i).getUserPhone().equals(s)){
                            _tmpList.add(response.body().get(i));
                        }
                    }
                }
                RequestListAdapter findFriendListAdapter = new RequestListAdapter((ArrayList<User>) _tmpList,RequestMessageActivity.this);
                listView_RequestMessage.setAdapter(findFriendListAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
            }
        });

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Session sessionManagement = new Session(RequestMessageActivity.this);
                ArrayList<String> userInfos = sessionManagement.getSession();
                ApiService.api.getAllUser().enqueue(new Callback<ArrayList<User>>() {
                    @Override
                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                        Session sessionManagement = new Session(RequestMessageActivity.this);
                        ArrayList<String> userInfos = sessionManagement.getSession();
                        ArrayList<User> _tmpList = new ArrayList<User>();
                        for (int i=0; i< ((ArrayList<User>) response.body()).size() ; i++){
                            for (String s : listRequestCurrentUser) {
                                if(response.body().get(i).getUserPhone().equals(s)){
                                    _tmpList.add(response.body().get(i));
                                }
                            }
                        }
                        RequestListAdapter findFriendListAdapter = new RequestListAdapter((ArrayList<User>) _tmpList,RequestMessageActivity.this);
                        listView_RequestMessage.setAdapter(findFriendListAdapter);
                        swiperefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                    }
                });
                swiperefresh.setRefreshing(false);
            }
        });
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