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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_message);

        listView_RequestMessage = findViewById(R.id.listView_RequestMessage);
        swiperefresh = findViewById(R.id.swiperefresh);

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        Session sessionManagement = new Session(RequestMessageActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();

        listRequestCurrentUser = new ArrayList<Conversation>();
        ApiService.api.getAllListRequest(userInfos.get(1)).enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
//                if(response.body()!=null){
//                    if(response.body().size()>0){
//                        listRequestCurrentUser = (ArrayList<Conversation>) response.body();
//                        findFriendListAdapter = new RequestListAdapter(listRequestCurrentUser,RequestMessageActivity.this);
//                        listView_RequestMessage.setAdapter(findFriendListAdapter);
//
//                    }
//                }
                if(response.body().size()==0) return;
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
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {

            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Session sessionManagement = new Session(RequestMessageActivity.this);
                ArrayList<String> userInfos = sessionManagement.getSession();

                ApiService.api.getAllListRequest(userInfos.get(1)).enqueue(new Callback<List<Conversation>>() {
                    @Override
                    public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                        if(response.body()!=null){
                            if(response.body().size()>0){
                                listRequestCurrentUser = (ArrayList<Conversation>) response.body();
                                findFriendListAdapter = new RequestListAdapter(listRequestCurrentUser,RequestMessageActivity.this);
                                listView_RequestMessage.setAdapter(findFriendListAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Conversation>> call, Throwable t) {

                    }
                });
                swiperefresh.setRefreshing(false);
            }
        });

        listView_RequestMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(RequestMessageActivity.this)
                        .setMessage("Nếu trả lời cuộc trò chuyện này, bạn sẽ chấp nhận tin nhắn đó!")
                        .setNeutralButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent result = new Intent(RequestMessageActivity.this, ChatScreenActivity.class);
                                result.putExtra("conversationObject", conversations.get(position));
                                startActivity(result);
                            }
                        }).show();
//                new AlertDialog.Builder(RequestMessageActivity.this)
//                        .setTitle("Chấp nhận " + listRequestCurrentUser.get(position).getUserName() + "?")
//                        .setMessage("Gay")
//                        .setCancelable(false)
//                        .setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                ApiService.api.AcceptFriendRequest(userInfos.get(1),listRequestCurrentUser.get(position).getUserPhone()).enqueue(new Callback<String>() {
//                                    @Override
//                                    public void onResponse(Call<String> call, Response<String> response) {
//                                        findFriendListAdapter.notifyDataSetChanged();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<String> call, Throwable t) {
//                                    }
//                                });
//                                dialog.cancel();
//                            }
//                        })
//                        .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                ApiService.api.AcceptFriendRequest(userInfos.get(1),listRequestCurrentUser.get(position).getUserPhone()).enqueue(new Callback<String>() {
//                                    @Override
//                                    public void onResponse(Call<String> call, Response<String> response) {
//                                        findFriendListAdapter.notifyDataSetChanged();
//                                        listRequestCurrentUser.remove(position);
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<String> call, Throwable t) {
//                                    }
//                                });
//                                dialog.cancel();
//                            }
//                        })
//                        .setNeutralButton("Trở về", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
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