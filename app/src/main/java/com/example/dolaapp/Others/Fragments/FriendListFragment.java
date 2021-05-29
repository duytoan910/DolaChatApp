package com.example.dolaapp.Others.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.Others.Conversaion_Group_BottomSheet;
import com.example.dolaapp.Others.Conversaion_U2U_BottomSheet;
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.Others.UserListAdapter;
import com.example.dolaapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListFragment extends Fragment {
    private ArrayList<User> userList;
    ListView listViewFriendList;
    SwipeRefreshLayout swiperefresh;

    ArrayList<String> userInfos;

    private String userID;

    public FriendListFragment() {
        // Required empty public constructor
    }
    public FriendListFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        Session sessionManagement = new Session(getContext());
        userInfos = sessionManagement.getSession();

        listViewFriendList = view.findViewById(R.id.listViewFriendList);
        swiperefresh = view.findViewById(R.id.swiperefresh);

        reloadList();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadList();
                swiperefresh.setRefreshing(false);
            }
        });
        listViewFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApiService.api.GetConversationOf2Users(
                        userList.get(position).getUserPhone(),
                        userInfos.get(1)).enqueue(new Callback<Conversation>() {
                    @Override
                    public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                        Intent result = new Intent(getContext(), ChatScreenActivity.class);
                        result.putExtra("conversationObject", response.body());
                        startActivity(result);
                    }

                    @Override
                    public void onFailure(Call<Conversation> call, Throwable t) {

                    }
                });
            }
        });
        listViewFriendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Conversaion_U2U_BottomSheet modal = new Conversaion_U2U_BottomSheet(userList.get(position), true);
                modal.show(getFragmentManager(),"info_u2u_Modal");

                return true;
            }
        });

        return view;
    }
    public void reloadList(){
        Loading load = new Loading(getActivity());
        load.startLoading();
        ApiService.api.getAllListFriend(userInfos.get(1)).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.body()!=null){
                    if(response.body().size()>0){
                        userList = (ArrayList<User>) response.body();
                        UserListAdapter adapter = new UserListAdapter(userList, getContext());
                        listViewFriendList.setAdapter(adapter);
                        load.stopLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                load.stopLoading();
            }
        });
    }
}