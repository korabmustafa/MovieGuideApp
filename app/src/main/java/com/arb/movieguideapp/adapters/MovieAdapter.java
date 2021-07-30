package com.arb.movieguideapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.listeners.MovieClickListener;
import com.arb.movieguideapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private List<Movie> mMovies;
    private MovieClickListener movieClickListener;

    public MovieAdapter(List<Movie> mMovies, MovieClickListener movieClickListener) {
        this.mMovies = mMovies;
        this.movieClickListener = movieClickListener;
    }

    public MovieAdapter(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Movie movie = mMovies.get(i);

        myViewHolder.bind(movie, movieClickListener);
    }

    @Override
    public int getItemCount() {
        return null != mMovies ? mMovies.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.item_movie_img);
        }

        public void bind(final Movie movie, final MovieClickListener movieClickListener) {
            String poster = "https://image.tmdb.org/t/p/w342" + movie.getThumbnail();

            Picasso.get()
                    .load(poster)
                    .placeholder(R.drawable.ic_baseline_blur_on_24)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(imgPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieClickListener.onMovieClick(movie);
                }
            });
        }
    }
}
