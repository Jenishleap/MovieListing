package com.example.leapfrog.movielisting.helper;


import com.example.leapfrog.movielisting.objects.Movie;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResponseHandler {


    public abstract class AllMoviesHandler {
        public abstract void handleResponse(LinkedHashMap<String, Movie> movies, ArrayList<String> movieIds, String category);
    }

    public abstract class MovieDetailHandler {
        public abstract void handleMovieDetail(MovieDetail movieDetail);
    }

    public abstract class MovieCasteHandler {
        public abstract void handleMovieCaste(MovieCasts movieCasts);
    }


}