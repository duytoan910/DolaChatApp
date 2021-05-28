package com.example.dolaapp.Entities;

import com.example.dolaapp.Others.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Message {
    private String Message; // message body
    private String MessageId; // data of the user that sent this message
    private String NameSender; // is this message sent by us?
    private String Receiver; // is this message sent by us?
    private String Sender; // is this message sent by us?

    public Message(String message, String messageId, String nameSender, String receiver, String sender, String timeStamp, boolean belongsToCurrentUser) {
        Message = message;
        MessageId = messageId;
        NameSender = nameSender;
        Receiver = receiver;
        Sender = sender;
        this.timeStamp = timeStamp;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    private String timeStamp; // is this message sent by us?

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getNameSender() {
        return NameSender;
    }

    public void setNameSender(String nameSender) {
        NameSender = nameSender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }


    private boolean belongsToCurrentUser;

}