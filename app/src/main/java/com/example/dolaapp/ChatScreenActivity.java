package com.example.dolaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dolaapp._AppConfig.AppServices;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Conversaion_Group_BottomSheet;
import com.example.dolaapp.Others.Conversaion_U2U_BottomSheet;
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp.Others.MessageAdapter;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp._AppConfig.ExternalServices.JsonObjectGenerator;
import com.example.dolaapp._AppConfig.ExternalServices.SocketIo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatScreenActivity extends AppCompatActivity {
    TextView chatUserName;
    ImageButton btnSend,btnReturn;
    EmojiconEditText txtMessageContent;
    ListView messageListView;
    SimpleDateFormat myFormat;
    MessageAdapter messageAdapter;
    Conversation conv;
    ArrayList<String> userInfos;
    Loading loading;
    Session sessionManagement;

    public Socket mSocket= SocketIo.getInstance().getmSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        chatUserName = findViewById(R.id.chatUserName);
        btnSend = findViewById(R.id.btnSend);
        btnReturn = findViewById(R.id.btnReturn);
        txtMessageContent = findViewById(R.id.txtMessageContent);
        messageListView = findViewById(R.id.messageListView);

        sessionManagement = new Session(ChatScreenActivity.this);
        userInfos = sessionManagement.getSession();
        loading = new Loading(ChatScreenActivity.this);
        loading.startLoading();

        myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");

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
        SocketIOSetting();
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
                            new AppServices().setImageToImageView(ChatScreenActivity.this, response.body().getAvatar(), findViewById(R.id.imgUserSetting));
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
                loading.stopLoading();
            }
            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                Toast.makeText(ChatScreenActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                loading.stopLoading();
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
                            messageAdapter.add(new Message(
                                    txtMessageContent.getText().toString().trim(),
                                    response.body().getMessageId(),
                                    userInfos.get(0),
                                    conv.getConversationID(),
                                    userInfos.get(1),
                                    myFormat.format(Calendar.getInstance().getTime()),
                                    true
                            ));

                            txtMessageContent.getText().clear();
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {

                        }
                    });
                    SocketEmitChatMessage(
                            "12",
                            userInfos.get(0),
                            userInfos.get(1),
                            conv.getConversationID(),
                            txtMessageContent.getText().toString().trim(),
                            myFormat.format(Calendar.getInstance().getTime())
                            );
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

    private void SocketIOSetting() {
        String GroupId = conv.getConversationID();
        mSocket.emit("join-conversation",GroupId);
        mSocket.on("Add-Message-To-Screen",receivermessage);
    }
    private void SocketEmitChatMessage (
            String MessId,
            String NameSender,
            String Sender,
            String Receiver,
            String Message,
            String time)
    {
        Message messageObj = new Message(Message,MessId,NameSender,Receiver,Sender,time,false);
        mSocket.emit("Send-Message-To-Conversation", JsonObjectGenerator.MessageJsonObject(messageObj, conv.getConversationMember()));
    }

    @Override
    public void finish() {
        super.finish();
        mSocket.emit("leave-conversation",conv.getConversationID());
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

    private Emitter.Listener receivermessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        messageAdapter.add(new Message(
                                data.getString("Message"),
                                data.getString("Id"),
                                data.getString("NameSender"),
                                data.getString("Receiver"),
                                data.getString("Sender"),
                                data.getString("Time"),
                                data.getBoolean("belongtocurrentuser")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}