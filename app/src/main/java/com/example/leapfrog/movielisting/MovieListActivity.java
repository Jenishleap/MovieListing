package com.example.leapfrog.movielisting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.leapfrog.movielisting.adapters.MovieAdapter;
import com.example.leapfrog.movielisting.adapters.MovieCasteAdapter;
import com.example.leapfrog.movielisting.helper.HandleResponse;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.Movie;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    RecyclerView rvmovie_list;
    MovieAdapter movieAdapter;

    private List<Movie> my_movies;
    private Context _mcontext;
    private RecyclerView.LayoutManager layoutManager;

    ServerRequest serverRequest;
    public String TAG = "all_movies";
    public String TOPRATED = "top_rated";
    public String POPULAR = "popular";
    public String LATEST = "latest";
    public String NOWPLAYING = "now_playing";
    public String UPCOMING = "upcoming";
    public static String MOVIE_ID = "movie_id";

    Button btnPopular, btnTopRated, btnUpComing, btnNowPlaying;

    MovieAdapter.OnItemClickListener movieClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        initializeUI();

        btnPopular.setOnClickListener(btnsClickListener);
        btnTopRated.setOnClickListener(btnsClickListener);
        btnUpComing.setOnClickListener(btnsClickListener);
        btnNowPlaying.setOnClickListener(btnsClickListener);


        rvmovie_list.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(movieClickListener);

        serverRequest = new ServerRequest(getApplicationContext(), new HandleResponse().new HandleAllMovies() {
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
        }, MovieListActivity.this);


        //fetch toprated movies as default
        serverRequest.getAllMovieLists(TOPRATED);


    }

    private void initializeUI() {
        rvmovie_list = (RecyclerView) findViewById(R.id.rvmovie_list);
        rvmovie_list.setHasFixedSize(true);
        _mcontext = getApplicationContext();
        layoutManager = new LinearLayoutManager(this);
        rvmovie_list.setLayoutManager(layoutManager);
        my_movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(_mcontext);
        btnPopular = (Button) findViewById(R.id.btnPopular);
        btnTopRated = (Button) findViewById(R.id.btnTopRated);
        btnUpComing = (Button) findViewById(R.id.btnUpComing);
        btnNowPlaying = (Button) findViewById(R.id.btnNowPlaying);
        movieClickListener = new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieAdapter.ItemHolder item, int position) {


                //go to movie detail activity
                Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
                intent.putExtra(MOVIE_ID, my_movies.get(position).getId().toString());
                startActivity(intent);
            }
        };

    }


    View.OnClickListener btnsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnPopular:
                    serverRequest.getAllMovieLists(POPULAR);
                    break;
                case R.id.btnTopRated:
                    serverRequest.getAllMovieLists(TOPRATED);
                    break;
                case R.id.btnUpComing:
                    serverRequest.getAllMovieLists(UPCOMING);
                    break;
                case R.id.btnNowPlaying:
                    serverRequest.getAllMovieLists(NOWPLAYING);
                    break;

                default:

                    break;

            }
        }
    };

}
