package com.example.dolaapp.API;

import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.Message;
import com.example.dolaapp.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService api = new Retrofit.Builder()
            .baseUrl("http://192.168.1.9:3000/api/")
//            .baseUrl("http://192.168.0.168:3000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    //Get all user
    @GET("users")
    Call<ArrayList<User>> getAllUser();

    //Get user by id
    @GET("Users/{id}")
    Call<User> getUserById(@Path("id") String id);

    //Get user by id
    @GET("Users/{id}/SendPassword")
    Call<String> sendPassword(@Path("id") String id);

    //Create new user
    @PUT("users")
    @FormUrlEncoded
    Call<User> createNewUser(@Field("phone") String phone, @Field("name") String name, @Field("Dob") String Dob, @Field("mail") String mail, @Field("password") String password);

    //Get all conversation of user
    //http://localhost:3001/api/:idUser/Conversations
    @GET("{id}/Conversations")
    Call<ArrayList<Conversation>> getAllConversationByUserID(@Path("id") String id);

    //Create new conversation
    //http://localhost:3001/api/:idUser/Conversations
    @POST("Conversations")
    @FormUrlEncoded
    Call<ArrayList<Conversation>> createConversation(
            @Field("ConversationName") String Name,
            @Field("ConversationMember") ArrayList<String> Member,
            @Field("ConversationAdmin") ArrayList<String> Admin,
            @Field("IsGroup") boolean IsGroup,
            @Field("SenderShown") boolean SenderShown,
            @Field("ReceiverShown") boolean ReceiverShown
            );

    //Get all user friends
    @GET("{id}/ListFriends/")
    Call<ArrayList<User>> getAllListFriend(@Path("id") String id);

    //Send friend request
    @GET("{UserPhone}/SendAddFriendReQuest/{TargetPhone}")
    Call<String> SendAddFriendReQuest(@Path("UserPhone") String UserPhone,@Path("TargetPhone") String TargetPhone);

    //Remove friend request
    @GET("{PhoneOfReceiver}/DeleteRequestAddFriend/{PhoneOfSender}")
    Call<String> DeleteRequestAddFriend(@Path("PhoneOfReceiver") String PhoneOfReceiver,@Path("PhoneOfSender") String PhoneOfSender);

    //Get all message in conversation
    @GET("getMessages2/{Receiver}")
    Call<ArrayList<Message>> getMessages2(@Path("Receiver") String Receiver);

    //Create new message
    @POST("Messages")
    @FormUrlEncoded
    Call<Message> createMessage(
            @Field("Message") String Message,
            @Field("Sender") String Sender,
            @Field("Receiver") String Receiver,
            @Field("NameSender") String NameSender,
            @Field("Time") String Time
    );
}
