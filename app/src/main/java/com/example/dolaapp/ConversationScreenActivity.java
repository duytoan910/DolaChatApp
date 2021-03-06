package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.Others.Fragments.ConversationListFragment;
import com.example.dolaapp.Others.Fragments.FriendListFragment;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp._AppConfig.AppServices;
import com.example.dolaapp._AppConfig.ExternalServices.SocketIo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ConversationScreenActivity extends AppCompatActivity {
    ImageButton imgBtnConversation,imgBtnContact,btnNewMessage,btnWaitMessage;
    EditText txtSearchConversation;
    TextView textView;
    ImageView imgUserSetting;
    Session sessionManagement;
    ConversationListFragment fragment;
    final Random myRandom = new Random();

    public Socket mSocket= SocketIo.getInstance().getmSocket();
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
        textView = (TextView) findViewById(R.id.textView);

        sessionManagement = new Session(ConversationScreenActivity.this);
        SocketIOSetting();


        fragment = new ConversationListFragment("asdasd");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentConversationList, fragment);
        transaction.commit();

        Intent intent = getIntent();
        if(intent.getStringExtra("isDenied") != null &&intent.getStringExtra("isDenied").equals("true")){
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run() {
                            imgBtnContact.performClick();
                        }
                    },
                    500);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionManagement = new Session(ConversationScreenActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();
        if(userInfos.get(0) == ""){
            Intent intent = new Intent(ConversationScreenActivity.this, LoginScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{

            new AppServices().setImageToImageView(ConversationScreenActivity.this,userInfos.get(3), findViewById(R.id.userAvt));
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

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
        textView.setText("C??c ??o???n h???i tho???i");
    }

    public void contactListClick(View view) {
        FriendListFragment fragment = new FriendListFragment("zx13");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentConversationList, fragment);
        transaction.commit();

        imgBtnConversation.setImageResource(R.drawable.message_blur_40);
        imgBtnContact.setImageResource(R.drawable.contact_focus_40);
        textView.setText("Danh s??ch b???n b??");
    }

    public void newGroup(View view) {
        Intent intent = new Intent(ConversationScreenActivity.this, NewGroupActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void SocketIOSetting() {
        String UserPhone = sessionManagement.getSession().get(1);
        mSocket.emit("new-connection",UserPhone);
        mSocket.on("ReloadConversationScreen",ReloadConversationScreen);
        mSocket.on("Have-new-add-friendRequest",NotificationFriendRequest);
        mSocket.on("TargetUser-Accepted-Request",NotificationFriendRequestAccepted);
    }

    private Emitter.Listener ReloadConversationScreen = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        new AppServices().createMessageNotification(
                                ConversationScreenActivity.this,
                                data.getString("Message"),
                                data.getString("NameSender")
                        );
                        fragment.reloadlist();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener NotificationFriendRequest = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        new AppServices().createNewFriendNotification(
                                ConversationScreenActivity.this,
                                data.getString("SenderName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener NotificationFriendRequestAccepted = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        new AppServices().createAcceptFriendNotification(
                                ConversationScreenActivity.this,
                                data.getString("AcceptorName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}