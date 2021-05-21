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
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp.Others.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestMessageActivity extends AppCompatActivity {
    private ArrayList<Conversation> conversations;
    ListView listView_RequestMessage;
    SwipeRefreshLayout swiperefresh;
    ArrayList<Conversation> listRequestCurrentUser;
    RequestListAdapter findFriendListAdapter;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_message);

        listView_RequestMessage = findViewById(R.id.listView_RequestMessage);
        swiperefresh = findViewById(R.id.swiperefresh);
        loading = new Loading(RequestMessageActivity.this);

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        Session sessionManagement = new Session(RequestMessageActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();

        listRequestCurrentUser = new ArrayList<Conversation>();
        loading.startLoading();
        ApiService.api.getAllListRequest(userInfos.get(1)).enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                if(response.body().size()==0){
                    loading.stopLoading();
                    return;
                }
                conversations = (ArrayList<Conversation>) response.body();
                ArrayList<Conversation> _Conv = new ArrayList<>();
                for (Conversation conversation : conversations) {
                    if(conversation.getReceiver().equals(userInfos.get(1))){
                        if(conversation.isReceiverShown() == false){
                            _Conv.add(conversation);
                        }
                    }else{
                        if(conversation.isSenderShown() == false){
                            _Conv.add(conversation);
                        }
                    }
                }
                findFriendListAdapter = new RequestListAdapter(_Conv, RequestMessageActivity.this);
                listView_RequestMessage.setAdapter(findFriendListAdapter);

                conversations = _Conv;
                loading.stopLoading();
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {

            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading.startLoading();
                Session sessionManagement = new Session(RequestMessageActivity.this);
                ArrayList<String> userInfos = sessionManagement.getSession();

                ApiService.api.getAllListRequest(userInfos.get(1)).enqueue(new Callback<List<Conversation>>() {
                    @Override
                    public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                        if(response.body()!=null){
                            if(response.body().size()>0){
                                listRequestCurrentUser = (ArrayList<Conversation>) response.body();
                                for (Conversation conversation : listRequestCurrentUser) {
                                    if(conversation.isGroupChat())
                                        listRequestCurrentUser.remove(conversation);
                                }
                                findFriendListAdapter = new RequestListAdapter(listRequestCurrentUser,RequestMessageActivity.this);
                                listView_RequestMessage.setAdapter(findFriendListAdapter);
                            }else{
                                loading.stopLoading();
                            }
                        }else{
                            loading.stopLoading();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Conversation>> call, Throwable t) {

                    }
                });
                swiperefresh.setRefreshing(false);
                loading.stopLoading();
            }
        });

        listView_RequestMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent(RequestMessageActivity.this, ChatScreenActivity.class);
                result.putExtra("conversationObject", conversations.get(position));
                result.putExtra("isRequest", true);
                startActivity(result);
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