package com.example.leapfrog.movielisting.helper;


import com.example.leapfrog.movielisting.objects.Movie;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HandleResponse {


    /*public abstract class HandleAllMovies {
        public abstract void handleResponse(ArrayList<Movie> movies, ArrayList<String> movie_ids, String category);
    }*/

    public abstract class HandleAllMovies {
        public abstract void handleResponse(LinkedHashMap<String, Movie> movies, ArrayList<String> movie_ids, String category);
    }

    public abstract class HandleMovieDetail {
        public abstract void handleMovieDetail(MovieDetail movieDetail);
    }

    public abstract class HandleMovieCaste {
        public abstract void handleMovieCaste(MovieCasts movieCasts);
    }

    public abstract class HandleAllResponse {
        public abstract void handleResponse(String response);
    }


}