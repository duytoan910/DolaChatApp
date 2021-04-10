package com.example.dolaapp.API;

import com.example.dolaapp.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService api = new Retrofit.Builder()
            .baseUrl("http://192.168.1.41:3000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("users")
    Call<List<User>> getAllUser();

    @PUT("users")
    @FormUrlEncoded
    Call<User> createNewUser(@Field("phone") String phone, @Field("name") String name, @Field("Dob") String Dob, @Field("mail") String mail, @Field("password") String password);

}
