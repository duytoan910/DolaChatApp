package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.FindFriendListAdapter;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp.Others.Session;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFriendActivity extends AppCompatActivity {
    private ArrayList<User> users;
    ListView listView_FindFriend;
    FindFriendListAdapter findFriendListAdapter;
    EditText txtSearchUser;
    SwipeRefreshLayout swiperefresh;
    Session sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        swiperefresh = findViewById(R.id.swiperefresh);
        txtSearchUser = findViewById(R.id.txtSearchUser);
        listView_FindFriend = (ListView) findViewById(R.id.listView_FindFriend);

        sessionManagement = new Session(FindFriendActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();

//        ApiService.api.getAllUser().enqueue(new Callback<ArrayList<User>>() {
//            @Override
//            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//                Session sessionManagement = new Session(FindFriendActivity.this);
//                ArrayList<String> userInfos = sessionManagement.getSession();
//                for (int i=0; i< ((ArrayList<User>) response.body()).size() ; i++){
//                    if(response.body().get(i).getUserPhone().equals(userInfos.get(1))){
//                        response.body().remove(i);
//                    }
//                }
//                findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
//                listView_FindFriend.setAdapter(findFriendListAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
//                Toast.makeText(FindFriendActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
//            }
//        });

        ApiService.api.SearchAccountByName(userInfos.get(1)," ").enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.body().size()<=0) return;
                findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                listView_FindFriend.setAdapter(findFriendListAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });

        txtSearchUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    if(txtSearchUser.getText().toString() == ""){
                        ApiService.api.SearchAccountByName(userInfos.get(1)," ")
                                .enqueue(new Callback<ArrayList<User>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                        if(response.body().size()<=0) return;
                                        findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                                        listView_FindFriend.setAdapter(findFriendListAdapter);
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                                    }
                        });
                    }else {
                        ApiService.api.SearchAccountByName(userInfos.get(1),txtSearchUser.getText().toString())
                            .enqueue(new Callback<ArrayList<User>>() {
                            @Override
                            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                if(response.body().size()<=0) return;
                                findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                                listView_FindFriend.setAdapter(findFriendListAdapter);
                            }

                            @Override
                            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                            }
                        });
                    }
                }
                return false;
            }
        });

        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString() == ""){
                    ApiService.api.SearchAccountByName(userInfos.get(1)," ")
                        .enqueue(new Callback<ArrayList<User>>() {
                            @Override
                            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                if(response.body().size()<=0) return;
                                findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                                listView_FindFriend.setAdapter(findFriendListAdapter);
                            }

                            @Override
                            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                            }

                    });
                }else{
                    ApiService.api.SearchAccountByName(userInfos.get(1),txtSearchUser.getText().toString())
                        .enqueue(new Callback<ArrayList<User>>() {
                            @Override
                            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                if(response.body().size()<=0) return;
                                findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                                listView_FindFriend.setAdapter(findFriendListAdapter);
                            }

                            @Override
                            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                            }
                    });
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiService.api.SearchAccountByName(userInfos.get(1),txtSearchUser.getText().toString()).enqueue(new Callback<ArrayList<User>>() {
                    @Override
                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                        if(response.body().size()<=0) return;
                        findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                        listView_FindFriend.setAdapter(findFriendListAdapter);
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