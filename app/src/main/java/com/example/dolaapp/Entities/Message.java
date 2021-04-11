package com.example.dolaapp.Entities;

public class Message {
    private String text; // message body
    private String userSendID; // data of the user that sent this message
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Message(String text, String userSendID, boolean belongsToCurrentUser) {
        this.text = text;
        this.userSendID = userSendID;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public String getUserSendID() {
        return userSendID;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}