package com.example.ukeje.hackacthon4justice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("hackathon/login.php?fn=\"elijah\"&ln=\"okokon\"")
    Call<ChildUser> getChildData();

    @GET("hackathon/contact?id=\"parent\"&pid=1")
    Call<ChildUser> getParentId();

    @GET("hackathon/contact?id=\"emergency\"")
    Call<ChildUser> getEmergencyContact();

    //@FormUrlEncoded
    @POST("hackathon/distress?")
    Call<Void> sendAlert(@Query("a") String distress,@Query("la") double latitude,@Query("lo") double longitude);

}
