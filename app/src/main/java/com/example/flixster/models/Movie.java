package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    List<Integer> genreIDs;
    List<String> genreStrings;
    Integer movieID;
    List<String> starring;

    public static final String GENRE_MAPPING_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=d93f42fb016d7133007e5d73db292fa9";
    public static final String MOVIE_CREDITS_URL = "https://api.themoviedb.org/3/movie/%s/credits?api_key=d93f42fb016d7133007e5d73db292fa9";

    public Movie() {};

    public Movie(JSONObject jsonObject) throws JSONException { // use throws instead of try/catch here
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        voteCount = jsonObject.getInt("vote_count");

        genreIDs = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("genre_ids");
        for(int i = 0; i < jsonArray.length(); i++){
            genreIDs.add(jsonArray.getInt(i));
        }

        genreStrings = getGenreNames(genreIDs);
        movieID = jsonObject.getInt("id");
        starring = getActorList(movieID);
    }

    public List<String> getActorList(Integer movieID) {
        AsyncHttpClient client = new AsyncHttpClient();
        ArrayList<String> actorNames = new ArrayList<>();
        String creditsURL = String.format(MOVIE_CREDITS_URL, movieID);
        client.get(creditsURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray cast = json.jsonObject.getJSONArray("cast");
                    for (int j = 0; j < java.lang.Math.min(cast.length(), 5); j++) {
                        actorNames.add(cast.getJSONObject(j).getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("Movie class", "Error trying to get movie cast");
            }
        });
        return actorNames;
    }

    public ArrayList<String> getGenreNames(List<Integer> genreIDs) {
        AsyncHttpClient client = new AsyncHttpClient();
        ArrayList<String> genreNames = new ArrayList<>();
        client.get(GENRE_MAPPING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray genres = json.jsonObject.getJSONArray("genres");
                    for (int j = 0; j < genreIDs.size(); j++) {
                        for (int k = 0; k < genres.length(); k++) {
                            if (genres.getJSONObject(k).getInt("id") == genreIDs.get(j))
                                genreNames.add(genres.getJSONObject(k).getString("name"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("Movie class", "Error trying to get genres");
            }
        });
        return genreNames;
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

    public String getTitle() { return title; }

    public String getOverview() { return overview; }

    public Double getVoteAverage() { return voteAverage; }

    public Integer getVoteCount() { return voteCount; }

    public List<String> getGenreStrings() { return genreStrings; }

    public List<String> getStarring() { return starring; }

}
