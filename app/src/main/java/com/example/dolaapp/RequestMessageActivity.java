package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.FindFriendListAdapter;
import com.example.dolaapp.Others.Fragments.ConversationListFragment;
import com.example.dolaapp.Others.Fragments.DeniedListFragment;
import com.example.dolaapp.Others.Fragments.FriendListFragment;
import com.example.dolaapp.Others.Fragments.RequestListFragment;
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
    ImageButton imgBtnRequest;
    ImageButton imgBtnDenied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_message);

        swiperefresh = findViewById(R.id.swiperefresh);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());


        imgBtnRequest = (ImageButton)findViewById(R.id.imgBtnConversation);
        imgBtnDenied = (ImageButton)findViewById(R.id.imgBtnContact);

        Session sessionManagement = new Session(RequestMessageActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();
        listRequestCurrentUser = new ArrayList<Conversation>();

        RequestListFragment fragment = new RequestListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentRequestList, fragment);
        transaction.commit();

    }

    public void requestMessage(View view) {
        RequestListFragment fragment = new RequestListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentRequestList, fragment);
        transaction.commit();

        //imgBtnRequest.setImageResource(R.drawable.message_focus_40);
        //imgBtnDenied.setImageResource(R.drawable.contact_blur_40);
    }

    public void deniedMessage(View view) {
        DeniedListFragment fragment = new DeniedListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentRequestList, fragment);
        transaction.commit();

        //imgBtnRequest.setImageResource(R.drawable.message_blur_40);
        //imgBtnDenied.setImageResource(R.drawable.contact_focus_40);
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