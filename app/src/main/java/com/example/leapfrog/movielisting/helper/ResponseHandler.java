package com.example.leapfrog.movielisting.helper;


import com.example.leapfrog.movielisting.objects.Movie;
import com.example.leapfrog.movielisting.objects.MovieCasts;


import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ResponseHandler {


    public interface AllMoviesHandler {
        void handleResponse(LinkedHashMap<String, Movie> movies, ArrayList<String> movieIds, String category);
    }


    public interface MovieCasteHandler {
        void handleMovieCaste(MovieCasts movieCasts);
    }


}