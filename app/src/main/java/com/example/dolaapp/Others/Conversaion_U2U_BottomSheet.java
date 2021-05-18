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
import com.example.dolaapp.LoginScreenActivity;
import com.example.dolaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Conversaion_U2U_BottomSheet extends BottomSheetDialogFragment {
    User currUser;
    public Conversaion_U2U_BottomSheet(User user){
        this.currUser = user;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conversation_u2u_modal,container, false);

        TextView userSettingName = v.findViewById(R.id.userSettingName);
        TextView createGroupWithText = v.findViewById(R.id.createGroupWithText);
        LinearLayout createGroupWith = v.findViewById(R.id.createGroupWith);
        LinearLayout loUserList = v.findViewById(R.id.loUserList);
        LinearLayout loUnfriendAMember = v.findViewById(R.id.loUnfriendAMember);
        LinearLayout loRequestMessage = v.findViewById(R.id.loRequestMessage);

        userSettingName.setText(currUser.getUserName());

        Session sessionManagement = new Session(getContext());
        ArrayList<String> userInfos = sessionManagement.getSession();

        String[] parts = currUser.getUserName().split(" ");
        createGroupWithText.setText("Tạo nhóm chat với " + parts[parts.length - 1]);
        loUnfriendAMember.setOnClickListener(new View.OnClickListener() {
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
        loRequestMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Đưa vào tin nhắn chờ?").setMessage("Bạn có muốn đưa người này vào tin nhắn chờ?")
                        .setPositiveButton("Đưa vào tin nhắn chờ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
