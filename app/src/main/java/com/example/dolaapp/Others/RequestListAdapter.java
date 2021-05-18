package com.example.dolaapp.Others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListAdapter extends BaseAdapter {
    private ArrayList<Conversation> list;
    private Context context;
    ArrayList<String> userInfos;
    User otherUser;
    public RequestListAdapter(ArrayList<Conversation> list, Context context) {
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
        convertView = inflater.inflate(R.layout.request_list, null);

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
                        ((TextView) finalConvertView.findViewById(R.id.txtUserName)).setText(otherUser.getUserName());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        }

        ((TextView) convertView.findViewById(R.id.txtUserName)).setText(list.get(position).getConversationName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessage)).setText(list.get(position).getConversationName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessageTime)).setText(list.get(position).getConversationName() + "");

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.api.DeleteRequestAddFriend(otherUser.getUserPhone(),userInfos.get(1)).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
                Toast.makeText(context, "Đã từ chối!", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                list.remove(position);
            }
        });


        return convertView;
    }
}
