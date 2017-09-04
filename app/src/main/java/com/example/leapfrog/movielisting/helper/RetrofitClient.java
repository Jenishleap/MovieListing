package com.example.leapfrog.movielisting.helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public final static String API_KEY = "5ba4b7b03f5316d2e5db0391f9190a76";
    public static String IMG_URL = "https://image.tmdb.org/t/p/w640";
    private static Retrofit retrofit = null;

    public static final String catagory = null;

    //make singlton instance of retrofit object
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;

    }
}
