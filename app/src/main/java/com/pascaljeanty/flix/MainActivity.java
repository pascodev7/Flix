package com.pascaljeanty.flix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.pascaljeanty.flix.adapters.MovieAdapter;
import com.pascaljeanty.flix.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON Json) {
                        // called when response HTTP status is "200 OK"
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = Json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            Log.i(TAG, "Results: " + results.toString());
                            movies.addAll(Movie.fromJsonArray(results));
                            movieAdapter.notifyDataSetChanged();
                            Log.i(TAG, "Movies: " + movies.size());
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit Json exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String errorResponse, Throwable throwable) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d(TAG, "onFailure");
                    }
                }
        );
    }

}