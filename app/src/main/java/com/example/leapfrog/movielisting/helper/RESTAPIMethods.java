package com.example.leapfrog.movielisting.helper;

import com.example.leapfrog.movielisting.objects.AllMovies;
import com.example.leapfrog.movielisting.objects.Movie;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


//synchronous
public interface RESTAPIMethods {


    @GET("movie/{category}")
    Call<AllMovies> getAllMovies(@Path("category") String category, @Query("api_key") String apiKey);


    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetail(@Path("id") int id, @Query("api_key") String apiKey);
    //make sure id is not in string

    @GET("movie/{id}/credits")
    Call<MovieCasts> getMovieCaste(@Path("id") int id, @Query("api_key") String apiKey);

}
