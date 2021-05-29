package com.example.dolaapp.Others.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolaapp.Others.Loading;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Conversaion_Group_BottomSheet;
import com.example.dolaapp.Others.Conversaion_U2U_BottomSheet;
import com.example.dolaapp.Others.ConversationListAdapter;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationListFragment extends Fragment {
    private ArrayList<Conversation> conversations;
    ListView listView;
    SwipeRefreshLayout swiperefresh;
    ArrayList<Conversation> conversationArrayList;
    List<User> list;
    User currentUser;
    ArrayList<String> userInfos;
    Loading load;

    private String userID;

    public ConversationListFragment() {
        // Required empty public constructor
    }
    public ConversationListFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadlist();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        listView = view.findViewById(R.id.listView);
        swiperefresh = view.findViewById(R.id.swiperefresh);

        Session sessionManagement = new Session(getContext());
        userInfos = sessionManagement.getSession();

        load = new Loading(getActivity());

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadlist();
                swiperefresh.setRefreshing(false);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent(getContext(), ChatScreenActivity.class);
                result.putExtra("conversationObject", conversations.get(position));
                if(!conversations.get(position).isGroupChat()){
                    ApiService.api.isFriend(
                            conversations.get(position).getConversationMember().get(0),
                            conversations.get(position).getConversationMember().get(1))
                            .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.body() == "false"){
                                result.putExtra("isRequest", true);
                                startActivity(result);
                            }else{
                                startActivity(result);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }else{
                    startActivity(result);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Conversation longClickedConv = conversations.get(position);
                if(longClickedConv.isGroupChat()){
                    Conversaion_Group_BottomSheet modal = new Conversaion_Group_BottomSheet(longClickedConv);
                    modal.show(getFragmentManager(),"info_group_Modal");
                }else{
                    for (String s : longClickedConv.getConversationMember()) {
                        if(!s.equals(userInfos.get(1))){
                            ApiService.api.getUserById(s).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    ApiService.api.isFriend(userInfos.get(1),response.body().getUserPhone()).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response1) {
                                            if(response1.body()=="true"){
                                                Conversaion_U2U_BottomSheet modal = new Conversaion_U2U_BottomSheet(response.body(), true);
                                                modal.show(getFragmentManager(),"info_u2u_Modal");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        }
                    }
                }
                return true;
            }
        });
        return view;
    }
    public void triggerSwipeRefresh(){
        swiperefresh.setRefreshing(true);
    }

    public void reloadlist(){
        load.startLoading();
        ApiService.api.getAllConversationByUserID(userInfos.get(1)).enqueue(new Callback<ArrayList<Conversation>>() {
            @Override
            public void onResponse(Call<ArrayList<Conversation>> call, Response<ArrayList<Conversation>> response) {
                if(response.body()== null ||response.body().size()==0) {
                    load.stopLoading();
                    return;
                }
                conversations = (ArrayList<Conversation>) response.body();
                ArrayList<Conversation> _Conv = new ArrayList<>();
                for (Conversation conversation : conversations) {
                    if(conversation.isGroupChat() ||
                            (conversation.getReceiver().equals(userInfos.get(1)) && conversation.isReceiverShown() != false) ||
                            (conversation.getSender().equals(userInfos.get(1)) && conversation.isSenderShown() != false)){
                        _Conv.add(conversation);
                    }
                }
                ConversationListAdapter adapter = new ConversationListAdapter(_Conv, getContext());
                conversations = _Conv;
                listView.setAdapter(adapter);
                load.stopLoading();
            }

            @Override
            public void onFailure(Call<ArrayList<Conversation>> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                load.stopLoading();
            }
        });
    }
}