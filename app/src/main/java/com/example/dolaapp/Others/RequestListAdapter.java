package com.example.dolaapp.Others;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
                        .setTitle("Chấp nhận tin nhắn?")
                        .setMessage("Bạn có muốn chấp nhận tin nhắn từ " + otherUser.getUserName() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ApiService.api.AcceptFriendRequest(userInfos.get(1),otherUser.getUserPhone()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        notifyDataSetChanged();
                                        list.remove(position);
                                        Toast.makeText(context, "Chấp nhận thành công!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                    }
                                });
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ApiService.api.DeleteRequestAddFriend(userInfos.get(1),otherUser.getUserPhone()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        notifyDataSetChanged();
                                        list.remove(position);
                                        Toast.makeText(context, "Từ chối thành công!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                    }
                                });
                                dialog.cancel();
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
