package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.Fragments.ConversationListFragment;

import java.util.ArrayList;
import java.util.List;

public class ConversationScreen extends AppCompatActivity {
    Button imgBtnConversation,imgBtnContact,btnNewMessage,btnWaitMessage;
    EditText txtSearchConversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversastion_screen);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentConversationList, new ConversationListFragment("asdasd"))
                .commit();
    }
}