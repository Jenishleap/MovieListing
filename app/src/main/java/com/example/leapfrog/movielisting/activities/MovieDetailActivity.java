package com.example.leapfrog.movielisting.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.leapfrog.movielisting.R;
import com.example.leapfrog.movielisting.adapters.MovieCasteAdapter;
import com.example.leapfrog.movielisting.fragments.MovieListFragment;
import com.example.leapfrog.movielisting.helper.ResponseHandler;
import com.example.leapfrog.movielisting.helper.RatingCalculator;
import com.example.leapfrog.movielisting.helper.RetrofitClient;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.MovieCasts;


public class MovieDetailActivity extends AppCompatActivity {


    public static final int NUM_OF_STARS = 5;

    String movieId, movieTitle, movieRating, movieReleaseDate, movieOverview, moviePosterPath, movieBackgroundPath;
    String tag = "movie_details";


    ImageView ivMovieBackground, ivMoviePoster;
    TextView tvMovieDescription, tvReleaseDate, tvRating;
    private LinearLayoutManager llManagerHorizontal;
    RecyclerView rvMovieCasts;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    RatingBar rbMovieRatings;


    ServerRequest serverRequest;
    public MovieCasts movieCasts;
    MovieCasteAdapter casteAdapter;

    private Bitmap bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity_collapse);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        intializeUi();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.movieId = extras.getString(MovieListFragment.MOVIE_ID);
            this.movieTitle = extras.getString(MovieListFragment.MOVIE_TITLE);
            this.movieRating = extras.getString(MovieListFragment.MOVIE_RATING);
            this.movieReleaseDate = extras.getString(MovieListFragment.MOVIE_RELEASE_DATE);
            this.movieOverview = extras.getString(MovieListFragment.MOVIE_OVERVIEW);
            this.moviePosterPath = extras.getString(MovieListFragment.MOVIE_POSTER_PATH);
            this.movieBackgroundPath = extras.getString(MovieListFragment.MOVIE_BACKGROUND_PATH);

        }
        Log.d(tag, "the movie detail: " + movieId);
        collapsingToolbarLayout.setTitle(movieTitle);

        updateUi();
        fetchMovieCaste(movieId);
        toolbarTextAppernce();
    }

    private void intializeUi() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ivMovieBackground = (ImageView) findViewById(R.id.ivMovieBackground);
        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        rbMovieRatings = (RatingBar) findViewById(R.id.rbMovieRatings);

        tvMovieDescription = (TextView) findViewById(R.id.tvMovieDescription);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvRating = (TextView) findViewById(R.id.tvRating);
        rvMovieCasts = (RecyclerView) findViewById(R.id.rvmovie_casts);
        llManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMovieCasts.setLayoutManager(llManagerHorizontal);
        casteAdapter = new MovieCasteAdapter(getApplicationContext());
        rvMovieCasts.setAdapter(casteAdapter);

    }


    public void fetchMovieCaste(String movieId) {
        serverRequest = new ServerRequest(getApplicationContext(), new ResponseHandler.MovieCasteHandler() {
            @Override
            public void handleMovieCaste(MovieCasts casts) {
                movieCasts = casts;

                try {

                    if (movieCasts.getCast().size() != 0) {
                        if (movieCasts.getCast().size() > 5) {
                            casteAdapter.update(movieCasts.getCast().subList(0, 5));//show top 5 casts only
                        } else {
                            casteAdapter.update(movieCasts.getCast());
                        }

                    }
                } catch (Exception ex) {


                }
            }
        }, MovieDetailActivity.this);
        serverRequest.getMovieCaste(movieId);

    }

    private void updateUi() {

        tvMovieDescription.setText(movieOverview);
        tvReleaseDate.setText("Release Date: " + movieReleaseDate);
        tvRating.setText(movieRating);


        //set backgroundpath for movie
        Glide.with(getApplicationContext())
                .load(RetrofitClient.IMG_URL + movieBackgroundPath) // Image URL
                .centerCrop() // Image scale type
                .crossFade()
//                    .override(800, 500) // Resize image
                .placeholder(R.drawable.bckgnd_placeholder) // Place holder image
                .error(R.drawable.bckgnd_placeholder) // On error image
                .into(ivMovieBackground); // ImageView to display image

        rbMovieRatings.setNumStars(NUM_OF_STARS);//total num of stars is 5
        rbMovieRatings.setRating(RatingCalculator.calculateRatingNum(movieRating));


        //set posterpath for the movie
        //grab color for stars with respect to posterpath
        Glide.with(this)
                .load(RetrofitClient.IMG_URL + moviePosterPath)
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap = resource;
                        setStarColors(bitmap);


                        ivMoviePoster.setImageDrawable(null);//in case default place holder is not working

                        Drawable d = new BitmapDrawable(getResources(), bitmap);

                        ivMoviePoster.setBackgroundDrawable(d);


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


                Drawable progress = rbMovieRatings.getProgressDrawable();
                DrawableCompat.setTint(progress, vibrantDark);


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
