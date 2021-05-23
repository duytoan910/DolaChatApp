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
import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.ConversationScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.LoginScreenActivity;
import com.example.dolaapp.NewGroupActivity;
import com.example.dolaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Conversaion_U2U_BottomSheet extends BottomSheetDialogFragment {
    User currUser;
    boolean isConversationScreen;
    public Conversaion_U2U_BottomSheet(User user){
        this.currUser = user;
        this.isConversationScreen = false;
    }
    public Conversaion_U2U_BottomSheet(User user, @Nullable boolean isConversationScreen){
        this.currUser = user;
        this.isConversationScreen = isConversationScreen;
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
        LinearLayout loAddfriendAMember = v.findViewById(R.id.loAddfriendAMember);

        userSettingName.setText(currUser.getUserName());

        Session sessionManagement = new Session(getContext());
        ArrayList<String> userInfos = sessionManagement.getSession();

        ApiService.api.isFriend(userInfos.get(1),currUser.getUserPhone()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()=="true"){
                    loAddfriendAMember.setVisibility(View.GONE);
                }else{
                    loUnfriendAMember.setVisibility(View.GONE);
                    loRequestMessage.setVisibility(View.GONE);
                    createGroupWith.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

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
                                        if(!isConversationScreen){
                                            Intent intent = new Intent(getContext(), ConversationScreenActivity.class);
                                            startActivity(intent);
                                        }
                                        dismiss();
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
                                ApiService.api.GetConversationOf2Users(userInfos.get(1),currUser.getUserPhone()).enqueue(new Callback<Conversation>() {
                                    @Override
                                    public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                                        ApiService.api.SwitchConversationStateShow(response.body().getConversationID(),userInfos.get(1)).enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<Conversation> call, Throwable t) {

                                    }
                                });
                                dismiss();
                            }
                        }).setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        createGroupWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewGroupActivity.class);
                intent.putExtra("withUser", currUser.getUserPhone());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                dismiss();
            }
        });
        loAddfriendAMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.api.DeleteRequestAddFriend(currUser.getUserPhone(), userInfos.get(1)).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
                dismiss();
            }
        });

        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
