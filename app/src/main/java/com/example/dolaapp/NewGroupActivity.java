package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.UserListAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewGroupActivity extends AppCompatActivity {
    ArrayList<String> userInfos;
    ListView listView_NewGroup;
    SwipeRefreshLayout swiperefresh;
    ChipGroup chipGroupUser;
    NewGroupAdapter adapter;
    ArrayList<User> userList;
    ArrayList<User> selectedUser;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        Session sessionManagement = new Session(NewGroupActivity.this);
        userInfos = sessionManagement.getSession();
        selectedUser = new ArrayList<>();

        listView_NewGroup = findViewById(R.id.listView_NewGroup);
        swiperefresh = findViewById(R.id.swiperefresh);
        chipGroupUser = findViewById(R.id.chipGroupUser);

        ApiService.api.getAllListFriend(userInfos.get(1)).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.body()!=null){
                    if(response.body().size()>0){
                        userList = (ArrayList<User>) response.body();
                        adapter = new NewGroupAdapter(userList, NewGroupActivity.this);
                        listView_NewGroup.setAdapter(adapter);

                        intent = getIntent();
                        if(intent.getStringExtra("withUser") != null && !intent.getStringExtra("withUser").isEmpty()){
                            String withUser = intent.getStringExtra("withUser");
                            ArrayList<User> removeIDs = new ArrayList<>();
                            for (User user : userList) {
                                    if(user.getUserPhone().equals(withUser)){
                                        int id = userList.indexOf(user);
                                        listView_NewGroup.performItemClick(
                                                listView_NewGroup.getAdapter().getView(id, null, null)
                                                ,id,
                                                listView_NewGroup.getAdapter().getItemId(id)
                                        );
                                        removeIDs.add(user);
                                        adapter.notifyDataSetChanged();
                                    }
                            }
                            for (User removeID : removeIDs) {
                                userList.remove(removeID);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(NewGroupActivity.this, "Nah", Toast.LENGTH_SHORT).show();
            }
        });

        listView_NewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Boolean returnFlag = false;
                CheckBox check = view.findViewById(R.id.checkBox);

                User sltUser = userList.get(position);
                for (int i=0; i<chipGroupUser.getChildCount();i++){
                    Chip chip = (Chip)chipGroupUser.getChildAt(i);
                    if (chip.getTag().equals(sltUser.getUserPhone())){
                        chipGroupUser.removeViewAt(i);
                        selectedUser.remove(i);
                        check.setChecked(false);

                        returnFlag = true;
                        break;
                    }
                }
                if(returnFlag) return;

                addNewChip(userList.get(position));
                selectedUser.add(userList.get(position));
                check.setChecked(true);
            }
        });
    }
    private void addNewChip(User user) {
        try {
            // Create a Chip from Layout.
            Chip newChip = new Chip(this);
            newChip.setText(user.getUserName());
            newChip.setTag(user.getUserPhone());
            newChip.setSingleLine(true);
            newChip.setHeight(30);
            newChip.setTextSize(18);

            this.chipGroupUser.addView(newChip);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void pressBack(View view) {
        finish();
    }

    public void createGroup(View view) {
        String groupName = "";
        ArrayList<String> createWithUserList = new ArrayList<String>();
        createWithUserList.add(userInfos.get(1));
        for (int i=0; i < chipGroupUser.getChildCount();i++) {
            Chip chip = (Chip) chipGroupUser.getChildAt(i);
            createWithUserList.add(chip.getTag().toString());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewGroupActivity.this);
        builder.setTitle("Tạo nhóm");
        final EditText input = new EditText(NewGroupActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        builder.setView(input);
        params.setMargins(10, 10, 10, 0);
        input.setLayoutParams(params);
        input.setText("");
        builder.setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> arr = new ArrayList<String>();
                ArrayList<String> arrAd = new ArrayList<String>();

                ApiService.api.createConversation(
                        input.getText().toString(),
                        createWithUserList,
                        arrAd,
                        true,
                        false,
                        false,
                        "",
                        ""
                ).enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                    }
                });
            }
        });
        builder.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
class NewGroupAdapter extends BaseAdapter {
    private ArrayList<User> list;
    private Context context;

    public NewGroupAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_new_group, null);

        LinearLayout loItem = convertView.findViewById(R.id.loItem);
        ((TextView) convertView.findViewById(R.id.txtUserName)).setText(list.get(position).getUserName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessage)).setText(list.get(position).getConversationName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessageTime)).setText(list.get(position).getConversationName() + "");
        return convertView;
    }
}
