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

import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.R;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ConversationListFragment extends Fragment {
    private ArrayList<Conversation> conversations;
    ListView listView;
    SwipeRefreshLayout swiperefresh;

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

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        conversations = new ArrayList<Conversation>();
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
        ConversationListAdapter adapter = new ConversationListAdapter(conversations, getContext());
        listView.setAdapter(adapter);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Collections.shuffle(conversations, new Random(System.currentTimeMillis()));
                ConversationListAdapter adapter = new ConversationListAdapter(conversations, getContext());
                listView.setAdapter(adapter);
                swiperefresh.setRefreshing(false);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent(getContext(), ChatScreenActivity.class);
                result.putExtra("userObject", conversations.get(position).getUserName());
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
}