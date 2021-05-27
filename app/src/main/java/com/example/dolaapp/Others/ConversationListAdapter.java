package com.example.dolaapp.Others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dolaapp.UserSettingActivity;
import com.example.dolaapp._AppConfig.AppServices;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationListAdapter extends BaseAdapter {
    private ArrayList<Conversation> list;
    private Context context;

    public ConversationListAdapter(ArrayList<Conversation> list, Context context) {
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
        convertView = inflater.inflate(R.layout.list_item_conversation, null);

        Session sessionManagement = new Session(context);
        ArrayList<String> userInfos = sessionManagement.getSession();

        if(list.get(position).isGroupChat()) {
            ((TextView) convertView.findViewById(R.id.txtUserName)).setText(list.get(position).getConversationName() + "");
        }else {
            for (String s : list.get(position).getConversationMember()) {
                if (s.equals(userInfos.get(1))) {
                    continue;
                } else {
                    View finalConvertView = convertView;
                    ApiService.api.getUserById(s).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            ((TextView) finalConvertView.findViewById(R.id.txtUserName)).setText(response.body().getUserName());
                            new AppServices().setImageToImageView(
                                    context,
                                    response.body().getAvatar(),
                                    finalConvertView.findViewById(R.id.imgUserSetting)
                            );
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        }
        DateFormat format = new SimpleDateFormat("HH:mm");
        if(list.get(position).getNewestSenderName() != null && list.get(position).getNewestMessage() != null && list.get(position).getNewestTime()!=null) {
            String[] parts = list.get(position).getNewestSenderName().split(" ");
            ((TextView) convertView.findViewById(R.id.txtUserMessage)).setText(parts[parts.length - 1] + ": " +list.get(position).getNewestMessage());
            ((TextView) convertView.findViewById(R.id.txtUserMessageTime)).setText(format.format(new Date(list.get(position).getNewestTime())));
        }

        return convertView;
    }
}
