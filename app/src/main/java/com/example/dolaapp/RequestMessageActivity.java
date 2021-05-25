package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Others.Fragments.DeniedListFragment;
import com.example.dolaapp.Others.Fragments.RequestListFragment;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp.Others.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RequestMessageActivity extends AppCompatActivity {
    private ArrayList<Conversation> conversations;
    TextView textView;
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
        textView = findViewById(R.id.textView);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());


        imgBtnRequest = (ImageButton)findViewById(R.id.imgBtnRequest);
        imgBtnDenied = (ImageButton)findViewById(R.id.imgBtnDenied);

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

        imgBtnRequest.setImageResource(R.drawable.user_plus_block);
        imgBtnDenied.setImageResource(R.drawable.user_denied_big_blur);
        textView.setText("Lời mời kết bạn");
    }

    public void deniedMessage(View view) {
        DeniedListFragment fragment = new DeniedListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentRequestList, fragment);
        transaction.commit();

        imgBtnRequest.setImageResource(R.drawable.user_plus_block_big_blur);
        imgBtnDenied.setImageResource(R.drawable.user_denied_big);
        textView.setText("Tin nhắn chờ");
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