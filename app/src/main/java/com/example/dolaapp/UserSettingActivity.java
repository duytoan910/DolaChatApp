package com.example.dolaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.Entities.User;
import com.example.dolaapp.Others.Loading;
import com.example.dolaapp._AppConfig.ExternalServices.ApiService;
import com.example.dolaapp.Others.Session;
import com.example.dolaapp._AppConfig.ExternalServices.SocketIo;
import com.example.dolaapp._AppConfig.AppServices;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSettingActivity extends AppCompatActivity {
    public final static int PICK_IMAGE_REQUEST = 1;
    public final static int READ_EXTERNAL_REQUEST = 2;
    ArrayList<String> userInfos;
    Session sessionManagement;
    TextInputEditText txtDialog_OldPass,txtDialog_NewPass,txtDialog_ReEnter;
    Loading load;

    public Socket mSocket= SocketIo.getInstance().getmSocket();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        sessionManagement = new Session(UserSettingActivity.this);
        userInfos = sessionManagement.getSession();

        load = new Loading(UserSettingActivity.this);

        if(userInfos.get(0) != ""){
            ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    ((TextView) findViewById(R.id.userSettingName)).setText(response.body().getUserName());
                    ((TextView) findViewById(R.id.userSettingPhone)).setText(response.body().getUserPhone());
                    new AppServices().setImageToImageView(UserSettingActivity.this, response.body().getAvatar(), findViewById(R.id.userAvt));

                    sessionManagement.saveSession(response.body());
                    load.stopLoading();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    load.stopLoading();
                }
            });
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        load.startLoading();
        ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ((TextView) findViewById(R.id.userSettingName)).setText(response.body().getUserName());
                ((TextView) findViewById(R.id.userSettingPhone)).setText(response.body().getUserPhone());
                new AppServices().setImageToImageView(UserSettingActivity.this, response.body().getAvatar(), findViewById(R.id.userAvt));
                load.stopLoading();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                load.stopLoading();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Session sessionManagement = new Session(UserSettingActivity.this);
        ArrayList<String> userInfos = sessionManagement.getSession();
        if(userInfos.get(0) == ""){
            Intent intent = new Intent(UserSettingActivity.this, LoginScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void userChangePassword(View view) {
        String userId = sessionManagement.getSession().get(1);
        String userPassword = sessionManagement.getSession().get(2);

        LayoutInflater inflater = getLayoutInflater();
        View customDialog = inflater.inflate(R.layout.dialog_change_password, null);

        txtDialog_OldPass = customDialog.findViewById(R.id.txtOldPassword);
        txtDialog_NewPass = customDialog.findViewById(R.id.txtNewPassword);
        txtDialog_ReEnter = customDialog.findViewById(R.id.txtReNewPassWord);

        txtDialog_OldPass.setText(userPassword);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(customDialog)
                .setPositiveButton("Xác nhận", null)
                .setNegativeButton("Hủy", null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChangePassValidate()){
                    String userNewPassword = txtDialog_NewPass.getText().toString();
                    ApiService.api.ChangePassword(userId,userPassword,userNewPassword).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            sessionManagement.savePassword(response.body());
                            Toast.makeText(UserSettingActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(UserSettingActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void userLogout(View view) {
        Session sessionManagement = new Session(UserSettingActivity.this);
        sessionManagement.removeSession();
        SocketIo.getInstance().Destroy();
        finish();
    }

    public void userTermsAndPolicies(View view) {
        Intent intent = new Intent(UserSettingActivity.this, TermsAndPoliciesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void userReport(View view) {
        Intent intent = new Intent(UserSettingActivity.this, ReportErrorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void userAboutUs(View view) {
        Intent intent = new Intent(UserSettingActivity.this, AppInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void pressBack(View view) {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void userPersonalSetting(View view) {
        Intent intent = new Intent(UserSettingActivity.this, UserPersonalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private boolean ChangePassValidate (){
        String old = txtDialog_OldPass.getText().toString();
        String newP = txtDialog_NewPass.getText().toString();
        String reNewP = txtDialog_ReEnter.getText().toString();
        if (old.length() == 0) {
            txtDialog_OldPass.setError("Nhập mật khẩu hiện tại");
            return false;
        }

        if (newP.length() == 0) {
            txtDialog_NewPass.setError("Hãy nhập mật khẩu mới");
            return false;
        }

        if (reNewP.length() == 0) {
            txtDialog_ReEnter.setError("Hãy nhập lại mật khẩu mới");
            return false;
        }

        if (!newP.equals(reNewP)){
            txtDialog_ReEnter.setError("Nhập lại không khớp");
            return false;
        }

        if (old.equals(reNewP) || old.equals(newP) )
        {
            txtDialog_ReEnter.setError("Mật khẩu mới không trùng với mk cũ");
            return false;
        }
        return  true;
    }

    public void userQRCode(View view) {
        Intent intent = new Intent(UserSettingActivity.this, MyQrCodeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}