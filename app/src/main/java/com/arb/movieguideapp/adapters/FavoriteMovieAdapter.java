package com.arb.movieguideapp.adapters;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.data.FavoriteDbHelper;
import com.arb.movieguideapp.listeners.MovieClickListener;
import com.arb.movieguideapp.models.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MyViewHolder> {

    private List<Movie> movieList;
    private MovieClickListener movieClickListener;

    public FavoriteMovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public FavoriteMovieAdapter(List<Movie> movieList, MovieClickListener movieClickListener) {
        this.movieList = movieList;
        this.movieClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorite, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Movie movie = movieList.get(i);

        myViewHolder.bind(movie, movieClickListener);
    }

    @Override
    public int getItemCount() {
        return null != movieList ? movieList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtReleaseDate;
        private TextView txtRatings;
        private ImageView imgMovie;
        private FloatingActionButton favoriteButton;

        private FavoriteDbHelper favoriteDbHelper;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.movie_title);
            txtReleaseDate = itemView.findViewById(R.id.year_genre_time);
            txtRatings = itemView.findViewById(R.id.ratings);
            imgMovie = itemView.findViewById(R.id.item_movie_img_f);
            favoriteButton = itemView.findViewById(R.id.remove_favorite);
        }

        public void bind(final Movie movie, final MovieClickListener movieClickListener) {
            txtTitle.setText(movie.getTitle());
            txtReleaseDate.setText(movie.getReleaseDate());

            String userRatingText = movie.getVoteAverage() + "/10";
            txtRatings.setText(userRatingText);

            String poster = "https://image.tmdb.org/t/p/w342" + movie.getThumbnail();

            Picasso.get()
                    .load(poster)
                    .placeholder(R.drawable.ic_baseline_blur_on_24)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(imgMovie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieClickListener.onMovieClick(movie);
                }
            });

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    ObjectAnimator.ofFloat(favoriteButton, "rotation", 0f, 360f).setDuration(800).start();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            favoriteButton.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_add_black_24dp));
                            favoriteDbHelper = new FavoriteDbHelper(view.getContext());
                            favoriteDbHelper.deleteFavorite(movie.getId());

                            Snackbar.make(view,  movie.getTitle() + " removed from favorites!", Snackbar.LENGTH_SHORT).show();
                        }
                    }, 400);
                }
            });
        }
    }
}
