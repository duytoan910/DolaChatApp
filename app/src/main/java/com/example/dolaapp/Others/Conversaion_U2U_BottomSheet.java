package com.example.dolaapp.Others;

import android.app.AlertDialog;
import android.content.Context;
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

import com.example.dolaapp.ChatScreenActivity;
import com.example.dolaapp.RequestMessageActivity;
import com.example.dolaapp._AppConfig.AppServices;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.ConversationScreenActivity;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
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
    Context context;
    public Conversaion_U2U_BottomSheet(User user){
        this.currUser = user;
        this.context = context;
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
        new AppServices().setImageToImageView(getContext(), currUser.getAvatar(), v.findViewById(R.id.userAvt));

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
        createGroupWithText.setText("T???o nh??m chat v???i " + parts[parts.length - 1]);
        loUnfriendAMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("H???y k???t b???n?")
                        .setMessage("B???n c?? mu???n h???y k???t b???n v???i\n" + parts[parts.length - 1])
                        .setPositiveButton("H???y k???t b???n", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ApiService.api.DeleteFriend(userInfos.get(1), currUser.getUserPhone()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                    }
                                });
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
                                        Intent intent = new Intent(getContext(), RequestMessageActivity.class);
                                        intent.putExtra("isDenied","true");
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Conversation> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Tr??? v???", new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(getContext()).setTitle("????a v??o tin nh???n ch????").setMessage("B???n c?? mu???n ????a ng?????i n??y v??o tin nh???n ch????")
                        .setPositiveButton("????a v??o tin nh???n ch???", new DialogInterface.OnClickListener() {
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
                                Intent intent = new Intent(getContext(), RequestMessageActivity.class);
                                intent.putExtra("isDenied","true");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                dismiss();
                            }
                        }).setNegativeButton("Tr??? v???", new DialogInterface.OnClickListener() {
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
