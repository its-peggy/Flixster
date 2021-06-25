package com.example.flixster;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;

    // view objects
    TextView tvDetailsTitle;
    TextView tvDetailsOverview;
    RatingBar rbVoteAverage;
    ImageView ivDetailsPoster;
    TextView tvVoteAverage;
    TextView tvGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        tvDetailsOverview = (TextView) findViewById(R.id.tvDetailsOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivDetailsPoster = (ImageView) findViewById(R.id.ivDetailsPoster);
        tvVoteAverage = (TextView) findViewById(R.id.tvVoteAverage);
        tvGenres = (TextView) findViewById(R.id.tvGenres);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvDetailsTitle.setText(movie.getTitle());
        tvDetailsOverview.setText(movie.getOverview());
        float voteAverage = movie.getVoteAverage().floatValue();
        Log.d("MovieDetailsActivity", String.format("Rating %s", voteAverage));
        rbVoteAverage.setRating(voteAverage / 2.0f);

        String imageUrl;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = movie.getBackdropPath();
        }
        else {
            imageUrl = movie.getPosterPath();
        }
        Glide.with(this).load(imageUrl).placeholder(R.drawable.flicks_movie_placeholder).into(ivDetailsPoster);

        String voteAverageWithCount = String.format("%s/10 (%s votes)", movie.getVoteAverage(), movie.getVoteCount());
        tvVoteAverage.setText(voteAverageWithCount);

        tvGenres.setText(createGenreString());
    }

    public String createGenreString() {
        HashMap<Integer, String> map = movie.getGenreMap();
        String separator = "";
        String res = "Genres: ";
        for (Integer id : movie.getGenreIDs()) {
            res += separator + map.get(id);
            separator = ", ";
        }
        return res;
    }



}
