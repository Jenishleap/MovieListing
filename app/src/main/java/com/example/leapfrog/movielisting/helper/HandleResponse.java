package com.example.leapfrog.movielisting.helper;


import com.example.leapfrog.movielisting.objects.Movie;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;


import java.util.List;

public class HandleResponse {


    public abstract class HandleAllMovies {
        public abstract void handleResponse(List<Movie> movies);
    }

    public abstract class HandleMovieDetail {
        public abstract void handleMovieDetail(MovieDetail movieDetail);
    }

    public abstract class HandleMovieCaste {
        public abstract void handleMovieCaste(MovieCasts movieCasts);
    }


}