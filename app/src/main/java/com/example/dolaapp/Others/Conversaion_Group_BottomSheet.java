package com.example.dolaapp.Others;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.ConversationScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.MemberGroupActivity;
import com.example.dolaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Conversaion_Group_BottomSheet extends BottomSheetDialogFragment {
    Conversation currConv;
    public Conversaion_Group_BottomSheet(Conversation currConv){
        this.currConv = currConv;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conversation_group_modal,container, false);

        TextView userSettingName = v.findViewById(R.id.userSettingName);
        LinearLayout loUserList = v.findViewById(R.id.loUserList);
        LinearLayout loLeaveGroup = v.findViewById(R.id.loLeaveGroup);

        Session sessionManagement = new Session(getContext());
        ArrayList<String> userInfos = sessionManagement.getSession();

        userSettingName.setText(currConv.getConversationName());
        loUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MemberGroupActivity.class);
                intent.putExtra("group_member",currConv.getConversationID());
                startActivity(intent);
            }
        });
        loLeaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Thoát khỏi nhóm?").setMessage("Bạn có muốn thoát khỏi nhóm chat này?")
                        .setPositiveButton("Thoát nhóm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ApiService.api.deleteMemberFromConversation(currConv.getConversationID(), userInfos.get(1)).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });

                                Intent intent = new Intent(getContext(), ConversationScreenActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
