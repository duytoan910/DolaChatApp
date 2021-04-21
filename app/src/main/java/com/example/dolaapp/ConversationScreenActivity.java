package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.Fragments.ConversationListFragment;
import com.example.dolaapp.Others.Session;

import java.util.ArrayList;
import java.util.List;

public class ConversationScreenActivity extends AppCompatActivity {
    Button imgBtnConversation,imgBtnContact,btnNewMessage,btnWaitMessage;
    EditText txtSearchConversation;
    ImageView imgUserSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgUserSetting = findViewById(R.id.imgUserSetting);

        setContentView(R.layout.activity_conversastion_screen);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentConversationList, new ConversationListFragment("asdasd"))
                .commit();

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

    public void userSetting(View view) {
        Intent intent = new Intent(ConversationScreenActivity.this, UserSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
    }
}