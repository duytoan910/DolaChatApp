package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.Fragments.ConversationListFragment;
import com.example.dolaapp.Others.Fragments.FriendListFragment;
import com.example.dolaapp.Others.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationScreenActivity extends AppCompatActivity {
    ImageButton imgBtnConversation,imgBtnContact,btnNewMessage,btnWaitMessage;
    EditText txtSearchConversation;
    ImageView imgUserSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversastion_screen);

        //Init Controls
        imgUserSetting = findViewById(R.id.imgUserSetting);
        txtSearchConversation = findViewById(R.id.txtSearchConversation);
        btnNewMessage = findViewById(R.id.btnNewMessage);
        btnWaitMessage = findViewById(R.id.btnWaitMessage);
        imgBtnConversation = (ImageButton)findViewById(R.id.imgBtnConversation);
        imgBtnContact = (ImageButton)findViewById(R.id.imgBtnContact);

        ConversationListFragment fragment = new ConversationListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentConversationList, fragment);
        transaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Session sessionManagement = new Session(ConversationScreenActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();
        if(userInfos.get(0) == ""){
            Intent intent = new Intent(ConversationScreenActivity.this, LoginScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void addNewFriend(View view) {
            Intent intent = new Intent(ConversationScreenActivity.this, FindFriendActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void requestMessage(View view) {
        Intent intent = new Intent(ConversationScreenActivity.this, RequestMessageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void userSetting(View view) {
        Intent intent = new Intent(ConversationScreenActivity.this, UserSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void chatListClick(View view) {
        ConversationListFragment fragment = new ConversationListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentConversationList, fragment);
        transaction.commit();

        imgBtnConversation.setImageResource(R.drawable.message_focus_40);
        imgBtnContact.setImageResource(R.drawable.contact_blur_40);
    }

    public void contactListClick(View view) {
        FriendListFragment fragment = new FriendListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentConversationList, fragment);
        transaction.commit();

        imgBtnConversation.setImageResource(R.drawable.message_blur_40);
        imgBtnContact.setImageResource(R.drawable.contact_focus_40);
    }
}