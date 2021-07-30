package com.arb.movieguideapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.listeners.TrailerClickListener;
import com.arb.movieguideapp.models.MovieTrailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {
    private final TrailerClickListener mTrailerClickListener;
    private final List<MovieTrailer> mMovieTrailers;

    public TrailerAdapter(List<MovieTrailer> mMovieTrailers, TrailerClickListener mTrailerClickListener) {
        this.mMovieTrailers = mMovieTrailers;
        this.mTrailerClickListener = mTrailerClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MovieTrailer mMovieTrailer = this.mMovieTrailers.get(position);
        holder.bind(mMovieTrailer, mTrailerClickListener);
    }

    @Override
    public int getItemCount() {
        return null !=  this.mMovieTrailers ? this.mMovieTrailers.size() : 0;
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mMovieTrailerName;
        private CardView mMovieCard;

        public MyViewHolder(final View itemView) {
            super(itemView);

            mMovieTrailerName = itemView.findViewById(R.id.tv_movie_trailer_name);
            mMovieCard = itemView.findViewById(R.id.cv_movie_trailer_card);
        }

        public void bind(final MovieTrailer mMovieTrailer, final TrailerClickListener mTrailerClickListener) {
            mMovieTrailerName.setText(mMovieTrailer.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTrailerClickListener.onMovieTrailerClick(mMovieTrailer);
                }
            });
        }
    }
}
