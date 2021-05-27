package com.example.dolaapp.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    protected String UserEmail;
    protected String UserPassword;
    protected String UserPhone;
    protected String UserName;
    protected String UserDoB;
    protected String Avatar;

    protected ArrayList<String> UserConversation;
    protected ArrayList<String> listrequest;
    protected ArrayList<String> listfriend;
    protected ArrayList<String> requestSend;

    public User(String userEmail, String userPassword, String userPhone, String userName, String userDoB, ArrayList<String> userConversation, ArrayList<String> listRequest, ArrayList<String> listFriend, ArrayList<String> requestSend) {
        UserEmail = userEmail;
        UserPassword = userPassword;
        UserPhone = userPhone;
        UserName = userName;
        UserDoB = userDoB;
        UserConversation = userConversation;
        this.listrequest = listRequest;
        this.listfriend = listFriend;
        this.requestSend = requestSend;
    }


    public User(String userEmail, String userPassword, String userPhone, String userName, String userDoB) {
        UserEmail = userEmail;
        UserPassword = userPassword;
        UserPhone = userPhone;
        UserName = userName;
        UserDoB = userDoB;
    }


    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public ArrayList<String> getListrequest() {
        return listrequest;
    }

    public void setListrequest(ArrayList<String> listrequest) {
        this.listrequest = listrequest;
    }

    public ArrayList<String> getListfriend() {
        return listfriend;
    }

    public void setListfriend(ArrayList<String> listfriend) {
        this.listfriend = listfriend;
    }
    public User(){}

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserDoB() {
        return UserDoB;
    }

    public void setUserDoB(String userDoB) {
        UserDoB = userDoB;
    }

    public List<String> getUserConversation() {
        return UserConversation;
    }

    public void setUserConversation(ArrayList<String> userConversation) {
        UserConversation = userConversation;
    }

    public List<String> getRequestSend() {
        return requestSend;
    }

    public void setRequestSend(ArrayList<String> requestSend) {
        this.requestSend = requestSend;
    }

    public List<String> getListRequest() {
        return listrequest;
    }

    public void getListRequest(ArrayList<String> listrequest) {
        this.listrequest = listrequest;
    }

    public List<String> getListFriend() {
        return listfriend;
    }

    public void setListFriend(ArrayList<String> listfriend) {
        this.listrequest = listfriend;
    }
}
