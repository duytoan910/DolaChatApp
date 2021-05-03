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

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Others.ConversationListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RequestMessageActivity extends AppCompatActivity {
    private ArrayList<Conversation> conversations;
    ListView listView_RequestMessage;
    SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_message);

        listView_RequestMessage = findViewById(R.id.listView_RequestMessage);
        swiperefresh = findViewById(R.id.swiperefresh);

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        conversations = new ArrayList<Conversation>();
//        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
//        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
//        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
//        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
//        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
//        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
//        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
//        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
//        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", currentTime));
//        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", currentTime));
        ConversationListAdapter adapter = new ConversationListAdapter(conversations, RequestMessageActivity.this);
        listView_RequestMessage.setAdapter(adapter);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Collections.shuffle(conversations, new Random(System.currentTimeMillis()));
                ConversationListAdapter adapter = new ConversationListAdapter(conversations, RequestMessageActivity.this);
                listView_RequestMessage.setAdapter(adapter);
                swiperefresh.setRefreshing(false);
            }
        });
        listView_RequestMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent result = new Intent(RequestMessageActivity.this, ChatScreenActivity.class);
//                result.putExtra("userObject", conversations.get(position).getUserName());
//                startActivity(result);
            }
        });
        listView_RequestMessage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(RequestMessageActivity.this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

                Log.e("Long click: ","Long press at " + position);

                return false;
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