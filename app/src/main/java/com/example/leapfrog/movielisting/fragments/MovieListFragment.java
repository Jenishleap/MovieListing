package com.example.leapfrog.movielisting.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.leapfrog.movielisting.MovieDetailActivity;
import com.example.leapfrog.movielisting.MovieListActivity;
import com.example.leapfrog.movielisting.R;
import com.example.leapfrog.movielisting.adapters.MovieAdapter;
import com.example.leapfrog.movielisting.helper.HandleResponse;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.Movie;

import java.util.ArrayList;
import java.util.List;


public class MovieListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";


    private String category;


    RecyclerView rvmovie_list;
    MovieAdapter movieAdapter;

    private List<Movie> my_movies;
    private Context _mcontext;
    private RecyclerView.LayoutManager layoutManager;

    ServerRequest serverRequest;
    public String TAG = "all_movies";


    public static String MOVIE_ID = "movie_id";
    public static String MOVIE_TITLE = "movie_title";
    public static String MOVIE_RATING = "movie_rating";
    public static String MOVIE_RELEASEDATE = "movie_releasedate";
    public static String MOVIE_OVERVIEW = "movie_overview";
    public static String MOVIE_POSTERPATH = "movie_posterpath";
    public static String MOVIE_BACKGROUNDPATH = "movie_backgrounpath";


    MovieAdapter.OnItemClickListener movieClickListener;


    public MovieListFragment() {

    }


    public static MovieListFragment newInstance(String cat) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);

        initializeUI(view);
        Log.d(TAG, "this category is: " + category);

        rvmovie_list.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(movieClickListener);

        serverRequest = new ServerRequest(_mcontext, new HandleResponse().new HandleAllMovies() {
            @Override
            public void handleResponse(List<Movie> movies) {

                try {

                    my_movies = movies;
                    Log.d(TAG, "all movies fetched: " + movies);
                    movieAdapter.update(movies);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, getActivity());//pass Activity reference


        //fetch toprated movies as default
        serverRequest.getAllMovieLists(category);

        return view;
    }


    private void initializeUI(View view) {
        rvmovie_list = (RecyclerView) view.findViewById(R.id.rvmovie_list);
        rvmovie_list.setHasFixedSize(true);
        _mcontext = getActivity().getApplicationContext();
        layoutManager = new LinearLayoutManager(_mcontext);
        rvmovie_list.setLayoutManager(layoutManager);
        my_movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(_mcontext);
        movieClickListener = new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieAdapter.ItemHolder item, int position) {


                //go to movie detail activity
                Intent intent = new Intent(_mcontext, MovieDetailActivity.class);
                intent.putExtra(MOVIE_ID, my_movies.get(position).getId().toString());

                intent.putExtra(MOVIE_TITLE, my_movies.get(position).getTitle());
                intent.putExtra(MOVIE_RATING, my_movies.get(position).getVoteAverage().toString());
                intent.putExtra(MOVIE_RELEASEDATE, my_movies.get(position).getReleaseDate());
                intent.putExtra(MOVIE_OVERVIEW, my_movies.get(position).getOverview());

                intent.putExtra(MOVIE_POSTERPATH, my_movies.get(position).getPosterPath());
                intent.putExtra(MOVIE_BACKGROUNDPATH, my_movies.get(position).getBackdropPath());

                startActivity(intent);
            }
        };

    }


}
