package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

// the class is parameterized by a ViewHolder (the type you just defined inside
// need to import the ViewHolder YOU just defined (MovieAdapter.ViewHolder), not the generic one
// The base RecyclerView.Adapter is an ABSTRACT CLASS, so we need to implement the following 3 methods
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // in order to implement the 3 methods, we need
    // 1. context, so we can inflate the ViewHolders
    // 2. the actual data
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflate a layout from XML and returns the holder (comparatively expensive)
    // note that only 4 or 5 (as many as can fit on the screen) calls of this should be seen in log
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        // recall that context is a member variable of the MovieAdapter class
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        // then wrap View inside ViewHolder
        return new ViewHolder(movieView);
    }

    // populating the data into the item through the holder (comparatively cheap)
    // this method is called whenever a new poster appears (e.g. 20 times total)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // get the movie at the position
        Movie movie = movies.get(position);
        // bind the movie data (3 fields: title, overview, poster) into the ViewHolder
        holder.bind(movie); // defined in ViewHolder class
    }

    // How many items are in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // DO THIS FIRST: define an inner ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // member variables for each component of a row
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        // A ViewHolder is a representation of a row in our RecyclerView
        // constructor
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            // populate each of the views
            // we use the simple getter methods in the Movie class
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            // need Glide to render remote images
            String imageUrl;
            // imageUrl is either poster or backdrop depending on orientation
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            }
            else {
                imageUrl = movie.getPosterPath();
            }
            Glide.with(context).load(imageUrl).placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster); // load INTO the ivPoster view
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // if position exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                // intent for new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // ??
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the new activity
                context.startActivity(intent);
            }
        }
    }
}
