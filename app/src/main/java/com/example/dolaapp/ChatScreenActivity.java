package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Conversaion_Group_BottomSheet;
import com.example.dolaapp.Others.Conversaion_U2U_BottomSheet;
import com.example.dolaapp.Others.MessageAdapter;
import com.example.dolaapp.Others.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatScreenActivity extends AppCompatActivity {
    TextView chatUserName;
    ImageButton btnSend,btnReturn;
    EmojiconEditText txtMessageContent;
    ListView messageListView;

    MessageAdapter messageAdapter;
    Intent i;
    Conversation conv;
    ArrayList<String> userInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        chatUserName = findViewById(R.id.chatUserName);
        btnSend = findViewById(R.id.btnSend);
        btnReturn = findViewById(R.id.btnReturn);
        txtMessageContent = findViewById(R.id.txtMessageContent);
        messageListView = findViewById(R.id.messageListView);

        Session sessionManagement = new Session(ChatScreenActivity.this);
        userInfos = sessionManagement.getSession();

        EmojIconActions emojIcon=new EmojIconActions(this,findViewById(R.id.rootView),txtMessageContent,findViewById(R.id.emojicon_icon),
                "#495C66",
                "#DCE1E2",
                "#E6EBEF");
        emojIcon.ShowEmojIcon();
        messageAdapter = new MessageAdapter(ChatScreenActivity.this);
        messageListView.setAdapter(messageAdapter);

        Intent i = getIntent();

        if(i.getBooleanExtra("isRequest", false)){
            ((ImageButton) findViewById(R.id.btnWaitMessage)).setVisibility(View.GONE);
        }

        conv = ((Conversation)i.getSerializableExtra("conversationObject"));
        if(conv.isGroupChat()) {
            chatUserName.setText(conv.getConversationName() + "");
        }else {
            for (String s : conv.getConversationMember()) {
                if (s.equals(userInfos.get(1))) {
                    continue;
                } else {
                    ApiService.api.getUserById(s).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            chatUserName.setText(response.body().getUserName());
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        }

        ApiService.api.getAllMessageByGroupId(conv.getConversationID()).enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                for (Message message : response.body()) {

                    String[] parts = message.getNameSender().split(" ");
                    String lastWord = parts[parts.length - 1];
                    messageAdapter.add(new Message(
                            message.getMessage(),
                            message.getMessageId(),
                            lastWord,
                            message.getReceiver(),
                            message.getSender(),
                            message.getTimeStamp(),
                            message.getSender().equals(userInfos.get(1))?true:false
                    ));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {

            }
        });

        txtMessageContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String message = txtMessageContent.getText().toString().trim();
                if (message.length() > 0) return false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    messageAdapter.add(new Message(
                            txtMessageContent.getText().toString().trim(),
                            "message.getMessageId()",
                            userInfos.get(0),
                            conv.getConversationID(),
                            userInfos.get(1),
                            ((Date)Calendar.getInstance().getTime()).toString(),
                            true
                    ));

                    txtMessageContent.getText().clear();
               }
                return false;
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = txtMessageContent.getText().toString().trim();
                if (message.length() > 0) {
                    ApiService.api.createMessage(
                            txtMessageContent.getText().toString().trim(),
                            userInfos.get(1),
                            conv.getConversationID(),
                            userInfos.get(0),
                            ((Date)Calendar.getInstance().getTime()).toString(),
                            userInfos.get(0)
                    ).enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {

                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {

                        }
                    });
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                    messageAdapter.add(new Message(
                            txtMessageContent.getText().toString().trim(),
                            "message.getMessageId()",
                            userInfos.get(0),
                            conv.getConversationID(),
                            userInfos.get(1),
                            myFormat.format(Calendar.getInstance().getTime()),
                            true
                    ));
                    txtMessageContent.getText().clear();
                }
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void convInfo(View view) {
        if(conv.isGroupChat()){
            Conversaion_Group_BottomSheet modal = new Conversaion_Group_BottomSheet(conv);
            modal.show(getSupportFragmentManager(), "info_group_Modal");
        }else{
            for (String s : conv.getConversationMember()) {
                if(!s.equals(userInfos.get(1))){
                    ApiService.api.getUserById(s).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Conversaion_U2U_BottomSheet modal = new Conversaion_U2U_BottomSheet(response.body());
                            modal.show(getSupportFragmentManager(), "info_u2u_Modal");
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }

        }
    }
}