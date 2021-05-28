package com.example.dolaapp.Others.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListFragment extends Fragment {
    private ArrayList<User> conversations;
    ListView listView;
    SwipeRefreshLayout swiperefresh;;
    ListView listView_RequestMessage;
    RequestListAdapter findFriendListAdapter;
    ArrayList<String> userInfos;

    private String userID;

    public RequestListFragment() {
        // Required empty public constructor
    }
    public RequestListFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        listView_RequestMessage = view.findViewById(R.id.listView);
        listView = view.findViewById(R.id.listView);
        swiperefresh = view.findViewById(R.id.swiperefresh);

        Session sessionManagement = new Session(getContext());
        userInfos = sessionManagement.getSession();

        reloadList();

        listView_RequestMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ApiService.api.GetConversationOf2Users(userInfos.get(1),conversations.get(position).getUserPhone()).enqueue(new Callback<Conversation>() {
                    @Override
                    public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                        Intent result = new Intent(getContext(), ChatScreenActivity.class);
                        result.putExtra("conversationObject", response.body());
                        result.putExtra("isRequest", true);
                        startActivity(result);
                    }

                    @Override
                    public void onFailure(Call<Conversation> call, Throwable t) {

                    }
                });
            }
        });

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadList();
                swiperefresh.setRefreshing(false);
            }
        });
        return view;
    }
    public void reloadList(){
        ApiService.api.getAllListRequest(userInfos.get(1)).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.body()==null || response.body().size() == 0) {
                    return;
                }
                conversations = (ArrayList<User>) response.body();
                findFriendListAdapter = new RequestListAdapter(conversations, getContext());
                listView_RequestMessage.setAdapter(findFriendListAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }
}