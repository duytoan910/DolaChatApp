package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Others.MessageAdapter;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

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
        chatUserName.setText(i.getStringExtra("userObject"));

        messageAdapter.add(new Message("Hi!", i.getStringExtra("userObject"), false));
        txtMessageContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                String message = txtMessageContent.getText().toString().trim();
//                if (message.length() > 0) return false;
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    messageAdapter.add(new Message(txtMessageContent.getText().toString().trim(), "toan", true));
//                    txtMessageContent.getText().clear();
//                }
                return false;
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = txtMessageContent.getText().toString().trim();
                if (message.length() > 0) {
                    messageAdapter.add(new Message(txtMessageContent.getText().toString().trim(), "toan", true));
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