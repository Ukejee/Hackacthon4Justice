package com.example.ukeje.hackacthon4justice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    private static final  String BASE_URL = "http://192.168.91.107:8080/hackathon/";

    public static Retrofit getClient(String baseUrl){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

