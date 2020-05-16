package com.pratapkumar.bakingapplication.api;

import android.content.Context;

import com.pratapkumar.bakingapplication.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingHelper {

    private static BakingService bakingApiService;

    public static synchronized BakingService getInstance(Context context) {
        if(bakingApiService== null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            bakingApiService = retrofit.create(BakingService.class);
        }

        return bakingApiService;
    }
}
