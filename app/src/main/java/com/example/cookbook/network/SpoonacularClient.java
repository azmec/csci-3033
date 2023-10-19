package com.example.cookbook.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpoonacularClient {
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static SpoonacularClient instance;
    private SpoonacularAPI api;

    private SpoonacularClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(SpoonacularAPI.class);
    }

    public static synchronized SpoonacularClient getInstance() {
        if (instance == null)
            instance = new SpoonacularClient();

        return instance;
    }

    public SpoonacularAPI getApi() {
        return api;
    }
}
