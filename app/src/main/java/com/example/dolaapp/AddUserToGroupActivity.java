package com.example.dolaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp._AppConfig.AppServices;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserToGroupActivity extends AppCompatActivity {
    ArrayList<String> userInfos;
    ListView listView_NewGroup;
    SwipeRefreshLayout swiperefresh;
    ChipGroup chipGroupUser;
    AddUserToGroupAdapter adapter;
    ArrayList<User> userList;
    ArrayList<User> selectedUser;
    Intent intent;
    Button btnNewGroup;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);
        Session sessionManagement = new Session(AddUserToGroupActivity.this);
        userInfos = sessionManagement.getSession();
        selectedUser = new ArrayList<>();

        listView_NewGroup = findViewById(R.id.listView_NewGroup);
        swiperefresh = findViewById(R.id.swiperefresh);
        chipGroupUser = findViewById(R.id.chipGroupUser);
        btnNewGroup = findViewById(R.id.btnNewGroup);
        loading = new Loading(AddUserToGroupActivity.this);
        loading.startLoading();

        intent = getIntent();
        Conversation conv = (Conversation) intent.getSerializableExtra("conversationObject");
        if(conv!=null){
            ApiService.api.GetListUserFriendNotInGroup(userInfos.get(1), conv.getConversationID()).enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                    if(response.body() == null || response.body().size() ==0){
                        loading.stopLoading();
                        return;
                    }
                    userList = response.body();
                    adapter = new AddUserToGroupAdapter(userList, AddUserToGroupActivity.this);
                    listView_NewGroup.setAdapter(adapter);
                    loading.stopLoading();

                }

                @Override
                public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                    Toast.makeText(AddUserToGroupActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    loading.stopLoading();
                }
            });
        }

        listView_NewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Boolean returnFlag = false;
                CheckBox check = view.findViewById(R.id.checkBox);

                User sltUser = userList.get(position);
                for (int i=0; i<chipGroupUser.getChildCount();i++){
                    Chip chip = (Chip)chipGroupUser.getChildAt(i);
                    if (chip.getTag().equals(sltUser.getUserPhone())) {
                        chipGroupUser.removeViewAt(i);
                        selectedUser.remove(i);
                        check.setChecked(false);

                        if (chipGroupUser.getChildCount() == 0) {
                            btnNewGroup.setText("Thêm vào nhóm");
                        } else{
                            btnNewGroup.setText("Thêm vào nhóm (" + chipGroupUser.getChildCount() + ")");
                        };

                        returnFlag = true;
                        break;
                    }
                }
                if(returnFlag) return;

                addNewChip(userList.get(position));
                selectedUser.add(userList.get(position));
                check.setChecked(true);

                if (chipGroupUser.getChildCount() == 0) {
                    btnNewGroup.setText("Thêm vào nhóm");
                } else{
                    btnNewGroup.setText("Thêm vào nhóm (" + chipGroupUser.getChildCount() + ")");
                };
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
        if(chipGroupUser.getChildCount() < 1 ){
            new AlertDialog.Builder(AddUserToGroupActivity.this)
                    .setTitle("Thông báo")
                    .setMessage("Vui lòng chọn ít nhất một người bạn")
                    .setNeutralButton("Đã hiểu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return;
        }

        loading.startLoading();

        Conversation conv = (Conversation) intent.getSerializableExtra("conversationObject");
        ArrayList<String> addUserToConv = new ArrayList<String>();
        for (int i=0; i < chipGroupUser.getChildCount();i++) {
            Chip chip = (Chip) chipGroupUser.getChildAt(i);
            addUserToConv.add(chip.getTag().toString());
        }
        ApiService.api.AddListMember(conv.getConversationID(), addUserToConv).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading.stopLoading();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddUserToGroupActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                loading.stopLoading();
            }
        });
    }
}

class AddUserToGroupAdapter extends BaseAdapter {
    private ArrayList<User> list;
    private Context context;

    public AddUserToGroupAdapter(ArrayList<User> list, Context context) {
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

        new AppServices().setImageToImageView(
                context,
                list.get(position).getAvatar(),
                convertView.findViewById(R.id.imgUserSetting)
        );
//        ((TextView) convertView.findViewById(R.id.txtUserMessage)).setText(list.get(position).getConversationName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessageTime)).setText(list.get(position).getConversationName() + "");
        return convertView;
    }
}
