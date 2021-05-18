package com.example.dolaapp.Entities;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Conversation implements Serializable{
    protected String ConversationID;
    protected String ConversationName;
    protected ArrayList<String> ConversationMember;
    protected ArrayList<String> ConversationAdmin;
    protected boolean IsGroupChat;
    protected boolean SenderShown;
    protected boolean ReceiverShown;
    protected String Sender;
    protected String Receiver;
    protected String NewestMessage;
    protected String NewestSenderName;
    protected String NewestTime;

    public Conversation(String ConversationID, String ConversationName, ArrayList<String> ConversationMember) {
        this.ConversationID = ConversationID;
        this.ConversationName = ConversationName;
        this.ConversationMember = ConversationMember;
    }
    public String getNewestMessage() {
        return NewestMessage;
    }

    public void setNewestMessage(String newestMessage) {
        NewestMessage = newestMessage;
    }

    public String getNewestSenderName() {
        return NewestSenderName;
    }

    public void setNewestSenderName(String newestSenderName) {
        NewestSenderName = newestSenderName;
    }

    public String getNewestTime() {
        return NewestTime;
    }

    public void setNewestTime(String newestTime) {
        NewestTime = newestTime;
    }

    public boolean isGroupChat() {
        return IsGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        IsGroupChat = groupChat;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getConversationID() {
        return ConversationID;
    }

    public void setConversationID(String conversationID) {
        ConversationID = conversationID;
    }

    public String getConversationName() {
        return ConversationName;
    }

    public void setConversationName(String conversationName) {
        ConversationName = conversationName;
    }

    public ArrayList<String> getConversationMember() {
        return ConversationMember;
    }

    public void setConversationMember(ArrayList<String> conversationMember) {
        ConversationMember = conversationMember;
    }

    public ArrayList<String> getConversationAdmin() {
        return ConversationAdmin;
    }

    public void setConversationAdmin(ArrayList<String> conversationAdmin) {
        ConversationAdmin = conversationAdmin;
    }

    public boolean isSenderShown() {
        return SenderShown;
    }

    public void setSenderShown(boolean senderShown) {
        SenderShown = senderShown;
    }

    public boolean isReceiverShown() {
        return ReceiverShown;
    }

    public void setReceiverShown(boolean receiverShown) {
        ReceiverShown = receiverShown;
    }


}
