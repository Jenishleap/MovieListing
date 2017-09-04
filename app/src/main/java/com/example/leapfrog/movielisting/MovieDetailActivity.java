package com.example.leapfrog.movielisting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leapfrog.movielisting.adapters.MovieCasteAdapter;
import com.example.leapfrog.movielisting.helper.HandleResponse;
import com.example.leapfrog.movielisting.helper.RetrofitClient;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;


public class MovieDetailActivity extends AppCompatActivity {


    ImageView ivMovieBackground, ivMoviePoster;
    TextView tvMovieTitle, tvMovieDescription, tvReleaseDate, tvRating;
    String movie_id;
    String TAG = "movie_details";
    ServerRequest serverRequest;
    public MovieDetail movieDetail;
    public MovieCasts movieCasts;
    MovieCasteAdapter casteAdapter;
    private LinearLayoutManager llManagerHorizontalKOT;
    RecyclerView rvmovie_casts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        intializeUI();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.movie_id = extras.getString(MovieListActivity.MOVIE_ID);
        }
        Log.d(TAG, "the movie detail: " + movie_id);
        fetchMovieDetail(movie_id);
        fetchMovieCaste(movie_id);
    }

    private void intializeUI() {
        ivMovieBackground = (ImageView) findViewById(R.id.ivMovieBackground);
        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);

        tvMovieTitle = (TextView) findViewById(R.id.tvMovieTitle);
        tvMovieDescription = (TextView) findViewById(R.id.tvMovieDescription);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvRating = (TextView) findViewById(R.id.tvRating);
        rvmovie_casts = (RecyclerView) findViewById(R.id.rvmovie_casts);
        llManagerHorizontalKOT = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvmovie_casts.setLayoutManager(llManagerHorizontalKOT);
        casteAdapter = new MovieCasteAdapter(getApplicationContext());
        rvmovie_casts.setAdapter(casteAdapter);

    }


    public void fetchMovieDetail(String movie_id) {
        serverRequest = new ServerRequest(getApplicationContext(), new HandleResponse().new HandleMovieDetail() {
            @Override
            public void handleMovieDetail(MovieDetail movie) {
                movieDetail = movie;
                updateUI(movieDetail);
            }
        }, MovieDetailActivity.this);
        serverRequest.getMovieDetail(movie_id);
    }

    public void fetchMovieCaste(String movie_id) {
        serverRequest = new ServerRequest(getApplicationContext(), new HandleResponse().new HandleMovieCaste() {
            @Override
            public void handleMovieCaste(MovieCasts casts) {
                movieCasts = casts;
                casteAdapter.update(movieCasts.getCast().subList(0, 5));//show top 5 casts only
            }
        }, MovieDetailActivity.this);
        serverRequest.getMovieCaste(movie_id);

    }

    private void updateUI(MovieDetail movie) {
        tvMovieTitle.setText(movie.getTitle());
        tvMovieDescription.setText(movie.getOverview());
        tvReleaseDate.setText("Release Date: " + movie.getReleaseDate());
        tvRating.setText(movie.getVote_average().toString());

        Glide.with(getApplicationContext())
                .load(RetrofitClient.IMG_URL + movie.getPosterPath()) // Image URL
                .centerCrop() // Image scale type
                .crossFade()
//                    .override(800, 500) // Resize image
                .placeholder(R.mipmap.ic_launcher) // Place holder image
                .error(R.mipmap.ic_launcher) // On error image
                .into(ivMoviePoster); // ImageView to display image


        Glide.with(getApplicationContext())
                .load(RetrofitClient.IMG_URL + movie.getBackdropPath()) // Image URL
                .centerCrop() // Image scale type
                .crossFade()
//                    .override(800, 500) // Resize image
                .placeholder(R.mipmap.ic_launcher) // Place holder image
                .error(R.mipmap.ic_launcher) // On error image
                .into(ivMovieBackground); // ImageView to display image

    }
}
