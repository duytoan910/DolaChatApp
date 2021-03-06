package com.example.dolaapp.Others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dolaapp.Entities.User;
import com.example.dolaapp.R;

import java.util.ArrayList;

public class GroupMemberListAdapter extends BaseAdapter {
    private ArrayList<User> list;
    private Context context;

    public GroupMemberListAdapter(ArrayList<User> list, Context context) {
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
        convertView = inflater.inflate(R.layout.list_item_user, null);

        ((TextView) convertView.findViewById(R.id.txtUserName)).setText(list.get(position).getUserName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessage)).setText(list.get(position).getConversationName() + "");
//        ((TextView) convertView.findViewById(R.id.txtUserMessageTime)).setText(list.get(position).getConversationName() + "");

        return convertView;
    }
}
