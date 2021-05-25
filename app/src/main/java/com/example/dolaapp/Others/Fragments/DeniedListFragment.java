package com.example.dolaapp.Others.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.RequestListAdapter;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeniedListFragment extends Fragment {
    private ArrayList<Conversation> conversations;
    ListView listView;
    SwipeRefreshLayout swiperefresh;;
    ListView listView_RequestMessage;
    DeniedListAdapter findFriendListAdapter;
    ArrayList<String> userInfos;

    private String userID;

    public DeniedListFragment() {
        // Required empty public constructor
    }
    public DeniedListFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        listView_RequestMessage = view.findViewById(R.id.listView);
        listView = view.findViewById(R.id.listView);
        swiperefresh = view.findViewById(R.id.swiperefresh);

        Session sessionManagement = new Session(getContext());
        userInfos = sessionManagement.getSession();

        ApiService.api.getDeniedList(userInfos.get(1)).enqueue(new Callback<ArrayList<Conversation>>() {
            @Override
            public void onResponse(Call<ArrayList<Conversation>> call, Response<ArrayList<Conversation>> response) {
                if(response.body().size()==0){
                    return;
                }
                conversations = (ArrayList<Conversation>) response.body();
                ArrayList<Conversation> _Conv = new ArrayList<>();
                for (Conversation conversation : conversations) {
                    if(conversation.getReceiver().equals(userInfos.get(1))){
                        if(conversation.isReceiverShown() == false){
                            _Conv.add(conversation);
                        }
                    }else{
                        if(conversation.isSenderShown() == false){
                            _Conv.add(conversation);
                        }
                    }
                }
                findFriendListAdapter = new DeniedListAdapter(_Conv, getContext());
                listView_RequestMessage.setAdapter(findFriendListAdapter);

                conversations = _Conv;
            }

            @Override
            public void onFailure(Call<ArrayList<Conversation>> call, Throwable t) {

            }
        });

        listView_RequestMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent(getContext(), ChatScreenActivity.class);
                result.putExtra("conversationObject", conversations.get(position));
                result.putExtra("isRequest", true);
                startActivity(result);
            }
        });

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiService.api.getDeniedList(userInfos.get(1)).enqueue(new Callback<ArrayList<Conversation>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Conversation>> call, Response<ArrayList<Conversation>> response) {
                        if(response.body().size()==0){
                            return;
                        }
                        conversations = (ArrayList<Conversation>) response.body();
                        ArrayList<Conversation> _Conv = new ArrayList<>();
                        for (Conversation conversation : conversations) {
                            if(conversation.getReceiver().equals(userInfos.get(1))){
                                if(conversation.isReceiverShown() == false){
                                    _Conv.add(conversation);
                                }
                            }else{
                                if(conversation.isSenderShown() == false){
                                    _Conv.add(conversation);
                                }
                            }
                        }
                        findFriendListAdapter = new DeniedListAdapter(_Conv, getContext());
                        listView_RequestMessage.setAdapter(findFriendListAdapter);

                        conversations = _Conv;
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Conversation>> call, Throwable t) {

                    }
                });
                swiperefresh.setRefreshing(false);
            }
        });
        return view;
    }
}
class DeniedListAdapter extends BaseAdapter {
    private ArrayList<Conversation> list;
    private Context context;
    ArrayList<String> userInfos;
    User otherUser;

    public DeniedListAdapter(ArrayList<Conversation> list, Context context) {
        this.list = list;
        this.context = context;
        Session sessionManagement = new Session(context);
        userInfos = sessionManagement.getSession();
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
        convertView = inflater.inflate(R.layout.denied_list, null);

        TextView btnReject = convertView.findViewById(R.id.btnReject);

        for (String s : list.get(position).getConversationMember()) {
            if (s.equals(userInfos.get(1))) {
                continue;
            } else {
                View finalConvertView = convertView;
                ApiService.api.getUserById(s).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        otherUser = response.body();
                        ((TextView) finalConvertView.findViewById(R.id.txtUserName)).setText(response.body().getUserName());
//        ((TextView) convertView.findViewById(R.id.txtUserMessage)).setText(list.get(position).getConversationName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessageTime)).setText(list.get(position).getConversationName() + "");

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        }
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Nhận tin nhắn?")
                        .setMessage("Bạn có muốn nhận tin nhắn từ " + otherUser.getUserName() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Nhận tin nhắn", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ApiService.api.SwitchConversationStateShow(list.get(position).getConversationID(),userInfos.get(1)).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNeutralButton("Trở về", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        return convertView;
    }
}