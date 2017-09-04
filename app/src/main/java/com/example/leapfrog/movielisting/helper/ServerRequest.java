package com.example.leapfrog.movielisting.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.leapfrog.movielisting.objects.AllMovies;
import com.example.leapfrog.movielisting.objects.MovieCasts;
import com.example.leapfrog.movielisting.objects.MovieDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;
import static com.example.leapfrog.movielisting.helper.RetrofitClient.API_KEY;


public class ServerRequest {

    private Context _mcontext;
    HandleResponse.HandleAllMovies handleResponse;
    HandleResponse.HandleMovieDetail handleMovieDetail;
    HandleResponse.HandleMovieCaste handleMovieCaste;
    public static ProgressDialog progressDialog;
    public String url;
    public String pb_title;
    private static Retrofit retrofit = null;
    Activity activity_ref;

    public ServerRequest(Context context, HandleResponse.HandleAllMovies response, Activity activity_ref) {
        pb_title = "Loading...";
        this._mcontext = context;
        this.handleResponse = response;
        this.activity_ref = activity_ref;
    }

    public ServerRequest(Context context, HandleResponse.HandleMovieDetail response, Activity activity_ref) {
        pb_title = "Loading...";
        this._mcontext = context;
        this.handleMovieDetail = response;
        this.activity_ref = activity_ref;
    }


    public ServerRequest(Context context, HandleResponse.HandleMovieCaste response, Activity activity_ref) {
        pb_title = "Loading...";
        this._mcontext = context;
        this.handleMovieCaste = response;
        this.activity_ref = activity_ref;
    }


    //request for movie list
    public void getAllMovieLists(String category) {
        showProgressBar();
        if (API_KEY.isEmpty()) {
            //this can get hour error, change it to log from toast
            Toast.makeText(_mcontext, "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        RESTAPIMethods client =
                RetrofitClient.getInstance().create(RESTAPIMethods.class);

        Call<AllMovies> call = client.getAllMovies(category, API_KEY);
        call.enqueue(new Callback<AllMovies>() {
            @Override
            public void onResponse(Call<AllMovies> call, Response<AllMovies> response) {

                hideProgressBar();
                if (response != null) {
                    handleResponse.handleResponse(response.body().getResults());
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


    //request for movie list
    public void getMovieDetail(String id) {
        showProgressBar();
        if (API_KEY.isEmpty()) {
            //this can get hour error, change it to log from toast
            Toast.makeText(_mcontext, "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        RESTAPIMethods client =
                RetrofitClient.getInstance().create(RESTAPIMethods.class);

        Call<MovieDetail> call = client.getMovieDetail(Integer.parseInt(id), API_KEY);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {

                hideProgressBar();
                if (response != null) {
                    MovieDetail movieDetail = response.body();
                    handleMovieDetail.handleMovieDetail(movieDetail);
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
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
            Toast.makeText(_mcontext, "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        RESTAPIMethods client =
                RetrofitClient.getInstance().create(RESTAPIMethods.class);

        Call<MovieCasts> call = client.getMovieCaste(Integer.parseInt(id), API_KEY);
        call.enqueue(new Callback<MovieCasts>() {
            @Override
            public void onResponse(Call<MovieCasts> call, Response<MovieCasts> response) {

//                hideProgressBar();
                if (response != null) {
                    MovieCasts movieCasts = response.body();
                    handleMovieCaste.handleMovieCaste(movieCasts);
                }
            }

            @Override
            public void onFailure(Call<MovieCasts> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
//                hideProgressBar();

                makeToast("No Intenet connection!!!");
            }
        });

    }


    public void showProgressBar() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(activity_ref);
                progressDialog.setMessage(pb_title);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
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
                Toast.makeText(_mcontext,
                        message,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
