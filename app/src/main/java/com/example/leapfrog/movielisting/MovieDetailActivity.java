package com.example.leapfrog.movielisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.leapfrog.movielisting.adapters.MovieCasteAdapter;
import com.example.leapfrog.movielisting.helper.HandleResponse;
import com.example.leapfrog.movielisting.helper.RatingCalculator;
import com.example.leapfrog.movielisting.helper.RetrofitClient;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;

import static android.R.attr.path;


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
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    RatingBar rbMovieRatings;
    private Bitmap bitmap = null;
    public static int NUM_OF_STARS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.movie_detail_activity);
        setContentView(R.layout.movie_detail_activity_collapse);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        intializeUI();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.movie_id = extras.getString(MovieListActivity.MOVIE_ID);
        }
        Log.d(TAG, "the movie detail: " + movie_id);
        fetchMovieDetail(movie_id);
        fetchMovieCaste(movie_id);
        toolbarTextAppernce();
    }

    private void intializeUI() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ivMovieBackground = (ImageView) findViewById(R.id.ivMovieBackground);
        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        rbMovieRatings = (RatingBar) findViewById(R.id.rbMovieRatings);

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

        rbMovieRatings.setNumStars(NUM_OF_STARS);
        rbMovieRatings.setRating(RatingCalculator.calculateRatingNum(movie.getVote_average().toString()));


        //grab color for stars
        Glide.with(this)
                .load(RetrofitClient.IMG_URL + movie.getPosterPath())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap = resource;
                        setStarColors(bitmap);
                    }
                });

    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }


    private void setStarColors(Bitmap bitmap) {

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {

                int defaultValue = 0x000000;
                int vibrant = palette.getVibrantColor(defaultValue);
                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                int muted = palette.getMutedColor(defaultValue);
                int mutedLight = palette.getLightMutedColor(defaultValue);
                int mutedDark = palette.getDarkMutedColor(defaultValue);


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
