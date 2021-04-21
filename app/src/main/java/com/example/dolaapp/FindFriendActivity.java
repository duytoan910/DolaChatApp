package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.FindFriendListAdapter;

import java.util.ArrayList;

public class FindFriendActivity extends AppCompatActivity {
    private ArrayList<User> users;
    ListView listView_FindFriend;
    FindFriendListAdapter findFriendListAdapter;
    EditText txtSearchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        txtSearchUser = findViewById(R.id.txtSearchUser);

        users = new ArrayList<User>();
        users.add(new User("Châu Nguyễn Duy Toàn", "", "", "Châu Nguyễn Duy Toàn", ""));
        users.add(new User("Phan Trọng Hinh", "", "", "Phan Trọng Hinh", ""));
        users.add(new User("Châu Nguyễn Duy Toàn", "", "", "zxzx qw x aweq", ""));
        users.add(new User("Phan Trọng Hinh", "", "", "Trần Ánh Kim", ""));
        users.add(new User("Châu Nguyễn Duy Toàn", "", "", "Nguyễn Văn A", ""));
        users.add(new User("Phan Trọng Hinh", "", "", "Nguyễn Thị B", ""));
        listView_FindFriend = (ListView) findViewById(R.id.listView_FindFriend);
        FindFriendListAdapter findFriendListAdapter = new FindFriendListAdapter(users,FindFriendActivity.this);
        listView_FindFriend.setAdapter(findFriendListAdapter);

        txtSearchUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    if(txtSearchUser.getText().toString() == ""){
                        findFriendListAdapter.getFilter().filter(" ");
                    }else findFriendListAdapter.getFilter().filter(txtSearchUser.getText().toString());
                }
                return false;
            }
        });
        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString() == ""){
                    findFriendListAdapter.getFilter().filter(" ");
                }else findFriendListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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