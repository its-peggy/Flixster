package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parceler.Parcel;

import okhttp3.Headers;

@Parcel
public class Movie {
    String posterPath;
    String backdropPath;
    String title;
    String overview;
    Double voteAverage;
    Integer voteCount;
    List<Integer> genreIDs = new ArrayList<Integer>();
    List<String> genreStrings = new ArrayList<String>();
    static HashMap<Integer, String> genreMap = createGenreMap();
    static String GENRE_MAPPING_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=d93f42fb016d7133007e5d73db292fa9";

    public Movie() {};

    public Movie(JSONObject jsonObject) throws JSONException { // use throws instead of try/catch here
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        voteCount = jsonObject.getInt("vote_count");
        JSONArray jsonArray = jsonObject.getJSONArray("genre_ids");
        for(int i = 0; i < jsonArray.length(); i++){
            genreIDs.add(jsonArray.getInt(i));
            genreStrings.add(genreMap.get(jsonArray.getInt(i)));
        }
    }

    private static HashMap<Integer, String> createGenreMap() {
        Log.d("making_map", "map making method is called");
        HashMap<Integer, String> genre_mapping = new HashMap<Integer, String>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GENRE_MAPPING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) { // these params are the info passed back by client.get ?
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("genres");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject objectAtIndex = results.getJSONObject(i);
                        Log.d("making_map", String.format("id is %s, name is %s", objectAtIndex.getInt("id"), objectAtIndex.getString("name")));
                        genre_mapping.put(objectAtIndex.getInt("id"), objectAtIndex.getString("name"));
                        Log.d("making_map", String.format("in the map: %s --> %s", objectAtIndex.getInt("id"), genre_mapping.get(objectAtIndex.getString("id"))));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                Log.d("making_map", "onFailure");
            }
        });
        return genre_mapping;
    }

    // create List of movies given a JSON Array of movies
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getVoteCount() { return voteCount; }

    public List<Integer> getGenreIDs() { return genreIDs; }

    public static HashMap<Integer, String> getGenreMap() { return genreMap; }
}
