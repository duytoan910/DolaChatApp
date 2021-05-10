package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.MessageAdapter;
import com.example.dolaapp.Others.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        chatUserName = findViewById(R.id.chatUserName);
        btnSend = findViewById(R.id.btnSend);
        btnReturn = findViewById(R.id.btnReturn);
        txtMessageContent = findViewById(R.id.txtMessageContent);
        messageListView = findViewById(R.id.messageListView);

        EmojIconActions emojIcon=new EmojIconActions(this,findViewById(R.id.rootView),txtMessageContent,findViewById(R.id.emojicon_icon),
                "#495C66",
                "#DCE1E2",
                "#E6EBEF");
        emojIcon.ShowEmojIcon();
        messageAdapter = new MessageAdapter(ChatScreenActivity.this);
        messageListView.setAdapter(messageAdapter);

        Intent i = getIntent();
        Session sessionManagement = new Session(ChatScreenActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();
        Conversation conv = ((Conversation)i.getSerializableExtra("conversationObject"));
        if(conv.isGroup()) {
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
                    ApiService.api.getUserById(message.getSender()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> callUser, Response<User> responseUser) {
                            String[] parts = responseUser.body().getUserName().split(" ");
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

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });

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
                            ((Date)Calendar.getInstance().getTime()).toString()
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
}