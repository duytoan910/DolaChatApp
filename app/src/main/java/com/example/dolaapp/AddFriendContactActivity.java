package com.example.dolaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp._AppConfig.AppServices;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp._AppConfig.ExternalServices.JsonObjectGenerator;
import com.example.dolaapp._AppConfig.ExternalServices.SocketIo;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendContactActivity extends AppCompatActivity {

    private ArrayList<User> users;
    ListView listView_friendcontact;
    AddFriendContactAdapter AddFriendContactAdapter;
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
        setContentView(R.layout.activity_add_friend_contact);

        txtSearchUser = findViewById(R.id.txtSearchUser);
        btnSeach = findViewById(R.id.btnSeach);
        listView_friendcontact = (ListView) findViewById(R.id.listView_friendcontact);
        loading = new Loading(AddFriendContactActivity.this);
        sessionManagement = new Session(AddFriendContactActivity.this);
        userInfos = sessionManagement.getSession();
        loading.startLoading();

        Intent intent = getIntent();
        if (intent.getSerializableExtra("userList") != null) {
            ArrayList<User> userList = (ArrayList<User>) intent.getSerializableExtra("userList");
            AddFriendContactAdapter adapter = new AddFriendContactAdapter(userList, AddFriendContactActivity.this);
            listView_friendcontact.setAdapter(adapter);
            loading.stopLoading();
        }
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

class AddFriendContactAdapter extends BaseAdapter implements Filterable {
    private ArrayList<User> list;
    private ArrayList<User> mDisplayedValues;
    private Context context;
    ArrayList<String> userInfos;
    public Socket mSocket= SocketIo.getInstance().getmSocket();

    public AddFriendContactAdapter(
            ArrayList<User> list, Context context) {
        this.list = list;
        this.mDisplayedValues = list;
        this.context = context;
        Session sessionManagement = new Session(context);
        userInfos = sessionManagement.getSession();
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.find_friend_list, null);

        Button btnAdd = ((Button) convertView.findViewById(R.id.btnAdd));

        ((TextView) convertView.findViewById(R.id.txtUserName)).setText(mDisplayedValues.get(position).getUserName() + "");

        new AppServices().setImageToImageView(
                context,
                mDisplayedValues.get(position).getAvatar(),
                convertView.findViewById(R.id.imgUserSetting)
        );
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnAdd.getText().toString().equals("Kết bạn")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Gửi lời mời kết bạn");
                    final EditText input = new EditText(context);
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
                            //m_Text = input.getText().toString();
                            ArrayList<String> arr = new ArrayList<String>(){{add(userInfos.get(1));add(list.get(position).getUserPhone());}};
                            ArrayList<String> arrAd = new ArrayList<String>();

                            ApiService.api.HaveConversation(userInfos.get(1),list.get(position).getUserPhone()).enqueue(new Callback<ArrayList<Conversation>>() {
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
                                                mDisplayedValues.get(position).getUserPhone()
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


                            Toast.makeText(context, "Đã gửi lời mời kết bạn!", Toast.LENGTH_SHORT).show();
                            btnAdd.setText("Hủy lời mời");
                            ApiService.api.SendAddFriendReQuest(userInfos.get(1),list.get(position).getUserPhone()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {}
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {}
                            });

                            try {
                                JSONObject jsonObj = JsonObjectGenerator
                                        .AddFriendRequestJsonObject(list.get(position).getUserPhone(),userInfos.get(0));
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
                    builder.show();
                }else{
                    ApiService.api.DeleteRequestAddFriend(list.get(position).getUserPhone(),userInfos.get(1)).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                    Toast.makeText(context, "Đã hủy lời mời kết bạn!", Toast.LENGTH_SHORT).show();
                    btnAdd.setText("Kết bạn");
                }
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<User>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<User> FilteredArrList = new ArrayList<User>();

                if (list == null) {
                    list = new ArrayList<User>(mDisplayedValues); // saves the original data in list
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the list(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {


                    // set the Original result to return
                    results.count = list.size();
                    results.values = list;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < list.size(); i++) {
                        String data = list.get(i).getUserName();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new User(
                                    list.get(i).getUserEmail(),
                                    list.get(i).getUserPassword(),
                                    list.get(i).getUserPhone(),
                                    list.get(i).getUserName(),
                                    list.get(i).getUserDoB()
                            ));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}