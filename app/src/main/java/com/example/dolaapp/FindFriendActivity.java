package com.example.dolaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.FindFriendListAdapter;
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp._AppConfig.ExternalServices.JsonObjectGenerator;
import com.example.dolaapp._AppConfig.ExternalServices.SocketIo;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFriendActivity extends AppCompatActivity {
    public int CODE_SCAN_QR = 15982;

    private ArrayList<User> users;
    ListView listView_FindFriend;
    FindFriendListAdapter findFriendListAdapter;
    EditText txtSearchUser;
    ImageButton btnSeach;
    SwipeRefreshLayout swiperefresh;
    Session sessionManagement;
    Loading loading;
    ArrayList<String> userInfos;
    public Socket mSocket= SocketIo.getInstance().getmSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        swiperefresh = findViewById(R.id.swiperefresh);
        txtSearchUser = findViewById(R.id.txtSearchUser);
        btnSeach = findViewById(R.id.btnSeach);
        listView_FindFriend = (ListView) findViewById(R.id.listView_FindFriend);
        loading = new Loading(FindFriendActivity.this);
        sessionManagement = new Session(FindFriendActivity.this);
        userInfos = sessionManagement.getSession();

        reloadList();
        btnSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadList();
            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading.startLoading();
                ApiService.api.SearchAccountByName(userInfos.get(1),txtSearchUser.getText().toString()).enqueue(new Callback<ArrayList<User>>() {
                    @Override
                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                        if(response.body()==null || response.body().size()<=0) {

                            loading.stopLoading();
                            return;
                        }
                        findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                        listView_FindFriend.setAdapter(findFriendListAdapter);

                        loading.stopLoading();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                        Toast.makeText(FindFriendActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        loading.stopLoading();
                    }
                });
                swiperefresh.setRefreshing(false);
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
    private void reloadList(){
        loading.startLoading();
        ApiService.api.SearchAccountByName(userInfos.get(1),"").enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.body()==null || response.body().size()<=0) {

                    loading.stopLoading();
                    return;
                }
                findFriendListAdapter = new FindFriendListAdapter((ArrayList<User>) response.body(),FindFriendActivity.this);
                listView_FindFriend.setAdapter(findFriendListAdapter);
                loading.stopLoading();
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(FindFriendActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                loading.stopLoading();
            }
        });
    }

    public void qrScan(View view) {
        Intent intent = new Intent(FindFriendActivity.this, QRScanActivity.class);
        startActivityForResult(intent, CODE_SCAN_QR);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode == CODE_SCAN_QR && data != null){
            loading.startLoading();
            String userName = data.getStringExtra("scannedUserPhone").split("\\|")[0];
            String userPhone = data.getStringExtra("scannedUserPhone").split("\\|")[1];
            AlertDialog.Builder builder = new AlertDialog.Builder(FindFriendActivity.this);
            builder.setTitle("Gửi lời mời kết bạn đến " + userName);
            final EditText input = new EditText(FindFriendActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            builder.setView(input);
            params.setMargins(10, 10, 10, 0);
            input.setLayoutParams(params);
            input.setText("Xin chào! Mình kết bạn nha.");
            builder.setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ArrayList<String> arr = new ArrayList<String>(){{add(userInfos.get(1));add(userPhone);}};
                    ArrayList<String> arrAd = new ArrayList<String>();

                    ApiService.api.HaveConversation(userInfos.get(1),userPhone).enqueue(new Callback<ArrayList<Conversation>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Conversation>> call, Response<ArrayList<Conversation>> response) {
                            if(response.body()== null || response.body().size()==0){
                                ApiService.api.createConversation(
                                        "",
                                        arr,
                                        arrAd,
                                        false,
                                        true,
                                        false,
                                        userInfos.get(1),
                                        userPhone
                                ).enqueue(new Callback<Conversation>() {
                                    @Override
                                    public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                                        ApiService.api.createMessage(
                                                input.getText().toString().trim(),
                                                userInfos.get(1),
                                                response.body().getConversationID(),
                                                userInfos.get(0),
                                                ((Date) Calendar.getInstance().getTime()).toString(),
                                                userInfos.get(0))
                                                .enqueue(new Callback<Message>() {
                                                    @Override
                                                    public void onResponse(Call<Message> call, Response<Message> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Message> call, Throwable t) {

                                                    }
                                                });
                                    }
                                    @Override
                                    public void onFailure(Call<Conversation> call, Throwable t) {

                                    }
                                });
                            }else{
                                ApiService.api.createMessage(
                                        input.getText().toString().trim(),
                                        userInfos.get(1),
                                        response.body().get(0).getConversationID(),
                                        userInfos.get(0),
                                        ((Date) Calendar.getInstance().getTime()).toString(),
                                        userInfos.get(0))
                                        .enqueue(new Callback<Message>() {
                                            @Override
                                            public void onResponse(Call<Message> call, Response<Message> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<Message> call, Throwable t) {

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Conversation>> call, Throwable t) {

                        }
                    });

                    Toast.makeText(FindFriendActivity.this, "Đã gửi lời mời kết bạn!", Toast.LENGTH_SHORT).show();

                    ApiService.api.SendAddFriendReQuest(userInfos.get(1),userPhone).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {}
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {}
                    });

                    try {
                        JSONObject jsonObj = JsonObjectGenerator
                                .AddFriendRequestJsonObject(userPhone,userInfos.get(0));
                        mSocket.emit("Send-Add-Friend-Request",jsonObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            ApiService.api.isFriend(userInfos.get(1), userPhone).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body()=="false"){

                        ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response1) {
                                boolean isExist = false;
                                if(response1.body().getRequestSend().size()>0){
                                    for (String requestSend : response1.body().getRequestSend()) {
                                        if(requestSend.equals(userPhone)){
                                            isExist = true;
                                            loading.stopLoading();
                                            Toast.makeText(FindFriendActivity.this, "Bạn đã gửi lời mời kết bạn đến " + userName + " rồi!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                if(!isExist){
                                    ApiService.api.getAllListRequest(userInfos.get(1)).enqueue(new Callback<ArrayList<User>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response2) {
                                            if (response2.body()==null || response2.body().size() == 0) {
                                                loading.stopLoading();
                                                builder.show();
                                                return;
                                            }
                                            boolean isExist2 = false;
                                            for (User user : response2.body()) {
                                                if(user.getUserPhone().equals(userPhone)){
                                                    isExist2 = true;
                                                    loading.stopLoading();
                                                    Toast.makeText(FindFriendActivity.this, userName +" đã gửi cho bạn một lời mời trước đó!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            if(!isExist2){
                                                builder.show();
                                                loading.stopLoading();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                                            Toast.makeText(FindFriendActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                            loading.stopLoading();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(FindFriendActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                loading.stopLoading();
                            }
                        });

                    }else{
                        Toast.makeText(FindFriendActivity.this, userName + " đã là bạn bè!", Toast.LENGTH_SHORT).show();
                        loading.stopLoading();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(FindFriendActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    loading.stopLoading();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void searchContact(View view) {
        loading.startLoading();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            ApiService.api.getListUserByListIDFilter(userInfos.get(1), getContactList()).enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                    if(response.body()!=null && response.body().size()>0 ){
                        loading.stopLoading();
                        Intent intent = new Intent(FindFriendActivity.this, AddFriendContactActivity.class);
                        intent.putExtra("userList", response.body());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else{
                        Toast.makeText(FindFriendActivity.this, "Không tìm thấy tài khoản DôLa nào trong danh bạ!", Toast.LENGTH_SHORT).show();
                        loading.stopLoading();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                    Toast.makeText(FindFriendActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    loading.stopLoading();
                }
            });
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(FindFriendActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
            }
        }
    }

    private ArrayList<String> getContactList() {
        ArrayList<String> arr = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        arr.add(phoneConvert(phoneNo));
                    }
                    pCur.close();
                }
            }
            arr = new ArrayList<String>(new LinkedHashSet<String>(arr));
        }
        if(cur!=null){
            cur.close();
        }
        return arr;
    }

    public String phoneConvert(String phone){
        try{
            return ((((phone.split("\\(")[1].split("\\)")[0] + phone.split("\\(")[1].split("\\)")[1]).split(" ")[0])+(phone.split("\\(")[1].split("\\)")[0] + phone.split("\\(")[1].split("\\)")[1]).split(" ")[1]).split("-")[0] + (((phone.split("\\(")[1].split("\\)")[0] + phone.split("\\(")[1].split("\\)")[1]).split(" ")[0]) + (phone.split("\\(")[1].split("\\)")[0] + phone.split("\\(")[1].split("\\)")[1]).split(" ")[1]).split("-")[1]).trim();
        }catch (Exception E){
            return phone.trim();
        }
    }
}