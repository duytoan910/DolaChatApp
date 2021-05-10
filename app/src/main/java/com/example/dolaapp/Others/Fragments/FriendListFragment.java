package com.example.dolaapp.Others.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.UserListAdapter;
import com.example.dolaapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListFragment extends Fragment {
    private ArrayList<Conversation> conversations;
    ListView listViewFriendList;
    SwipeRefreshLayout swiperefresh;

    private String userID;

    public FriendListFragment() {
        // Required empty public constructor
    }
    public FriendListFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        listViewFriendList = view.findViewById(R.id.listViewFriendList);
        swiperefresh = view.findViewById(R.id.swiperefresh);

        Session sessionManagement = new Session(getContext());
        ArrayList<String> userInfos = sessionManagement.getSession();
        ApiService.api.getAllListFriend(userInfos.get(1)).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                UserListAdapter adapter = new UserListAdapter((ArrayList<User>) response.body(), getContext());
                listViewFriendList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Nah", Toast.LENGTH_SHORT).show();
            }
        });

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Session sessionManagement = new Session(getContext());
                ArrayList<String> userInfos = sessionManagement.getSession();
                ApiService.api.getAllListFriend(userInfos.get(1)).enqueue(new Callback<ArrayList<User>>() {
                    @Override
                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                        Log.e("res",response.message());
                        UserListAdapter adapter = new UserListAdapter((ArrayList<User>) response.body(), getContext());
                        listViewFriendList.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                        Toast.makeText(getContext(), "Nah", Toast.LENGTH_SHORT).show();
                    }
                });
                swiperefresh.setRefreshing(false);
            }
        });
        listViewFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent result = new Intent(getContext(), ChatScreenActivity.class);
//                result.putExtra("userObject", conversations.get(position).getUserName());
//                startActivity(result);
            }
        });
        listViewFriendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getContext()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

                Log.e("Long click: ","Long press at " + position);

                return false;
            }
        });
        return view;
    }
}