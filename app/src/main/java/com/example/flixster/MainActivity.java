 package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=d93f42fb016d7133007e5d73db292fa9";
    public static final String TAG = "MinActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        RecyclerView rvMovies = findViewById(R.id.rvMovies); // recall this is actually the only view in our activity_main.xml

        // create the adapter - just use the MovieAdapter constructor
        // context IS the MainActivity
        MovieAdapter movieAdapter = new MovieAdapter(this, movies); // note that movies is not initialized yet (initialized later when getting response from API)

        // set the adapter on the RecyclerView
        rvMovies.setAdapter(movieAdapter);

        // set a Layout Manager
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        // retrieving data from the query URL
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() { // using JsonHttpResponse Handler b/c API returns JSON
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) { // these params are the info passed back by client.get ?
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results"); // array of JSON objects (all now-playing movies)
                    Log.i(TAG, "Results: " + results.toString());
                    // movies = Movie.fromJsonArray(results);
                    movies.addAll(Movie.fromJsonArray(results)); // needs to modify the existing movies, because that is the one the Adapter has reference to
                    // let Adapter know whenever the data changes (is added) so it can re-render the RecyclerView
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}