package com.example.leapfrog.movielisting.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.leapfrog.movielisting.activities.MovieDetailActivity;
import com.example.leapfrog.movielisting.R;
import com.example.leapfrog.movielisting.activities.MovieListActivity;
import com.example.leapfrog.movielisting.adapters.MovieAdapter;
import com.example.leapfrog.movielisting.helper.CompareLists;
import com.example.leapfrog.movielisting.helper.HandleResponse;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.Movie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class MovieListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";


    private String category;


    RecyclerView rvmovie_list;
    MovieAdapter movieAdapter;

    private ArrayList<Movie> my_movies;
    private Context _mcontext;
    private RecyclerView.LayoutManager layoutManager;

    ServerRequest serverRequest;
    public String TAG = "all_movies";
    public String LIFECYCLE = "lifecycle";


    public static String MOVIE_ID = "movie_id";
    public static String MOVIE_TITLE = "movie_title";
    public static String MOVIE_RATING = "movie_rating";
    public static String MOVIE_RELEASEDATE = "movie_releasedate";
    public static String MOVIE_OVERVIEW = "movie_overview";
    public static String MOVIE_POSTERPATH = "movie_posterpath";
    public static String MOVIE_BACKGROUNDPATH = "movie_backgrounpath";


    public static String POPULAR_MOVIES = "popular_movies";
    public static String TOPRATEDMOVIES = "toprated_movies";
    public static String UPCOMINGMOVIES = "upcoming_movies";
    public static String NOWPLAYINGMOVIES = "now_playing_movies";


    MovieAdapter.OnItemClickListener movieClickListener;

    public List<String> popular_movie_ids;
    public List<String> toprated_movie_ids;
    public List<String> upcoming_movie_ids;
    public List<String> nowplaying_movie_ids;


    public ArrayList<Movie> retrieved_movie_lists;


    public List<String> old_lists;


    public ArrayList<String> temp_ids;
    String i_belong_to;


    public MovieListFragment() {

    }


    public static MovieListFragment newInstance(String cat) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, cat);
        args.putString("belong1", cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            category = getArguments().getString(ARG_PARAM1);
            i_belong_to = getArguments().getString("belong1");
            String saved_value = getArguments().getString("saved_value");
            Log.d("saved_value", "the saved value is: " + saved_value);

        }
        Log.d(LIFECYCLE, "onCreate is called for " + category);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);

        initializeUI(view);
        Log.d(TAG, "this category is: " + category);
        Log.d("category_name", "this category is: " + category);

        rvmovie_list.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(movieClickListener);

        serverRequest = new ServerRequest(_mcontext, new HandleResponse().new HandleAllMovies() {
            @Override
            public void handleResponse(LinkedHashMap<String, Movie> new_movies, ArrayList<String> new_list, String category) {

                try {


                    temp_ids = new_list;

                    my_movies = new ArrayList<>(new_movies.values());


                    if (retrieved_movie_lists != null) {


                        CompareLists compareLists = new CompareLists();


                        if (compareLists.equalLists(new_list, old_lists)) {
                            //both lists are same
                            //no need to do anything


                        } else {
                            //lists vary
                            //further operation for identifying new ids
                            Log.d("different", "lists are different");


                            System.out.println("old list: " + old_lists);
                            System.out.println("old list: " + new_list);


                            //removing stale elements from the old list
                            boolean found;
                            Iterator<Movie> it_movie = retrieved_movie_lists.iterator();
                            Iterator<String> it_id = old_lists.iterator();
                            while (it_movie.hasNext() && it_id.hasNext()) {
                                found = false;


                                Movie movie = it_movie.next();
                                String oldid = it_id.next();

                                for (String newid : new_list) {

                                    if (oldid.equals(newid)) {
                                        //means this old id matches with new id
                                        //no need to further look on this oldid
                                        //break the loop
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    movieAdapter.remove(old_lists.indexOf(oldid));
                                    it_movie.remove();
                                    it_id.remove();


                                }
                            }

                            System.out.println("the filtered out list: " + old_lists);

                            //now adding new elements from the new list
                            List<String> new_copy = new ArrayList<>(new_list);
                            new_copy.removeAll(old_lists);//remove same items
                            System.out.println("the new ids are: " + new_copy);
                            for (String newid : new_copy) {
                                old_lists.add(newid);
                                movieAdapter.add(movieAdapter.getItemCount(), new_movies.get(newid));
                                retrieved_movie_lists.add(new_movies.get(newid));
                                //update adapter
                            }
                            System.out.println("the updated list: " + old_lists);


                        }
                    } else {
                        //list_to_compare null, means display latest lists

                        Log.d(TAG, "all movies fetched: " + new_movies);
                        movieAdapter.update(my_movies);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, getActivity());//pass Activity reference


//        serverRequest.getAllMovieLists(category);


        Log.d("belong", "I belong to " + i_belong_to);
        String retrieved_belong;

        switch (category) {


            case "top_rated":
                if (savedInstanceState != null) {
                    serverRequest.getAllMovieLists(category);
                    toprated_movie_ids = savedInstanceState.getStringArrayList(MovieListActivity.TOPRATED);
                    old_lists = toprated_movie_ids;
                    retrieved_movie_lists = savedInstanceState.getParcelableArrayList(TOPRATEDMOVIES);

                    Log.d("retrieved_ids", "the retrieved movie lists: " + toprated_movie_ids);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    retrieved_belong = savedInstanceState.getString("belong2");
                    Log.d("mah_belongs", "retrieved belongs: " + retrieved_belong);
                } else {
                    Log.d("request", "request sent for :" + category);
                    serverRequest.getAllMovieLists(category);
                }
                break;
            case "popular":
                if (savedInstanceState != null) {
                    serverRequest.getAllMovieLists(category);
                    popular_movie_ids = savedInstanceState.getStringArrayList(MovieListActivity.POPULAR);
                    old_lists = popular_movie_ids;
                    retrieved_movie_lists = savedInstanceState.getParcelableArrayList(POPULAR_MOVIES);
                    Log.d("retrieved_ids", "the retrieved movie lists: " + popular_movie_ids);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    retrieved_belong = savedInstanceState.getString("belong2");
                    Log.d("mah_belongs", "retrieved belongs: " + retrieved_belong);
                } else {
                    Log.d("request", "request sent for :" + category);
                    serverRequest.getAllMovieLists(category);
                }
                break;
            case "now_playing":
                if (savedInstanceState != null) {
                    serverRequest.getAllMovieLists(category);
                    nowplaying_movie_ids = savedInstanceState.getStringArrayList(MovieListActivity.NOWPLAYING);
                    old_lists = nowplaying_movie_ids;
                    retrieved_movie_lists = savedInstanceState.getParcelableArrayList(NOWPLAYINGMOVIES);
                    Log.d("retrieved_ids", "the retrieved movie lists: " + nowplaying_movie_ids);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    retrieved_belong = savedInstanceState.getString("belong2");
                    Log.d("mah_belongs", "retrieved belongs: " + retrieved_belong);
                } else {
                    Log.d("request", "request sent for :" + category);
                    serverRequest.getAllMovieLists(category);
                }
                break;
            case "upcoming":
                if (savedInstanceState != null) {
                    serverRequest.getAllMovieLists(category);
                    upcoming_movie_ids = savedInstanceState.getStringArrayList(MovieListActivity.UPCOMING);
                    old_lists = upcoming_movie_ids;
                    retrieved_movie_lists = savedInstanceState.getParcelableArrayList(UPCOMINGMOVIES);
                    Log.d("retrieved_ids", "the retrieved movie lists: " + upcoming_movie_ids);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    retrieved_belong = savedInstanceState.getString("belong2");
                    Log.d("mah_belongs", "retrieved belongs: " + retrieved_belong);
                } else {
                    Log.d("request", "request sent for :" + category);
                    serverRequest.getAllMovieLists(category);
                }
                break;
            default:
                break;
        }


        if (old_lists != null) {
            movieAdapter.update(retrieved_movie_lists);
        } else {
            //show some kind of progress bar or waiting logo
        }


        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("saved_value", "my_saved_value");

        Log.d("belong", "I belong to " + i_belong_to);

        bundle.putString("belong2", i_belong_to);
        switch (category) {
            case "top_rated":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.TOPRATED, temp_ids);
                bundle.putParcelableArrayList(TOPRATEDMOVIES, my_movies);
                break;
            case "popular":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.POPULAR, temp_ids);
                bundle.putParcelableArrayList(POPULAR_MOVIES, my_movies);
                break;
            case "now_playing":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.NOWPLAYING, temp_ids);
                bundle.putParcelableArrayList(NOWPLAYINGMOVIES, my_movies);
                break;
            case "upcoming":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.UPCOMING, temp_ids);
                bundle.putParcelableArrayList(UPCOMINGMOVIES, my_movies);
                break;
            default:
                break;

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LIFECYCLE, "onResumed is called for " + category);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LIFECYCLE, "onPause is called for " + category);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LIFECYCLE, "onStop is called for " + category);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LIFECYCLE, "onDestroy is called for " + category);
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


                String id, title, rating, releasedate, overview, posterpath, backgroundpath;


                try {

                    if (retrieved_movie_lists == null) {
                        id = String.valueOf(my_movies.get(position).getId());
                        title = my_movies.get(position).getTitle();
                        rating = my_movies.get(position).getVoteAverage().toString();
                        releasedate = my_movies.get(position).getReleaseDate();
                        overview = my_movies.get(position).getOverview();
                        posterpath = my_movies.get(position).getPosterPath();
                        backgroundpath = my_movies.get(position).getBackdropPath();

                    } else {
                        id = String.valueOf(retrieved_movie_lists.get(position).getId());
                        title = retrieved_movie_lists.get(position).getTitle();
                        rating = retrieved_movie_lists.get(position).getVoteAverage().toString();
                        releasedate = retrieved_movie_lists.get(position).getReleaseDate();
                        overview = retrieved_movie_lists.get(position).getOverview();
                        posterpath = retrieved_movie_lists.get(position).getPosterPath();
                        backgroundpath = retrieved_movie_lists.get(position).getBackdropPath();

                    }


                    //go to movie detail activity
                    Intent intent = new Intent(_mcontext, MovieDetailActivity.class);
                    intent.putExtra(MOVIE_ID, id);

                    intent.putExtra(MOVIE_TITLE, title);
                    intent.putExtra(MOVIE_RATING, rating);
                    intent.putExtra(MOVIE_RELEASEDATE, releasedate);
                    intent.putExtra(MOVIE_OVERVIEW, overview);

                    intent.putExtra(MOVIE_POSTERPATH, posterpath);
                    intent.putExtra(MOVIE_BACKGROUNDPATH, backgroundpath);

                    startActivity(intent);


                } catch (Exception ex) {


                }


            }
        };

    }


}
