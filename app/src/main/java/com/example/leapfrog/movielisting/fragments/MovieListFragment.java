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
import com.example.leapfrog.movielisting.helper.ResponseHandler;
import com.example.leapfrog.movielisting.helper.ServerRequest;
import com.example.leapfrog.movielisting.objects.Movie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class MovieListFragment extends Fragment {

    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String MOVIE_RELEASE_DATE = "movie_releasedate";
    public static final String MOVIE_OVERVIEW = "movie_overview";
    public static final String MOVIE_POSTER_PATH = "movie_posterpath";
    public static final String MOVIE_BACKGROUND_PATH = "movie_backgrounpath";
    public static final String POPULAR_MOVIES = "popular_movies";
    public static final String TOP_RATED_MOVIES = "toprated_movies";
    public static final String UPCOMING_MOVIES = "upcoming_movies";
    public static final String NOW_PLAYING_MOVIES = "now_playing_movies";
    private static final String CATEGORY = "cats";

    public final String tag = "all_movies";
    public final String lifeCycle = "lifecycle";
    private String category;

    private RecyclerView rvMovieList;
    private MovieAdapter movieAdapter;
    private Context mContext;
    private RecyclerView.LayoutManager layoutManager;

    ServerRequest serverRequest;
    MovieAdapter.OnItemClickListener movieClickListener;


    public ArrayList<Movie> retrievedMovies;
    private ArrayList<Movie> myMovies;
    public ArrayList<String> newMovieIds;
    public List<String> oldMovieIds;


    public MovieListFragment() {

    }


    public static MovieListFragment newInstance(String cat) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
        }
        Log.d(lifeCycle, "onCreate is called for " + category);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        initializeUi(view);
        Log.d(tag, "this category is: " + category);
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("saved_value", "my_saved_value");


        bundle.putString("checkCatName", category);
        switch (category) {
            case "top_rated":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.TOP_RATED, newMovieIds);
                bundle.putParcelableArrayList(TOP_RATED_MOVIES, myMovies);
                break;
            case "popular":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.POPULAR, newMovieIds);
                bundle.putParcelableArrayList(POPULAR_MOVIES, myMovies);
                break;
            case "now_playing":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.NOW_PLAYING, newMovieIds);
                bundle.putParcelableArrayList(NOW_PLAYING_MOVIES, myMovies);
                break;
            case "upcoming":
                Log.d("save_instance_state", category + " is going to pause");
                bundle.putStringArrayList(MovieListActivity.UPCOMING, newMovieIds);
                bundle.putParcelableArrayList(UPCOMING_MOVIES, myMovies);
                break;
            default:
                break;
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovieList.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(movieClickListener);

        Log.d("belong", "I belong to " + category);
        String checkCatName;
        switch (category) {
            case MovieListActivity.TOP_RATED:
                if (savedInstanceState != null) {
                    oldMovieIds = savedInstanceState.getStringArrayList(MovieListActivity.TOP_RATED);
                    retrievedMovies = savedInstanceState.getParcelableArrayList(TOP_RATED_MOVIES);

                    Log.d("retrieved_ids", "the retrieved movie lists: " + oldMovieIds);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    checkCatName = savedInstanceState.getString("checkCatName");
                    Log.d("checkCatName", "retrieved belongs: " + checkCatName);
                }
                Log.d("request", "request sent for :" + category);
                serverRequest.getAllMovieLists(category);

                break;
            case "popular":
                if (savedInstanceState != null) {
                    oldMovieIds = savedInstanceState.getStringArrayList(MovieListActivity.POPULAR);
                    retrievedMovies = savedInstanceState.getParcelableArrayList(POPULAR_MOVIES);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    checkCatName = savedInstanceState.getString("checkCatName");
                    Log.d("checkCatName", "retrieved belongs: " + checkCatName);
                }
                Log.d("request", "request sent for :" + category);
                serverRequest.getAllMovieLists(category);
                break;
            case "now_playing":
                if (savedInstanceState != null) {
                    oldMovieIds = savedInstanceState.getStringArrayList(MovieListActivity.NOW_PLAYING);
                    retrievedMovies = savedInstanceState.getParcelableArrayList(NOW_PLAYING_MOVIES);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    checkCatName = savedInstanceState.getString("checkCatName");
                    Log.d("checkCatName", "retrieved belongs: " + checkCatName);
                }
                Log.d("request", "request sent for :" + category);
                serverRequest.getAllMovieLists(category);
                break;
            case "upcoming":
                if (savedInstanceState != null) {
                    oldMovieIds = savedInstanceState.getStringArrayList(MovieListActivity.UPCOMING);
                    retrievedMovies = savedInstanceState.getParcelableArrayList(UPCOMING_MOVIES);
                    Log.d("save_instance_state", "movie id list retrieved for " + category);
                    checkCatName = savedInstanceState.getString("checkCatName");
                    Log.d("checkCatName", "retrieved belongs: " + checkCatName);
                }
                Log.d("request", "request sent for :" + category);
                serverRequest.getAllMovieLists(category);
                break;
            default:
                break;
        }

        if (oldMovieIds != null) {
            movieAdapter.update(retrievedMovies);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(lifeCycle, "onResumed is called for " + category);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(lifeCycle, "onPause is called for " + category);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(lifeCycle, "onStop is called for " + category);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(lifeCycle, "onDestroy is called for " + category);
    }

    private void initializeUi(View view) {
        rvMovieList = (RecyclerView) view.findViewById(R.id.rvmovie_list);
        rvMovieList.setHasFixedSize(true);
        mContext = getActivity().getApplicationContext();
        layoutManager = new LinearLayoutManager(mContext);
        rvMovieList.setLayoutManager(layoutManager);
        myMovies = new ArrayList<>();
        movieAdapter = new MovieAdapter(mContext);
        movieClickListener = new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieAdapter.ItemHolder item, int position) {

                String id, title, rating, releaseDate, overview, posterPath, backgroundPath;

                try {

                    if (retrievedMovies == null) {
                        id = String.valueOf(myMovies.get(position).getId());
                        title = myMovies.get(position).getTitle();
                        rating = myMovies.get(position).getVoteAverage().toString();
                        releaseDate = myMovies.get(position).getReleaseDate();
                        overview = myMovies.get(position).getOverview();
                        posterPath = myMovies.get(position).getPosterPath();
                        backgroundPath = myMovies.get(position).getBackdropPath();

                    } else {
                        id = String.valueOf(retrievedMovies.get(position).getId());
                        title = retrievedMovies.get(position).getTitle();
                        rating = retrievedMovies.get(position).getVoteAverage().toString();
                        releaseDate = retrievedMovies.get(position).getReleaseDate();
                        overview = retrievedMovies.get(position).getOverview();
                        posterPath = retrievedMovies.get(position).getPosterPath();
                        backgroundPath = retrievedMovies.get(position).getBackdropPath();

                    }


                    //go to movie detail activity
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(MOVIE_ID, id);

                    intent.putExtra(MOVIE_TITLE, title);
                    intent.putExtra(MOVIE_RATING, rating);
                    intent.putExtra(MOVIE_RELEASE_DATE, releaseDate);
                    intent.putExtra(MOVIE_OVERVIEW, overview);

                    intent.putExtra(MOVIE_POSTER_PATH, posterPath);
                    intent.putExtra(MOVIE_BACKGROUND_PATH, backgroundPath);

                    startActivity(intent);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        serverRequest = new ServerRequest(mContext, new ResponseHandler().new AllMoviesHandler() {
            @Override
            public void handleResponse(LinkedHashMap<String, Movie> newMovies, ArrayList<String> newIds, String category) {

                try {
                    newMovieIds = newIds;
                    myMovies = new ArrayList<>(newMovies.values());

                    if (retrievedMovies != null) {
                        CompareLists compareLists = new CompareLists();
                        if (!compareLists.equalLists(newIds, oldMovieIds)) {
                            //lists vary
                            //further operation for identifying new ids
                            Log.d("different", "lists are different");

                            System.out.println("old list: " + oldMovieIds);
                            System.out.println("old list: " + newIds);

                            //removing stale elements from the old list
                            boolean found;
                            Iterator<Movie> iterMovie = retrievedMovies.iterator();
                            Iterator<String> iterId = oldMovieIds.iterator();
                            while (iterMovie.hasNext() && iterId.hasNext()) {
                                found = false;

                                Movie movie = iterMovie.next();//this is required
                                String oldId = iterId.next();

                                for (String newId : newIds) {

                                    if (oldId.equals(newId)) {
                                        //means this old id matches with new id
                                        //no need to further look on this oldId
                                        //break the loop
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    movieAdapter.remove(oldMovieIds.indexOf(oldId));
                                    iterMovie.remove();
                                    iterId.remove();
                                }
                            }

                            System.out.println("the filtered out list: " + oldMovieIds);

                            //now adding new elements from the new list
                            List<String> newMovieIdsCopy = new ArrayList<>(newIds);
                            newMovieIdsCopy.removeAll(oldMovieIds);//remove same items
                            System.out.println("the new ids are: " + newMovieIdsCopy);
                            for (String newId : newMovieIdsCopy) {
                                oldMovieIds.add(newId);
                                movieAdapter.add(movieAdapter.getItemCount(), newMovies.get(newId));
                                retrievedMovies.add(newMovies.get(newId));
                                //update adapter
                            }
                            System.out.println("the updated list: " + oldMovieIds);
                        }
                    } else {
                        //list_to_compare null, means display latest lists
                        Log.d(tag, "all movies fetched: " + newMovies);
                        movieAdapter.update(myMovies);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, getActivity());//pass Activity reference

    }


}
