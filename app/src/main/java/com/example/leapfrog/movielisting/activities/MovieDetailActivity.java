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
import com.example.leapfrog.movielisting.helper.HandleResponse;
import com.example.leapfrog.movielisting.helper.RatingCalculator;
import com.example.leapfrog.movielisting.helper.RetrofitClient;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;


public class MovieDetailActivity extends AppCompatActivity {


    ImageView ivMovieBackground, ivMoviePoster;
    TextView tvMovieDescription, tvReleaseDate, tvRating;

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
    String movie_id, movie_title, movie_rating, movie_releasedate, movie_overview, movie_posterpath, movie_backgroundpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity_collapse);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        intializeUI();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.movie_id = extras.getString(MovieListFragment.MOVIE_ID);
            this.movie_title = extras.getString(MovieListFragment.MOVIE_TITLE);
            this.movie_rating = extras.getString(MovieListFragment.MOVIE_RATING);
            this.movie_releasedate = extras.getString(MovieListFragment.MOVIE_RELEASEDATE);
            this.movie_overview = extras.getString(MovieListFragment.MOVIE_OVERVIEW);
            this.movie_posterpath = extras.getString(MovieListFragment.MOVIE_POSTERPATH);
            this.movie_backgroundpath = extras.getString(MovieListFragment.MOVIE_BACKGROUNDPATH);

        }
        Log.d(TAG, "the movie detail: " + movie_id);
        collapsingToolbarLayout.setTitle(movie_title);

        updateUI();
        fetchMovieCaste(movie_id);
        toolbarTextAppernce();
    }

    private void intializeUI() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ivMovieBackground = (ImageView) findViewById(R.id.ivMovieBackground);
        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        rbMovieRatings = (RatingBar) findViewById(R.id.rbMovieRatings);

        tvMovieDescription = (TextView) findViewById(R.id.tvMovieDescription);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvRating = (TextView) findViewById(R.id.tvRating);
        rvmovie_casts = (RecyclerView) findViewById(R.id.rvmovie_casts);
        llManagerHorizontalKOT = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvmovie_casts.setLayoutManager(llManagerHorizontalKOT);
        casteAdapter = new MovieCasteAdapter(getApplicationContext());
        rvmovie_casts.setAdapter(casteAdapter);

    }


    public void fetchMovieCaste(String movie_id) {
        serverRequest = new ServerRequest(getApplicationContext(), new HandleResponse().new HandleMovieCaste() {
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
        serverRequest.getMovieCaste(movie_id);

    }

    private void updateUI() {

        tvMovieDescription.setText(movie_overview);
        tvReleaseDate.setText("Release Date: " + movie_releasedate);
        tvRating.setText(movie_rating);


        //set backgroundpath for movie
        Glide.with(getApplicationContext())
                .load(RetrofitClient.IMG_URL + movie_backgroundpath) // Image URL
                .centerCrop() // Image scale type
                .crossFade()
//                    .override(800, 500) // Resize image
                .placeholder(R.mipmap.ic_launcher) // Place holder image
                .error(R.mipmap.ic_launcher) // On error image
                .into(ivMovieBackground); // ImageView to display image

        rbMovieRatings.setNumStars(NUM_OF_STARS);//total num of stars is 5
        rbMovieRatings.setRating(RatingCalculator.calculateRatingNum(movie_rating));


        //set posterpath for the movie
        //grab color for stars with respect to posterpath
        Glide.with(this)
                .load(RetrofitClient.IMG_URL + movie_posterpath)
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap = resource;
                        setStarColors(bitmap);


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
//                DrawableCompat.setTint(progress, getResources().getColor(R.color.green));
                DrawableCompat.setTint(progress, vibrant);


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

    public void adjustStarColor() {
        Drawable progress = rbMovieRatings.getProgressDrawable();
//        DrawableCompat.setTint(progress, Color.WHITE);
        DrawableCompat.setTint(progress, getResources().getColor(R.color.green));

    }
}
