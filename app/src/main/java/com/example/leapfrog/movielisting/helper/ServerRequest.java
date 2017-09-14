package com.example.leapfrog.movielisting.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.leapfrog.movielisting.objects.AllMovies;
import com.example.leapfrog.movielisting.objects.Movie;
import com.example.leapfrog.movielisting.objects.MovieCasts;


import java.util.ArrayList;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.content.ContentValues.TAG;
import static com.example.leapfrog.movielisting.helper.RetrofitClient.API_KEY;


public class ServerRequest {

    public String pbTitle;
    public static ProgressDialog progressDialog = null;

    private Context mContext;


    ResponseHandler.AllMoviesHandler handleResponse;
    ResponseHandler.MovieCasteHandler handleMovieCaste;

    Activity activityRef;

    public static boolean pbShowed;

    public ServerRequest(Context context, ResponseHandler.AllMoviesHandler response, Activity activityRef) {
        pbTitle = "Loading...";
        this.mContext = context;
        this.handleResponse = response;
        this.activityRef = activityRef;
        progressDialog = getProgressDialog(activityRef, pbTitle);
    }


    public ServerRequest(Context context, ResponseHandler.MovieCasteHandler response, Activity activityRef) {
        pbTitle = "Loading...";
        this.mContext = context;
        this.handleMovieCaste = response;
        this.activityRef = activityRef;
        progressDialog = getProgressDialog(activityRef, pbTitle);
    }


    public static ProgressDialog getProgressDialog(Activity activity, String pbTitle) {

        //make single instance of progress dialog
        // so that same progress dialog shows for each fragment
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(pbTitle);
            progressDialog.setCancelable(false);
            pbShowed = false;
        }

        return progressDialog;

    }


    //request for movie list
    public void getAllMovieLists(final String category) {
        showProgressBar();
        if (API_KEY.isEmpty()) {
            //this can get hour error, change it to log from toast
            Toast.makeText(mContext, "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        RestApiMethods client =
                RetrofitClient.getInstance().create(RestApiMethods.class);

        Call<AllMovies> call = client.getAllMovies(category, API_KEY);

        call.enqueue(new Callback<AllMovies>() {
            @Override
            public void onResponse(Call<AllMovies> call, Response<AllMovies> response) {

                hideProgressBar();
                if (response != null) {

                    LinkedHashMap<String, Movie> my_movies = new LinkedHashMap<>();
                    ArrayList<String> movie_ids = new ArrayList<>();
                    for (Movie movie : response.body().getResults()) {
                        movie_ids.add(String.valueOf(movie.getId()));
                        my_movies.put(String.valueOf(movie.getId()), movie);
                    }
                    Log.d("movie_ids", "the list of movie_ids: " + movie_ids);

//                    handleResponse.handleResponse(response.body().getResults(), movie_ids, category);
                    handleResponse.handleResponse(my_movies, movie_ids, category);
                }
            }

            @Override
            public void onFailure(Call<AllMovies> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                hideProgressBar();
                makeToast("No Intenet connection!!!");
            }
        });

    }


    public void getMovieCaste(String id) {
//        showProgressBar();
        if (API_KEY.isEmpty()) {
            //this can get hour error, change it to log from toast
            Toast.makeText(mContext, "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        RestApiMethods client =
                RetrofitClient.getInstance().create(RestApiMethods.class);

        Call<MovieCasts> call = client.getMovieCaste(Integer.parseInt(id), API_KEY);
        call.enqueue(new Callback<MovieCasts>() {
            @Override
            public void onResponse(Call<MovieCasts> call, Response<MovieCasts> response) {

                hideProgressBar();
                if (response != null) {
                    MovieCasts movieCasts = response.body();
                    handleMovieCaste.handleMovieCaste(movieCasts);
                }
            }

            @Override
            public void onFailure(Call<MovieCasts> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                hideProgressBar();

                makeToast("No Intenet connection!!!");
            }
        });

    }


    public void showProgressBar() {
        if (!pbShowed) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                    pbShowed = true;
                }
            });
        }

    }

    public void hideProgressBar() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.hide();
                }

            }
        });
    }


    public void makeToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext,
                        message,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
