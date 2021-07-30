package com.arb.movieguideapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.listeners.MovieClickListener;
import com.arb.movieguideapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.MyViewHolder> {

    private List<Movie> movieList;
    private MovieClickListener movieClickListener;

    public SearchMovieAdapter(List<Movie> movieList, MovieClickListener movieClickListener) {
        this.movieList = movieList;
        this.movieClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public SearchMovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_search, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMovieAdapter.MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.bind(movie, movieClickListener);
    }

    @Override
    public int getItemCount() {
        return null != movieList ? movieList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMovie;
        private TextView txtTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtSearchMovie);
            imgMovie = itemView.findViewById(R.id.item_movie_search);
        }

        public void bind(final Movie movie, final MovieClickListener movieClickListener) {
            String poster = "https://image.tmdb.org/t/p/w342" + movie.getThumbnail();

            txtTitle.setText(movie.getTitle());
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
        }
    }
}
