package com.example.dolaapp.Entities;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Conversation implements Serializable{
    protected String ConversationID;
    protected String ConversationName;
    protected ArrayList<String> ConversationMember;

    public Conversation(String ConversationID, String ConversationName, ArrayList<String> ConversationMember) {
        this.ConversationID = ConversationID;
        this.ConversationName = ConversationName;
        this.ConversationMember = ConversationMember;
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


}
