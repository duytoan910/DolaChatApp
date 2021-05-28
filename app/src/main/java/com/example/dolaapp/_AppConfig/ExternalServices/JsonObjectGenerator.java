package com.example.dolaapp._AppConfig.ExternalServices;

import com.example.dolaapp.Entities.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class JsonObjectGenerator {

    private static SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");

    public static JSONObject MessageJsonObject (Message messageObj, ArrayList<String> ListMember) {
        JSONObject jsonObject= new JSONObject();
        JSONArray array = new JSONArray(ListMember);

        try {
            jsonObject.put("Id", messageObj.getMessageId());
            jsonObject.put("Message", messageObj.getMessage());
            jsonObject.put("NameSender", messageObj.getNameSender());
            jsonObject.put("Sender", messageObj.getSender());
            jsonObject.put("Receiver", messageObj.getReceiver());
            jsonObject.put("Time", myFormat.format(Calendar.getInstance().getTime()));
            jsonObject.put("belongtocurrentuser", false);
            jsonObject.put("listmember", array);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static JSONObject AddFriendRequestJsonObject (String TargetId, String UserName) throws JSONException {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("TargetId",TargetId );
            jsonObject.put("SenderName", UserName);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
