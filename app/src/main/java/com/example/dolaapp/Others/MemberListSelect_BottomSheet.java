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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.ConversationScreenActivity;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberListSelect_BottomSheet extends BottomSheetDialogFragment {
    private User currUser;
    public MemberListSelect_BottomSheet(User user){
        this.currUser = user;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conversation_group_member_modal,container, false);

        LinearLayout unFriend = v.findViewById(R.id.unFriend);
        LinearLayout loLeaveConv = v.findViewById(R.id.loLeaveConv);
        Session sessionManagement = new Session(getContext());
        ArrayList<String> userInfos = sessionManagement.getSession();

        String[] parts = currUser.getUserName().split(" ");
        unFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Hủy kết bạn?")
                        .setMessage("Bạn có muốn hủy kết bạn với\n" + parts[parts.length - 1])
                        .setPositiveButton("Hủy kết bạn", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ApiService.api.DeleteFriend(userInfos.get(1), currUser.getUserPhone()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Intent intent = new Intent(getContext(), ConversationScreenActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        loLeaveConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Đưa vào tin nhắn chờ?")
                        .setMessage("Bạn có muốn đưa \n" + parts[parts.length - 1] + "\n vào tin nhắn chờ?")
                        .setPositiveButton("Đưa vào tin nhắn chờ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        return v;
    }
}
