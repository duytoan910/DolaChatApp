package com.example.dolaapp.Entities;

public class Conversation {
    protected String userName;
    protected String userMessage;
    protected String userMessageTime;


    public Conversation(String userName, String userMessage, String userMessageTime) {
        this.userName = userName;
        this.userMessage = userMessage;
        this.userMessageTime = userMessageTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
    public String getUserMessageTime() {
        return userMessageTime;
    }

    public void setUserMessageTime(String userMessageTime) {
        this.userMessageTime = userMessageTime;
    }
}
