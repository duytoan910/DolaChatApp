package com.example.dolaapp.Others;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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
        LinearLayout loUnfriendAMember = v.findViewById(R.id.loUnfriendAMember);
        LinearLayout loLeaveGroup = v.findViewById(R.id.loLeaveGroup);

        userSettingName.setText(currConv.getConversationName());

        loLeaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Thoát khỏi nhóm?").setMessage("Bạn có muốn thoát khỏi nhóm chat này?")
                        .setPositiveButton("Thoát nhóm", new DialogInterface.OnClickListener() {
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
