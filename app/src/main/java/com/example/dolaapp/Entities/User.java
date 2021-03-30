package com.example.dolaapp.Entities;

import java.util.List;

public class User {
    protected String UserEmail;
    protected String UserPassword;
    protected String UserPhone;
    protected String UserName;
    protected String UserDoB;

    protected List<String> UserConversation;
    protected List<String> requestSend;
    protected List<String> listRequest;
    protected List<String> listFriend;

    public User(String userEmail, String userPassword, String userPhone, String userName, String userDoB) {
        UserEmail = userEmail;
        UserPassword = userPassword;
        UserPhone = userPhone;
        UserName = userName;
        UserDoB = userDoB;
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

    public void setUserConversation(List<String> userConversation) {
        UserConversation = userConversation;
    }

    public List<String> getRequestSend() {
        return requestSend;
    }

    public void setRequestSend(List<String> requestSend) {
        this.requestSend = requestSend;
    }

    public List<String> getListRequest() {
        return listRequest;
    }

    public void getListRequest(List<String> listrequest) {
        this.listRequest = listrequest;
    }

    public List<String> getListFriend() {
        return listFriend;
    }

    public void setListFriend(List<String> listfriend) {
        this.listFriend = listfriend;
    }
}
