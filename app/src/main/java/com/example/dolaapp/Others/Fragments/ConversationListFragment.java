package com.example.dolaapp.Others.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.ConversationScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.LoginScreenActivity;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.R;

import java.io.Console;
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

public class ConversationListFragment extends Fragment {
    private ArrayList<Conversation> conversations;
    ListView listView;
    SwipeRefreshLayout swiperefresh;
    ArrayList<Conversation> conversationArrayList;
    List<User> list;
    User currentUser;
    ArrayList<String> userInfos;

    private String userID;

    public ConversationListFragment() {
        // Required empty public constructor
    }
    public ConversationListFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        listView = view.findViewById(R.id.listView);
        swiperefresh = view.findViewById(R.id.swiperefresh);

        Session sessionManagement = new Session(getContext());
        userInfos = sessionManagement.getSession();

        ApiService.api.getAllConversationByUserID(userInfos.get(1)).enqueue(new Callback<ArrayList<Conversation>>() {
            @Override
            public void onResponse(Call<ArrayList<Conversation>> call, Response<ArrayList<Conversation>> response) {
                if(response.body()== null ||response.body().size()==0) return;
                conversations = (ArrayList<Conversation>) response.body();
                ArrayList<Conversation> _Conv = new ArrayList<>();
                for (Conversation conversation : conversations) {
                    if(conversation.getReceiver().equals(userInfos.get(1))){
                        if(conversation.isReceiverShown() != false){
                            _Conv.add(conversation);
                        }
                    }else{
                        if(conversation.isSenderShown() != false){
                            _Conv.add(conversation);
                        }
                    }
                }
                ConversationListAdapter adapter = new ConversationListAdapter(_Conv, getContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Conversation>> call, Throwable t) {

            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiService.api.getAllConversationByUserID(userInfos.get(1)).enqueue(new Callback<ArrayList<Conversation>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Conversation>> call, Response<ArrayList<Conversation>> response) {
                        if(response.body()== null ||response.body().size()==0) return;
                        conversations = (ArrayList<Conversation>) response.body();
                        ArrayList<Conversation> _Conv = new ArrayList<>();
                        for (Conversation conversation : conversations) {
                            if(conversation.getReceiver().equals(userInfos.get(1))){
                                if(conversation.isReceiverShown() != false){
                                    _Conv.add(conversation);
                                }
                            }else{
                                if(conversation.isSenderShown() != false){
                                    _Conv.add(conversation);
                                }
                            }
                        }
                        ConversationListAdapter adapter = new ConversationListAdapter(_Conv, getContext());
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Conversation>> call, Throwable t) {

                    }
                });
                swiperefresh.setRefreshing(false);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent(getContext(), ChatScreenActivity.class);
                result.putExtra("conversationObject", conversations.get(position));
                startActivity(result);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
    public void triggerSwipeRefresh(){
        swiperefresh.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        swiperefresh.setRefreshing(true);
        ApiService.api.getAllConversationByUserID(userInfos.get(1)).enqueue(new Callback<ArrayList<Conversation>>() {
            @Override
            public void onResponse(Call<ArrayList<Conversation>> call, Response<ArrayList<Conversation>> response) {
                if(response.body()== null ||response.body().size()==0) return;
                conversations = (ArrayList<Conversation>) response.body();
                ArrayList<Conversation> _Conv = new ArrayList<>();
                for (Conversation conversation : conversations) {
                    if(conversation.getReceiver().equals(userInfos.get(1))){
                        if(conversation.isReceiverShown() != false){
                            _Conv.add(conversation);
                        }
                    }else{
                        if(conversation.isSenderShown() != false){
                            _Conv.add(conversation);
                        }
                    }
                }
                ConversationListAdapter adapter = new ConversationListAdapter(_Conv, getContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Conversation>> call, Throwable t) {

            }
        });
        swiperefresh.setRefreshing(false);
    }
}