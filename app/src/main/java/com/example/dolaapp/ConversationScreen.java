package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Others.ConversationListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConversationScreen extends AppCompatActivity {
    private ArrayList<Conversation> conversations;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversastion_screen);

        listView = findViewById(R.id.listView);

        conversations = new ArrayList<Conversation>();
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", "19:00"));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", "9:00"));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", "19:00"));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", "9:00"));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", "19:00"));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", "9:00"));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", "19:00"));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", "9:00"));
        conversations.add(new Conversation("Châu Nguyễn Duy Toàn", "Xin chào Toàn!", "19:00"));
        conversations.add(new Conversation("Phan Trọng Hinh", "Xin chào Hinh!", "9:00"));
        ConversationListAdapter adapter = new ConversationListAdapter(conversations, this);
        listView.setAdapter(adapter);
    }
}