package com.example.leapfrog.movielisting.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.leapfrog.movielisting.R;
import com.example.leapfrog.movielisting.adapters.FragmentAdapter;
import com.example.leapfrog.movielisting.adapters.MovieAdapter;
import com.example.leapfrog.movielisting.adapters.MovieCasteAdapter;
import com.example.leapfrog.movielisting.fragments.MovieListFragment;
import com.example.leapfrog.movielisting.helper.HandleResponse;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.Movie;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {


    private Context _mcontext;
    public static String TOPRATED = "top_rated";
    public static String POPULAR = "popular";
    public static String NOWPLAYING = "now_playing";
    public static String UPCOMING = "upcoming";


    FragmentAdapter fragmentAdapter;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);

        initializeUI();

        viewpager.setAdapter(fragmentAdapter);


    }

    private void initializeUI() {
        _mcontext = getApplicationContext();


        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewpager = (ViewPager) findViewById(R.id.viewpager);


    }


}
